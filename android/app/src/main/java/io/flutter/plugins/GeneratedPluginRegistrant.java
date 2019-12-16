package io.flutter.plugins;

import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugins.firebase.cloudfirestore.CloudFirestorePlugin;
import io.flutter.plugins.firebase.core.FirebaseCorePlugin;
import io.github.edufolly.flutterbluetoothserial.FlutterBluetoothSerialPlugin;
import com.kiwi.fluttercrashlytics.FlutterCrashlyticsPlugin;
import alihoseinpoor.com.open_settings.OpenSettingsPlugin;
import io.flutter.plugins.sharedpreferences.SharedPreferencesPlugin;
import com.example.systemshortcuts.SystemShortcutsPlugin;
import com.example.volume.VolumePlugin;

/**
 * Generated file. Do not edit.
 */
public final class GeneratedPluginRegistrant {
  public static void registerWith(PluginRegistry registry) {
    if (alreadyRegisteredWith(registry)) {
      return;
    }
    CloudFirestorePlugin.registerWith(registry.registrarFor("io.flutter.plugins.firebase.cloudfirestore.CloudFirestorePlugin"));
    FirebaseCorePlugin.registerWith(registry.registrarFor("io.flutter.plugins.firebase.core.FirebaseCorePlugin"));
    FlutterBluetoothSerialPlugin.registerWith(registry.registrarFor("io.github.edufolly.flutterbluetoothserial.FlutterBluetoothSerialPlugin"));
    FlutterCrashlyticsPlugin.registerWith(registry.registrarFor("com.kiwi.fluttercrashlytics.FlutterCrashlyticsPlugin"));
    OpenSettingsPlugin.registerWith(registry.registrarFor("alihoseinpoor.com.open_settings.OpenSettingsPlugin"));
    SharedPreferencesPlugin.registerWith(registry.registrarFor("io.flutter.plugins.sharedpreferences.SharedPreferencesPlugin"));
    SystemShortcutsPlugin.registerWith(registry.registrarFor("com.example.systemshortcuts.SystemShortcutsPlugin"));
    VolumePlugin.registerWith(registry.registrarFor("com.example.volume.VolumePlugin"));
  }

  private static boolean alreadyRegisteredWith(PluginRegistry registry) {
    final String key = GeneratedPluginRegistrant.class.getCanonicalName();
    if (registry.hasPlugin(key)) {
      return true;
    }
    registry.registrarFor(key);
    return false;
  }
}
