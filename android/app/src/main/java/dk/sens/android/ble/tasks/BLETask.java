package dk.sens.android.ble.tasks;

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

import android.support.annotation.CallSuper;
import android.util.Log;

import dk.sens.android.ble.ExtendedBLEPeripheral;
import dk.sens.android.ble.generic.GenericBLEDevice;
import dk.sens.android.util.ErrorHandling;
import dk.sens.android.util.MainThreadRun;
import dk.sens.android.util.SimpleTimer;

public class BLETask
{
    static final String TAG ="S-BLE BLETask";

    Runnable onCompletedOK = null;
    Runnable onCompletedFailure = null;
    SimpleTimer mTimeoutTimer = null;
    int mTimeout = 15000;
    boolean mExecuting = false;

    BLETaskQueue mQueue = null;
    public void setQueue(BLETaskQueue q)
    {
        mQueue = q;
    }

    class BLETaskFailedException extends Exception
    {
        BLETaskFailedException(String msg) {super(msg);}
    }

    public BLETask()
    {
        mTimeoutTimer = new SimpleTimer(new Runnable()
        {
            @Override
            public void run()
            {
                //Bugsnag.leaveBreadcrumb(mQueue.mPeripheral.macString());
                String msg = String.format("%s - Timeout", this.getClass().getName());
                ErrorHandling.report(new BLETaskFailedException(msg));
                onCompleted(false);
            }
        }, false);
    }

    public BLETask setOnCompletedOK(Runnable task)
    {
        onCompletedOK = task; return this;
    }
    public BLETask setOnCompletedFailure(Runnable task)
    {
        onCompletedFailure = task; return this;
    }
    public BLETask setTimeout(int milliSeconds)
    {
        mTimeout = milliSeconds; return this;
    }

    @CallSuper
    public void execute(ExtendedBLEPeripheral peripheral)
    {
        Log.d(TAG, String.format("Executing %s", this.getClass().getName()));
        mExecuting = true;
        if (mTimeout != 0)
        {
            mTimeoutTimer.start(mTimeout);
        }
    }

    @CallSuper
    public void stop()
    {
        if (!mExecuting) { return; }
        mTimeoutTimer.stop();
    }

    public void onCompleted(boolean success)
    {
        mTimeoutTimer.stop();
        if (!mExecuting) { return; }
        mExecuting = false;
        if (success && onCompletedOK != null)
        {
            onCompletedOK.run();
        }
        else if (!success && onCompletedFailure != null)
        {
            Log.i(TAG, "Task Completed with Failure");
            onCompletedFailure.run();
        }
        MainThreadRun.run(new Runnable()
        {
            @Override
            public void run()
            {
                mQueue.taskCompleted(BLETask.this, success);
            }
        });
    }

    public void onDeviceDisconnected(GenericBLEDevice device, boolean userDisconnected)
    {
        onCompleted(false);
    }

    public void onServicesDiscovered(GenericBLEDevice device, boolean status) {}
    public void onCharacteristicRead(GenericBLEDevice device, UUID char_uuid, byte[] data, boolean status) {}
    public void onCharacteristicWrite(GenericBLEDevice device, UUID char_uuid, boolean status) {}
    public void onCharacteristicSubscribed(GenericBLEDevice device, UUID char_uuid, boolean status) {}
    public void onCharacteristicChanged(GenericBLEDevice device, UUID char_uuid, byte[] data) {}
}
