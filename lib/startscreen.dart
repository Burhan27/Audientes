import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:volume/volume.dart';
import 'dart:async';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:system_shortcuts/system_shortcuts.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bluetooth_serial/flutter_bluetooth_serial.dart';
import 'package:audientes/mixer.dart';

class MyApp extends StatefulWidget {
  @override
  StartScreen createState() => StartScreen();
}

class StartScreen extends State<MyApp> {
  int maxVol = 0, currentVol = 0;

  static const MethodChannel _channel = const MethodChannel('system_shortcuts');

  @override
  void initState() {
    super.initState();
    initPlatformState();
    updateVolumes();
  }

  Future<void> initPlatformState() async {
    await Volume.controlVolume(AudioManager.STREAM_MUSIC);
  }

  updateVolumes() async {
    // get Max Volume
    print(Text("hej" + maxVol.toString()));
    maxVol = await Volume.getMaxVol;
 //   maxVol = 15;
    // get Current Volume
    currentVol = await Volume.getVol;
 //   currentVol = 4;
    setState(() {});
  }

  setVol(int i) async {
    await Volume.setVol(i);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Color(0xff131313),
        title: Text('Audientes'),
      ),
      body: Container(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            Test(),
            threeButtons(),
            Container(
              decoration: new BoxDecoration(
                borderRadius: new BorderRadius.circular(16.0),
                color: Color(0xff303030),

              ),
              width: MediaQuery
                .of(context)
                .size
                .width * 0.8,
              height: MediaQuery
                .of(context)
                .size
                .width * 0.3,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                frontScreenIcon(Colors.blue, Colors.white, MediaQuery
                    .of(context)
                    .size
                    .width * 0.07, Icons.radio, 1, 'Music'),

                frontScreenIcon(Colors.blue, Colors.white, MediaQuery
                    .of(context)
                    .size
                    .width * 0.07, Icons.home, 1, 'Home'),

                frontScreenIcon(Colors.blue, Colors.white, MediaQuery
                    .of(context)
                    .size
                    .width * 0.07, Icons.work, 1, 'Work'),
              ],
            ),
            ),
          ],
        ),
        color: Color(0xff131313),
      ),
    );
  }

  Widget threeButtons() {
    return Row(
      //    mainAxisAlignment: MainAxisAlignment.center,

      //   mainAxisAlignment: ,
      //  crossAxisAlignment: ,
      //  mainAxisSize: ,

      children: [
     /*   Expanded(
          child: Icon(Icons.directions_bus,
              size: MediaQuery
                  .of(context)
                  .size
                  .width * 0.125, color: Colors.green),
          flex: 2,
        ), */
        Expanded(

          child: frontScreenIcon(Colors.blue, Colors.white, MediaQuery
              .of(context)
              .size
              .width * 0.50, Icons.bluetooth_connected, 0, ''),

          flex: 6,
        ),
          /*     Expanded(
          child :RawMaterialButton(
            onPressed: ( ) {
              Navigator.pushNamed(context, '/settings');
            },
            child:

           new Icon(
              Icons.menu,
              color: Colors.white,
              size: MediaQuery
                  .of(context)
                  .size
                  .width * 0.125,
            ),
            shape: new CircleBorder(),
            elevation: 2.0,
            padding: const EdgeInsets.all(15.0),
          ),



          flex: 2,
        ), */
      ],
    );



  }

  Widget Test() {
    return Container(
        decoration: new BoxDecoration(
          borderRadius: new BorderRadius.circular(16.0),
          color: Color(0xff303030),
        ),
        width: MediaQuery
            .of(context)
            .size
            .width * 0.8,
        child: Row(children: <Widget>[
          Expanded(
            child: RawMaterialButton(
              onPressed: () {},
              child: new Icon(
                FontAwesomeIcons.volumeDown,
                color: Colors.blue,
                size: 20.0,
              ),
              shape: new CircleBorder(),
              fillColor: Colors.white,
            ),
            flex: 2,
          ),
          Expanded(
            child: SliderTheme(
            data: SliderThemeData(
            thumbColor: Color(0xff38E2CF),
            trackHeight: 10,
            thumbShape: RoundSliderThumbShape(enabledThumbRadius: 12.5)),
            child: Slider(
              min: 0.0,
              max: maxVol + 0.0,
             // divisions: maxVol,
              value: currentVol / 1.0,
              activeColor: Color(0xff38E2CF),
              onChanged: (double d) {
                setVol(d.toInt());
                updateVolumes();
              },
            ),),
            flex: 6,
          ),
          Expanded(
            child: RawMaterialButton(
              onPressed: () async {
                await SystemShortcuts.volUp();
                 updateVolumes();
              },
              child: new Icon(
                FontAwesomeIcons.volumeUp,
                color: Colors.blue,
                size: 20.0,
              ),
              shape: new CircleBorder(),
              fillColor: Colors.white,
            ),
            flex: 2,
          )
        ]));
  }

  Widget frontScreenIcon(Color iconColor, Color fillColor, double size,
      IconData icon, int onClick, String text) {
    return Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [

      RawMaterialButton(
        onPressed: ( ) {

          },
        child: new Icon(
          icon,
          color: iconColor,
          size: size,
        ),
        shape: new CircleBorder(),
        elevation: 2.0,
        fillColor: fillColor,
        padding: const EdgeInsets.all(15.0),
       ),
          Text(text,style: TextStyle(
            color: Colors.teal
          ),
          )
    ]);
  }



}