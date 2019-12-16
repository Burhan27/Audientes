package com.audientes.android.professional.app;

import android.util.Log;

import com.audientes.android.professional.device.rpc.RPCCallTask;
import com.audientes.android.professional.device.rpc.RPCCodes;
import com.audientes.android.professional.device.rpc.UUIDs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dk.sens.android.ble.ExtendedBLEPeripheral;
import dk.sens.android.ble.generic.GenericBLEDevice;
import dk.sens.android.ble.generic.GenericBLEScanRecord;
import dk.sens.android.ble.tasks.BLETask;
import dk.sens.android.util.Timestamp;
import io.realm.RealmList;

import static dk.sens.android.util.ByteUtils.int16;
import static dk.sens.android.util.ByteUtils.int32;


/**
 * Created by morten on 16/07/2017.
 */

public class AudientesDevice implements ExtendedBLEPeripheral.ExtendedBLEPeripheralDelegate
{

    String TAG()
    {
        return "S-BLE AudientesDevice (" + mState.name() + ")";
    }

    private ExtendedBLEPeripheral mBleDevice;
    private boolean mConnect = false;
    private Timestamp mEpoch = new Timestamp();
    private Timestamp mLastTestTime = new Timestamp();
    private byte mTestState = 0;
    private TestResult mActiveTest = null;

    private List<LogEntry> mLog = new LinkedList<>();

    public AudientesDevice(GenericBLEDevice bleDevice)
    {
        mBleDevice = new ExtendedBLEPeripheral(null, bleDevice, null, null);
        mBleDevice.setDelegate(this);
    }

    /*
     * API
     */

    public void setConnectState(boolean connectState)
    {
        if (mConnect == connectState) { return; }
        mConnect = connectState;

        if (mConnect && mState == State.Ignored)
        {
            changeState(State.Connecting);
        }
        else if (!mConnect && mState != State.Ignored)
        {
            changeState(State.Disconnecting);
        }
    }

    public void clearLog()
    {
        mLog.clear();
    }

    public void addLog(String msg)
    {
        LogEntry e = new LogEntry();
        e.msg = msg;
        mLog.add(e);
    }

    public List<LogEntry> getLog()
    {
        return mLog;
    }

    /*
     * RPC Helpers
     */

    public void rpcCall(byte cmd, byte[] data)
    {
        if (data == null)
        {
            data = new byte[0];
        }
        mBleDevice.addTask(new RPCCallTask(cmd, data));
    }

    public void rpcCallGetVersion()
    {
        rpcCall(RPCCodes.CMD_VERSION, null);
    }

    public void rpcCallGetTime()
    {
        rpcCall(RPCCodes.CMD_GET_TIME, null);
    }

    public boolean startTest(TestResult meta)
    {
        mActiveTest = meta;
        clearLog();
        addLog("Requested: Start Test");

        if (mState == State.Idle || mState == State.TestCompleted)
        {
            changeState(State.TestStarting);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean restartTest()
    {
        if (mState == State.Idle || mState == State.TestCompleted)
        {
            changeState(State.TestStarting);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void rpcCallCancelTest()
    {
        rpcCall(RPCCodes.CMD_CANCEL_TEST, null);
    }

    public void rpcCallPlayPauseTest(boolean pause)
    {
        rpcCall(RPCCodes.CMD_PLAY_PAUSE_TEST, new byte[]{ pause ? (byte)1 : (byte)0} );
    }

    public void rpcCallGetTestResult()
    {
        rpcCall(RPCCodes.CMD_READ_TEST_RESULT, null);
    }

    public boolean isConnected()
    {
        return mState != State.Ignored && mState != State.Connecting;
    }

    /*
     * SECTION - ExtendedBLEPeripheralDelegate
     */

    @Override
    public void onPeripheralConnected(ExtendedBLEPeripheral device)
    {
        Log.i(TAG(), "Connected");
        if (mState == State.Connecting)
        {
            changeState(State.Interrogating);
        }
        AudientesApp.instance.onDeviceConnected();
    }

    @Override
    public void onPeripheralConnectionTimeout(ExtendedBLEPeripheral device)
    {
        if (mState == State.Connecting)
        {
            Log.i(TAG(), "Retrying");
            onEntry();
        }
    }

    @Override
    public void onPeripheralDisconnected(ExtendedBLEPeripheral device, boolean expected)
    {
        Log.i(TAG(), "Disconnected");
        if (mConnect)
        {
            changeState(State.Connecting);
            Log.i(TAG(), "Re-connecting");
        }
        AudientesApp.instance.onDeviceDisconnected();
    }

    @Override
    public void onPeripheralTaskStarted(ExtendedBLEPeripheral device, BLETask task)
    {

    }

    @Override
    public void onPeripheralTaskCompleted(ExtendedBLEPeripheral device, BLETask task)
    {
        if (task instanceof RPCCallTask)
        {
            RPCCallTask t = (RPCCallTask) task;

            Log.i(TAG(), String.format("RPC Task Completed id:%d", t.getRPCCode()));

            if (t.getRPCStatus() != RPCCodes.STATUS_OK)
            {
                Log.i(TAG(), String.format("RPC Task Failed id:%d - status:%d", t.getRPCCode(), t.getRPCStatus()));
                changeState(State.Disconnecting);
                return;
            }

            byte[] data = t.getRPCReply();

            switch (t.getRPCCode())
            {
                case RPCCodes.CMD_VERSION:
                {
                    AudientesApp.instance.onDeviceVersionReceived(data[0], data[1], data[2]);
                    break;
                }
                case RPCCodes.CMD_GET_TIME:
                {
                    long tx = int32(data, 0);
                    mEpoch.setToMSecondsAgo(tx * 100);
                    //AudientesApp.instance.onDeviceTimeReceived(tx);
                    break;
                }
                case RPCCodes.CMD_GET_TEST_STATE:
                {
                    mTestState = data[0];
                    long tx_test = int32(data, 1);
                    Timestamp testTime = new Timestamp(mEpoch);
                    testTime.addMSeconds(tx_test * 100);
                    mLastTestTime = testTime;
                    if (mState == State.Interrogating)
                    {
                        if (mTestState == RPCCodes.RPC_TEST_STATE_RUNNING)
                        {
                            changeState(State.TestInProgress);
                        }
                        else if (mTestState == RPCCodes.RPC_TEST_STATE_COMPLETED)
                        {
                            changeState(State.TestCompleted);
                        }
                        else if (mTestState == RPCCodes.RPC_TEST_STATE_INACTIVE)
                        {
                            changeState(State.Idle);
                        }
                    }
                    break;
                }
                case RPCCodes.CMD_START_TEST:
                {
                    changeState(State.TestInProgress);
                    break;
                }
                case RPCCodes.EVT_TEST_PROGRESS:
                {
                    break;
                }
                case RPCCodes.CMD_READ_TEST_RESULT:
                {
                    mActiveTest.left[0] = data[3];
                    mActiveTest.left[1] = data[0];
                    mActiveTest.left[2] = data[1];
                    mActiveTest.left[3] = data[2];
                    mActiveTest.right[0] = data[7];
                    mActiveTest.right[1] = data[4];
                    mActiveTest.right[2] = data[5];
                    mActiveTest.right[3] = data[6];
                    //System.arraycopy(data, 0, mActiveTest.left, 0, 4);
                    //System.arraycopy(data, 4, mActiveTest.right, 0, 4);
                    mActiveTest.completed = true;
                    mActiveTest.log = new RealmList<LogEntry>();
                    for (LogEntry e: mLog)
                    {
                        mActiveTest.log.add(e);
                    }
                    AudientesApp.instance.onTestResult(mActiveTest);
                }
            }
        }
    }

    @Override
    public void onPeripheralAllTasksCompleted(ExtendedBLEPeripheral device)
    {

    }

    @Override
    public void onPeripheralTasksFailed(ExtendedBLEPeripheral device)
    {
        changeState(State.Disconnecting);
        return;
    }

    @Override
    public void onPeripheralDiscovered(ExtendedBLEPeripheral device, GenericBLEScanRecord scanRecord)
    {

    }

    @Override
    public void onPeripheralNotification(ExtendedBLEPeripheral device, UUID char_uuid, byte[] data)
    {
        Log.i(TAG(), String.format("Notification Data %d %d", data.length, data[0]));

        if (data.length == 0) { return; }

        if (char_uuid.equals(UUIDs.REPLY_CHAR_UUID) && data.length >= 2)
        {
            Log.i(TAG(), String.format("RPC Event %d", data[0]));

            byte cmd = data[0];
            byte args[] = new byte[data.length - 1];
            System.arraycopy(data, 1, args, 0, args.length);
            if (cmd == RPCCodes.EVT_TEST_PROGRESS)
            {
                mTestState = args[0];
                if (mTestState == RPCCodes.RPC_TEST_STATE_RUNNING)
                {
                    changeState(State.TestInProgress);
                }
                else if (mTestState == RPCCodes.RPC_TEST_STATE_COMPLETED)
                {
                    changeState(State.TestCompleted);
                }
                else if (mTestState == RPCCodes.RPC_TEST_STATE_INACTIVE)
                {
                    changeState(State.Idle);
                }
            }
            else if (cmd == RPCCodes.EVT_TRACE)
            {
                LogEntry e = new LogEntry();
                int id = int16(args, 0);
                long ts = int32(args, 2);
                int p1 = int16(args, 6);
                int p2 = int16(args, 8);
                int p3 = int16(args, 10);
                e.msg = translateTrace(id, ts, p1, p2, p3);
                mLog.add(e);
            }
        }
    }

    /*
     * SECTION - STATE MACHINE
     */

    // State Def
    public static enum State
    {
        Ignored,
        Connecting,
        Interrogating,
        Idle,
        TestStarting,
        TestInProgress,
        TestCompleted,
        Disconnecting
    }

    private State mState = State.Ignored;


    public State getState()
    {
        return mState;
    }

    private void changeState(State newState)
    {
        if (mState != newState)
        {
            onExit();
            Log.i(TAG(), "State: " + newState.name());
            mState = newState;
            onEntry();
        }
    }

    private void onEntry()
    {
        switch (mState)
        {
            case Ignored:
                break;
            case Connecting:
                mBleDevice.connect();
                mBleDevice.subscribe(UUIDs.SERVICE_UUID, UUIDs.REPLY_CHAR_UUID);
                break;
            case Interrogating:
                rpcCall(RPCCodes.CMD_GET_TIME, null);
                rpcCall(RPCCodes.CMD_GET_TEST_STATE, null);
                break;
            case TestStarting:
                rpcCall(RPCCodes.CMD_START_TEST, null);
                break;
            case TestCompleted:
                rpcCall(RPCCodes.CMD_READ_TEST_RESULT, null);
                break;
            case Disconnecting:
                mBleDevice.disconnect();
                break;

        }
    }

    private void onExit()
    {
        switch (mState)
        {
            case Ignored:
                break;
            case Connecting:
                break;

        }
    }


    // Trace Stuff

    static class TraceEntry
    {
        public String name;
        public String p1;
        public String p2;

        public TraceEntry(String name, String p1, String p2)
        {
            this.name = name;
            this.p1 = p1;
            this.p2 = p2;
        }

        public static Map<Integer, TraceEntry> map = new HashMap<>();

        public static void put(int id, String name)
        {
            map.put(new Integer(id), new TraceEntry(name, null, null));
        }

        public static void put(int id, String name, String p1, String p2)
        {
            map.put(new Integer(id), new TraceEntry(name, p1, p2));
        }

        static
        {
            put(0x01, "MENU_KEY");
            put(0x02, "MENU_KEY_LONG");
            put(0x03, "PLUS_KEY");
            put(0x04, "PLUS_KEY_LONG");
            put(0x05, "MINUS_KEY");
            put(0x06, "MINUS_KEY_LONG");
            put(0x07, "POWER_KEY");
            put(0x08, "POWER_KEY_LONG");
            put(0x10, "HT_START");
            put(0x11, "HT_CANCEL");
            put(0x12, "HT_SELECT");
            put(0x13, "HT_SWEEP_LEFT", "freq", "level");
            put(0x14, "HT_SWEEP_RIGHT", "freq", "level");
            put(0x15, "HT_SWEEP_COMPL", "left", "right");
            put(0x16, "HT_RESULT_LEFT", "freq", "level");
            put(0x17, "HT_RESULT_RIGHT", "freq", "level");
            put(0x18, "HT_NEXT_FREQ");
            put(0x19, "HT_NEXT_EAR");
            put(0x1A, "HT_COMPLETE");
            put(0x20, "TONE_START_LEFT", "freq", "level");
            put(0x21, "TONE_START_RIGHT", "freq", "level");
            put(0x22, "TONE_STOP_LEFT");
            put(0x23, "TONE_STOP_RIGHT");
        }
    }

    private String translateTrace(int id, long ts, int p1, int p2, int p3)
    {
        TraceEntry e = TraceEntry.map.get(new Integer(id));

        if (e == null)
        {
            return String.format("TS=%d TRACE_ID=%d P1=%d P2=%d", ts, id, p1, p2);
        }
        else if (e.p1 == null)
        {
            return String.format("TS=%d %s", ts, e.name);
        }
        else
        {
            return String.format("TS=%d %s %s=%d %s=%d", ts, e.name, e.p1, p1, e.p2, p2);
        }
    }

}
