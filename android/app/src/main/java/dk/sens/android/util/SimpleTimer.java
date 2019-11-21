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

public class SimpleTimer implements Runnable
{
    Runnable mTask;
    boolean mTriggered;
    boolean mStarted;
    boolean mGuiThread;
    int mPeriodMS;

    public SimpleTimer(Runnable task, boolean guiThread)
    {
        mTask = task;
        mTriggered = false;
        mStarted = false;
        mGuiThread = guiThread;
        mPeriodMS = 0;
    }

    public SimpleTimer(Runnable task)
    {
        this(task, false);
    }

    public void start(int timeout_ms)
    {
        startPeriodic(timeout_ms, 0);
    }

    public void startPeriodic(int period_ms)
    {
        startPeriodic(period_ms, period_ms);
    }

    public void startPeriodic(int timeout_ms, int period_ms)
    {
        stop();
        mTriggered = false;
        mStarted = true;
        mPeriodMS = period_ms;
        MainThreadRun.runDelayed(this, timeout_ms);
    }

    public void stop()
    {
        MainThreadRun.cancel(this);
        mTriggered = false;
        mStarted = false;
    }

    public boolean isStarted()
    {
        return (mStarted && !mTriggered);
    }

    @Override
    public void run()
    {
        mTask.run();
        if (mPeriodMS == 0)
        {
            mTriggered = true;
            mStarted = false;
        }
        else
        {
            if (mStarted)
            {
                MainThreadRun.runDelayed(this, mPeriodMS);
            }
        }
    }
}
