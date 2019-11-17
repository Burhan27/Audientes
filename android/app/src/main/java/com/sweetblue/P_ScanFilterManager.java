package com.sweetblue;

import java.util.ArrayList;


final class P_ScanFilterManager
{
	private final ArrayList<BleManagerConfig.ScanFilter> m_filters = new ArrayList<BleManagerConfig.ScanFilter>();
	private BleManagerConfig.ScanFilter m_default;
	private final BleManager m_mngr;
	
	P_ScanFilterManager(final BleManager mngr, final BleManagerConfig.ScanFilter defaultFilter)
	{
		m_mngr = mngr;
		m_default = defaultFilter;
	}

	void updateFilter(BleManagerConfig.ScanFilter filter)
	{
		m_default = filter;
	}
	
	void clear()
	{
		m_filters.clear();
	}
	
	void remove(BleManagerConfig.ScanFilter filter)
	{
		while( m_filters.remove(filter) ){};
	}
	
	void add(BleManagerConfig.ScanFilter filter)
	{
		if( filter == null )  return;
		
		if( m_filters.contains(filter) )
		{
			return;
		}
		
		m_filters.add(filter);
	}

	public boolean makeEvent()
	{
		return m_default != null || m_filters.size() > 0;
	}
	
	BleManagerConfig.ScanFilter.Please allow(P_Logger logger, final BleManagerConfig.ScanFilter.ScanEvent e)
	{
		if( m_filters.size() == 0 && m_default == null )  return BleManagerConfig.ScanFilter.Please.acknowledge();

		if( m_default != null )
		{
			final BleManagerConfig.ScanFilter.Please please = m_default.onEvent(e);
			
			logger.checkPlease(please, BleManagerConfig.ScanFilter.Please.class);

			stopScanningIfNeeded(m_default, please);
			
			if( please != null && please.ack() )
			{
				return please;
			}
		}
		
		for( int i = 0; i < m_filters.size(); i++ )
		{
			final BleManagerConfig.ScanFilter ithFilter = m_filters.get(i);
			
			final BleManagerConfig.ScanFilter.Please please = ithFilter.onEvent(e);
			
			logger.checkPlease(please, BleManagerConfig.ScanFilter.Please.class);

			stopScanningIfNeeded(ithFilter, please);
			
			if( please != null && please.ack() )
			{
				return please;
			}
		}
		
		return BleManagerConfig.ScanFilter.Please.ignore();
	}

	private void stopScanningIfNeeded(final BleManagerConfig.ScanFilter filter, final BleManagerConfig.ScanFilter.Please please_nullable)
	{
		if( please_nullable != null )
		{
			if( please_nullable.ack() )
			{
				if( (please_nullable.m_stopScanOptions & BleManagerConfig.ScanFilter.Please.STOP_PERIODIC_SCAN) != 0x0 )
				{
					m_mngr.stopPeriodicScan(filter);
				}

				if( (please_nullable.m_stopScanOptions & BleManagerConfig.ScanFilter.Please.STOP_SCAN) != 0x0 )
				{
					m_mngr.stopScan(filter);
				}
			}
		}
	}
}
