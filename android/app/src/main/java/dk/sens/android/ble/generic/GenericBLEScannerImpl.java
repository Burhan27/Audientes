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

import android.bluetooth.BluetoothAdapter;

import dk.sens.android.ble.BLEAddress;

public abstract class GenericBLEScannerImpl
{
    public abstract void stopScan();
    public abstract void scanForDevices(final GenericBLEScanner.discoveredListener listener);
    public abstract void scanForDevices(final UUID withServiceUUID, final GenericBLEScanner.discoveredListener listener);
    public abstract boolean isScanning();
    public abstract GenericBLEDevice getDevice(BLEAddress address);

    public boolean isBluetoothEnabled()
    {
        return BluetoothAdapter.getDefaultAdapter() != null && BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    public void enableBluetooth(boolean enable)
    {
        if (enable)
        {
            BluetoothAdapter.getDefaultAdapter().enable();
        }
        else
        {
            BluetoothAdapter.getDefaultAdapter().disable();
        }
    }

    public void undiscoverAll()
    {

    }

    public void reset()
    {

    }

}
