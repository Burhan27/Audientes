import 'package:audientes/AppColors.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:community_material_icon/community_material_icon.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_xlider/flutter_xlider.dart';
import 'package:volume/volume.dart';
import 'dart:async';
import 'package:audientes/model/programItem.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:audientes/controller/ProgramController.dart';

class MyApp extends StatefulWidget {
  @override
  StartScreen createState() => StartScreen();
}

class StartScreen extends State<MyApp> {
  int maxVol = 0, currentVol = 0;
  double leftEar = 0.0, rightEar = 0.0;
  List<ProgramItem> programItems = new List<ProgramItem>();
  ProgramController programController = new ProgramController();
  List<IconData> iconList = new List<IconData>();
  IconData iconMain = Icons.headset;
  var documents = [];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        child: Column(
          children: <Widget>[
            ClipPath(
              clipper: CurveClip(),
              child: Container(
                width: MediaQuery.of(context).size.width,
                height: MediaQuery.of(context).size.height / 2,
                child: Row(
                  children: <Widget>[
                    Expanded(
                      child: GestureDetector(
                        child: Container(
                          child: Text("L"),
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            color: Colors.deepPurple,
                          ),
                          height: MediaQuery.of(context).size.width / 6,
                          width: MediaQuery.of(context).size.height / 6,
                          alignment: Alignment.center,
                        ),
                        onTap: () => showDialogLeft(),
                      ),
                      flex: 4,
                    ),
                    Expanded(
                      child: programIcon(),
                      flex: 6,
                    ),
                    Expanded(
                      child: GestureDetector(
                        child: Container(
                          child: Text("R"),
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            color: Colors.deepPurple,
                          ),
                          height: MediaQuery.of(context).size.width / 6,
                          width: MediaQuery.of(context).size.height / 6,
                          alignment: Alignment.center,
                        ),
                        onTap: () => showDialogRight(),
                      ),
                      flex: 4,
                    ),
                  ],
                ),
                color: AppColors().bar,
              ),
            ),
          ],
        ),
        color: AppColors().background,
        width: double.infinity,
        height: double.infinity,
      ),
    );
  }

  Future<void> initPlatformState() async {
    await Volume.controlVolume(AudioManager.STREAM_MUSIC);
  }

  void showDialogLeft() async {
    final leftEarValue = await showDialog<double>(
      context: context,
      builder: (context) =>
          DialogPicker(initialLeftEar: leftEar, keyEar: "Leftear"),
    );
    if (leftEarValue != null) {
      setState(() {
        leftEar = leftEarValue;
      });
    }
  }

  fillIconList(List<IconData> iconList) {
    if (iconList.isEmpty) {
      iconList.add(CommunityMaterialIcons.weather_windy);
      iconList.add(Icons.headset);
      iconList.add(CommunityMaterialIcons.weather_pouring);
      iconList.add(CommunityMaterialIcons.music);
      iconList.add(CommunityMaterialIcons.briefcase);
      iconList.add(CommunityMaterialIcons.voice);
      iconList.add(Icons.traffic);
      iconList.add(CommunityMaterialIcons.weather_night);
      iconList.add(CommunityMaterialIcons.phone_in_talk);
    }
  }

  void showDialogRight() async {
    // <-- note the async keyword here

    // this will contain the result from Navigator.pop(context, result)
    final rightEarValue = await showDialog<double>(
      context: context,
      builder: (context) =>
          DialogPicker(initialLeftEar: rightEar, keyEar: "Rightear"),
    );
    if (rightEarValue != null) {
      setState(() {
        rightEar = rightEarValue;
      });
    }
  }

  readEar() async {
    final prefs = await SharedPreferences.getInstance();
    leftEar = prefs.getDouble("Leftear") ?? 0.0;
    rightEar = prefs.getDouble("Rightear") ?? 0.0;
  }

  findItem(async) {}

  @override
  void initState() {
    super.initState();
    readEar();
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

  Widget programIcon() {
    fillIconList(iconList);
    return GestureDetector(
      child: Container(
          child: new Icon(
            iconMain,
            color: Colors.white,
            size: MediaQuery.of(context).size.width/3,
          ),
          decoration: BoxDecoration(
              shape: BoxShape.circle, color: AppColors().background),
          height: double.infinity,
          width: double.infinity),
      onTap: () => iconDialog(),
    );
  }

  void iconDialog() {
    showDialog(
        context: context,
        builder: (BuildContext contex) {
          return Dialog(
            backgroundColor: Colors.transparent,
            child: Container(
              height: 300,
              child: GridView.count(
                crossAxisCount: 3,
                children: List.generate(iconList.length, (index) {
                  return RawMaterialButton(
                    onPressed: () {
                      iconMain = iconList.elementAt(index);
                      Navigator.pop(context);
                      setState(() {});
                    },
                    shape: new CircleBorder(),
                    child: new Icon(
                      iconList.elementAt(index),
                      color: Colors.white30,
                    ),
                    fillColor: Colors.black,
                  );
                }),
                padding: const EdgeInsets.all(10),
                mainAxisSpacing: 10,
                crossAxisSpacing: 10,
              ),
            ),
          );
        });
  }
}

class CurveClip extends CustomClipper<Path> {
  @override
  Path getClip(Size size) {
    var path = Path();
    path.lineTo(0.0, size.height - 50);
    path.quadraticBezierTo(
        size.width / 4, size.height, size.width / 2, size.height);
    path.quadraticBezierTo(size.width - (size.width / 4), size.height,
        size.width, size.height - 50);
    path.lineTo(size.width, 0.0);
    path.close();
    return path;
  }

  @override
  bool shouldReclip(CustomClipper<Path> oldClipper) {
    // TODO: implement shouldReclip
    return null;
  }
}

class DialogPicker extends StatefulWidget {
  /// initial selection for the slider
  final double initialLeftEar;
  final String keyEar;

  const DialogPicker({Key key, this.initialLeftEar, this.keyEar})
      : super(key: key);

  @override
  DialogPickerState createState() => DialogPickerState();
}

class DialogPickerState extends State<DialogPicker> {
  /// current selection of the slider
  double ear;
  String keyEar;
  String Ear;

  @override
  void initState() {
    super.initState();
    ear = widget.initialLeftEar;
    keyEar = widget.keyEar;

    if (keyEar.contains("Left")) {
      Ear = "left ear";
    } else {
      Ear = "right ear";
    }
  }

  saveEar(String keyEar, double ear) async {
    final prefs = await SharedPreferences.getInstance();
    prefs.setDouble(keyEar, ear);
  }

  @override
  Widget build(BuildContext context) {
    return Dialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30.0)),
      backgroundColor: Colors.transparent,
      child: Container(
        height: 350.0,
        width: 20.0,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Padding(
              padding: EdgeInsets.only(top: 15.0),
              child: Text(
                'Volume for ' + Ear,
                style: TextStyle(color: Colors.white),
              ),
            ),
            Padding(
              padding: EdgeInsets.only(top: 10.0, bottom: 10.0),
              child: RotatedBox(
                quarterTurns: 3,
                child: Container(
                  child: Slider(
                    max: 100.0,
                    min: 0.0,
                    value: ear,
                    onChanged: (double qqq) {
                      ear = qqq;
                      setState(() {
                        ear = qqq;
                        saveEar(keyEar, ear);
                      });
                    },
                  ),
                  color: Colors.deepPurple,
                  height: 50,
                ),
              ),
            ),
            Text(
              ear.round().toString(),
              style: TextStyle(color: Colors.white),
            ),
            Padding(padding: EdgeInsets.only(bottom: 1.0)),
            FlatButton(
                onPressed: () {
                  Navigator.pop(context, ear);
                },
                child: Text(
                  'Done',
                  style: TextStyle(color: Colors.white, fontSize: 18.0),
                ))
          ],
        ),
      ),
    );
  }
}
