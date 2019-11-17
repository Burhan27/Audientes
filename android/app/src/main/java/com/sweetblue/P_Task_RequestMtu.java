package com.sweetblue;

import android.bluetooth.BluetoothGatt;

import com.sweetblue.utils.Utils;


final class P_Task_RequestMtu extends PA_Task_Transactionable implements PA_Task.I_StateListener
{

	protected final BleDevice.ReadWriteListener m_readWriteListener;
	private final int m_mtu;


	public P_Task_RequestMtu(BleDevice device, BleDevice.ReadWriteListener readWriteListener, BleTransaction txn_nullable, PE_TaskPriority priority, final int mtu)
	{
		super(device, txn_nullable, false, priority);
		
		m_readWriteListener = readWriteListener;
		m_mtu = mtu;
	}
	
	private BleDevice.ReadWriteListener.ReadWriteEvent newEvent(BleDevice.ReadWriteListener.Status status, int gattStatus, int mtu)
	{
		return new BleDevice.ReadWriteListener.ReadWriteEvent(getDevice(), /*mtu=*/mtu, status, gattStatus, getTotalTime(), getTotalTimeExecuting(), /*solicited=*/true);
	}

	@Override protected final void onNotExecutable()
	{
		super.onNotExecutable();

		getDevice().invokeReadWriteCallback(m_readWriteListener, newEvent(BleDevice.ReadWriteListener.Status.NOT_CONNECTED, BleStatuses.GATT_STATUS_NOT_APPLICABLE, 0));
	}
	
	private void fail(BleDevice.ReadWriteListener.Status status, int gattStatus)
	{
		this.fail();

		getDevice().invokeReadWriteCallback(m_readWriteListener, newEvent(status, gattStatus, 0));
	}

	@Override public final void execute()
	{
		if( Utils.isLollipop() )
		{
			if( false == getDevice().layerManager().requestMtu(m_mtu))
			{
				fail(BleDevice.ReadWriteListener.Status.FAILED_TO_SEND_OUT, BleStatuses.GATT_STATUS_NOT_APPLICABLE);
			}
			else
			{
				// SUCCESS, so far...
			}
		}
		else
		{
			//--- DRK > Should be checked for before the task is even created but just being anal.
			fail(BleDevice.ReadWriteListener.Status.ANDROID_VERSION_NOT_SUPPORTED, BleStatuses.GATT_STATUS_NOT_APPLICABLE);
		}
	}
	
	private void succeed(int gattStatus, int mtu)
	{
		super.succeed();

		getDevice().onMtuChanged();

		final BleDevice.ReadWriteListener.ReadWriteEvent event = newEvent(BleDevice.ReadWriteListener.Status.SUCCESS, gattStatus, mtu);
		
		getDevice().invokeReadWriteCallback(m_readWriteListener, event);
	}
	
	final void onMtuChanged(BluetoothGatt gatt, int mtu, int gattStatus)
	{
		getManager().ASSERT(getDevice().layerManager().gattEquals(gatt));
		
		if( Utils.isSuccess(gattStatus) )
		{
			succeed(gattStatus, mtu);
		}
		else
		{
			fail(BleDevice.ReadWriteListener.Status.REMOTE_GATT_FAILURE, gattStatus);
		}
	}
	
	@Override public final void onStateChange(PA_Task task, PE_TaskState state)
	{
		if( state == PE_TaskState.TIMED_OUT )
		{
			getDevice().invokeReadWriteCallback(m_readWriteListener, newEvent(BleDevice.ReadWriteListener.Status.TIMED_OUT, BleStatuses.GATT_STATUS_NOT_APPLICABLE, 0));
		}
		else if( state == PE_TaskState.SOFTLY_CANCELLED )
		{
			getDevice().invokeReadWriteCallback(m_readWriteListener, newEvent(getCancelType(), BleStatuses.GATT_STATUS_NOT_APPLICABLE, 0));
		}
	}
	
	@Override protected final BleTask getTaskType()
	{
		return BleTask.SET_MTU;
	}
}
