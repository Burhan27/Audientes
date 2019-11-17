package com.professional.device.rpc;

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

import java.util.UUID;

import dk.sens.android.ble.ExtendedBLEPeripheral;
import dk.sens.android.ble.generic.GenericBLEDevice;
import dk.sens.android.ble.tasks.BLECompositeTask;
import dk.sens.android.ble.tasks.WriteCharTask;
import dk.sens.android.util.ByteUtils;


public class RPCCallTask extends BLECompositeTask
{
    static final String TAG = "S-BLE RPCCallTask";

    private int mCode;
    private RPCResponse mReply;

    public RPCCallTask(int code)
    {
        this(code, new byte[0]);
    }

    public RPCCallTask()
    {
        mCode = RPCCodes.ID_INVALID;
    }

    public RPCCallTask(int code, byte[] arg)
    {
        build(code, arg);
    }

    protected void build(int code, byte[] arg)
    {
        mCode = code;
        mReply = new RPCResponse(code);

        int left = arg.length;
        boolean first = true;
        while(left > 0 || first)
        {
            int l = Math.min(18, left);
            byte[] part = new byte[l + 1];
            if (l == left)
            {
                part[0] = (byte)(code);
            }
            else
            {
                part[0] = (byte)((int)0x80 + (int)code);
            }
            if (l > 0)
            {
                System.arraycopy(arg, arg.length - left, part, 1, l);
            }
            left -= l;
            Log.i(TAG, "Planning to write " + (l + 1) + " bytes " + ByteUtils.toHexString(part));
            this.addSubTask(new WriteCharTask(UUIDs.SERVICE_UUID, UUIDs.CALL_CHAR_UUID, part));
            first = false;
        }
    }

    @Override
    public void onCharacteristicChanged(GenericBLEDevice device, UUID char_uuid, byte[] data)
    {
        if (!char_uuid.equals(UUIDs.REPLY_CHAR_UUID))
        {
            return;
        }
        if (data[0] < 0 || data[0] > 30)
        {
            return;
        }
        Log.i(TAG, "Reply Char");
        mReply.addPacket(data);
        if (!mReply.isWaiting())
        {
            this.parseReply(mReply.getResponse());
            if (mReply.getErrorCode() == RPCCodes.STATUS_OK)
            {
                Log.i(TAG, "Completed (" + mCode + ") " + ByteUtils.toHexString(mReply.getResponse()));
                this.onCompleted(true);
            }
            else
            {
                Log.i(TAG, "Completed (" + mCode + ") With Error Code " + mReply.getErrorCode());
                this.onCompleted(true);
            }
        }
    }

    protected void parseReply(byte[] data) {}

    @Override
    public void onSubTasksCompleted(boolean success)
    {
        // Don't complete on success, wait for reply
        if (!success)
        {
            super.onSubTasksCompleted(success);
        }
    }

    public int getRPCStatus()
    {
        return mReply.getErrorCode();
    }

    public int getRPCCode()
    {
        return mCode;
    }

    public byte[] getRPCReply() { return mReply.getResponse(); }

    @Override
    public void execute(ExtendedBLEPeripheral peripheral)
    {
        Log.i(TAG, "Executing");
        super.execute(peripheral);
    }
}
