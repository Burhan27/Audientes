import 'package:flutter/material.dart';
import 'package:volume/volume.dart';
import 'dart:async';
import 'package:flutter/services.dart';

class MyApp extends StatefulWidget {
  @override
  StartScreen createState() => StartScreen();
}

class StartScreen extends State<MyApp>   {
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
      backgroundColor: const Color(0xFF37474F),
      appBar: AppBar(
        title: Text('First Route'),
      ),



      body: Column(


           mainAxisAlignment: MainAxisAlignment.spaceEvenly,

        //  crossAxisAlignment: ,
        //  mainAxisSize:


        children: <Widget>[
          Test(),


        threeButtons(),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
        Icon(Icons.stars, size: 50.0),
        Icon(Icons.stars, size: 50.0),
        Icon(Icons.stars, size: 50.0),
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

    );


    }

    Widget threeButtons() {
    return Row(
        mainAxisAlignment: MainAxisAlignment.center,

        //   mainAxisAlignment: ,
    //  crossAxisAlignment: ,
    //  mainAxisSize: ,
      children: [
        Icon(Icons.stars, size: 60.0),
        Icon(Icons.stars, size: 250.0),

      RawMaterialButton(
      onPressed: () {

      },

      child: new Icon(
      Icons.menu,
      color: Colors.blue,
      size: 15.0,
      ),
      shape: new CircleBorder(),
      elevation: 2.0,
      fillColor: Colors.white,

      )
      ],
    );

    }



  Widget Test() {
    return Container(


      child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          //  mainAxisSize: ,
          children: <Widget>[


            RawMaterialButton(
             onPressed: () {

               },

            child: new Icon(
              Icons.menu,
               color: Colors.blue,
             size: 10.0,
              ),
             shape: new CircleBorder(),
              elevation: 2.0,
              fillColor: Colors.white,
      //        padding: const EdgeInsets.all(15.0),

    ),



            Slider(
              min: 0.0,
              max: maxVol + 0.0,
              divisions: maxVol,
              value: currentVol / 1.0,
              onChanged: (double d) {
                setVol(d.toInt());
                updateVolumes();
              },
            ),


            RawMaterialButton(
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
              elevation: 2.0,
              fillColor: Colors.white,
         //     padding: const EdgeInsets.all(15.0),

            ),

          ]

      )


    );
  }


  Widget voulmeBar() {
    return Row (
      children: [
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

          Slider(
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
      ]

      );


  }



  }

