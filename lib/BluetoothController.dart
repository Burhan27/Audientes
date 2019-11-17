

import 'package:flutter/services.dart';

class BluetoothController{

  static const platform =  const MethodChannel("audientes.android");

  Future<void> onConnected() async{
    try{
      await platform.invokeMethod("onDeviceConnected");

    }on PlatformException catch (e){
      print('FAILED HORRIBLY' + e.message);
    }
  }


}