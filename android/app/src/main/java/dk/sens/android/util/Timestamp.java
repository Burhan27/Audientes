package dk.sens.android.util;

import android.text.format.DateUtils;

import java.util.Date;

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

public class Timestamp
{
    Date time;

    public Timestamp()
    {
        time = null;
    }

    public Timestamp(Timestamp t)
    {
        time = t.time;
    }

    public static Timestamp now()
    {
        Timestamp time = new Timestamp();
        time.setToNow();
        return time;
    }

    public void setToNow()
    {
        time = new Date();
    }

    public void setToMSecondsAgo(long ms)
    {
        Date now = new Date();
        time = new Date(now.getTime() - ms);
    }

    public void addMSeconds(long ms)
    {
        time = new Date(time.getTime() + ms);
    }

    public boolean ever()
    {
        return time != null;
    }

    public boolean longerThanSecondsAgo(long seconds)
    {
        if (!ever())
        {
            return true;
        }
        else
        {
            return (((new Date()).getTime() - time.getTime()) / 1000) > seconds;
        }
    }

    public boolean longerThanSecondsAgoFrom(Timestamp epoch, long seconds)
    {
        if (!ever())
        {
            return true;
        }
        else if (!epoch.ever())
        {
            return false;
        }
        else
        {
            return ((epoch.time.getTime() - time.getTime()) / 1000) > seconds;
        }
    }

    public boolean longerThanTimeDeltaAgo(TimeDelta timedelta)
    {
        return longerThanSecondsAgo(timedelta.asSeconds());
    }

    public String agoString()
    {
        if (time == null)
        {
            return "never";
        }
        else
        {
            if (longerThanSecondsAgo(60))
            {
                return DateUtils.getRelativeTimeSpanString(time.getTime()).toString();
            }
            else
            {
                return "just now";
            }
        }
    }

    public String secondsAgoString()
    {
        if (time == null)
        {
            return "never";
        }
        else
        {
            return String.format("%d", (int)(((new Date()).getTime() - time.getTime()) / 1000));
        }
    }

    public TimeDelta agoTime()
    {
        Date now = new Date();
        return new TimeDelta(now.getTime() - this.time.getTime());
    }
}
