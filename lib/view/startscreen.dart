import 'package:audientes/AppColors.dart';
import 'package:expandable/expandable.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_xlider/flutter_xlider.dart';
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
                      child: clickableSound("L", 0.0),
                      flex: 4,
                    ),
                    Expanded(
                      child: programIcon(),
                      flex: 6,
                    ),
                    Expanded(
                      child: testVert(),
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

  Widget programIcon() {
    return Container(
        child: new Icon(
          Icons.headset,
          size: MediaQuery.of(context).size.width / 3,
          color: Colors.white,
        ),
        decoration: BoxDecoration(
            shape: BoxShape.circle, color: AppColors().background),
        height: double.infinity,
        width: double.infinity);
  }

  Widget clickableSound(String text, double currentVol) {
    return GestureDetector(
      child: Container(
        child: Text(text),
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: Colors.deepPurple,
        ),
        height: MediaQuery.of(context).size.width / 6,
        width: MediaQuery.of(context).size.height / 6,
        alignment: Alignment.center,
      ),
      onTap: () => showDialog(
          context: context, builder: (BuildContext context) => sliderDialog()),
    );
  }

  Widget testVert() {
    return Container(
      child: FlutterSlider(
        values: [20.0],
        min: 0.0,
        max: 100.0,
        axis: Axis.vertical,
        rtl: true,
        handlerAnimation: FlutterSliderHandlerAnimation(
            curve: Curves.elasticOut,
            reverseCurve: Curves.bounceIn,
            duration: Duration(milliseconds: 500),
            scale: 1.5
        ),
        onDragging: (handlerIndex, lowerValue, upperValue) {
          lowerValue = lowerValue;
          upperValue = upperValue;
          setState(() {});
        },
      ),
      width: 80,
    );
  }

  Widget verticalSlider(double value) {
    return RotatedBox(
      child: Container(
        child: Slider(
            max: 100.0,
            min: 0.0,
            value: value,
            onChanged: (double q) {
              value = q;
              setState(() {});
            }),
        color: Colors.deepPurple,
        height: 50,
      ),
      quarterTurns: 3,
    );
  }

  Dialog sliderDialog() {
    return new Dialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30.0)),
      backgroundColor: Colors.transparent,
      child: Container(
        height: 300.0,
        width: 20.0,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Padding(
              padding: EdgeInsets.only(top: 15.0),
              child: Text(
                'Volume',
                style: TextStyle(color: Colors.white),
              ),
            ),
            Padding(
                padding: EdgeInsets.only(top: 10.0, bottom: 10.0),
                child: verticalSlider(20.0)),
            Padding(padding: EdgeInsets.only(bottom: 1.0)),
            FlatButton(
                onPressed: () {
                  Navigator.of(context).pop();
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
