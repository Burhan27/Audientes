package com.sweetblue;


/**
 * 
 * 
 */
class P_WrappingReadWriteListener extends PA_CallbackWrapper implements BleDevice.ReadWriteListener
{
	private final BleDevice.ReadWriteListener m_listener;
	
	P_WrappingReadWriteListener(BleDevice.ReadWriteListener listener, P_SweetHandler handler, boolean postToMain)
	{
		super(handler, postToMain);
		
		m_listener = listener;
	}
	
	protected void onEvent(final BleDevice.ReadWriteListener listener, final ReadWriteEvent result)
	{
		if( listener == null )  return;
		
		if( postToMain() )
		{
			m_handler.post(new Runnable()
			{
				@Override public void run()
				{
					listener.onEvent(result);
				}
			});
		}
		else
		{
			listener.onEvent(result);
		}
	}
	
	@Override public void onEvent(final ReadWriteEvent result)
	{
		onEvent(m_listener, result);
	}
}
