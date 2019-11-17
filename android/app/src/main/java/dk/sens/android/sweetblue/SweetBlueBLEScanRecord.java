package dk.sens.android.sweetblue;

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

import com.sweetblue.utils.BleScanInfo;

import dk.sens.android.ble.generic.GenericBLEScanRecord;
import dk.sens.android.ble.generic.GenericBLEScanRecordImpl;

public class SweetBlueBLEScanRecord implements GenericBLEScanRecordImpl
{
    BleScanInfo mScanRecord;

    private SweetBlueBLEScanRecord(BleScanInfo scanRecord)
    {
        mScanRecord = scanRecord;
    }

    public static GenericBLEScanRecord create(BleScanInfo scanRecord)
    {
        return new GenericBLEScanRecord(new SweetBlueBLEScanRecord(scanRecord));
    }

    @Override
    public byte[] getManufData(long id)
    {
        return mScanRecord.getManufacturerData();
    }
}
