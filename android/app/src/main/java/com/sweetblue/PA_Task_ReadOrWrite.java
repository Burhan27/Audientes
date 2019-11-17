package com.sweetblue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.sweetblue.utils.PresentData;
import com.sweetblue.utils.Utils;


abstract class PA_Task_ReadOrWrite extends PA_Task_Transactionable implements PA_Task.I_StateListener
{

	private final UUID m_charUuid;
	private final UUID m_servUuid;

	protected final BleDevice.ReadWriteListener m_readWriteListener;
	
	private Boolean m_authRetryValue_onExecute = null;
	private boolean m_triedToKickOffBond = false;
	protected final DescriptorFilter m_descriptorFilter;

	private BluetoothGattCharacteristic m_filteredCharacteristic;
	private List<BluetoothGattCharacteristic> m_characteristicList;

	
	PA_Task_ReadOrWrite(BleDevice device, BluetoothGattCharacteristic nativeChar, BleDevice.ReadWriteListener readWriteListener, boolean requiresBonding, BleTransaction txn_nullable, PE_TaskPriority priority)
	{
		super(device, txn_nullable, requiresBonding, priority);

		m_charUuid = nativeChar.getUuid();
		m_servUuid = nativeChar.getService().getUuid();

		m_readWriteListener = readWriteListener;

		m_descriptorFilter = null;
	}

	PA_Task_ReadOrWrite(BleDevice device, UUID serviceUuid, UUID charUuid, boolean requiresBonding, BleTransaction txn_nullable, PE_TaskPriority priority, DescriptorFilter filter, BleDevice.ReadWriteListener readWriteListener)
	{
		super(device, txn_nullable, requiresBonding, priority);

		m_servUuid = serviceUuid;
		m_charUuid = charUuid;

		m_readWriteListener = readWriteListener;

		m_descriptorFilter = filter;

		m_characteristicList = new ArrayList<>();
	}

	
	protected abstract BleDevice.ReadWriteListener.ReadWriteEvent newReadWriteEvent(BleDevice.ReadWriteListener.Status status, int gattStatus, BleDevice.ReadWriteListener.Target target, UUID serviceUuid, UUID charUuid, UUID descUuid);
	protected abstract void executeReadOrWrite();


	protected UUID getActualDescUuid(UUID descUuid)
	{
		return descUuid != null ? descUuid : m_descriptorFilter != null ? m_descriptorFilter.descriptorUuid() : null;
	}

	protected BleDevice.ReadWriteListener.Target getDefaultTarget()
	{
		return BleDevice.ReadWriteListener.Target.CHARACTERISTIC;
	}
	
	protected void fail(BleDevice.ReadWriteListener.Status status, int gattStatus, BleDevice.ReadWriteListener.Target target, UUID charUuid, UUID descUuid)
	{
		this.fail();

		getDevice().invokeReadWriteCallback(m_readWriteListener, newReadWriteEvent(status, gattStatus, target, getServiceUuid(), charUuid, descUuid));
	}

	@Override protected void onNotExecutable()
	{
		super.onNotExecutable();

		final BleDevice.ReadWriteListener.ReadWriteEvent event = newReadWriteEvent(BleDevice.ReadWriteListener.Status.NOT_CONNECTED, BleStatuses.GATT_STATUS_NOT_APPLICABLE, getDefaultTarget(), getServiceUuid(), getCharUuid(), getDescUuid());

		getDevice().invokeReadWriteCallback(m_readWriteListener, event);
	}
	
	protected boolean acknowledgeCallback(int status)
	{
		 //--- DRK > As of now, on the nexus 7, if a write requires authentication, it kicks off a bonding process
		 //---		 and we don't get a callback for the write (android bug), so we let this write task be interruptible
		 //---		 by an implicit bond task. If on other devices we *do* get a callback, we ignore it so that this
		 //---		 library's logic always follows the lowest common denominator that is the nexus 7.
		//---		NOTE: Also happens with tab 4, same thing as nexus 7.
		 if( status == BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION || status == BleStatuses.GATT_AUTH_FAIL )
		 {
			 return false;
		 }
		 
		 return true;
	}

	protected BluetoothGattCharacteristic getFilteredCharacteristic()
	{
		return m_filteredCharacteristic;
	}
	
	private void checkIfBondingKickedOff()
	{
		if( getState() == PE_TaskState.EXECUTING )
		{
			if( m_triedToKickOffBond == false )
			{
				final Boolean authRetryValue_now = getAuthRetryValue();
				
				if( m_authRetryValue_onExecute != null && authRetryValue_now != null )
				{
					if( m_authRetryValue_onExecute == false && authRetryValue_now == true )
					{
						m_triedToKickOffBond = true;
						
						getManager().getLogger().i("Kicked off bond!");
					}
				}
			}
		}
	}
	
	private boolean triedToKickOffBond()
	{
		return m_triedToKickOffBond;
	}
	
	@Override public void execute()
	{
		m_authRetryValue_onExecute = getAuthRetryValue();

		if (m_descriptorFilter == null)
		{
			executeReadOrWrite();
		}
		else
		{
			List<BluetoothGattCharacteristic> charList = getDevice().getNativeCharacteristics_List(getServiceUuid());
			if (charList != null)
			{
				int size = charList.size();
				for (int i = 0; i < size; i++)
				{
					BluetoothGattCharacteristic ch = charList.get(i);
					if (ch.getUuid().equals(getCharUuid()))
					{
						m_characteristicList.add(ch);
					}
				}
				size = m_characteristicList.size();
				if (size == 0)
				{
					fail(BleDevice.ReadWriteListener.Status.NO_MATCHING_TARGET, BleStatuses.GATT_STATUS_NOT_APPLICABLE, BleDevice.ReadWriteListener.Target.CHARACTERISTIC, getCharUuid(), m_descriptorFilter.descriptorUuid());
					return;
				}

				final UUID descUuid = m_descriptorFilter.descriptorUuid();

				if (descUuid != null)
				{
					final BluetoothGattCharacteristic m_char = m_characteristicList.get(0);
					final BluetoothGattDescriptor m_desc = m_char.getDescriptor(descUuid);
					if (m_desc == null)
					{
						fail(BleDevice.ReadWriteListener.Status.NO_MATCHING_TARGET, BleStatuses.GATT_STATUS_NOT_APPLICABLE, BleDevice.ReadWriteListener.Target.CHARACTERISTIC, getCharUuid(), m_descriptorFilter.descriptorUuid());
					}
					else
					{
						if (false == getDevice().layerManager().readDescriptor(m_desc))
						{
							fail(BleDevice.ReadWriteListener.Status.FAILED_TO_SEND_OUT, BleStatuses.GATT_STATUS_NOT_APPLICABLE, BleDevice.ReadWriteListener.Target.DESCRIPTOR, m_char.getUuid(), m_desc.getUuid());
						}
						else
						{
							// Wait for the descriptor read callback
						}
					}
				}
				else
				{
					// If the descriptor Uuid is null, then we'll forward all chars we find and let the app decide if it's the right one or not
					for (BluetoothGattCharacteristic ch : m_characteristicList)
					{
						final DescriptorFilter.DescriptorEvent event = new DescriptorFilter.DescriptorEvent(ch.getService(), ch, null, PresentData.EMPTY);
						final DescriptorFilter.Please please = m_descriptorFilter.onEvent(event);
						if (please.isAccepted())
						{
							m_filteredCharacteristic = ch;
							executeReadOrWrite();
							return;
						}
					}

					// If we got here, we couldn't find a valid char to write to
					fail(BleDevice.ReadWriteListener.Status.NO_MATCHING_TARGET, BleStatuses.GATT_STATUS_NOT_APPLICABLE, BleDevice.ReadWriteListener.Target.CHARACTERISTIC, getCharUuid(), m_descriptorFilter.descriptorUuid());
				}
			}
			else
			{
				fail(BleDevice.ReadWriteListener.Status.NO_MATCHING_TARGET, BleStatuses.GATT_STATUS_NOT_APPLICABLE, BleDevice.ReadWriteListener.Target.CHARACTERISTIC, getCharUuid(), m_descriptorFilter.descriptorUuid());
			}
		}
	}
	
	@Override public void update(double timeStep)
	{
		if (getDevice().is(BleDeviceState.CONNECTED))
		{
			checkIfBondingKickedOff();
		}
	}
	
	private Boolean getAuthRetryValue()
	{
		return getDevice().layerManager().getGattLayer().getAuthRetryValue();
	}
	
	@Override protected UUID getCharUuid()
	{
		return m_charUuid;
	}

	protected UUID getServiceUuid()
	{
		return m_servUuid;
	}

	public boolean isFor(final BluetoothGattCharacteristic characteristic)
	{
		return
				characteristic.getUuid().equals(getCharUuid()) &&
				characteristic.getService().getUuid().equals(getServiceUuid());
	}

	public boolean isFor(final BluetoothGattDescriptor descriptor)
	{
		return descriptor.getUuid().equals(getDescUuid()) && isFor(descriptor.getCharacteristic());
	}
	
	protected boolean isForCharacteristic(UUID uuid)
	{
		return uuid.equals(getCharUuid());
	}
	
	@Override protected String getToStringAddition()
	{
		final String txn = getTxn() != null ? " txn!=null" : " txn==null";
		return getManager().getLogger().uuidName(getCharUuid()) + txn;
	}
	
	@Override public void onStateChange(PA_Task task, PE_TaskState state)
	{
		if( state == PE_TaskState.TIMED_OUT )
		{
			checkIfBondingKickedOff();
			
			if( triedToKickOffBond() )
			{
				getDevice().notifyOfPossibleImplicitBondingAttempt();
				getDevice().m_bondMngr.saveNeedsBondingIfDesired();
				
				getManager().getLogger().i("Kicked off bond and " + PE_TaskState.TIMED_OUT.name());
			}
		}
	}

	boolean descriptorMatches(BluetoothGattDescriptor descriptor)
	{
		if (m_descriptorFilter == null)
		{
			return isFor(descriptor);
		}
		else
		{
			return descriptor.getUuid().equals(m_descriptorFilter.descriptorUuid());
		}
	}

	void onDescriptorReadCallback(BluetoothGatt gatt, BleDescriptorWrapper desc, byte[] value, int gattStatus)
	{
		if (m_descriptorFilter == null)
		{
			onDescriptorRead(gatt, desc.getDescriptor().getUuid(), value, gattStatus);
		}
		else
		{
			if (!m_characteristicList.contains(desc.getDescriptor().getCharacteristic()))
			{
				return;
			}
			if( Utils.isSuccess(gattStatus))
			{
				final DescriptorFilter.DescriptorEvent event = new DescriptorFilter.DescriptorEvent(desc.getDescriptor().getCharacteristic().getService(), desc.getDescriptor().getCharacteristic(), desc.getDescriptor(), new PresentData(value));
				final DescriptorFilter.Please please = m_descriptorFilter.onEvent(event);
				if (please.isAccepted())
				{
					m_filteredCharacteristic = desc.getDescriptor().getCharacteristic();
					executeReadOrWrite();
				}
				else
				{
					m_characteristicList.remove(desc.getDescriptor().getCharacteristic());
					if (m_characteristicList.size() == 0)
					{
						fail(BleDevice.ReadWriteListener.Status.NO_MATCHING_TARGET, BleStatuses.GATT_STATUS_NOT_APPLICABLE, BleDevice.ReadWriteListener.Target.DESCRIPTOR, desc.getDescriptor().getCharacteristic().getUuid(), desc.getDescriptor().getUuid());
					}
					else
					{
						final BluetoothGattCharacteristic ch = m_characteristicList.get(0);
						final BluetoothGattDescriptor descr = ch.getDescriptor(m_descriptorFilter.descriptorUuid());
						if (false == getDevice().layerManager().readDescriptor(descr))
						{
							fail(BleDevice.ReadWriteListener.Status.FAILED_TO_SEND_OUT, BleStatuses.GATT_STATUS_NOT_APPLICABLE, BleDevice.ReadWriteListener.Target.DESCRIPTOR, ch.getUuid(), descr.getUuid());
						}
						else
						{
							// SUCCESS for now until the descriptor read comes back, and we can compare it to the given namespaceanddescription
						}
					}
				}
			}
			else
			{
				fail(BleDevice.ReadWriteListener.Status.REMOTE_GATT_FAILURE, gattStatus, getDefaultTarget(), getCharUuid(), getDescUuid());
			}
		}
	}


	protected void onDescriptorRead(BluetoothGatt gatt, UUID descriptorUuid, byte[] value, int gattStatus)
	{
	}

	protected void onCharacteristicOrDescriptorRead(BluetoothGatt gatt, UUID uuid, byte[] value, int gattStatus, BleDevice.ReadWriteListener.Type type)
	{
		getManager().ASSERT(getDevice().layerManager().getGattLayer().equals(gatt));

//		if( false == this.isForCharacteristic(uuid) )  return;

		if( false == acknowledgeCallback(gattStatus) )  return;

		if( Utils.isSuccess(gattStatus) )
		{
			if( value != null )
			{
				if( value.length == 0 )
				{
					fail(BleDevice.ReadWriteListener.Status.EMPTY_DATA, gattStatus, getDefaultTarget(), getCharUuid(), getDescUuid());
				}
				else
				{
					succeedRead(value, getDefaultTarget(), type);
				}
			}
			else
			{
				fail(BleDevice.ReadWriteListener.Status.NULL_DATA, gattStatus, getDefaultTarget(), getCharUuid(), getDescUuid());

				getManager().uhOh(BleManager.UhOhListener.UhOh.READ_RETURNED_NULL);
			}
		}
		else
		{
			fail(BleDevice.ReadWriteListener.Status.REMOTE_GATT_FAILURE, gattStatus, getDefaultTarget(), getCharUuid(), getDescUuid());
		}
	}

	private BleDevice.ReadWriteListener.ReadWriteEvent newSuccessReadWriteEvent(byte[] data, BleDevice.ReadWriteListener.Target target, BleDevice.ReadWriteListener.Type type, UUID charUuid, UUID descUuid, DescriptorFilter descriptorFilter)
	{
		return new BleDevice.ReadWriteListener.ReadWriteEvent(getDevice(), getServiceUuid(), charUuid, descUuid, descriptorFilter, type, target, data, BleDevice.ReadWriteListener.Status.SUCCESS, BluetoothGatt.GATT_SUCCESS, getTotalTime(), getTotalTimeExecuting(), /*solicited=*/true);
	}

	private void succeedRead(byte[] value, BleDevice.ReadWriteListener.Target target, BleDevice.ReadWriteListener.Type type)
	{
		super.succeed();

		final BleDevice.ReadWriteListener.ReadWriteEvent event = newSuccessReadWriteEvent(value, target, type, getCharUuid(), getDescUuid(), m_descriptorFilter);
		getDevice().addReadTime(event.time_total().secs());

		getDevice().invokeReadWriteCallback(m_readWriteListener, event);
	}

	protected void succeedWrite()
	{
		super.succeed();

		final BleDevice.ReadWriteListener.ReadWriteEvent event = newReadWriteEvent(BleDevice.ReadWriteListener.Status.SUCCESS, BluetoothGatt.GATT_SUCCESS, getDefaultTarget(), getServiceUuid(), getCharUuid(), BleDevice.ReadWriteListener.ReadWriteEvent.NON_APPLICABLE_UUID);
		getDevice().addWriteTime(event.time_total().secs());
		getDevice().invokeReadWriteCallback(m_readWriteListener, event);
	}

	protected boolean write_earlyOut(final byte[] data_nullable)
	{
		if( data_nullable == null )
		{
			fail(BleDevice.ReadWriteListener.Status.NULL_DATA, BleStatuses.GATT_STATUS_NOT_APPLICABLE, BleDevice.ReadWriteListener.Target.CHARACTERISTIC, getCharUuid(), getDescUuid());

			return true;
		}
		else if( data_nullable.length == 0 )
		{
			fail(BleDevice.ReadWriteListener.Status.EMPTY_DATA, BleStatuses.GATT_STATUS_NOT_APPLICABLE, BleDevice.ReadWriteListener.Target.CHARACTERISTIC, getCharUuid(), getDescUuid());

			return true;
		}
		else
		{
			return false;
		}
	}
}
