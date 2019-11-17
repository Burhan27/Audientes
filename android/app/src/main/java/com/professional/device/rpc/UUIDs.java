package com.professional.device.rpc;

import java.util.UUID;

import dk.sens.android.ble.UUIDs.*

;

/**
 * Created by morten on 28/05/2017.
 */

public class UUIDs
{
    public static final String AUDIENTES_UUID_ROOT = "e2190000-ec7a-45d0-9ecf-6ccfdee39a27";

    public static final UUID SERVICE_UUID = UUIDs.fromShort("0001", AUDIENTES_UUID_ROOT);
    public static final UUID VERSION_CHAR_UUID = UUIDs.fromShort("0002", AUDIENTES_UUID_ROOT);
    public static final UUID CALL_CHAR_UUID = UUIDs.fromShort("0003", AUDIENTES_UUID_ROOT);
    public static final UUID REPLY_CHAR_UUID = UUIDs.fromShort("0004", AUDIENTES_UUID_ROOT);

}
