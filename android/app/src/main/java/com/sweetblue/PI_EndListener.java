package com.sweetblue;

import com.sweetblue.BleTransaction.EndReason;

interface PI_EndListener
{
	void onTransactionEnd(BleTransaction txn, EndReason reason, BleDevice.ReadWriteListener.ReadWriteEvent failReason);
}