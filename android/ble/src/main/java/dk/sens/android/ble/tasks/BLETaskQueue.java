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

import java.util.LinkedList;
import java.util.Queue;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import dk.sens.android.ble.ExtendedBLEPeripheral;

public class BLETaskQueue
{
    static final String TAG = "S-BLE BLETaskQueue";

    Queue<BLETask> mTaskQueue;
    public boolean busy;
    ExtendedBLEPeripheral mPeripheral;

    public BLETaskQueue(ExtendedBLEPeripheral peripheral)
    {
        mTaskQueue = new LinkedList<BLETask>();
        busy = false;
        mPeripheral = peripheral;
    }

    public void stop()
    {
        for (BLETask t: mTaskQueue)
        {
            t.stop();
        }
    }

    public void clear()
    {
        this.stop();
        mTaskQueue.clear();
        busy = false;
    }

    public void add(BLETask task)
    {
        task.setQueue(this);
        mTaskQueue.add(task);
        handleNext();
    }

    public void taskCompleted(BLETask task, boolean success)
    {
        Log.i(TAG, "taskCompleted");
        if (!success)
        {
            ExtendedBLEPeripheral.ExtendedBLEPeripheralDelegate d = mPeripheral.getDelegate();
            this.clear();
            if (d != null) { d.onPeripheralTasksFailed(mPeripheral); }
        }
        else
        {

            if (task == mTaskQueue.peek())
            {
                task.stop();
                mTaskQueue.poll();
                busy = false;
            }
            handleNext();

            ExtendedBLEPeripheral.ExtendedBLEPeripheralDelegate d = mPeripheral.getDelegate();
            if (d != null)
            {
                d.onPeripheralTaskCompleted(mPeripheral, task);
            }

            if (!busy)
            {
                Log.i(TAG, "allTaskCompleted");
                if (d != null)
                {
                    d.onPeripheralAllTasksCompleted(mPeripheral);
                }
            }
        }
    }

    private void onTaskQueueEmpty()
    {
        ExtendedBLEPeripheral.ExtendedBLEPeripheralDelegate d = mPeripheral.getDelegate();
        if (d != null) { d.onPeripheralAllTasksCompleted(mPeripheral); }
    }

    public void handleNext()
    {
        if (!busy)
        {
            if (!mTaskQueue.isEmpty())
            {
                busy = true;
                final BLETask task = mTaskQueue.peek();

                ExtendedBLEPeripheral.ExtendedBLEPeripheralDelegate d = mPeripheral.getDelegate();
                if (d != null) { d.onPeripheralTaskStarted(mPeripheral, task); }

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mPeripheral.mReadyForTasks)
                        {
                            task.execute(mPeripheral);
                        }
                        else
                        {
                            Log.w(TAG, "Trying to run task, but not ready");
                        }
                    }
                }, 10);
            }
        }
    }

    public BLETask peek()
    {
        return mTaskQueue.peek();
    }
}
