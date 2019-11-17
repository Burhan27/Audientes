package com.sweetblue;


import com.sweetblue.utils.Utils;

/**
 * 
 * 
 */
abstract class PA_CallbackWrapper
{
	protected final P_SweetHandler m_handler;
	protected final boolean m_forcePostToMain;
	
	PA_CallbackWrapper(P_SweetHandler handler, boolean postToMain)
	{
		m_handler = handler;
		m_forcePostToMain = postToMain;
	}
	
	protected boolean postToMain()
	{
		return m_forcePostToMain && !Utils.isOnMainThread();
	}
}
