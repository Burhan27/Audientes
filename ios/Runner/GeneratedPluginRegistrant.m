//
//  Generated file. Do not edit.
//

#import "GeneratedPluginRegistrant.h"
#import <flutter_bluetooth_serial/FlutterBluetoothSerialPlugin.h>
#import <flutter_crashlytics/FlutterCrashlyticsPlugin.h>
#import <open_settings/OpenSettingsPlugin.h>
#import <system_shortcuts/SystemShortcutsPlugin.h>
#import <volume/VolumePlugin.h>

@implementation GeneratedPluginRegistrant

+ (void)registerWithRegistry:(NSObject<FlutterPluginRegistry>*)registry {
  [FlutterBluetoothSerialPlugin registerWithRegistrar:[registry registrarForPlugin:@"FlutterBluetoothSerialPlugin"]];
  [FlutterCrashlyticsPlugin registerWithRegistrar:[registry registrarForPlugin:@"FlutterCrashlyticsPlugin"]];
  [OpenSettingsPlugin registerWithRegistrar:[registry registrarForPlugin:@"OpenSettingsPlugin"]];
  [SystemShortcutsPlugin registerWithRegistrar:[registry registrarForPlugin:@"SystemShortcutsPlugin"]];
  [VolumePlugin registerWithRegistrar:[registry registrarForPlugin:@"VolumePlugin"]];
}

@end
