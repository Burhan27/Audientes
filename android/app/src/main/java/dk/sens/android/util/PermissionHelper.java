package dk.sens.android.util;

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

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.util.Log;

public class PermissionHelper
{
    private static final String TAG = "SNS PermissionHelper";

    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int RC_HANDLE_BLE_PERM = 3;

    /*
     * Location Enabled
     */

    public static boolean hasLocationEnabled(Context context)
    {
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /*
     * Bluetooth Permission
     */

    private static void requestBletoothPermission(Activity activity)
    {
        Log.w(TAG, "Bluetooth permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};

        ActivityCompat.requestPermissions(activity, permissions, RC_HANDLE_BLE_PERM);
    }

    public static boolean hasBluetoothPermission(Context context)
    {
        int rc = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        Log.i(TAG, "BLE permission " + rc);
        return (rc == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean checkBluetoothPermission(Activity activity)
    {
        if (hasBluetoothPermission(activity))
        {
            return true;
        }
        else
        {
            requestBletoothPermission(activity);
            return false;
        }
    }

    /*
     * Camera Permission
     */

    private static void requestCameraPermission(Activity activity)
    {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(activity, permissions, RC_HANDLE_CAMERA_PERM);
    }

    public static boolean checkCameraPermission(Activity activity)
    {
        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        Log.i(TAG, "Camera permission " + rc);
        if (rc == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            requestCameraPermission(activity);
            return false;
        }
    }

    /*
     *
     */

    public static boolean onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        /*
        if (requestCode != RC_HANDLE_CAMERA_PERM && requestCode != RC_HANDLE_BLE_PERM)
        {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        */

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


}
