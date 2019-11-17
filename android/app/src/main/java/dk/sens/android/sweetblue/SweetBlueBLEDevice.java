package dk.sens.android.sweetblue;

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


import android.util.Log;

import com.sweetblue.BleDevice;
import com.sweetblue.BleDeviceConfig;
import com.sweetblue.BleDeviceState;
import com.sweetblue.BleNodeConfig;

import java.util.UUID;

import dk.sens.android.ble.BLEAddress;
import dk.sens.android.ble.generic.GenericBLEDevice;
import dk.sens.android.ble.generic.GenericBLEDeviceImpl;

public class SweetBlueBLEDevice extends GenericBLEDeviceImpl implements BleDevice.StateListener, BleDevice.ReadWriteListener, BleDevice.ConnectionFailListener
{

    String TAG()
    {
        return "SwBLEDevice (" + getAddress().macString() + ")";
    }

    private BleDevice mDevice;
    private boolean mConnecting;

    private class NoRetryFilter implements BleNodeConfig.ReconnectFilter
    {

        @Override
        public Please onEvent(ReconnectEvent e)
        {
            return Please.stopRetrying();
        }
    }

    private SweetBlueBLEDevice(BleDevice bleDevice)
    {
        this.mDevice = bleDevice;
        BleDeviceConfig cfg = new BleDeviceConfig();
        cfg.reconnectFilter = new NoRetryFilter();
        cfg.autoBondFixes = false;
        this.mDevice.setConfig(cfg);
        this.mConnecting = false;
    }

    public static GenericBLEDevice create(BleDevice bleDevice)
    {
        return GenericBLEDevice.create(new SweetBlueBLEDevice(bleDevice));
    }

    public void connect()
    {
        this.mDevice.setListener_ConnectionFail(this);
        this.mDevice.setListener_State(this);
        //this.mDevice.disconnect();
        mConnecting = true;
        this.mDevice.connect();
    }

    public void disconnect()
    {
        mConnecting = false;
        this.mDevice.disconnect();
    }

    public void close()
    {
        this.mDevice.undiscover();
    }

    @Override
    public boolean writeChar(UUID serviceUUID, UUID charUUID, byte[] data)
    {
        mDevice.write(charUUID, data, this);
        return true;
    }

    @Override
    public boolean readChar(UUID serviceUUID, UUID charUUID)
    {
        mDevice.read(serviceUUID, charUUID);
        return true;
    }

    @Override
    public boolean subscribeChar(UUID uuid)
    {
        mDevice.enableNotify(uuid, this);
        return true;
    }

    @Override
    public boolean discoverServices()
    {
        // SweetBlue automatically discovers services, so just return success here.
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                parent.mStateListener.onDeviceServicesDiscovered(parent, true);
            }
        });
        return true;
    }

    @Override
    public String getName()
    {
        return mDevice.getName_normalized();
    }

    @Override
    public String getNormalizedName()
    {
        return mDevice.getName_normalized();
    }

    @Override
    public BLEAddress getAddress()
    {
        return new BLEAddress(mDevice.getMacAddress());
    }

    @Override
    public void onEvent(BleDevice.StateListener.StateEvent e)
    {
        Log.i(TAG(), e.toString());
        if (e.didEnter(BleDeviceState.INITIALIZED))
        {
            mConnecting = false;
            if ((parent != null) && (parent.mStateListener != null))
            {
                parent.mStateListener.onDeviceConnected(parent);
            }
        }
        else if (e.didEnter(BleDeviceState.DISCONNECTED))
        {
            if (!mConnecting) // ignore disconnects when connecting beause we automatically retry
            {
                if ((parent != null) && (parent.mStateListener != null))
                {
                    parent.mStateListener.onDeviceDisconnected(parent);
                }
            }
        }
    }

    @Override
    public void onEvent(ReadWriteEvent e)
    {
        if (e.isNotification())
        {
            parent.mStateListener.onDeviceNotification(parent, e.charUuid(), e.data());
        }
        else if (e.isRead())
        {
            parent.mStateListener.onDeviceCharacteristicRead(parent, e.charUuid(), e.data(), true);
        }
        else if (e.isWrite())
        {
            parent.mStateListener.onDeviceCharacteristicWrite(parent, e.charUuid(), true);
        }
        else if (e.type() == Type.ENABLING_NOTIFICATION)
        {
            parent.mStateListener.onDeviceCharacteristicSubscribed(parent, e.charUuid(), true);
        }
    }

    @Override
    public Please onEvent(ConnectionFailEvent e)
    {
        return Please.retry();
    }
}
