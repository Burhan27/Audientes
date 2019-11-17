package com.sweetblue.utils;

import com.idevicesinc.sweetblue.*;
import com.sweetblue.BleDevice;
import com.sweetblue.BleManager;
import com.sweetblue.BleManagerConfig;
import com.sweetblue.BleServer;

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
