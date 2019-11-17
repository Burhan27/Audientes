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

import dk.sens.android.ble.BLEAddress;
import dk.sens.android.ble.generic.GenericBLEDevice;
import dk.sens.android.ble.generic.GenericBLEScanRecord;
import dk.sens.android.ble.generic.GenericBLEScanner;
import dk.sens.android.ble.generic.GenericBLEScannerImpl;
import dk.sens.android.ble.generic.GenericRSSI;
import dk.sens.android.util.SimpleTimer;

import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

public class SimBLEScanner extends GenericBLEScannerImpl
{
    Random generator = new Random();

    class NearbyDevice
    {
        SimBLEDevice mDev;
        boolean discoverable;
        int rssi;

        public NearbyDevice(SimBLEDevice dev, int rssi)
        {
            this.mDev = dev;
            this.discoverable = true;
            this.rssi = rssi;
        }
    }

    boolean mScanning = false;
    GenericBLEScanner.discoveredListener mListener;
    LinkedList<NearbyDevice> mDevices = new LinkedList<>();
    SimpleTimer mDiscoverTimer;
    Random r = new Random();

    public SimBLEScanner()
    {
        mDiscoverTimer = new SimpleTimer(new Runnable()
        {
            @Override
            public void run()
            {
                triggerDiscoveredDevices();
            }
        }, true);
    }

    public void addSimDevice(SimBLEDevice dev, int rssi)
    {
        mDevices.add(new NearbyDevice(dev, rssi));
        GenericBLEDevice.create(dev);
    }

    public LinkedList<NearbyDevice> getDevices()
    {
        return mDevices;
    }

    @Override
    public void stopScan()
    {
        mScanning = false;
        mDiscoverTimer.stop();
    }

    @Override
    public void scanForDevices(GenericBLEScanner.discoveredListener listener)
    {
        mScanning = true;
        mListener = listener;
        mDiscoverTimer.startPeriodic(500, 500);
    }

    @Override
    public void scanForDevices(UUID withServiceUUID, GenericBLEScanner.discoveredListener listener)
    {
        scanForDevices(listener);
    }

    @Override
    public boolean isScanning()
    {
        return mScanning;
    }

    public GenericBLEDevice getDevice(BLEAddress address)
    {
        for (NearbyDevice dev: mDevices)
        {
            if (dev.mDev.getAddress().equals(address));
            {
                return dev.mDev.parent;
            }
        }
        return null;
    }

    private void triggerDiscoveredDevices()
    {
        if (mScanning && mListener != null)
        {
            for (NearbyDevice dev: mDevices)
            {
                if (r.nextFloat() < 0.50 && dev.discoverable)
                {
                    mListener.onDiscoveredDevice(dev.mDev.parent, new GenericBLEScanRecord(dev.mDev.getScanRecord()), new GenericRSSI(dev.rssi - 5 + generator.nextInt(10)));
                }
            }
        }
    }

    public boolean isBluetoothEnabled()
    {
        return true;
    }

    private boolean mSimBroken;
    public boolean isSimBroken()
    {
        return mSimBroken;
    }
    public void setSimBroken(boolean value)
    {
        mSimBroken = value;
    }


}
