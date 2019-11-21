package com.audientes.android.professional.device.rpc;

/**
 * Created by morten on 19/07/2017.
 */

public class RPCCodes
{
    public static final byte ID_INVALID = (byte)0xFF;

    public static final byte CMD_VERSION = 0x00;
    public static final byte CMD_GET_TIME = 0x01;

    public static final byte CMD_START_TEST = 0x10;
    public static final byte CMD_READ_TEST_RESULT = 0x11;
    public static final byte CMD_CANCEL_TEST = 0x12;
    public static final byte CMD_PLAY_PAUSE_TEST = 0x13;
    public static final byte CMD_GET_TEST_STATE = 0x14;

    public static final byte STATUS_OK = 0x00;

    public static final byte EVT_FIRST = 0x60;
    public static final byte EVT_TEST_PROGRESS = 0x60;
    public static final byte EVT_TRACE = 0x70;


    public static final byte RPC_TEST_STATE_INACTIVE = 0;
    public static final byte RPC_TEST_STATE_RUNNING = 1;
    public static final byte RPC_TEST_STATE_PAUSED = 2;
    public static final byte RPC_TEST_STATE_COMPLETED = 3;


    public static final int TRACE_STEP = 1;

}
