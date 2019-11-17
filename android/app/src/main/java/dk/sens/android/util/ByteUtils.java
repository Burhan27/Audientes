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

public class ByteUtils
{
    public static final String EMPTY_HEX_STRING = "EMPTY_BYTE_ARRAY";
    private static final int BASE_16 = 16;

    /**
     * Converts a hex string to a byte array.
     *
     * @param hexString hex string
     * @return byte array containing the value represented by {@param hexString}
     */
    public static byte[] fromHexString(String hexString) {
        final int kCharactersPerByte = 2;
        final int byteLength = hexString.length() / kCharactersPerByte;
        byte[] data = new byte[byteLength];

        for (int i = 0; i < byteLength; i += 1) {
            int byteStartIndex = i * kCharactersPerByte;
            data[i] = (byte) ((Character.digit(hexString.charAt(byteStartIndex), BASE_16) << 4)
                    + Character.digit(hexString.charAt(byteStartIndex + 1), BASE_16));
        }

        return data;
    }

    public static String toHexString(byte[] bytes) {
        return toHexString(bytes, " ");
    }

    public static String toHexString(byte[] bytes, String separator) {
        if (bytes.length == 0) {
            return EMPTY_HEX_STRING;
        }

        int strLen = (bytes.length * 2) + (separator.length() * (bytes.length - 1));
        StringBuilder sb = new StringBuilder(strLen);

        for (int i = 0; i < bytes.length; ++i) {
            if (i + 1 == bytes.length) {
                sb.append(String.format("%02x", bytes[i] & 0xff));
            } else {
                sb.append(String.format("%02x%s", bytes[i] & 0xff, separator));
            }
        }

        return sb.toString().toUpperCase();
    }

    /*
     * New functions
     */

    public static long int32(byte[] data, int i)
    {
        return (data[i] & 0x000000FF) | ((data[i+1] << 8) & 0x0000FF00) | ((data[i+2] << 16) & 0x00FF0000) | ((data[i+3] << 24) & 0xFF000000);
    }

    public static long int32big(byte[] data, int i)
    {
        return (data[i+3] & 0x000000FF) | ((data[i+2] << 8) & 0x0000FF00) | ((data[i+1] << 16) & 0x00FF0000) | ((data[i] << 24) & 0xFF000000);
    }

    public static int int16(byte[] data, int i)
    {
        return (data[i] & 0xFF) | ((data[i+1] & 0xFF) << 8);
    }

    public static int int8(byte[] data, int i)
    {
        return (data[i] & 0xFF);
    }

    public static void int32bytes(long value, byte[] dest, int i)
    {
        dest[i] = (byte)(value & 0xFF);
        dest[i+1] = (byte)(value >> 8 & 0xFF);
        dest[i+2] = (byte)(value >> 16 & 0xFF);
        dest[i+3] = (byte)(value >> 24 & 0xFF);
    }

    public static void int16bytes(int value, byte[] dest, int i)
    {
        dest[i] = (byte)(value & 0xFF);
        dest[i+1] = (byte)(value >> 8 & 0xFF);
    }

    public static byte[] combine(byte[] a, byte[] b)
    {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
}
