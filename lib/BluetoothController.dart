
import 'package:flutter/services.dart';

class BluetoothController{

  static const platform =  const MethodChannel("audientes.android");

  Future<void> onConnected() async{
    try{
      print("BEFORE WE WEEE:");
      await platform.invokeMethod("onDeviceConnected");
      print("WEEEEEE");

    }on PlatformException catch (e){
      print('FAILED HORRIBLY' + e.message);
    }
  }
}
