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


import android.content.Context;

import com.idevicesinc.sweetblue.BleManager;

import dk.sens.android.ble.BLEAddress;
import dk.sens.android.ble.generic.GenericBLEDevice;
import dk.sens.android.ble.nordic.NordicBLEScanner;
import no.nordicsemi.android.support.v18.scanner.ScanResult;

public class SweetBlueBLEScannerNordic extends NordicBLEScanner
{
    private static final String TAG = "SweetBlueBLEScannerNordic";

    BleManager mManager;

    public SweetBlueBLEScannerNordic(final Context context)
    {
        super(context);
        mManager = BleManager.get(context);
    }

    @Override
    protected GenericBLEDevice createDevice(ScanResult result)
    {
        return SweetBlueBLEDevice.create(mManager.newDevice(result.getDevice().getAddress(), result.getDevice().getName()));
    }

    public GenericBLEDevice getDevice(BLEAddress address)
    {
        return SweetBlueBLEDevice.create(mManager.newDevice(address.macString(), ""));
    }

}
