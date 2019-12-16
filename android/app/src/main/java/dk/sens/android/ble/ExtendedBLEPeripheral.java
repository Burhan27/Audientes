package dk.sens.android.ble;

/*
 * Copyright (c) 2016 SENS Innovation ApS <morten@sens.dk>
 * All rights reserved.
 *
 * - Redistribution and use in source and binary forms, with or without
 *   modification, are permitted only with explicit permission from the copyright
 *   owner.
 * - Any redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.UUID;

import dk.sens.android.ble.generic.GenericBLEDevice;
import dk.sens.android.ble.generic.GenericBLEScanRecord;
import dk.sens.android.ble.generic.GenericBLEScanner;
import dk.sens.android.ble.generic.GenericRSSI;
import dk.sens.android.ble.tasks.BLEDisconnectTask;
import dk.sens.android.ble.tasks.BLETask;
import dk.sens.android.ble.tasks.BLETaskQueue;
import dk.sens.android.ble.tasks.DiscoverServicesTask;
import dk.sens.android.ble.tasks.ReadCharTask;
import dk.sens.android.ble.tasks.SubscribeTask;
import dk.sens.android.ble.tasks.WriteCharTask;
import dk.sens.android.util.SimpleTimer;
import dk.sens.android.util.Timestamp;


public class ExtendedBLEPeripheral implements GenericBLEDevice.deviceStateListener
{
    String TAG()
    {
        return "SNS ExtBLEPeripheral (" + macString() + ")";
    }


    private GenericBLEDevice mDevice;
    private ExtendedBLEPeripheralDelegate mDelegate;
    private BLETaskQueue mTaskQueue = new BLETaskQueue(this);
    private Timestamp mLastSeen;
    private GenericRSSI mLastRSSI;
    private boolean mUserDisconnect = false;
    private SimpleTimer mConnectionTimer;
    private boolean mConnected = false;
    public boolean mReadyForTasks = false;
    private boolean mIsConnecting = false;
    private GenericBLEScanRecord mScanRecord;
    private ExtendedBLEScanner mParent;

    public interface ExtendedBLEPeripheralDelegate
    {
        void onPeripheralConnected(ExtendedBLEPeripheral device);
        void onPeripheralConnectionTimeout(ExtendedBLEPeripheral device);
        void onPeripheralDisconnected(ExtendedBLEPeripheral device, boolean expected);
        void onPeripheralTaskStarted(ExtendedBLEPeripheral device, BLETask task);
        void onPeripheralTaskCompleted(ExtendedBLEPeripheral device, BLETask task);
        void onPeripheralAllTasksCompleted(ExtendedBLEPeripheral device);
        void onPeripheralTasksFailed(ExtendedBLEPeripheral device);
        void onPeripheralDiscovered(ExtendedBLEPeripheral device, GenericBLEScanRecord scanRecord);
        void onPeripheralNotification(ExtendedBLEPeripheral device, UUID char_uuid, byte[] data);
    }

    private ExtendedBLEPeripheral()
    {
        mConnectionTimer = new SimpleTimer(new Runnable()
        {
            @Override
            public void run()
            {
                ExtendedBLEPeripheral.this.onConnectionTimeout();
            }
        });
    }

    public ExtendedBLEPeripheral(ExtendedBLEScanner parent, GenericBLEDevice device, GenericBLEScanRecord scanResult, GenericRSSI rssi)
    {
        this();
        mDevice = device;
        mLastSeen = new Timestamp();
        mLastSeen.setToNow();
        mLastRSSI = rssi;
        mScanRecord = scanResult;
        mParent = parent;
    }

    public ExtendedBLEPeripheral(BLEAddress address)
    {
        this();
        mDevice = GenericBLEScanner.getDevice(address);
        mLastSeen = new Timestamp();
    }

    public void close()
    {
        mDevice.close();
        mDevice = null;
    }

    public void setDelegate(ExtendedBLEPeripheralDelegate delegate)
    {
        mDelegate = delegate;
    }

    public ExtendedBLEPeripheralDelegate getDelegate()
    {
        return mDelegate;
    }

    public GenericBLEDevice getDevice()
    {
        return mDevice;
    }

    public String macString()
    {
        return mDevice.getAddress().macString();
    }

    public void connect()
    {
        if (mDevice == null) { return; }
        mConnected = false;
        Log.i(TAG(), "Asked to connect");
        mUserDisconnect = false;
        mTaskQueue.clear();
        mTaskQueue.busy = true;
        addTask(new DiscoverServicesTask());

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                connect_();
            }
        });
    }

    private void connect_()
    {
        Log.i(TAG(), String.format("Actually Connecting %s", mDevice.toString()));
        //todo: GenericBLEScanner.pauseScan();
        mDevice.connect(this);
        mIsConnecting = true;
        mConnectionTimer.start(30000);
    }

    private void onConnectionTimeout()
    {
        Log.i(TAG(), "Connection Timeout");
        //todo: BLEManager.getScanner().unpauseScan();
        mIsConnecting = false;
        if (mDevice != null)
        {
            mDevice.disconnect();
        }
        if (mDelegate != null) { mDelegate.onPeripheralConnectionTimeout(this); }
    }

    public void disconnect()
    {
        Log.i(TAG(), "Asked to disconnect");
        mUserDisconnect = true;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                disconnect_();
            }
        });
    }

    private void disconnect_()
    {
        if (mDevice != null)
        {
            Log.i(TAG(), "Actually disconnecting");
            mDevice.disconnect();
        }
    }

    public boolean isConnected()
    {
        return mConnected;
    }

    public boolean isConnecting()
    {
        return mIsConnecting;
    }

    public GenericBLEScanRecord getScanRecord()
    {
        return mScanRecord;
    }

    public void onDiscovered(GenericBLEDevice device, GenericBLEScanRecord scanResult, GenericRSSI rssi)
    {
        if (mIsConnecting)
        {
            Log.i(TAG(), "Seen when connecting " + mDevice.getAddress());
        }
        if (mDelegate != null) { mDelegate.onPeripheralDiscovered(this, scanResult); }
        mScanRecord = scanResult;
        mLastSeen.setToNow();
        mLastRSSI = rssi;
    }

    public Timestamp getLastSeen()
    {
        return mLastSeen;
    }

    public boolean isOnline(long timeout)
    {
        boolean longer = mLastSeen.longerThanSecondsAgoFrom(mParent.getLastScanTime(), 5);
        Log.i("TIMESTAMP", String.format("Timestamp %s %s %b", mLastSeen.secondsAgoString(), mParent.getLastScanTime().secondsAgoString(), longer));
        return !longer;
    }

    public int getLastRSSI()
    {
        return mLastRSSI.value;
    }

    public void write(UUID serviceUUID, UUID charUUID, byte[] data)
    {
        addTask(new WriteCharTask(serviceUUID, charUUID, data));
    }

    public void read(UUID serviceUUID, UUID charUUID)
    {
        addTask(new ReadCharTask(serviceUUID, charUUID));
    }

    public void subscribe(UUID serviceUUID, UUID charUUID)
    {
        addTask(new SubscribeTask(serviceUUID, charUUID));
    }

    public void addTask(BLETask task)
    {
        mTaskQueue.add(task);
        Log.i(TAG(), "adding task " + task.toString());
        if (mReadyForTasks) { mTaskQueue.handleNext(); } else { Log.i(TAG(), "Task queued"); }
    }

    public void disconnectWhenDone()
    {
        mTaskQueue.add(new BLEDisconnectTask());
    }

    @Override
    public void onDeviceServicesDiscovered(GenericBLEDevice device, boolean status)
    {
        Log.i(TAG(), "Discover Services Done " + new Boolean(status).toString());

        if (mTaskQueue.peek() != null)
        {
            mTaskQueue.peek().onServicesDiscovered(device, status);
        }
    }

    @Override // BluetoothGattCallback
    public void onDeviceCharacteristicWrite(GenericBLEDevice device, UUID char_uuid, boolean status)
    {
        Log.v(TAG(), "Write Completed status:" + new Boolean(status).toString());

        if (mTaskQueue.peek() != null)
        {
            mTaskQueue.peek().onCharacteristicWrite(device, char_uuid, status);
        }
    }

    @Override // BluetoothGattCallback
    public void onDeviceCharacteristicRead(GenericBLEDevice device, UUID char_uuid, byte[] data, boolean status)
    {
        Log.v(TAG(), "onCharacteristicRead " + new Boolean(status).toString());

        if (mTaskQueue.peek() != null)
        {
            mTaskQueue.peek().onCharacteristicRead(device, char_uuid, data, status);
        }
    }

    @Override // BluetoothGattCallback
    public void onDeviceCharacteristicSubscribed(GenericBLEDevice device, UUID char_uuid, boolean status)
    {
        Log.v(TAG(), "onCharSubscribed " + new Boolean(status).toString());

        if (mTaskQueue.peek() != null)
        {
            mTaskQueue.peek().onCharacteristicSubscribed(device, char_uuid, status);
        }
    }

    @Override
    public void onDeviceConnected(GenericBLEDevice device)
    {
        if (mConnected == false)
        {
            mConnected = true;
            mConnectionTimer.stop();
            // todo: BLEManager.getScanner().unpauseScan();
            mIsConnecting = false;
            Log.i(TAG(), "Connected");
            if (mDelegate != null)
            {
                mDelegate.onPeripheralConnected(this);
            }
            mTaskQueue.busy = false;
            mReadyForTasks = true;
            mTaskQueue.handleNext();
        }
    }

    @Override
    public void onDeviceDisconnected(GenericBLEDevice device)
    {
        mConnected = false;
        mConnectionTimer.stop();
        if (mIsConnecting)
        {
            // todo: BLEManager.getScanner().unpauseScan();
            mIsConnecting = false;
        }

        if (mUserDisconnect == true)
        {
            Log.i(TAG(), "Disconnected");
        }
        else
        {
            Log.i(TAG(), "Disconnected (Unexpected)");
        }

        BLETask t = mTaskQueue.peek();
        if (t != null)
        {
            t.onDeviceDisconnected(mDevice, mUserDisconnect);
        }

        mReadyForTasks = false;
        mTaskQueue.clear();

        if (mDelegate != null)
        {
            mDelegate.onPeripheralDisconnected(this, mUserDisconnect);
        }
    }

    @Override
    public void onDeviceNotification(GenericBLEDevice device, UUID char_uuid, byte[] data)
    {
        Log.v(TAG(), "onCharacteristicChanged() " + char_uuid.toString());
        if (mTaskQueue.peek() != null)
        {
            mTaskQueue.peek().onCharacteristicChanged(device, char_uuid, data);
        }

        if (mDelegate != null)
        {
            mDelegate.onPeripheralNotification(this, char_uuid, data);
        }

    }
}
