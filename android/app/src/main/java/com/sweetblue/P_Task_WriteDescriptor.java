package com.sweetblue;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;

import com.sweetblue.BleManager.UhOhListener.UhOh;
import com.sweetblue.utils.FutureData;
import com.sweetblue.utils.Utils;

import java.util.UUID;

final class P_Task_WriteDescriptor extends PA_Task_ReadOrWrite
{
	private final UUID m_descriptorUuid;

	private final FutureData m_futureData;
	private byte[] m_data = null;

	public P_Task_WriteDescriptor(BleDevice device, BluetoothGattDescriptor descriptor, FutureData futureData, boolean requiresBonding, BleDevice.ReadWriteListener readListener, BleTransaction txn, PE_TaskPriority priority)
	{
		super(device, descriptor.getCharacteristic(), readListener, requiresBonding, txn, priority);

		m_descriptorUuid = descriptor.getUuid();
		m_futureData = futureData;
	}
	
	@Override protected BleDevice.ReadWriteListener.ReadWriteEvent newReadWriteEvent(BleDevice.ReadWriteListener.Status status, int gattStatus, BleDevice.ReadWriteListener.Target target, UUID serviceUuid, UUID charUuid, UUID descUuid)
	{
		return new BleDevice.ReadWriteListener.ReadWriteEvent(getDevice(), serviceUuid, charUuid, descUuid, null, BleDevice.ReadWriteListener.Type.WRITE, target, m_data, status, gattStatus, getTotalTime(), getTotalTimeExecuting(), /*solicited=*/true);
	}

	@Override protected UUID getDescUuid()
	{
		return m_descriptorUuid;
	}

	@Override protected BleDevice.ReadWriteListener.Target getDefaultTarget()
	{
		return BleDevice.ReadWriteListener.Target.DESCRIPTOR;
	}

	@Override protected void executeReadOrWrite()
	{
		m_data = m_futureData.getData();

		if( false == write_earlyOut(m_data) )
		{
			final BluetoothGattDescriptor desc_native = getDevice().getNativeDescriptor(getServiceUuid(), getCharUuid(), getDescUuid());

			if( desc_native == null )
			{
				fail(BleDevice.ReadWriteListener.Status.NO_MATCHING_TARGET, BleStatuses.GATT_STATUS_NOT_APPLICABLE, getDefaultTarget(), getCharUuid(), getDescUuid());
			}
			else
			{
				if( false == getDevice().layerManager().setDescValue(desc_native, m_data) )
				{
					fail(BleDevice.ReadWriteListener.Status.FAILED_TO_SET_VALUE_ON_TARGET, BleStatuses.GATT_STATUS_NOT_APPLICABLE, getDefaultTarget(), getCharUuid(), getDescUuid());
				}
				else
				{
					if( false == getDevice().layerManager().writeDescriptor(desc_native) )
					{
						fail(BleDevice.ReadWriteListener.Status.FAILED_TO_SEND_OUT, BleStatuses.GATT_STATUS_NOT_APPLICABLE, getDefaultTarget(), getCharUuid(), getDescUuid());
					}
					else
					{
						// DRK > SUCCESS, for now...
					}
				}
			}
		}
	}
	
	public void onDescriptorWrite(BluetoothGatt gatt, UUID uuid, int gattStatus)
	{
		getManager().ASSERT(getDevice().layerManager().gattEquals(gatt));

//		if( !this.isForCharacteristic(uuid) )  return;

		if( false == acknowledgeCallback(gattStatus) )  return;

		if( Utils.isSuccess(gattStatus) )
		{
			succeedWrite();
		}
		else
		{
			fail(BleDevice.ReadWriteListener.Status.REMOTE_GATT_FAILURE, gattStatus, getDefaultTarget(), getCharUuid(), getDescUuid());
		}
	}
	
	@Override public void onStateChange(PA_Task task, PE_TaskState state)
	{
		super.onStateChange(task, state);
		
		if( state == PE_TaskState.TIMED_OUT )
		{
			getLogger().w(getLogger().descriptorName(getDescUuid()) + " read timed out!");

			final BleDevice.ReadWriteListener.ReadWriteEvent event = newReadWriteEvent(BleDevice.ReadWriteListener.Status.TIMED_OUT, BleStatuses.GATT_STATUS_NOT_APPLICABLE, getDefaultTarget(), getServiceUuid(), getCharUuid(), getDescUuid());
			
			getDevice().invokeReadWriteCallback(m_readWriteListener, event);
			
			getManager().uhOh(UhOh.WRITE_TIMED_OUT);
		}
		else if( state == PE_TaskState.SOFTLY_CANCELLED )
		{
			final BleDevice.ReadWriteListener.ReadWriteEvent event = newReadWriteEvent(getCancelType(), BleStatuses.GATT_STATUS_NOT_APPLICABLE, getDefaultTarget(), getServiceUuid(), getCharUuid(), getDescUuid());
			getDevice().invokeReadWriteCallback(m_readWriteListener, event);
		}
	}
	
	@Override protected BleTask getTaskType()
	{
		return BleTask.WRITE_DESCRIPTOR;
	}
}
