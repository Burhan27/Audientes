package dk.sens.android.util;

/**
 * Created by morten on 10/11/2016.
 */

public class UIUpdater
{
    Runnable mUpdateFunction;
    SimpleTimer mUpdateTimer;

    public UIUpdater(Runnable updateFunction)
    {
        mUpdateFunction = updateFunction;
        mUpdateTimer = new SimpleTimer(new Runnable() {
            public void run() {
                UIUpdater.this.trigger();
            }
        }, true);
    }

    public void startPeriodic()
    {
        mUpdateTimer.stop();
        mUpdateTimer.startPeriodic(1000, 1000);
    }

    public void startFast()
    {
        mUpdateTimer.stop();
        mUpdateTimer.startPeriodic(250, 250);
    }

    public void stop()
    {
        mUpdateTimer.stop();
    }

    private void trigger()
    {
        mUpdateFunction.run();
    }
}
