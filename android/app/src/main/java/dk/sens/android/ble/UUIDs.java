package dk.sens.android.ble;

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


public class UUIDs
{
    public static final UUID INVALID = new UUID(0x0, 0x0);
    public static final String STANDARD_UUID_TEMPLATE								= "00000000-0000-1000-8000-00805f9b34fb";


    public static UUID fromShort(String assignedNumber)
    {
        return fromShort(assignedNumber, STANDARD_UUID_TEMPLATE);
    }

    public static UUID fromShort(short assignedNumber)
    {
        return fromShort(assignedNumber, STANDARD_UUID_TEMPLATE);
    }

    public static UUID fromShort(int assignedNumber)
    {
        return fromShort((short)assignedNumber);
    }

    public static UUID fromShort(short assignedNumber, String uuidTemplate)
    {
        String hex = Integer.toHexString(assignedNumber & 0xffff);

        return fromShort(hex, uuidTemplate);
    }

    public static UUID fromShort(int assignedNumber, String uuidTemplate)
    {
        return fromShort((short) assignedNumber, uuidTemplate);
    }

    public static UUID fromShort(String assignedNumber, String uuidTemplate)
    {
        assignedNumber = chopOffHexPrefix(assignedNumber);

        if( assignedNumber_earlyOut(assignedNumber, 4) )  return INVALID;

        String uuid = uuidTemplate.substring(0, 4) + padAssignedNumber(assignedNumber, 4) + uuidTemplate.substring(8, uuidTemplate.length());

        return fromString(uuid);
    }

    public static UUID fromInt(final int assignedNumber)
    {
        return fromInt(assignedNumber, STANDARD_UUID_TEMPLATE);
    }

    public static UUID fromInt(int assignedNumber, String uuidTemplate)
    {
        final String hex = Integer.toHexString(assignedNumber);

        return fromInt(hex, uuidTemplate);
    }

    public static UUID fromInt(String assignedNumber)
    {
        return fromInt(assignedNumber, STANDARD_UUID_TEMPLATE);
    }

    public static UUID fromInt(String assignedNumber, String uuidTemplate)
    {
        assignedNumber = chopOffHexPrefix(assignedNumber);

        if( assignedNumber_earlyOut(assignedNumber, 8) )  return INVALID;

        String uuid = padAssignedNumber(assignedNumber, 8) + uuidTemplate.substring(8, uuidTemplate.length());

        return fromString(uuid);
    }


    public static UUID fromString(final String value)
    {
        if( value.length() == 4 || (value.length() == 6 && hasHexPrefix(value)) )
        {
            return fromShort(value);
        }
        else if( value.length() == 8 || (value.length() == 10 && hasHexPrefix(value)) )
        {
            return fromInt(value);
        }
        else
        {
            return UUID.fromString(value);
        }
    }

    private static boolean hasHexPrefix(final String string)
    {
        if( string == null )
        {
            return false;
        }
        else
        {
            if( string.length() > 2 )
            {
                if( string.charAt(0) == '0' && string.charAt(1) == 'x' )
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
    }

    private static String chopOffHexPrefix(final String string)
    {
        if( hasHexPrefix(string) )
        {
            return string.substring(2);
        }
        else
        {
            return string;
        }
    }

    private static boolean assignedNumber_earlyOut(final String assignedNumber, final int length)
    {
        if( assignedNumber == null || assignedNumber.isEmpty() || assignedNumber.length() > length )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private static String padAssignedNumber(String assignedNumber, final int length)
    {
        while(assignedNumber.length() < length )
        {
            assignedNumber = "0" + assignedNumber;
        }

        return assignedNumber;
    }
}
