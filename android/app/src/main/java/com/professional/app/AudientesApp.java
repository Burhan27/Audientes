package com.professional.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.CallSuper;

import com.example.audientes.BuildConfig;
import com.example.audientes.MainActivity;
import com.professional.device.rpc.UUIDs;
import com.professional.device.sim.WaveSimScanner;

import dk.sens.android.ble.BLEAddress;
import dk.sens.android.ble.generic.GenericBLEDevice;
import dk.sens.android.ble.generic.GenericBLEScanRecord;
import dk.sens.android.ble.generic.GenericBLEScanner;
import dk.sens.android.ble.generic.GenericRSSI;
import dk.sens.android.sweetblue.SweetBlueBLEScannerNordic;
import dk.sens.android.util.ErrorHandling;
import dk.sens.android.util.ModalHelper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
/**
 * Created by morten on 15/07/2017.
 */

public class AudientesApp extends Application implements GenericBLEScanner.discoveredListener
{
    private static final String TAG = "AD AudientesApp";

    public static AudientesApp instance;
    Realm realm;

    private Config mConfig;

    public static AudientesDevice mAudientesDevice = null;
    public static TestHistory mHistory = new TestHistory();


    public void onDeviceConnected()
    {
        if (getCurrentActivity() != null)
        {
            getCurrentActivity().onDeviceConnected();
        }
    }

    public void onDeviceDisconnected()
    {
        if (getCurrentActivity() != null)
        {
            getCurrentActivity().onDeviceDisconnected();
        }
    }

    public void onDeviceVersionReceived(byte datum, byte datum1, byte datum2) {
    }

    public void onTestResult(TestResult mActiveTest) {
    }

    /*
     * Parameters
     */
    public class Parameters
    {
        public static final String REST_URL = "http://192.168.1.52:5000";
    }

    @CallSuper
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        mAudientesDevice = null;

        // Use the Sentry DSN (client key) from the Project Settings page on Sentry
        String sentryDsn = "https://ee54ccabf20c4bffb65c2a3f9d43db09:d6ca639fe05144b2a8d02e5b85848f43@sentry.io/215394";
        Sentry.init(sentryDsn, new AndroidSentryClientFactory(this));

        Sentry.capture("This is a test");

        ModalHelper.init(this);

        Log.i(TAG, "onCreate");

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();


        // Get a Realm instance for this thread
        realm = Realm.getInstance(config);

        // Query Realm for config
        final RealmResults<Config> configs = realm.where(Config.class).findAll();
        mConfig = null;
        if (configs.size() != 0)
        {
            mConfig = configs.first();
        }
        if (mConfig == null)
        {
            ErrorHandling.showToast("Creating Default Config");
            Log.i(TAG, "Creating Default Config");
            realm.beginTransaction();
            mConfig = realm.createObject(Config.class); // Create managed objects directly
            realm.commitTransaction();
        }
        else
        {
            ErrorHandling.showToast("Loaded Config");
            Log.i(TAG, "Loaded Config");
        }

/*
        if (BleHelper.checkConnected(this, "Audientes"))
        {
            BleHelper.restartBLEAdapter(this);
        }
*/

        if (BuildConfig.FLAVOR.equals("sim"))
        {
            GenericBLEScanner.create(new WaveSimScanner());
        }
        else
        {
            GenericBLEScanner.create(new SweetBlueBLEScannerNordic(this));
        }

        // Check config
        if (mConfig.deviceMac != null)
        {
            mAudientesDevice = new AudientesDevice(GenericBLEScanner.getDevice(new BLEAddress(mConfig.deviceMac)));
            mAudientesDevice.setConnectState(true);
        }

        /*
        mAudientesDevice = new AudientesDevice(SimWaveDevice.create("AA.BB.CC.DD.EE.FF"));
        mAudientesDevice.setConnectState(true);
        */

        /*
        TestResult t1 = TestResult.createFromMeta("Person 2", 65, 1, "none@none.com", "00");
        t1.left = new byte[]{10,20,30,40,50};
        t1.right = new byte[]{20,30,40,50,60};
        mHistory.addResult(t1);
        */

        /*
        TestResult t2 = TestResult.createFromMeta("Default Person", 65, 1, "none@none.com", "00000000");
        t2.left = new byte[]{30,40,50,60};
        t2.right = new byte[]{20,30,40,50};
        mHistory.addResult(t2);
        submitResult(t2);
        */

        ErrorHandling.context = this;
    }

    public boolean isPaired()
    {
        return (mAudientesDevice != null);
    }

    public boolean isScanning()
    {
        return GenericBLEScanner.isScanning();
    }

    public void scanForDevice(Context context)
    {
        GenericBLEScanner.scanForDevices(UUIDs.SERVICE_UUID, this);
    }

    public void stopScan()
    {
        GenericBLEScanner.stopScan();
    }

    @Override
    public void onDiscoveredDevice(GenericBLEDevice device, GenericBLEScanRecord scanResult, GenericRSSI rssi)
    {
        if (!device.getNormalizedName().equals("audientes"))
        {
            return;
        }

        Log.i(TAG, String.format("%s %s", device.getAddress().macString(), device.getNormalizedName()));

        if (!isPaired())
        {
            mAudientesDevice = new AudientesDevice(device);
            mAudientesDevice.setConnectState(true);
            GenericBLEScanner.stopScan();
            realm.beginTransaction();
            mConfig.deviceMac = device.getAddress().macString();
            realm.commitTransaction();

        }
    }

    MainActivity mCurrentActivity = null;
    public void setCurrentActivity(MainActivity activity)
    {
        mCurrentActivity = activity;
    }

    public void clearCurrentActivity(MainActivity activity)
    {
        if (mCurrentActivity == activity)
        {
            mCurrentActivity = null;
        }
    }

    public MainActivity getCurrentActivity()
    {
        return mCurrentActivity;
    }


    public void beginTest(TestResult meta)
    {
        AudientesApp.mAudientesDevice.startTest(meta);
    }

    public void restartTest()
    {
        AudientesApp.mAudientesDevice.restartTest();
    }

    public void submitResult(TestResult r)
    {
        RestAPITask t = new RestAPITask();
        t.execute(r);
    }

    public void unpair()
    {
        if (isPaired())
        {
            mAudientesDevice.setConnectState(false);
            mAudientesDevice = null;
            realm.beginTransaction();
            mConfig.deviceMac = null;
            realm.commitTransaction();
        }
    }

}