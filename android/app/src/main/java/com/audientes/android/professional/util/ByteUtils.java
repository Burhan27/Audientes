package com.audientes.android.professional.util;

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

    public static int byteToUnsignedInt(byte b) {
        return b & 0xFF;
    }

    /**
     * Turns 2 bytes from the firmware into an integer, but note that the firmware seems to have a
     * different byte-endianness. Ex: passing in [0x01, 0x00] returns 0x0001 and not 0x0100!
     *
     * @param array    byte array
     * @param startIdx start index to find the uint16 integer
     * @return integer value of the uint16
     */
    public static int uint16BytesToInt(byte[] array, int startIdx) {
        return (array[startIdx + 1] & 0x0000FF00) | (array[startIdx] & 0x000000FF);
    }


    public static long int32(byte[] data, int i)
    {
        return (data[i] & 0x000000FF) | ((data[i+1] << 8) & 0x0000FF00) | ((data[i+2] << 16) & 0x00FF0000) | ((data[i+3] << 24) & 0xFF000000);
    }

    public static int int16(byte[] data, int i)
    {
        return (data[i] & 0xFF) | ((data[i+1] & 0xFF) << 8);
    }

    public static int int16_be(byte[] data, int i)
    {
        return (data[i+1] & 0xFF) | ((data[i] & 0xFF) << 8);
    }

    public static int int8(byte[] data, int i)
    {
        return (data[i] & 0xFF);
    }
}
