
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:open_settings/open_settings.dart';

class BluetoothButton extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return BluetoothButtonState();
  }
}

class BluetoothButtonState extends State<BluetoothButton> {
  @override
  Widget build(BuildContext context) {


    return Material(
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            RaisedButton(
              child: Text('Get Battery Level'),
              onPressed: _onConnected,
            ),
            Text(_batteryLevel),
          ],
        ),
      ),
    );
  }

  static const platform =  const MethodChannel("audientes.android");

  Future<void> _onConnected() async{
    String batteryLevel;
    try{
      final int result = await platform.invokeMethod("onDeviceConnected");
      batteryLevel = 'Battery level at $result % .';

    }on PlatformException catch (e){
      print('FAILED HORRIBLY: ' + e.message);
    }

    setState(() {
      _batteryLevel = batteryLevel;
    });

  }

  // Get battery level.
  String _batteryLevel = 'Unknown battery level.';

  Future<void> _getBatteryLevel() async {
    String batteryLevel;
    try {
      final int result = await platform.invokeMethod('getBatteryLevel');
      batteryLevel = 'Battery level at $result % .';
    } on PlatformException catch (e) {
      batteryLevel = "Failed to get battery level: '${e.message}'.";
    }

    setState(() {
      _batteryLevel = batteryLevel;
    });
  }
}
