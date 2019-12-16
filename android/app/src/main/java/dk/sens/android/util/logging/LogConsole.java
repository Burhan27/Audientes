package dk.sens.android.util.logging;

import androidx.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by morten on 04/01/2018.
 */

public class LogConsole extends Timber.DebugTree
{
    static Date mStarted;
    static String mStartedString;

    public LogConsole()
    {
        mStarted = new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        mStartedString = format.format(mStarted);
    }

    @Override
    protected void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable t)
    {
        if (priority == Log.VERBOSE || priority == Log.DEBUG)
        {
            priority = Log.INFO;
        }
        super.log(priority, tag, "(" + mStartedString + ") " + message, t);
    }

}
