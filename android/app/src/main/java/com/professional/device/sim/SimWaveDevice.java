package com.professional.device.sim;

import android.util.Log;

import com.professional.device.rpc.RPCCodes;
import com.professional.device.rpc.UUIDs;

import java.util.UUID;

import dk.sens.android.ble.BLEAddress;
import dk.sens.android.ble.sim.SimBLEDevice;
import dk.sens.android.util.MainThreadRun;
import dk.sens.android.util.SimpleTimer;
import dk.sens.android.util.Timestamp;

import static dk.sens.android.util.ByteUtils.int16bytes;
import static dk.sens.android.util.ByteUtils.int32bytes;

/**
 * Created by morten on 18/07/2017.
 */

public class SimWaveDevice extends SimBLEDevice
{
    static final String TAG = "SimWaveDevice";

    private byte[] mTestResult = new byte[10];
    private Timestamp mTestTime = new Timestamp();
    private Timestamp mSystemTime = Timestamp.now();

    public SimWaveDevice(BLEAddress mac)
    {
        super(mac);
    }

    @Override
    public String getNormalizedName()
    {
        return "audientes";
    }

    @Override
    public void connect()
    {
        super.connect();
        MainThreadRun.runDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //disconnect();
            }
        }, 15000);
    }

    long getTX()
    {
        return mSystemTime.agoTime().asMSeconds() / 100;
    }

    @Override
    public boolean writeChar(UUID service_uuid, UUID char_uuid, byte[] data)
    {
        if (data.length == 0) { return true; }

        if (char_uuid.equals(UUIDs.CALL_CHAR_UUID))
        {
            byte cmd = data[0];
            if (cmd == RPCCodes.CMD_VERSION)
            {
                postReply(RPCCodes.CMD_VERSION, RPCCodes.STATUS_OK, new byte[]{ 1, 0, 0} );
            }
            if (cmd == RPCCodes.CMD_GET_TIME)
            {
                long tx = mSystemTime.agoTime().asMSeconds() / 100;
                byte[] outdata = new byte[4];
                ByteUtils.int32bytes(tx, outdata, 0);
                postReply(RPCCodes.CMD_GET_TIME, RPCCodes.STATUS_OK, outdata);
            }
            else if (cmd == RPCCodes.CMD_START_TEST)
            {
                startTestLoop();
            }
            else if (cmd == RPCCodes.CMD_READ_TEST_RESULT)
            {
                postReply(RPCCodes.CMD_READ_TEST_RESULT, RPCCodes.STATUS_OK, mTestResult );
            }
            else if (cmd == RPCCodes.CMD_CANCEL_TEST)
            {
                stopTestLoop(false);
            }
            else if (cmd == RPCCodes.CMD_PLAY_PAUSE_TEST)
            {
                if (data[1] == 0) {
                    unpauseTestLoop();
                } else {
                    pauseTestLoop();
                }
            }
            else if (cmd == RPCCodes.CMD_GET_TEST_STATE)
            {
                long tx = mSystemTime.agoTime().asMSeconds() / 100;
                byte[] outdata = new byte[5];
                outdata[0] = (byte)mTestState;
                ByteUtils.int32bytes(tx, outdata, 1);
                postReply(RPCCodes.CMD_GET_TIME, RPCCodes.STATUS_OK, outdata);
            }
            return true;
        }

        return false;
    }

    private void postTrace(int id, int p0, int p1, int p2)
    {
        byte[] data = new byte[12];
        ByteUtils.int16bytes(id, data, 0);
        ByteUtils.int32bytes(getTX(), data, 2);
        ByteUtils.int16bytes(p0, data, 6);
        ByteUtils.int16bytes(p1, data, 8);
        ByteUtils.int16bytes(p2, data, 10);
        postEvent(RPCCodes.EVT_TRACE, data);
    }

    private void postReply(byte cmd, byte status, byte[] data)
    {
        final byte[] reply = new byte[2+data.length];
        reply[0] = cmd;
        reply[1] = status;
        System.arraycopy(data, 0, reply, 2, data.length);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                parent.mStateListener.onDeviceNotification(parent, UUIDs.REPLY_CHAR_UUID, reply);
            }
        }, 50);
    }

    private void postEvent(byte cmd, byte[] data)
    {
        final byte[] reply = new byte[1+data.length];
        reply[0] = cmd;
        System.arraycopy(data, 0, reply, 1, data.length);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                parent.mStateListener.onDeviceNotification(parent, UUIDs.REPLY_CHAR_UUID, reply);
            }
        }, 50);
    }

    /*
     *
     */

    public void onClick()
    {
        Log.i(TAG, "onClick");
        if (mTestState == RPCCodes.RPC_TEST_STATE_RUNNING)
        {
            mdBTestStep = mdBs.length - 1;
            incrementTestStep();
        }
    }

    /*
     * Simulation Functionality
     */

    private int mSideTestStep;
    private int mFreqTestStep;
    private int mdBTestStep;
    private int mTestState;
    private SimpleTimer mTestTimer;

    private static final int[] mFreqs = {
            2125, 4250, 8500, 17000, 34000
    };
    // 212.5Hz, 425Hz, 850 Hz, 1.7kHz, 3.4 kHz
    // over 35dB er høre tab

    private static final int[] mdBs = {
            20, 30, 40, 50, 60, 70, 80, 90
            //20db -> 90db
            // kryds / circler
            // 20-40 let
            // 40-60 moderat
            // - 70 heavy
    };

    private void startTestLoop()
    {
        mSideTestStep = 0;
        mFreqTestStep = 0;
        mdBTestStep = 0;
        mTestState = RPCCodes.RPC_TEST_STATE_RUNNING;
        sendTestStep();

        if (mTestTimer == null)
        {
            mTestTimer = new SimpleTimer(new Runnable()
            {
                @Override
                public void run()
                {
                    nextTestStep();
                }
            }, true);
        }

        mTestTimer.startPeriodic(75, 75);
    }

    private void stopTestLoop(boolean completed)
    {
        mSideTestStep = 0;
        mFreqTestStep = 0;
        mdBTestStep = 0;
        mTestState = completed ? RPCCodes.RPC_TEST_STATE_COMPLETED : RPCCodes.RPC_TEST_STATE_INACTIVE;

        sendTestStep();

        for (int i = 0; i < 5; i +=1 )
        {
            mTestResult[i] = (byte)mdBs[(int)(Math.random() * 5)];
            mTestResult[i+5] = (byte)mdBs[(int)(Math.random() * 5)];
        }

        if (mTestTimer != null)
        {
            mTestTimer.stop();
        }
    }

    private void pauseTestLoop()
    {
        if (mTestState == RPCCodes.RPC_TEST_STATE_RUNNING)
        {
            mTestState = RPCCodes.RPC_TEST_STATE_PAUSED;
            sendTestStep();
        }
    }

    private void unpauseTestLoop()
    {
        if (mTestState == RPCCodes.RPC_TEST_STATE_PAUSED)
        {
            mTestState = RPCCodes.RPC_TEST_STATE_RUNNING;
            sendTestStep();
        }
    }

    private void sendTestStep()
    {
        postEvent(RPCCodes.EVT_TEST_PROGRESS, new byte[]
                {
                        (byte) mTestState,
                        (byte) mSideTestStep,
                        (byte) ((mFreqs[mFreqTestStep] >> 8) & 0xFF),
                        (byte) ((mFreqs[mFreqTestStep]) & 0xFF),
                        (byte) mdBs[mdBTestStep]
                });
    }

    private void incrementTestStep()
    {
        mdBTestStep += 1;
        postTrace(RPCCodes.TRACE_STEP, mdBTestStep, mFreqTestStep, 0);
        if (mdBTestStep == mdBs.length)
        {
            mdBTestStep = 0;
            mFreqTestStep += 1;
        }
        if (mFreqTestStep == mFreqs.length)
        {
            mFreqTestStep = 0;
            mSideTestStep += 1;
        }
        if (mSideTestStep == 2)
        {
            stopTestLoop(true);
        }
    }

    private void nextTestStep()
    {
        if (mTestState != RPCCodes.RPC_TEST_STATE_RUNNING) {
            return;
        }

        incrementTestStep();
        if (mTestState == RPCCodes.RPC_TEST_STATE_RUNNING)
        {
            //sendTestStep();
        }
    }


}
