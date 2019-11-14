package dk.sens.android.ble.generic;

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
import android.content.Context;

import dk.sens.android.ble.BLEAddress;
import dk.sens.android.ble.sim.SimBLEScanner;

public class GenericBLEScanner
{
    private static GenericBLEScannerImpl mImpl = null;

    public static void create(GenericBLEScannerImpl impl)
    {
        mImpl = impl;
    }

    public static void scanForDevices(UUID withServiceUUID, discoveredListener listener)
    {
        mImpl.scanForDevices(withServiceUUID, listener);
    }

    public static void scanForDevices(discoveredListener listener)
    {
        mImpl.scanForDevices(listener);
    }

    public static void stopScan()
    {
        mImpl.stopScan();
    }

    public static boolean isScanning()
    {
        return mImpl.isScanning();
    }

    public static GenericBLEDevice getDevice(BLEAddress address) { return mImpl.getDevice(address); }

    public interface discoveredListener
    {
        void onDiscoveredDevice(GenericBLEDevice device, final GenericBLEScanRecord scanResult, GenericRSSI rssi);
    }

    public static boolean isBLESimulated()
    {
        return mImpl instanceof SimBLEScanner;
    }

    public static boolean isBluetoothEnabled() { return mImpl.isBluetoothEnabled(); }
    public static void enableBluetooth() { mImpl.enableBluetooth(true); }
    public static void disableBluetooth() { mImpl.enableBluetooth(false); }
    public static void undiscoverAll() { mImpl.undiscoverAll(); }
    public static void reset() { mImpl.reset(); }
}
