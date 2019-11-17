package com.sweetblue.utils;


public interface GenericListener_Void<T_Event extends Event>
{
	void onEvent(T_Event e);
}
