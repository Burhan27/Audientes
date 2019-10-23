import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:volume/volume.dart';
import 'dart:async';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class MyApp extends StatefulWidget {
  @override
  StartScreen createState() => StartScreen();
}

class StartScreen extends State<MyApp> {
  int maxVol, currentVol;

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
    maxVol = await Volume.getMaxVol;
    // get Current Volume
    currentVol = await Volume.getVol;
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

/*
                Icon(Icons.bluetooth_disabled,
                    size: MediaQuery
                        .of(context)
                        .size
                        .width * 0.125,
                    color: Colors.red),
                Icon(Icons.home,
                    size: MediaQuery
                        .of(context)
                        .size
                        .width * 0.125,
                    color: Colors.red),*/
              ],
            ),
            ),
            RaisedButton(
              child: Text('Initiate Hearing Test'),
              onPressed: () {
                //Navigate til screen
                Navigator.pushNamed(context, '/settings');
              },
            ),
          ],
        ),
        color: Color(0xff131313),),
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
        Expanded(
          child: Icon(Icons.menu,
              size: MediaQuery
                  .of(context)
                  .size
                  .width * 0.125, color: Colors.white),
          flex: 2,
        ),
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
                FontAwesomeIcons.gamepad,
                color: Colors.blue,
                size: 10.0,
              ),
              shape: new CircleBorder(),
              elevation: 2.0,
              fillColor: Colors.white,
            ),
            flex: 2,
          ),
          Expanded(
            child: Slider(
              min: 0.0,
              max: maxVol + 0.0,
              divisions: maxVol,
              value: currentVol / 1.0,
              activeColor: Color(0xff38E2CF),
              onChanged: (double d) {
                setVol(d.toInt());
                updateVolumes();
              },
            ),
            flex: 6,
          ),
          Expanded(
            child: RawMaterialButton(
              onPressed: () {
                //Volume.volUp();
                // updateVolumes();
              },
              child: new Icon(
                Icons.menu,
                color: Colors.blue,
                size: 10.0,
              ),
              shape: new CircleBorder(),
              fillColor: Colors.white,
            ),
            flex: 2,
          )
        ]));
  }

  Widget voulmeBar() {
    return Row(children: [
      Container(
        child: RawMaterialButton(
          onPressed: () {
            //     Volume.volDown();
            //    updateVolumes();
          },
          child: new Icon(
            Icons.menu,
            color: Colors.blue,
            size: 25.0,
          ),
          shape: new CircleBorder(),
          elevation: 2.0,
          fillColor: Colors.white,
          padding: const EdgeInsets.all(15.0),
        ),
      ),
      CupertinoSlider(
        min: 0.0,
        max: maxVol + 0.0,
        divisions: maxVol,
        value: currentVol / 1.0,
        onChanged: (double d) {
          setVol(d.toInt());
          updateVolumes();
        },
      ),
      Container(
        child: RawMaterialButton(
          onPressed: () {
            //Volume.volUp();
            // updateVolumes();
          },
          child: new Icon(
            Icons.menu,
            color: Colors.blue,
            size: 25.0,
          ),
          shape: new CircleBorder(),
          elevation: 2.0,
          fillColor: Colors.white,
          padding: const EdgeInsets.all(15.0),
        ),
      ),
    ]);
  }

  Widget frontScreenIcon(Color iconColor, Color fillColor, double size,
      IconData icon, int onClick, String text) {
    return Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [

      RawMaterialButton(
        onPressed: () {

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