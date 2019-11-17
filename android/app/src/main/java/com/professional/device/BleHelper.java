package com.professional.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by morten on 03/08/2017.
 */

public class BleHelper
{
    private static final String TAG = "BleHelper";

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

}
