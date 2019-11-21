package com.idevicesinc.sweetblue.utils;

import com.idevicesinc.sweetblue.BleDevice;
import com.idevicesinc.sweetblue.BleManager;
import com.idevicesinc.sweetblue.BleManagerConfig;
import com.idevicesinc.sweetblue.BleServer;

/**
 * Most of the methods of core SweetBlue classes like {@link BleDevice}, {@link BleManager}, and {@link BleServer}
 * must be called from the main thread, similar to how Android will complain if you try to edit a {@link android.view.View}
 * from another thread.
 *
 * @see BleManagerConfig#allowCallsFromAllThreads
 */
public final class WrongThreadError extends Error
{
	public WrongThreadError(final String message)
	{
		super(message);
	}
}
