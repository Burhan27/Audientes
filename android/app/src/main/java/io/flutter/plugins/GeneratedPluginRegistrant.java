package io.flutter.plugins;

import io.flutter.plugin.common.PluginRegistry;
import io.github.edufolly.flutterbluetoothserial.FlutterBluetoothSerialPlugin;
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
    FlutterBluetoothSerialPlugin.registerWith(registry.registrarFor("io.github.edufolly.flutterbluetoothserial.FlutterBluetoothSerialPlugin"));
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
