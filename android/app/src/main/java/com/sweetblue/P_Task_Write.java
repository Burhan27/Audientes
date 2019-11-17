package com.sweetblue;

import java.util.UUID;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.sweetblue.utils.FutureData;
import com.sweetblue.utils.Utils;
import com.sweetblue.BleManager.UhOhListener.UhOh;

final class P_Task_Write extends PA_Task_ReadOrWrite
{

	private byte[] m_data = null;

	private final FutureData m_futureData;
	private final BleDevice.ReadWriteListener.Type m_writeType;


	public P_Task_Write(BleDevice device, BluetoothGattCharacteristic characteristic, final FutureData futureData, boolean requiresBonding, BleDevice.ReadWriteListener.Type writeType, BleDevice.ReadWriteListener writeListener, BleTransaction txn, PE_TaskPriority priority)
	{
		super(device, characteristic, writeListener, requiresBonding, txn, priority);

		m_futureData = futureData;

		m_writeType = writeType;
	}

	public P_Task_Write(BleDevice device, UUID serviceUuid, UUID charUuid, DescriptorFilter filter, final FutureData futureData, boolean requiresBonding, BleDevice.ReadWriteListener.Type writeType, BleDevice.ReadWriteListener writeListener, BleTransaction txn, PE_TaskPriority priority)
	{
		super(device, serviceUuid, charUuid, requiresBonding, txn, priority, filter, writeListener);

		m_futureData = futureData;

		m_writeType = writeType;
	}
	
	@Override protected BleDevice.ReadWriteListener.ReadWriteEvent newReadWriteEvent(final BleDevice.ReadWriteListener.Status status, final int gattStatus, final BleDevice.ReadWriteListener.Target target, final UUID serviceUuid, final UUID charUuid, final UUID descUuid)
	{
		final BleCharacteristicWrapper char_native = getDevice().getNativeBleCharacteristic(serviceUuid, charUuid);
		final BleDevice.ReadWriteListener.Type type = P_DeviceServiceManager.modifyResultType(char_native, BleDevice.ReadWriteListener.Type.WRITE);
		final UUID actualDescUuid = getActualDescUuid(descUuid);
		
		return new BleDevice.ReadWriteListener.ReadWriteEvent(getDevice(), serviceUuid, charUuid, actualDescUuid, m_descriptorFilter, type, target, m_data, status, gattStatus, getTotalTime(), getTotalTimeExecuting(), /*solicited=*/true);
	}

	@Override protected void executeReadOrWrite()
	{
		m_data = m_futureData.getData();

		if( false == write_earlyOut(m_data) )
		{
			final BluetoothGattCharacteristic char_native = getFilteredCharacteristic() != null ? getFilteredCharacteristic() : getDevice().getNativeCharacteristic(getServiceUuid(), getCharUuid());

			if( char_native == null )
			{
				fail(BleDevice.ReadWriteListener.Status.NO_MATCHING_TARGET, BleStatuses.GATT_STATUS_NOT_APPLICABLE, getDefaultTarget(), getCharUuid(), BleDevice.ReadWriteListener.ReadWriteEvent.NON_APPLICABLE_UUID);
			}
			else
			{
				// Set the write type now, if it is not null
				if (m_writeType != null)
				{
					if (m_writeType == BleDevice.ReadWriteListener.Type.WRITE_NO_RESPONSE)
					{
						char_native.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
					}
					else if (m_writeType == BleDevice.ReadWriteListener.Type.WRITE_SIGNED)
					{
						char_native.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_SIGNED);
					}
					else if (char_native.getWriteType() != BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
					{
						char_native.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
					}
				}

				if( false == getDevice().layerManager().setCharValue(char_native, m_data) )
				{
					fail(BleDevice.ReadWriteListener.Status.FAILED_TO_SET_VALUE_ON_TARGET, BleStatuses.GATT_STATUS_NOT_APPLICABLE, getDefaultTarget(), getCharUuid(), BleDevice.ReadWriteListener.ReadWriteEvent.NON_APPLICABLE_UUID);
				}
				else
				{
					if( false == getDevice().layerManager().writeCharacteristic(char_native) )
					{
						fail(BleDevice.ReadWriteListener.Status.FAILED_TO_SEND_OUT, BleStatuses.GATT_STATUS_NOT_APPLICABLE, getDefaultTarget(), getCharUuid(), BleDevice.ReadWriteListener.ReadWriteEvent.NON_APPLICABLE_UUID);
					}
					else
					{
						// SUCCESS, for now...
					}
				}
			}
		}
	}

	public void onCharacteristicWrite(final BluetoothGatt gatt, final UUID uuid, final int gattStatus)
	{
		 getManager().ASSERT(getDevice().layerManager().gattEquals(gatt));
		 
		 if( false == this.isForCharacteristic(uuid) )  return;
		 
		 if( false == acknowledgeCallback(gattStatus) )  return;

		 if( Utils.isSuccess(gattStatus) )
		 {
			 succeedWrite();
		 }
		 else
		 {
			 fail(BleDevice.ReadWriteListener.Status.REMOTE_GATT_FAILURE, gattStatus, getDefaultTarget(), uuid, BleDevice.ReadWriteListener.ReadWriteEvent.NON_APPLICABLE_UUID);
		 }
	}

	@Override public void onStateChange(final PA_Task task, final PE_TaskState state)
	{
		super.onStateChange(task, state);
		
		if( state == PE_TaskState.TIMED_OUT )
		{
			getLogger().w(getLogger().charName(getCharUuid()) + " write timed out!");
			
			getDevice().invokeReadWriteCallback(m_readWriteListener, newReadWriteEvent(BleDevice.ReadWriteListener.Status.TIMED_OUT, BleStatuses.GATT_STATUS_NOT_APPLICABLE, getDefaultTarget(), getServiceUuid(), getCharUuid(), BleDevice.ReadWriteListener.ReadWriteEvent.NON_APPLICABLE_UUID));
			
			getManager().uhOh(UhOh.WRITE_TIMED_OUT);
		}
		else if( state == PE_TaskState.SOFTLY_CANCELLED )
		{
			getDevice().invokeReadWriteCallback(m_readWriteListener, newReadWriteEvent(getCancelType(), BleStatuses.GATT_STATUS_NOT_APPLICABLE, getDefaultTarget(), getServiceUuid(), getCharUuid(), BleDevice.ReadWriteListener.ReadWriteEvent.NON_APPLICABLE_UUID));
		}
	}
	
	@Override protected BleTask getTaskType()
	{
		return BleTask.WRITE;
	}
}
