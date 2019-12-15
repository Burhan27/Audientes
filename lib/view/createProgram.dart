import 'dart:async';
import 'package:audientes/AppColors.dart';
import 'package:audientes/controller/ProgramController.dart';
import 'package:community_material_icon/community_material_icon.dart';
import 'package:flutter/material.dart';

class createProgram extends StatefulWidget {
  @override
  createProgramState createState() => createProgramState();
}

class createProgramState extends State<createProgram> {
  TextEditingController nameController = new TextEditingController();
  String name;
  Timer timer;
  Color test = Colors.orangeAccent;
  IconData standardIcon = Icons.headset;
  int i = 0;
  double highPlus = 0.0, high = 0.0, low = 0.0, medium = 0.0, lowPlus = 0.0;
  int colorCode = 99, iconCode = 99;
  List<Color> colorList = new List<Color>();
  List<IconData> iconList = new List<IconData>();
  ProgramController programController = new ProgramController();
  var documents = [];

  createProgramstate() {
    nameController.addListener(nameListener);
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

  fillColorList(List<Color> colorList) {
    if (colorList.isEmpty) {
      colorList.add(AppColors().blue1);
      colorList.add(AppColors().orange1);
      colorList.add(AppColors().red1);
      colorList.add(AppColors().green1);
      colorList.add(AppColors().turq1);
      colorList.add(Colors.purpleAccent);
      colorList.add(Colors.limeAccent);
      colorList.add(Colors.indigoAccent);
      colorList.add(Colors.amberAccent);
    }
  }

  @override
  void initState() {
    super.initState();

    fillIconList(iconList);
    fillColorList(colorList);
  }

  nameListener() {
    if (nameController.text.isEmpty) {
      name = "";
    } else {
      name = nameController.text;
    }
  }

  void selectColorDialog() {
    showDialog(
        context: context,
        builder: (BuildContext contex) {
          return AlertDialog(
            contentPadding: const EdgeInsets.all(10.0),
            backgroundColor: AppColors().bar,
            title: new Text(
              'Pick a color!',
              style: new TextStyle(
                  fontWeight: FontWeight.bold, color: AppColors().text),
            ),
            content: new Container(
                width: MediaQuery.of(context).size.width * .7,
                height: MediaQuery.of(context).size.width * .6,
                color: Colors.transparent,
                child: GridView.count(
                  crossAxisCount: 4,
                  children: List.generate(colorList.length, (index) {
                    return RawMaterialButton(
                      onPressed: () {
                        colorCode = index;
                        test = colorList.elementAt(index);
                        Navigator.pop(context);
                        setState(() {});
                      },
                      shape: new CircleBorder(),
                      fillColor: colorList.elementAt(index),
                    );
                  }),
                  padding: const EdgeInsets.all(10),
                  mainAxisSpacing: 10,
                  crossAxisSpacing: 10,
                )),
          );
        });
  }

  void selectIconDialog() {
    showDialog(
        context: context,
        builder: (BuildContext contex) {
          return AlertDialog(
            contentPadding: const EdgeInsets.all(10.0),
            backgroundColor: AppColors().bar,
            title: new Text(
              'Pick a icon!',
              style: new TextStyle(
                  fontWeight: FontWeight.bold, color: AppColors().text),
            ),
            content: new Container(
                width: MediaQuery.of(context).size.width * .7,
                height: MediaQuery.of(context).size.width * .6,
                child: GridView.count(
                  crossAxisCount: 5,
                  children: List.generate(iconList.length, (index) {
                    return RawMaterialButton(
                      onPressed: () {
                        standardIcon = iconList.elementAt(index);
                        iconCode = index;
                        Navigator.pop(context);
                        setState(() {});
                      },
                      child: new Icon(
                        iconList.elementAt(index),
                        color: AppColors().highlight,
                        size: 20.0,
                      ),
                      shape: new CircleBorder(),
                      fillColor: AppColors().bar2,
                    );
                  }),
                  padding: const EdgeInsets.all(10),
                  mainAxisSpacing: 10,
                  crossAxisSpacing: 10,
                )),
          );
        });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomPadding: false,
      appBar: AppBar(
        iconTheme: IconThemeData(color: AppColors().highlight),
        backgroundColor: AppColors().background,
      ),
      body: Container(
          width: double.infinity,
          alignment: Alignment.center,
          color: AppColors().background,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              SizedBox(height: 10),
              Container(
                width: MediaQuery.of(context).size.width*0.95,
                child:TextField(
                style: TextStyle(color: AppColors().bar1),
                autofocus: false,
                onChanged: (text) {
                  name = text;
                },
                decoration: new InputDecoration(
                  enabledBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: AppColors().bar2),
                    borderRadius: BorderRadius.all(Radius.circular(20)),
                  ),
                  labelStyle: TextStyle(color: AppColors().bar2),
                  labelText: 'Program name',
                  fillColor: AppColors().text,
                  focusColor: AppColors().highlight,
                  border: OutlineInputBorder(
                    borderSide: BorderSide(color: AppColors().highlight),
                    borderRadius: BorderRadius.all(Radius.circular(4)),
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(
                      color: AppColors().highlight,
                    ),
                    borderRadius: BorderRadius.all(Radius.circular(4)),
                  ),
                ),
              ),
      ),
              SizedBox(height: 10),
              Container(
                padding: EdgeInsets.fromLTRB(10, 10, 10, 0),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: <Widget>[
                    Container(
                      width: MediaQuery.of(context).size.width / 2.2,
                      height: MediaQuery.of(context).size.height /10,
                      child: Card(
                        color: AppColors().background,
                        elevation: 5,
                        child: Row(
                          children: <Widget>[
                            Text(
                              " Color: ",
                              style: TextStyle(
                                  color: AppColors().plainText, fontSize: 20),
                            ),
                            FlatButton(
                              onPressed: () {
                                selectColorDialog();
                              },
                              color: test,
                            ),
                          ],
                        ),
                      ),
                    ),
                    Container(
                      width: MediaQuery.of(context).size.width / 2.2,
                      height: MediaQuery.of(context).size.height /10,
                      child: Card(
                        color: AppColors().background,
                        elevation: 5,
                        child: Row(
                          children: <Widget>[
                            Text(
                              " Icon: ",
                              style: TextStyle(
                                  color: AppColors().plainText, fontSize: 20),
                            ),
                            GestureDetector(
                              onTap: () {
                                selectIconDialog();
                              },
                              child: Container(
                                height: MediaQuery.of(context).size.width / 7,
                                width: MediaQuery.of(context).size.height / 7,
                                child: new Icon(
                                  standardIcon,
                                  color: AppColors().highlight,
                                  size: 25.0,
                                ),
                                decoration: BoxDecoration(
                                    shape: BoxShape.circle,
                                color: AppColors().bar1),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              SizedBox(height: 10),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[
                  Text(
                    'High+',
                    style: TextStyle(color: AppColors().plainText, fontSize: 20),
                  ),
                  Text(
                    'High',
                    style: TextStyle(color: AppColors().plainText, fontSize: 20),
                  ),
                  Text(
                    'Medium',
                    style: TextStyle(color: AppColors().plainText, fontSize: 20),
                  ),
                  Text(
                    'Low',
                    style: TextStyle(color: AppColors().plainText, fontSize: 20),
                  ),
                  Text(
                    'Low+',
                    style: TextStyle(color: AppColors().plainText, fontSize: 20),
                  ),
                ],
              ),
              RotatedBox(
                quarterTurns: 3,
                child: Container(
                  width: MediaQuery.of(context).size.height * 0.35,
                  child: Column(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: <Widget>[
                        SliderTheme(
                          data: SliderThemeData(
                              thumbColor: AppColors().highlight,
                              trackHeight: 10,
                              inactiveTrackColor: AppColors().inactive,
                              activeTrackColor: AppColors().highlight,
                              inactiveTickMarkColor: AppColors().highlight,
                              activeTickMarkColor: AppColors().orange1,
                              thumbShape: RoundSliderThumbShape(
                                  enabledThumbRadius: 15)),
                          child: Slider(
                            min: 0.0,
                            max: 5.0,
                            divisions: 5,
                            value: highPlus,
                            onChanged: (double h) {
                              highPlus = h;
                              setState(() {
                                highPlus = h;
                              });
                            },
                          ),
                        ),
                        SliderTheme(
                          data: SliderThemeData(
                              thumbColor: AppColors().highlight,
                              trackHeight: 10,
                              inactiveTrackColor: AppColors().inactive,
                              activeTrackColor: AppColors().highlight,
                              inactiveTickMarkColor: AppColors().highlight,
                              activeTickMarkColor: AppColors().orange1,
                              thumbShape: RoundSliderThumbShape(
                                  enabledThumbRadius: 15)),
                          child: Slider(
                            min: 0.0,
                            max: 5.0,
                            divisions: 5,
                            value: high,
                            onChanged: (double hh) {
                              high = hh;
                              setState(() {
                                high = hh;
                              });
                            },
                          ),
                        ),
                        SliderTheme(
                          data: SliderThemeData(
                              thumbColor: AppColors().highlight,
                              trackHeight: 10,
                              inactiveTrackColor: AppColors().inactive,
                              activeTrackColor: AppColors().highlight,
                              inactiveTickMarkColor: AppColors().highlight,
                              activeTickMarkColor: AppColors().orange1,
                              thumbShape: RoundSliderThumbShape(
                                  enabledThumbRadius: 15)),
                          child: Slider(
                            min: 0.0,
                            max: 5.0,
                            divisions: 5,
                            value: medium,
                            onChanged: (double m) {
                              medium = m;
                              setState(() {
                                medium = m;
                              });
                            },
                          ),
                        ),
                        SliderTheme(
                          data: SliderThemeData(
                              thumbColor: AppColors().highlight,
                              trackHeight: 10,
                              inactiveTrackColor: AppColors().inactive,
                              activeTrackColor: AppColors().highlight,
                              inactiveTickMarkColor: AppColors().highlight,
                              activeTickMarkColor: AppColors().orange1,
                              thumbShape: RoundSliderThumbShape(
                                  enabledThumbRadius: 15)),
                          child: Slider(
                            min: 0.0,
                            max: 5.0,
                            divisions: 5,
                            value: low,
                            onChanged: (double l) {
                              low = l;
                              setState(() {
                                low = l;
                              });
                            },
                          ),
                        ),
                        SliderTheme(
                          data: SliderThemeData(
                              thumbColor: AppColors().highlight,
                              trackHeight: 10,
                              inactiveTrackColor: AppColors().inactive,
                              activeTrackColor: AppColors().highlight,
                              inactiveTickMarkColor: AppColors().highlight,
                              activeTickMarkColor: AppColors().orange1,
                              thumbShape: RoundSliderThumbShape(
                                  enabledThumbRadius: 15)),
                          child: Slider(
                            min: 0.0,
                            max: 5.0,
                            divisions: 5,
                            value: lowPlus,
                            onChanged: (double ll) {
                              lowPlus = ll;
                              setState(() {
                                lowPlus = ll;
                              });
                            },
                          ),
                        ),
                      ]),
                  padding: EdgeInsets.fromLTRB(0, 5, 0, 0),
                ),
              ),
              Container(
                width: MediaQuery.of(context).size.width * .7,
                child: Card(
                  color: Colors.transparent,
                  elevation: 10,
                  child: FlatButton(
                    color: AppColors().bar2,
                    child: Text(
                      'Create new program',
                      style: TextStyle(color: AppColors().text, fontSize: 20),
                    ),
                    shape: RoundedRectangleBorder(
                        borderRadius: new BorderRadius.circular(18.0),
                        side: BorderSide()),
                    onPressed: () {
                      setState(() {});

                      if (colorCode == 99 || iconCode == 99) {
                        Navigator.pop(context);
                      } else {
                        programController.createProgram2(name, colorCode,
                            iconCode, high, highPlus, medium, low, lowPlus);
                        Navigator.pop(context);
                      }
                    },
                  ),
                ),
              ),
            ],
          )),
    );
  }
}
