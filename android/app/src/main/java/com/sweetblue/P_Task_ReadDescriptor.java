package com.sweetblue;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;

import com.sweetblue.BleManager.UhOhListener.UhOh;

import java.util.UUID;

final class P_Task_ReadDescriptor extends PA_Task_ReadOrWrite
{
	private final BleDevice.ReadWriteListener.Type m_type;
	private final UUID m_descriptorUuid;

	public P_Task_ReadDescriptor(BleDevice device, BluetoothGattDescriptor descriptor, BleDevice.ReadWriteListener.Type type, boolean requiresBonding, BleDevice.ReadWriteListener readListener, BleTransaction txn, PE_TaskPriority priority)
	{
		super(device, descriptor.getCharacteristic(), readListener, requiresBonding, txn, priority);
		
		m_type = type;
		m_descriptorUuid = descriptor.getUuid();
	}
	
	@Override protected BleDevice.ReadWriteListener.ReadWriteEvent newReadWriteEvent(BleDevice.ReadWriteListener.Status status, int gattStatus, BleDevice.ReadWriteListener.Target target, UUID serviceUuid, UUID charUuid, UUID descUuid)
	{
		return new BleDevice.ReadWriteListener.ReadWriteEvent(getDevice(), serviceUuid, charUuid, descUuid, m_descriptorFilter, m_type, target, null, status, gattStatus, getTotalTime(), getTotalTimeExecuting(), /*solicited=*/true);
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
		final BluetoothGattDescriptor desc_native = getDevice().getNativeDescriptor(getServiceUuid(), getCharUuid(), getDescUuid());

		if( desc_native == null )
		{
			fail(BleDevice.ReadWriteListener.Status.NO_MATCHING_TARGET, BleStatuses.GATT_STATUS_NOT_APPLICABLE, BleDevice.ReadWriteListener.Target.DESCRIPTOR, getCharUuid(), getDescUuid());
		}
		else
		{
			if( false == getDevice().layerManager().readDescriptor(desc_native) )
			{
				fail(BleDevice.ReadWriteListener.Status.FAILED_TO_SEND_OUT, BleStatuses.GATT_STATUS_NOT_APPLICABLE, BleDevice.ReadWriteListener.Target.DESCRIPTOR, getCharUuid(), getDescUuid());
			}
			else
			{
				// DRK > SUCCESS, for now...
			}
		}
	}

	@Override
	public void onDescriptorRead(BluetoothGatt gatt, UUID uuid, byte[] value, int gattStatus)
	{
		getManager().ASSERT(getDevice().layerManager().gattEquals(gatt));

		onCharacteristicOrDescriptorRead(gatt, uuid, value, gattStatus, m_type);
	}
	
	@Override public void onStateChange(PA_Task task, PE_TaskState state)
	{
		super.onStateChange(task, state);
		
		if( state == PE_TaskState.TIMED_OUT )
		{
			getLogger().w(getLogger().descriptorName(getDescUuid()) + " read timed out!");

			final BleDevice.ReadWriteListener.ReadWriteEvent event = newReadWriteEvent(BleDevice.ReadWriteListener.Status.TIMED_OUT, BleStatuses.GATT_STATUS_NOT_APPLICABLE, BleDevice.ReadWriteListener.Target.DESCRIPTOR, getServiceUuid(), getCharUuid(), getDescUuid());
			
			getDevice().invokeReadWriteCallback(m_readWriteListener, event);
			
			getManager().uhOh(UhOh.READ_TIMED_OUT);
		}
		else if( state == PE_TaskState.SOFTLY_CANCELLED )
		{
			final BleDevice.ReadWriteListener.ReadWriteEvent event = newReadWriteEvent(getCancelType(), BleStatuses.GATT_STATUS_NOT_APPLICABLE, BleDevice.ReadWriteListener.Target.DESCRIPTOR, getServiceUuid(), getCharUuid(), getDescUuid());
			getDevice().invokeReadWriteCallback(m_readWriteListener, event);
		}
	}
	
	@Override protected BleTask getTaskType()
	{
		return BleTask.READ_DESCRIPTOR;
	}
}
