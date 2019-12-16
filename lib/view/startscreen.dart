import 'package:audientes/AppColors.dart';
import 'package:community_material_icon/community_material_icon.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
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
  List<String> nameList = new List<String>();
  List<IconData> iconList = new List<IconData>();
  IconData iconMain = Icons.headset;
  var documents = [];
  List<int> results = new List<int>();

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
                          child: Text(
                            "L",
                            style: TextStyle(color: AppColors().text),
                          ),
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            color: AppColors().bar2,
                            boxShadow: [
                              BoxShadow(
                                color: Colors.black12,
                                blurRadius: 5.0,
                                // has the effect of softening the shadow
                                spreadRadius: 2.0,
                                // has the effect of extending the shadow
                                offset: Offset(
                                  1.0, // horizontal, move right 10
                                  1.0, // vertical, move down 10
                                ),
                              )
                            ],
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
                          child: Text(
                            "R",
                            style: TextStyle(color: AppColors().text),
                          ),
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            color: AppColors().bar2,
                            boxShadow: [
                              BoxShadow(
                                color: Colors.black12,
                                blurRadius: 5.0,
                                // has the effect of softening the shadow
                                spreadRadius: 2.0,
                                // has the effect of extending the shadow
                                offset: Offset(
                                  1.0, // horizontal, move right 10
                                  1.0, // vertical, move down 10
                                ),
                              )
                            ],
                          ),
                          height: MediaQuery.of(context).size.width / 6,
                          width: MediaQuery.of(context).size.height / 6,
                          alignment: Alignment.center,
                        ),
                        onTap: () {
                          showDialogRight();
                        },
                      ),
                      flex: 4,
                    ),
                  ],
                ),
                color: AppColors().bar,
              ),
            ),
            Container(
              margin: EdgeInsets.only(top: 10),
              child: Column(
                children: <Widget>[
                  Text("Recent Result",
                  style: TextStyle(
                    color: AppColors().text,
                    fontSize: 20,
                  ),)
                ],
              ),
            ),
            Container(
              decoration: new BoxDecoration(
                borderRadius: new BorderRadius.circular(16.0),
                color: AppColors().bar,
              ),
              margin: EdgeInsets.symmetric(vertical: 10.0, horizontal: 10),
              padding: EdgeInsets.symmetric(horizontal: 10),
              //Tjek lige det her
              width: MediaQuery.of(context).size.width * 0.8,
              height: MediaQuery.of(context).size.height * 0.45,

              child: Column(
                children: <Widget>[
                  Text(
                    "18-12-2019",
                    style: TextStyle(
                      color: AppColors().text,
                      fontSize: 16,
                    ),
                  ),
                  FlChart(
                    swapAnimationDuration: Duration(milliseconds: 250),
                    chart: LineChart(sampleData2(results)),
                  )
                ],
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


  LineChartData sampleData2(List<int> data) {
    if(data.isEmpty){
      results.add(2);
      results.add(4);
      results.add(6);
      results.add(7);
      results.add(4);
      results.add(7);
      results.add(6);
      results.add(1);
      results.add(9);
      results.add(2);
      results.add(5);
      results.add(7);
    }
    return LineChartData(
      lineTouchData: const LineTouchData(enabled: false),
      gridData: FlGridData(
        drawVerticalGrid: true,
        drawHorizontalGrid: true,
        show: true,
        getDrawingHorizontalGridLine: (value) {
          return const FlLine(
            color: Color(0xff2271bf),
            strokeWidth: 1,
          );
        },
        getDrawingVerticalGridLine: (value) {
          return const FlLine(
            color: Color(0xff37434d),
            strokeWidth: 1,
          );
        },
      ),
      titlesData: FlTitlesData(
        bottomTitles: SideTitles(
          showTitles: true,
          textStyle: TextStyle(
            color: AppColors().text,
            fontWeight: FontWeight.bold,
            fontSize: 16,
          ),
          getTitles: (value) {
            switch (value.toInt()) {
              case 1:
                return '125';
              case 2:
                return '250';
              case 3:
                return '500';
              case 4:
                return '1k';
              case 5:
                return '2k';
              case 6:
                return '4k';
              case 7:
                return '8k';
            }
            return '';
          },
          margin: 10,
        ),
        leftTitles: SideTitles(
          showTitles: true,
          textStyle: TextStyle(
            color: AppColors().text,
            fontWeight: FontWeight.bold,
            fontSize: 14,
          ),
          getTitles: (value) {
            switch (value.toInt()) {
              case -1:
                return '100';
              case 0:
                return '90';
              case 1:
                return '80';
              case 2:
                return '70';
              case 3:
                return '60';
              case 4:
                return '50';
              case 5:
                return '40';
              case 6:
                return '30';
              case 7:
                return '20';
              case 8:
                return '10';
              case 9:
                return '0';
              case 10:
                return '-10';
            }
            return '';
          },
          margin: 10,
        ),
      ),
      borderData: FlBorderData(
          show: true,
          border: Border(
            bottom: BorderSide(
              color: AppColors().inactive,
              width: 4,
            ),
            left: BorderSide(
              color: Colors.transparent,
            ),
            right: BorderSide(
              color: Colors.transparent,
            ),
            top: BorderSide(
              color: Colors.transparent,
            ),
          )),
      minX: 1,
      maxX: 7.3,
      maxY: 10.4,
      minY: -1,
      lineBarsData: linesBarData2(data),
    );
  }

  List<LineChartBarData> linesBarData2(List<int> data) {
    return [
      LineChartBarData(
        spots: [
          FlSpot(2, data.elementAt(0).toDouble()),
          FlSpot(3, data.elementAt(1).toDouble()),
          FlSpot(4, data.elementAt(2).toDouble()),
          FlSpot(5, data.elementAt(3).toDouble()),
          FlSpot(6, data.elementAt(4).toDouble()),
          FlSpot(7, data.elementAt(5).toDouble()),
        ],
        isCurved: true,
        curveSmoothness: 0,
        colors: [
          AppColors().green1,
          Colors.greenAccent,
          Colors.green,
        ],
        barWidth: 2,
        isStrokeCapRound: true,
        dotData: FlDotData(
          show: true,
        ),
        belowBarData: BarAreaData(
          show: false,
        ),
      ),
      LineChartBarData(
        spots: [
          FlSpot(2, data.elementAt(6).toDouble()),
          FlSpot(3, data.elementAt(7).toDouble()),
          FlSpot(4, data.elementAt(8).toDouble()),
          FlSpot(5, data.elementAt(9).toDouble()),
          FlSpot(6, data.elementAt(10).toDouble()),
          FlSpot(7, data.elementAt(11).toDouble()),
        ],
        isCurved: true,
        curveSmoothness: 0,
        colors: [
          AppColors().red1,
          Colors.redAccent,
        ],
        barWidth: 2,
        isStrokeCapRound: true,
        dotData: FlDotData(
          show: true,
        ),
        belowBarData: BarAreaData(
          show: false,
        ),
      ),
    ];
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

  fillNameList(List<String> nameList) {
    if (nameList.isEmpty) {
      nameList.add("Windy");
      nameList.add("Standard ");
      nameList.add("Heavy rain");
      nameList.add("Music");
      nameList.add("Office");
      nameList.add("Noisy area");
      nameList.add("Traffic");
      nameList.add("Night time");
      nameList.add("Phone call");
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

  @override
  void initState() {
    super.initState();
    readEar();
  }

  Widget programIcon() {
    fillIconList(iconList);
    fillNameList(nameList);
    return GestureDetector(
      child: Container(
          child: new Icon(
            iconMain,
            color: Colors.white,
            size: MediaQuery.of(context).size.width / 3,
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
                children: List.generate(
                  iconList.length,
                  (index) {
                    return Column(
                      children: <Widget>[
                        Container(
                          child: RawMaterialButton(
                            onPressed: () {
                              Navigator.pop(context);
                            },
                            shape: new CircleBorder(),
                            child: new Icon(
                              iconList.elementAt(index),
                              color: Colors.white,
                              size: 30,
                            ),
                          ),
                          width: MediaQuery.of(context).size.width / 5,
                          decoration: BoxDecoration(
                              shape: BoxShape.circle, color: Colors.black),
                        ),
                        Text(
                          nameList.elementAt(index),
                          style: TextStyle(
                            color: Colors.white,
                          ),
                        )
                      ],
                    );
                  },
                ),
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
    // TODO: implement should Reclip
    return null;
  }
}

class ProgramPicker extends StatefulWidget {
  final double initialLeftEar;
  final String keyEar;

  const ProgramPicker({Key key, this.initialLeftEar, this.keyEar})
      : super(key: key);

  @override
  DialogPickerState createState() => DialogPickerState();
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
                  color: AppColors().bar2,
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
                  Navigator.pop(context);
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
