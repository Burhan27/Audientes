
import 'package:flutter/material.dart';
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
    // TODO: implement build
    return Container(
      child: GestureDetector(
        child: Icon(Icons.bluetooth),
        onTap: ()=> OpenSettings.openBluetoothSetting(),
      ),
    );
  }
}
