import 'package:audientes/AppColors.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_xlider/flutter_xlider.dart';
import 'package:volume/volume.dart';
import 'dart:async';
import 'package:audientes/model/programItem.dart';
import 'package:audientes/view/widgets/programItemView.dart';
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

  Future<void> initPlatformState() async {
    await Volume.controlVolume(AudioManager.STREAM_MUSIC);
  }

  void showDialogLeft() async {
    final leftEarValue = await showDialog<double>(
      context: context,
      builder: (context) => DialogPicker(initialLeftEar: leftEar, keyEar: "Leftear" ),
    );
    if (leftEarValue != null) {
      setState(() {
        leftEar = leftEarValue;
      });
    }
  }

  void showDialogRight() async {
    // <-- note the async keyword here

    // this will contain the result from Navigator.pop(context, result)
    final rightEarValue = await showDialog<double>(
      context: context,
      builder: (context) => DialogPicker(initialLeftEar: rightEar, keyEar: "Rightear" ),
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
                      child: Column(
                        children: <Widget>[
                          SizedBox(height: 10),

                      RawMaterialButton(
                        onPressed: ()  {
                          showDialogLeft();
                        },
                        shape: new CircleBorder(),
                        fillColor: Colors.green,
                      ),

                          Text(leftEar.toString()),

                        ]
                      ),
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
              setState(() {
                value = q;
              });
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



                child: RotatedBox(quarterTurns: 3,
                  child: Container(
                    child: Slider(
                        max: 100.0,
                        min: 0.0,
                      value: leftEar,
                      onChanged: (double q) {
                          leftEar = q;
                          setState(() {
                            leftEar = q;
                          });
                      },
                    ),
                    color: Colors.deepPurple,
                    height: 50,
                  ),
                )
            ),

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

/*
  void selectIconDialog() {
    showDialog(
        context: context,
        builder: (BuildContext contex) {
          return AlertDialog(
            contentPadding: const EdgeInsets.all(10.0),
            title: new Text(
              'Pick a icon!',
              style:
              new TextStyle(fontWeight: FontWeight.bold, color: Colors.black),
            ),
            content: new Container(

                width: MediaQuery.of(context).size.width * .7,
                height: MediaQuery.of(context).size.width * .6,
                color: Colors.black,

                child: RotatedBox(quarterTurns: 3,
              child: Container(
                child: Slider(
                  max: 100.0,
                  min: 0.0,
                  value: Test,
                  onChanged: (double qqq) {
                    Test = qqq;
                    setState(() {
                      Test = qqq;
                    });
                    setState(() {
                    });
                  },
                ),
                color: Colors.deepPurple,
                height: 50,
              ),
            )



            ),
          );
        }
    );
  }
*/
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

  const DialogPicker({Key key, this.initialLeftEar, this.keyEar}) : super(key: key);

  @override
  DialogPickerState createState() => DialogPickerState();
}

class DialogPickerState extends State<DialogPicker> {
  /// current selection of the slider
  double ear;
  String keyEar;

  @override
  void initState() {
    super.initState();
    ear = widget.initialLeftEar;
    keyEar = widget.keyEar;
  }


  saveEar(String keyEar, double ear) async {
    final prefs = await SharedPreferences.getInstance();
    prefs.setDouble(keyEar, ear);
  }


  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text('Font Size'),
      content:new Container(

          width: MediaQuery.of(context).size.width * .7,
          height: MediaQuery.of(context).size.width * .6,
          color: Colors.black,

          child: RotatedBox(quarterTurns: 3,
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
          )

      ),
      actions: <Widget>[
        FlatButton(
          onPressed: () {
            // Use the second argument of Navigator.pop(...) to pass
            // back a result to the page that opened the dialog
            Navigator.pop(context, ear);
          },
          child: Text('DONE'),
        )
      ],
    );
  }


}
