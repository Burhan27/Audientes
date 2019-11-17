package com.sweetblue;


import com.sweetblue.utils.Utils;


final class P_Task_RequestConnectionPriority extends PA_Task_Transactionable implements PA_Task.I_StateListener
{
	protected final BleDevice.ReadWriteListener m_readWriteListener;
	private final BleConnectionPriority m_connectionPriority;

	public P_Task_RequestConnectionPriority(BleDevice device, BleDevice.ReadWriteListener readWriteListener, BleTransaction txn_nullable, PE_TaskPriority priority, final BleConnectionPriority connectionPriority)
	{
		super(device, txn_nullable, false, priority);
		
		m_readWriteListener = readWriteListener;
		m_connectionPriority = connectionPriority;
	}
	
	private BleDevice.ReadWriteListener.ReadWriteEvent newEvent(final BleDevice.ReadWriteListener.Status status, final int gattStatus, final BleConnectionPriority connectionPriority)
	{
		return new BleDevice.ReadWriteListener.ReadWriteEvent(getDevice(), /*connectionPriority=*/connectionPriority, status, gattStatus, getTotalTime(), getTotalTimeExecuting(), /*solicited=*/true);
	}

	@Override protected void onNotExecutable()
	{
		super.onNotExecutable();

		getDevice().invokeReadWriteCallback(m_readWriteListener, newEvent(BleDevice.ReadWriteListener.Status.NOT_CONNECTED, BleStatuses.GATT_STATUS_NOT_APPLICABLE, m_connectionPriority));
	}
	
	private void fail(BleDevice.ReadWriteListener.Status status, int gattStatus)
	{
		this.fail();

		getDevice().invokeReadWriteCallback(m_readWriteListener, newEvent(status, gattStatus, m_connectionPriority));
	}

	@Override public void execute()
	{
		if( Utils.isLollipop() )
		{
			if( false == getDevice().layerManager().requestConnectionPriority(m_connectionPriority) )
			{
				fail(BleDevice.ReadWriteListener.Status.FAILED_TO_SEND_OUT, BleStatuses.GATT_STATUS_NOT_APPLICABLE);
			}
			else
			{
				// SUCCESS, and we'll wait a half second or so (for now hardcoded) until actually succeeding, cause there's no native callback for this one.
			}
		}
		else
		{
			//--- DRK > Should be checked for before the task is even created but just being anal.
			fail(BleDevice.ReadWriteListener.Status.ANDROID_VERSION_NOT_SUPPORTED, BleStatuses.GATT_STATUS_NOT_APPLICABLE);
		}
	}

	@Override protected void update(double timeStep)
	{
		final double timeToSuccess = .25d; //TODO

		if( getState() == PE_TaskState.EXECUTING && getTotalTimeExecuting() >= timeToSuccess )
		{
			succeed(m_connectionPriority);
		}
	}
	
	private void succeed(final BleConnectionPriority connectionPriority)
	{
		super.succeed();

		final BleDevice.ReadWriteListener.ReadWriteEvent event = newEvent(BleDevice.ReadWriteListener.Status.SUCCESS, BleStatuses.GATT_SUCCESS, connectionPriority);
		
		getDevice().invokeReadWriteCallback(m_readWriteListener, event);
	}
	
	@Override public void onStateChange(PA_Task task, PE_TaskState state)
	{
		if( state == PE_TaskState.TIMED_OUT )
		{
			getDevice().invokeReadWriteCallback(m_readWriteListener, newEvent(BleDevice.ReadWriteListener.Status.TIMED_OUT, BleStatuses.GATT_STATUS_NOT_APPLICABLE, m_connectionPriority));
		}
		else if( state == PE_TaskState.SOFTLY_CANCELLED )
		{
			getDevice().invokeReadWriteCallback(m_readWriteListener, newEvent(getCancelType(), BleStatuses.GATT_STATUS_NOT_APPLICABLE, m_connectionPriority));
		}
		else if( state == PE_TaskState.SUCCEEDED )
		{
			getDevice().updateConnectionPriority(m_connectionPriority);
		}
	}
	
	@Override protected BleTask getTaskType()
	{
		return BleTask.SET_CONNECTION_PRIORITY;
	}
}
