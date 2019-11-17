import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:volume/volume.dart';
import 'dart:async';
import 'package:audientes/model/programItem.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:system_shortcuts/system_shortcuts.dart';
import 'package:flutter/services.dart';
import 'package:audientes/view/widgets/programItemView.dart';
import 'package:audientes/controller/ProgramController.dart';



class MyApp extends StatefulWidget {
  @override
  StartScreen createState() => StartScreen();
}

class StartScreen extends State<MyApp> {
  int maxVol = 0, currentVol = 0;
  List<ProgramItem> programItems = new List<ProgramItem>();
  ProgramController programController = new ProgramController();


  @override
  void initState() {
    super.initState();
    initPlatformState();
    updateVolumes();
    programItems.add(new ProgramItem("test1", Icons.home, Colors.white, false, false, 0));
    programItems.add(new ProgramItem("test2", Icons.home, Colors.white, false, false, 6));
    programItems.add(new ProgramItem("test3", Icons.terrain, Colors.white, false, false, 0));
    programItems.add(new ProgramItem("test3", Icons.home, Colors.white, false, false, 0));
  //  programController.createProgram(programItems.elementAt(1));
  }

  Future<void> initPlatformState() async {
    await Volume.controlVolume(AudioManager.STREAM_MUSIC);
  }

  updateVolumes() async {
    // get Max Volume
    maxVol = await Volume.getMaxVol;
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
            ProgramItemView(programItems),
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
              onPressed: ()  {
                programItems.add(new ProgramItem("test3", Icons.access_time, Colors.white, false, false, 0));
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
        onPressed: (
        ) {
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