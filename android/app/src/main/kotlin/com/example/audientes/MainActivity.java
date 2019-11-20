package com.example.audientes;

import android.util.Log;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;

import com.audientes.android.professional.app.AudientesApp;
import org.jetbrains.annotations.Nullable;
import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugins.GeneratedPluginRegistrant;

public final class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "audientes.android";
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith((PluginRegistry)this);
        new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(
                new MethodChannel.MethodCallHandler() {
                    @Override
                    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
                        // Note: this method is invoked on the main thread.
                        if(call.method.equals("onDeviceConnected")){
                            int response = onDeviceConnected();
                            if (response != -1) {
                                result.success(response);
                            } else {
                                result.error("UNAVAILABLE", "Battery level not available.", null);
                            }
                        } else {
                            result.notImplemented();
                        }
                    }
                });
    }

    public int onDeviceConnected() {

        int result = -1;

        android.util.Log.d("METHODCHANNEL", "onDeviceConnected: " + result);

        if(AudientesApp.instance.isPaired() && AudientesApp.mAudientesDevice.isConnected()){
            Log.d("METHODCHANNEL","DEVICE CONNECTED");
            result = 99;
        }
        else{
            Log.d("METHODCHANNEL","DEVICE NOT CONNECTED");
            result = 50;
        }

        android.util.Log.d("BITCONNECT", "onDeviceConnected AFTER: " + result);
        return result;

    }

}