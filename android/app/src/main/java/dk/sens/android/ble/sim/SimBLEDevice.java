package dk.sens.android.ble.sim;

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

import java.util.UUID;

import dk.sens.android.ble.BLEAddress;
import dk.sens.android.ble.generic.GenericBLEDevice;
import dk.sens.android.ble.generic.GenericBLEDeviceImpl;

public class SimBLEDevice extends GenericBLEDeviceImpl
{
    static final String TAG = "SimSENSBLEDevice";

    private boolean mConnected = false;
    private BLEAddress mMac = null;

    protected SimBLEDevice(BLEAddress mac)
    {
        this.mMac = mac;
    }

    public static GenericBLEDevice create(BLEAddress mac)
    {
        return GenericBLEDevice.create(new SimBLEDevice(mac));
    }

    @Override
    public void connect()
    {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mConnected = true;
                if (parent.mStateListener != null)
                {
                    parent.mStateListener.onDeviceConnected(parent);
                }
            }
        }, 1000);
    }

    @Override
    public void disconnect()
    {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mConnected = false;
                if (parent.mStateListener != null)
                {
                    parent.mStateListener.onDeviceDisconnected(parent);
                }
            }
        }, 500);
    }

    @Override
    public void close()
    {

    }

    @Override
    public boolean subscribeChar(final UUID char_uuid)
    {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                parent.mStateListener.onDeviceCharacteristicSubscribed(parent, char_uuid, true);
            }
        }, 100);
        return true;
    }

    @Override
    public boolean writeChar(UUID service_uuid, UUID char_uuid, byte[] data)
    {
        return false;
    }

    @Override
    public boolean readChar(UUID serviceUUID, UUID charUUID)
    {
        return false;
    }

    public boolean discoverServices()
    {
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
        return "s";
    }

    @Override
    public String getNormalizedName()
    {
        return "s";
    }

    @Override
    public BLEAddress getAddress()
    {
        return mMac;
    }

    public SimBLEScanRecord getScanRecord()
    {
        return new SimBLEScanRecord();
    }

}
