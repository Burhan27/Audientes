package com.professional.device.sim;

import dk.sens.android.ble.BLEAddress;
import dk.sens.android.ble.sim.SimBLEScanner;

/**
 * Created by morten on 18/07/2017.
 */


public class WaveSimScanner extends SimBLEScanner
{
    public WaveSimScanner()
    {
        addSimDevice(new SimWaveDevice(new BLEAddress("AA:BB:CC:DD:EE:FF")), -30);
    }
}
