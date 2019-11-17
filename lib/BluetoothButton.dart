
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:open_settings/open_settings.dart';

class BluetoothButton extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
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
              onPressed: _getBatteryLevel,
            ),
            Text(_batteryLevel),
          ],
        ),
      ),
    );
    // TODO: implement build
    /*return Container(
      child: GestureDetector(
        child: Text(_batteryLevel, style: new TextStyle(color: Colors.white, fontSize: 20),),
        onTap: ()=> _getBatteryLevel(),
      ),
    );*/
  }

  static const platform =  const MethodChannel("audientes.android");

  Future<void> _onConnected() async{
    String batteryLevel;
    try{
      print("ALEN har ingen venner");
      final int result = await platform.invokeMethod("onDeviceConnected");
      print("Alle hader ALEN");
      batteryLevel = 'Battery level at $result % .';

    }on PlatformException catch (e){
      print('FAILED HORRIBLY' + e.message);
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
