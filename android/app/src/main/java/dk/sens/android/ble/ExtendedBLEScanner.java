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

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import dk.sens.android.ble.generic.GenericBLEDevice;
import dk.sens.android.ble.generic.GenericBLEScanRecord;
import dk.sens.android.ble.generic.GenericBLEScanner;
import dk.sens.android.ble.generic.GenericRSSI;
import dk.sens.android.util.ByteUtils;
import dk.sens.android.util.ErrorHandling;
import dk.sens.android.util.MainThreadRun;
import dk.sens.android.util.TimeDelta;
import dk.sens.android.util.Timestamp;


public class ExtendedBLEScanner
{

    private static final String TAG = "S-BLE BleScanner";
    private BleScannerDelegate mDelegate;
    private boolean mScanning;
    private boolean mScanningPaused;
    private boolean mScanFailed;
    private HashMap<BLEAddress, ExtendedBLEPeripheral> mPeripherals;

    private UUID mServiceUUID;
    private Timestamp mLastScan;

    private class FoundInvalidDeviceException extends Exception
    {
        public FoundInvalidDeviceException(String s)
        {
            super(s);
        }
    }

    private GenericBLEScanner.discoveredListener scanCallback = new GenericBLEScanner.discoveredListener()
    {
        @Override
        public void onDiscoveredDevice(GenericBLEDevice device, final GenericBLEScanRecord scanResult, final GenericRSSI rssi)
        {

            // Sanity check for SENS device. Some error on 6.0 android
            if (!device.getNormalizedName().equals("s"))
            {
                ErrorHandling.report(
                        new FoundInvalidDeviceException(device.getAddress().macString().concat(" wrong name ").concat(device.getNormalizedName()))
                );
                return;
            }

            // Sanity check, length of manuf data
            byte[] manufData = scanResult.getManufData(0xFF00);
            if (manufData.length != 8)
            {
                ErrorHandling.report(
                        new FoundInvalidDeviceException(device.getAddress().macString().concat(" wrong data len ").concat(ByteUtils.toHexString(manufData)))
                );
                return;
            }

            // Santfy check, manuf data contains mac
            String shortName2 = String.format("%02X:%02X", manufData[2], manufData[1]);
            String shortName1 = device.getAddress().macString().substring(12);
            if (!shortName2.equals(shortName1))
            {
                ErrorHandling.report(
                        new FoundInvalidDeviceException(device.getAddress().macString().concat(" wrong data content ").concat(ByteUtils.toHexString(manufData)))
                );
                return;
            }

            BLEAddress address = device.getAddress();
            ExtendedBLEPeripheral peripheral = mPeripherals.get(address);
            if (peripheral == null)
            {
                if (device.getName() != null)
                {
                    Log.i(TAG, String.format("NEW DEVICE %s %s", device.getAddress(), device.getName()));
                }
                else
                {
                    Log.i(TAG, String.format("NEW DEVICE %s", device.getAddress()));
                }
                peripheral = new ExtendedBLEPeripheral(ExtendedBLEScanner.this, device, scanResult, rssi);
                mPeripherals.put(address, peripheral);

                if (mDelegate != null)
                {
                    mDelegate.onNewPeripheralDiscovered(peripheral, scanResult);
                }
            }
            else
            {
                //Log.i(TAG, String.format("OLD DEVICE %s", device.getAddress()));
                peripheral.onDiscovered(device, scanResult, rssi);
            }
        }

    };

    public ExtendedBLEScanner(BleScannerDelegate delegate)
    {
        mDelegate = delegate;
        mScanning = false;
        mScanningPaused = false;
        mScanFailed = false;
        mLastScan = new Timestamp();

        mPeripherals = new HashMap<>();
    }

    public void undiscoverAll()
    {
        GenericBLEScanner.undiscoverAll();
    }

    public void reset()
    {
        GenericBLEScanner.reset();
    }

    public void undiscover(ExtendedBLEPeripheral peripheral)
    {
        ExtendedBLEPeripheral p = mPeripherals.remove(peripheral.getDevice().getAddress());
        if (p != null)
        {
            p.setDelegate(null);
            p.close();
        }
    }

    public boolean isBluetoothEnabled() { return GenericBLEScanner.isBluetoothEnabled(); }

    public boolean isBluetoothBroken() { return scanFailedCNt >= 6; }

    public void enableBluetooth() { GenericBLEScanner.enableBluetooth(); }

    public void disableBluetooth() { GenericBLEScanner.disableBluetooth(); }

    public boolean lastScanFailed()
    {
        return mScanFailed;
    }

    public ArrayList<ExtendedBLEPeripheral> getRecent(long onlineSeconds, long timeoutSeconds)
    {
        ArrayList<ExtendedBLEPeripheral> list = new ArrayList<>();
        for (ExtendedBLEPeripheral p: mPeripherals.values())
        {
            if (p.isOnline(onlineSeconds) && !p.getLastSeen().longerThanSecondsAgo(timeoutSeconds))
            {
                list.add(p);
            }
        }
        return list;
    }

    public synchronized boolean isScanning()
    {
        return mScanning;
    }

    public synchronized boolean startScanWithTimeout(TimeDelta timeout)
    {
        if (startScan())
        {
            MainThreadRun.runDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    ExtendedBLEScanner.this.stopScan();
                }
            }, timeout.asMSeconds());
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setServiceUUID(UUID serviceUUID)
    {
        mServiceUUID = serviceUUID;
    }

    public synchronized boolean startScan()
    {
        Log.i(TAG, "startScan");
        if (!mScanning || mScanningPaused)
        {
            GenericBLEScanner.scanForDevices(mServiceUUID, scanCallback);

            mScanning = true;
            if (mDelegate != null && !mScanningPaused)
            {
                mDelegate.onScanningStarted();
            }
            mScanningPaused = false;

            return true;
        }

        return false;
    }

    int scanFailedCNt = 0;

    public static class ResetBLEManagerException extends Exception
    {
    }

    public synchronized boolean stopScan()
    {
        mLastScan.setToNow();
        Log.i(TAG, "stopScan");
        if (mScanning)
        {
            mScanFailed = !GenericBLEScanner.isScanning();
            GenericBLEScanner.stopScan();

            if (mScanFailed)
            {
                scanFailedCNt += 1;
                if (scanFailedCNt >= 3 && (scanFailedCNt % 3 == 0))
                {
                    //Bugsnag.leaveBreadcrumb(String.format("Failure Count %d", scanFailedCNt));
                    ErrorHandling.report(new ResetBLEManagerException());
                    GenericBLEScanner.reset();
                }
            }
            else
            {
                scanFailedCNt = 0;
            }

            MainThreadRun.runDelayed( () ->
            {
                mScanning = false;
                if (mDelegate != null)
                {
                    mDelegate.onScanningStopped();
                }
            }, 500);

            return true;
        }

        return false;
    }

    public synchronized boolean pauseScan()
    {
        Log.i(TAG, "pauseScan");
        if (mScanning && !mScanningPaused)
        {
            GenericBLEScanner.stopScan();
            mScanningPaused = true;
            return true;
        }

        return false;
    }

    public synchronized boolean unpauseScan()
    {
        Log.i(TAG, "unpauseScan");
        if (mScanning && mScanningPaused)
        {
            this.startScan();
            return true;
        }

        return false;
    }

    public synchronized ExtendedBLEPeripheral getPeripheralFromAddress(BLEAddress address)
    {
        ExtendedBLEPeripheral peripheral = mPeripherals.get(address);
        if (peripheral == null)
        {
            peripheral = new ExtendedBLEPeripheral(address);
            mPeripherals.put(address, peripheral);
        }
        return peripheral;
    }

    public interface BleScannerDelegate
    {
        void onNewPeripheralDiscovered(ExtendedBLEPeripheral device, GenericBLEScanRecord scanRecord);
        void onScanningStopped();
        void onScanningStarted();
    }

    public Timestamp getLastScanTime()
    {
        if (mScanning)
        {
            return Timestamp.now();
        }
        else
        {
            return mLastScan;
        }
    }
}
