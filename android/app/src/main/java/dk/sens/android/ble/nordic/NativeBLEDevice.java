package dk.sens.android.ble.nordic;

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

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.UUID;

import dk.sens.android.ble.BLEAddress;
import dk.sens.android.ble.generic.GenericBLEDevice;
import dk.sens.android.ble.generic.GenericBLEDeviceImpl;
import dk.sens.android.util.SimpleTimer;

public class NativeBLEDevice extends GenericBLEDeviceImpl
{

    String TAG()
    {
        return "NativeBLEDevice (" + getAddress().macString() + ")";
    }

    private BluetoothDevice mDevice;
    private BluetoothGatt mGatt = null;
    public static Context context;
    private SimpleTimer mConnectionTimer;
    private boolean mUserDisconnect = false;

    private NativeBLEDevice(BluetoothDevice bleDevice)
    {
        this.mDevice = bleDevice;
    }

    public static GenericBLEDevice create(BluetoothDevice bleDevice)
    {
        return GenericBLEDevice.create(new NativeBLEDevice(bleDevice));
    }

    public void connect()
    {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                _connect();
            }
        });
    }

    private void _connect()
    {
        // Where is this compatibility check documented?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            mGatt = mDevice.connectGatt(NativeBLEDevice.context, false, mGattCallback, BluetoothDevice.TRANSPORT_LE);
        }
        else
        {
            mGatt = mDevice.connectGatt(NativeBLEDevice.context, false, mGattCallback);
        }
        mConnectionTimer.start(10000);
    }

    private void onConnectionTimeout()
    {
        Log.i(TAG(), "Connection Timeout");
        if (mGatt != null)
        {
            mGatt.disconnect();
            if (mGatt != null)
            {
                mGatt.close();
            }
            mGatt = null;
        }
        parent.mStateListener.onDeviceDisconnected(parent);
    }

    public void disconnect()
    {
        Log.i(TAG(), "Asked to disconnect");

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                disconnect_();
            }
        });
    }

    public void disconnect_()
    {
        if (mGatt != null)
        {
            Log.i(TAG(), "Actually disconnecting");
            mGatt.disconnect();
        }
        else
        {
            Log.i(TAG(), "mGatt was null");
        }
    }

    public void close()
    {

    }

    @Override
    public boolean writeChar(UUID serviceUUID, UUID charUUID, byte[] data)
    {

        return true;
    }

    @Override
    public boolean readChar(UUID serviceUUID, UUID charUUID)
    {

        return true;
    }

    @Override
    public boolean subscribeChar(UUID uuid)
    {

        return true;
    }

    @Override
    public boolean discoverServices()
    {

        return true;
    }

    @Override
    public String getName()
    {
        return mDevice.getName();
    }

    @Override
    public String getNormalizedName()
    {
        return mDevice.getName().toLowerCase();
    }

    @Override
    public BLEAddress getAddress()
    {
        return new BLEAddress(mDevice.getAddress());
    }

    private boolean mConnected = false;

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
    {

        @Override // BluetoothGattCallback
        public void onConnectionStateChange (BluetoothGatt gatt, int status, int newState)
        {
            String stateText = "Other";
            if (newState == BluetoothGatt.STATE_CONNECTED)
            {
                stateText = "Connected";
            }
            else if (newState == BluetoothGatt.STATE_DISCONNECTED)
            {

                stateText = "Disconnected";
            }
            Log.i(TAG(), "onConnectionStateChange state: (" + stateText + ")" + newState + " status:" + status);
            if (newState == BluetoothGatt.STATE_CONNECTED && status == BluetoothGatt.GATT_SUCCESS)
            {
                if (mConnected == false)
                {
                    mConnected = true;
                    mConnectionTimer.stop();
                    Log.i(TAG(), "Connected");
                    parent.mStateListener.onDeviceConnected(parent);
                }
            }
            else if (newState == BluetoothGatt.STATE_CONNECTED)
            {
                Log.i(TAG(), "Connected with errors, retrying");
                new Handler(Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        _connect();
                    }
                });
            }
            else if (newState == BluetoothGatt.STATE_DISCONNECTED)
            {
                if (status == 22)
                {
                    // Indicates bonding problems, so we remove the bond here
                    disconnect();
                    //this.removeBond();
                }
                if (mConnected == false && mConnectionTimer.isStarted() && mUserDisconnect == false)
                {
                    Log.i(TAG(), "Connect failed, retrying");
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            _connect();
                        }
                    });
                }
                else
                {
                    mConnected = false;
                    mConnectionTimer.stop();
                    if (mUserDisconnect == true)
                    {
                        Log.i(TAG(), "Disconnected");
                    }
                    else
                    {
                        Log.i(TAG(), "Disconnected (Unexpected)");
                    }
                    if (mGatt != null)
                    {
                        mGatt.close();
                        mGatt = null;
                    }

                    parent.mStateListener.onDeviceDisconnected(parent);

                }
            }
        }

        @Override
        public void onServicesDiscovered (BluetoothGatt gatt, int status)
        {
            Log.i(TAG(), "Discover Services Done " + status);

            parent.mStateListener.onDeviceServicesDiscovered(parent, status == BluetoothGatt.GATT_SUCCESS);

        }

        @Override // BluetoothGattCallback
        public void onCharacteristicWrite (BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
        {
            Log.i(TAG(), "Write Completed status:" + status);
            parent.mStateListener.onDeviceCharacteristicWrite(parent, characteristic.getUuid(), status == BluetoothGatt.GATT_SUCCESS);
        }

        @Override // BluetoothGattCallback
        public void onCharacteristicRead (BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
        {
            Log.i(TAG(), "onCharacteristicRead " + status);
            parent.mStateListener.onDeviceCharacteristicRead(parent, characteristic.getUuid(), characteristic.getValue(), status == BluetoothGatt.GATT_SUCCESS);
        }

        @Override // BluetoothGattCallback
        public void onDescriptorWrite (BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
        {
            parent.mStateListener.onDeviceCharacteristicSubscribed(parent, descriptor.getCharacteristic().getUuid(), status == BluetoothGatt.GATT_SUCCESS);
        }

        @Override // BluetoothGattCallback
        public void onCharacteristicChanged (BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
        {
            parent.mStateListener.onDeviceNotification(parent, characteristic.getUuid(), characteristic.getValue());
        }

    };

}
