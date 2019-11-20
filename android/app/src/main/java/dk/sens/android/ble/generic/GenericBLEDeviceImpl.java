package dk.sens.android.ble.generic;

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

import android.os.Handler;

import java.util.UUID;

import dk.sens.android.ble.BLEAddress;

public abstract class GenericBLEDeviceImpl
{
    public abstract void connect();
    public abstract void disconnect();
    public abstract void close();
    public abstract boolean writeChar(UUID serviceUUID, UUID charUUID, byte[] data);
    public abstract boolean readChar(UUID serviceUUID, UUID charUUID);
    public abstract boolean subscribeChar(UUID uuid);
    public abstract boolean discoverServices();
    public abstract String getName();
    public abstract String getNormalizedName();
    public abstract BLEAddress getAddress();
    public GenericBLEDevice parent;

    protected Handler mHandler = new Handler();
}