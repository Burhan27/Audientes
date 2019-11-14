package com.example.audientes;

import android.os.Bundle;

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
                        // TODO
                    }
                });
    }
}