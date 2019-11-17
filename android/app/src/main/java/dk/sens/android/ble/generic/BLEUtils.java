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

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.List;


public class BLEUtils
{
    private static final String TAG = "BLEUtils";

    public static void restartBLEAdapter(Context context){
        final BluetoothManager btManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();

        if (btAdapter.isEnabled()){
            Log.i(TAG, "Restarting BLEAdapter");
            btAdapter.disable();
            while(btAdapter.getState() != BluetoothAdapter.STATE_OFF);
            btAdapter.enable();
        }
        else{
            btAdapter.enable();
        }
        while(btAdapter.getState() != BluetoothAdapter.STATE_ON);
        Log.i(TAG, "BLEAdapter (Re)started");
    }

    public static boolean checkConnected(Context context, String name)
    {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        List<BluetoothDevice> devices = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);
        for(BluetoothDevice device : devices) {
            if(device.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                Log.i(TAG, String.format("Already Connected %s %s", device.getAddress(), device.getName()));
                if (device.getName().equals(name))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isBluetoothEnabled()
    {
        return GenericBLEScanner.isBluetoothEnabled();
    }

    public static void enableBluetooth()
    {
        GenericBLEScanner.enableBluetooth();
    }

    public static void disableBluetooth()
    {
        GenericBLEScanner.disableBluetooth();
    }

}
