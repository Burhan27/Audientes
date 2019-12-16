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


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dk.sens.android.ble.BLEAddress;
import dk.sens.android.ble.generic.GenericBLEDevice;
import dk.sens.android.ble.generic.GenericBLEScanner;
import dk.sens.android.ble.generic.GenericBLEScannerImpl;
import dk.sens.android.ble.generic.GenericRSSI;
import dk.sens.android.util.ErrorHandling;
import dk.sens.android.util.TimeDelta;
import dk.sens.android.util.logging.LogContext;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

public class NordicBLEScanner extends GenericBLEScannerImpl
{
    private LogContext LOG = LogContext.create(this);

    private GenericBLEScanner.discoveredListener mListener;

    private ScanSettings mScanSettings = null;
    private List<ScanFilter> mScanFilters = null;
    BluetoothLeScannerCompat mScanner;
    boolean mScanning = false;

    private ScanCallback scanCallback = new ScanCallback()
    {
        @Override
        public void onScanResult(final int callbackType, final ScanResult result)
        {
            // do nothing
        }

        @Override
        public void onBatchScanResults(final List<ScanResult> results)
        {
            for (ScanResult s : results)
            {
                BLEAddress address = new BLEAddress(s.getDevice().getAddress());

                LOG.i(TimeDelta.seconds(15), "Discovered a device");

                LOG.v(TimeDelta.seconds(30), String.format("DISCOVERED %s", address.macString()));

                GenericBLEDevice gd = createDevice(s);
                mListener.onDiscoveredDevice(gd, NordicBLEScanRecord.create(s.getScanRecord()), new GenericRSSI(s.getRssi()));
            }
        }

        @Override
        public void onScanFailed(final int errorCode)
        {
            LOG.e("Start Scan Failed");
            ErrorHandling.report(new RuntimeException(String.format("ScanFailed errCode %d", errorCode)));
        }
    };

    protected GenericBLEDevice createDevice(ScanResult result)
    {
        return null; // Should override
    }

    @Override
    public GenericBLEDevice getDevice(BLEAddress address)
    {
        return null; // Should override
    }

    public NordicBLEScanner(final Context context)
    {
        super();

        mScanner = BluetoothLeScannerCompat.getScanner();

        mScanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .setReportDelay(100).setUseHardwareBatchingIfSupported(false).build();

        mScanFilters = new ArrayList<>();
        //mScanFilters.add(new ScanFilter.Builder().setDeviceName("CBSTII01").build());

    }

    @Override
    public void scanForDevices(GenericBLEScanner.discoveredListener listener)
    {
        scanForDevices(null, listener);
    }

    @Override
    public void scanForDevices(final UUID withServiceUUID, final GenericBLEScanner.discoveredListener listener)
    {
        mListener = listener;
        startScan();
    }


    public boolean startScan()
    {
        if (mScanSettings == null || mScanFilters == null)
        {
            return false;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                _startScan();
            }
        });
        return true;
    }

    public synchronized boolean _startScan()
    {

        if (!mScanning)
        {
            LOG.i("Starting Scan");
            mScanner.startScan(mScanFilters, mScanSettings, scanCallback);
            mScanning = true;
            return true;
        }
        else
        {
            LOG.w("StartScan failed - Already Scan");
        }
        return false;
    }


    @Override
    public boolean isScanning()
    {
        return mScanning;
    }

    @Override
    public void stopScan()
    {
        if (mScanning)
        {
            LOG.i("Stopping Scan");
            mScanner.stopScan(scanCallback);
            mScanning = false;
            return;
        }
        else
        {
            LOG.w("StopScan failed - Not Scanning");
        }

        return;
    }
}
