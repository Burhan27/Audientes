import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:volume/volume.dart';
import 'dart:async';
import 'package:flutter/services.dart';

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
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                Icon(Icons.stars,
                    size: MediaQuery.of(context).size.width * 0.125),
                Icon(Icons.stars,
                    size: MediaQuery.of(context).size.width * 0.125),
                Icon(Icons.stars,
                    size: MediaQuery.of(context).size.width * 0.125),
              ],
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
        decoration: BoxDecoration(
            gradient: LinearGradient(
                begin: Alignment.topCenter,
                end: Alignment.bottomCenter,
                stops: [
              0.3,
              0.7,
              0.9,
              1.0
            ],
                colors: [
              Color(0xff131313),
              Color(0xff424242),
              Color(0xff595959),
              Color(0xff707070)
            ])),
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
        Expanded(
          child: Icon(Icons.stars,
              size: MediaQuery.of(context).size.width * 0.125),
          flex: 2,
        ),
        Expanded(
          child: Icon(
            Icons.stars,
            size: MediaQuery.of(context).size.width * 0.50,
          ),
          flex: 6,
        ),
        Expanded(
          child: Icon(Icons.stars,
              size: MediaQuery.of(context).size.width * 0.125),
          flex: 2,
        ),
      ],
    );
  }

  Widget Test() {
    return Container(
        decoration: new BoxDecoration(
          borderRadius: new BorderRadius.circular(16.0),
          color: Color(0xffBBBBBB),
        ),
        width: MediaQuery.of(context).size.width * 0.8,
        child: Row(children: <Widget>[
          Expanded(
            child: RawMaterialButton(
              onPressed: () {},
              child: new Icon(
                Icons.menu,
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
      )
    ]);
  }
}
