package dk.sens.android.util.logging;

import java.util.HashMap;

import dk.sens.android.util.TimeDelta;
import dk.sens.android.util.Timestamp;
import timber.log.Timber;

/**
 * Created by morten on 04/01/2018.
 */

public class LogContext
{
    public String tag;

    public static LogContext create(String tag)
    {
        return new LogContext(tag);
    }

    public static LogContext create(Object cls)
    {
        return new LogContext(cls.getClass().getSimpleName());
    }

    public LogContext(String tag)
    {
        this.tag = "SNS/" + tag;
    }

    HashMap<Integer, Timestamp> mClutterFilter = new HashMap<Integer, Timestamp>();

    private boolean shouldLog(TimeDelta timeout, String msg)
    {
        Integer key = msg.hashCode();
        Timestamp lastLog = mClutterFilter.get(key);
        if (lastLog == null || lastLog.longerThanTimeDeltaAgo(timeout))
        {
            mClutterFilter.put(key, Timestamp.now());
            return true;
        }
        else
        {
            return false;
        }
    }

    /*
     * Log Functions
     */


    public void e(String msg)
    {
        Timber.tag(this.tag);
        Timber.e(msg);
    }

    public void w(String msg)
    {
        Timber.tag(this.tag);
        Timber.w(msg);
    }

    public void i(String msg)
    {
        Timber.tag(this.tag);
        Timber.i(msg);
    }

    public void i(TimeDelta timeout, String msg)
    {
        if (shouldLog(timeout, msg))
        {
            Timber.tag(this.tag);
            Timber.i(msg);
        }
    }

    public void d(String msg)
    {
        Timber.tag(this.tag);
        Timber.d(msg);
    }

    public void v(String msg)
    {
        Timber.tag(this.tag);
        Timber.v(msg);
    }

    public void v(TimeDelta timeout, String msg)
    {
        if (shouldLog(timeout, msg))
        {
            Timber.tag(this.tag);
            Timber.v(msg);
        }
    }

}
