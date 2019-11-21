package com.audientes.android.professional.device.rpc;

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

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;

import dk.sens.android.util.ByteUtils;

public class RPCResponse implements Closeable
{
    private static final String TAG = "S-BLE RPCResponse";

    private static final int MORE_DATA_FLAG = 0x80;

    private ByteArrayOutputStream mResponseByteStream;

    private int mCode = RPCCodes.ID_INVALID;
    private int mErrorCode = RPCCodes.STATUS_OK;

    private boolean mNeedsMoreData = true;

    public RPCResponse() {
        this(RPCCodes.ID_INVALID);
    }

    public RPCResponse(int code) {
        mResponseByteStream = new ByteArrayOutputStream();
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public boolean isWaiting() {
        return mNeedsMoreData;
    }

    public byte[] getResponse() {
        return mResponseByteStream.toByteArray();
    }

    public void addPacket(byte[] packet) {
        Log.d(TAG, "Reply part: " + ByteUtils.toHexString(packet));

        mNeedsMoreData = ((packet[0] & MORE_DATA_FLAG) == MORE_DATA_FLAG);
        boolean isFirstPacket = mResponseByteStream.size() == 0;

        if (!mNeedsMoreData && ((packet[0] & 0x7F) != mCode)) {
            Log.w(TAG, String.format("Unexpected Command Reply %d != %d", packet[0], mCode));
            //throw new UnhandledResponseException("Received unexpected response!");
        }

        int dataStart = 1;
        if (isFirstPacket)
        {
            dataStart = 2;
            mErrorCode = packet[1];
        }
        Log.v(TAG, "Keeping: " + (packet.length - dataStart) + " bytes." );
        mResponseByteStream.write(packet, dataStart, (packet.length - dataStart));
    }

    public void reset() {
        mResponseByteStream.reset();
        mNeedsMoreData = true;
    }

    @Override
    public void close() {
        try {
            mResponseByteStream.close();
        } catch (Exception e) {
            Log.w(TAG, "Could not close bytestream.");
        }
    }

}
