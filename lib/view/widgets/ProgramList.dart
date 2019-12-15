import 'package:audientes/AppColors.dart';
import 'package:audientes/view/widgets/settingOption.dart';
import 'package:audientes/view/widgets/mixer.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:expandable/expandable.dart';
import 'package:audientes/controller/ProgramController.dart';
import 'package:flutter/material.dart';
import 'package:cloud_firestore/cloud_firestore.dart';

class ProgramList extends StatefulWidget {
//  ProgramItemView(programItems);
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return ProgramListState();
  }
}

class ProgramListState extends State<ProgramList> {
  ProgramController programController = new ProgramController();
  var documents = [];

  @override
  void initState() {
    super.initState();
    Firestore.instance.collection("Programs").getDocuments().then((data) {
      var list = data.documents;
      setState(() {
        documents = list;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: new BoxDecoration(
        borderRadius: new BorderRadius.circular(16.0),
        color: AppColors().bar,
      ),
      width: MediaQuery.of(context).size.width,
      height: MediaQuery.of(context).size.width,
      child: new ListView.builder(
          itemCount: documents.length,
          itemBuilder: (BuildContext ctxt, int index) {
            return new ExpandableNotifier(
              child: Column(
                children: <Widget>[
                  Expandable(
                    collapsed: ExpandableButton(
                      child: settingOption(
                          programController
                              .getColor(documents[index].data['color']),
                          programController
                              .getIcon(documents[index].data['icon']),
                          documents[index].data['name'],
                          false),
                    ),
                    expanded: Card(
                      elevation: 5,
                      color: AppColors().bar,
                      child: Column(
                        children: <Widget>[
                          settingOption(
                              programController
                                  .getColor(documents[index].data['color']),
                              programController
                                  .getIcon(documents[index].data['icon']),
                              documents[index].data['name'],
                              true),
                          new Container(
                            child: Column(
                              children: <Widget>[
                                Row(
                                  mainAxisAlignment:
                                      MainAxisAlignment.spaceEvenly,
                                  children: <Widget>[
                                    Text(
                                      'High+',
                                      style: TextStyle(
                                          color: Colors.white, fontSize: 20),
                                    ),
                                    Text(
                                      'High',
                                      style: TextStyle(
                                          color: Colors.white, fontSize: 20),
                                    ),
                                    Text(
                                      'Medium',
                                      style: TextStyle(
                                          color: Colors.white, fontSize: 20),
                                    ),
                                    Text(
                                      'Low',
                                      style: TextStyle(
                                          color: Colors.white, fontSize: 20),
                                    ),
                                    Text(
                                      'Low+',
                                      style: TextStyle(
                                          color: Colors.white, fontSize: 20),
                                    ),
                                  ],
                                ),
                                RotatedBox(
                                  quarterTurns: 3,
                                  child: Container(
                                    width: MediaQuery.of(context).size.height *
                                        0.35,
                                    child: Column(
                                        mainAxisAlignment:
                                            MainAxisAlignment.spaceEvenly,
                                        children: <Widget>[
                                          SliderTheme(
                                            data: SliderThemeData(
                                                thumbColor:
                                                    AppColors().highlight,
                                                trackHeight: 10,
                                                inactiveTrackColor:
                                                    AppColors().inactive,
                                                activeTrackColor:
                                                    AppColors().highlight,
                                                inactiveTickMarkColor:
                                                    AppColors().highlight,
                                                activeTickMarkColor:
                                                    AppColors().orange1,
                                                thumbShape:
                                                    RoundSliderThumbShape(
                                                        enabledThumbRadius:
                                                            15)),
                                            child: Slider(
                                              min: 0.0,
                                              max: 5.0,
                                              divisions: 5,
                                              value: documents[index]
                                                  .data['high+'],
                                              onChanged: (double h) {
                                                documents[index].data['high+'] =
                                                    h;
                                                setState(() {
                                                  documents[index]
                                                      .data['high+'] = h;
                                                  programController
                                                      .updateProgram(
                                                          documents[index]
                                                              .data['name'],
                                                          documents[index]
                                                              .data['high'],
                                                          documents[index]
                                                              .data['high+'],
                                                          documents[index]
                                                              .data['medium'],
                                                          documents[index]
                                                              .data['low'],
                                                          documents[index]
                                                              .data['low+']);
                                                });
                                              },
                                            ),
                                          ),
                                          SliderTheme(
                                            data: SliderThemeData(
                                                thumbColor:
                                                    AppColors().highlight,
                                                trackHeight: 10,
                                                inactiveTrackColor:
                                                    AppColors().inactive,
                                                activeTrackColor:
                                                    AppColors().highlight,
                                                inactiveTickMarkColor:
                                                    AppColors().highlight,
                                                activeTickMarkColor:
                                                    AppColors().orange1,
                                                thumbShape:
                                                    RoundSliderThumbShape(
                                                        enabledThumbRadius:
                                                            15)),
                                            child: Slider(
                                              min: 0.0,
                                              max: 5.0,
                                              divisions: 5,
                                              value:
                                                  documents[index].data['high'],
                                              onChanged: (double hh) {
                                                documents[index].data['high'] =
                                                    hh;
                                                setState(() {
                                                  documents[index]
                                                      .data['high'] = hh;
                                                  programController
                                                      .updateProgram(
                                                          documents[index]
                                                              .data['name'],
                                                          documents[index]
                                                              .data['high'],
                                                          documents[index]
                                                              .data['high+'],
                                                          documents[index]
                                                              .data['medium'],
                                                          documents[index]
                                                              .data['low'],
                                                          documents[index]
                                                              .data['low+']);
                                                });
                                              },
                                            ),
                                          ),
                                          SliderTheme(
                                            data: SliderThemeData(
                                                thumbColor:
                                                    AppColors().highlight,
                                                trackHeight: 10,
                                                inactiveTrackColor:
                                                    AppColors().inactive,
                                                activeTrackColor:
                                                    AppColors().highlight,
                                                inactiveTickMarkColor:
                                                    AppColors().highlight,
                                                activeTickMarkColor:
                                                    AppColors().orange1,
                                                thumbShape:
                                                    RoundSliderThumbShape(
                                                        enabledThumbRadius:
                                                            15)),
                                            child: Slider(
                                              min: 0.0,
                                              max: 5.0,
                                              divisions: 5,
                                              value: documents[index]
                                                  .data['medium'],
                                              onChanged: (double m) {
                                                documents[index]
                                                    .data['medium'] = m;
                                                setState(() {
                                                  documents[index]
                                                      .data['medium'] = m;
                                                  programController
                                                      .updateProgram(
                                                          documents[index]
                                                              .data['name'],
                                                          documents[index]
                                                              .data['high'],
                                                          documents[index]
                                                              .data['high+'],
                                                          documents[index]
                                                              .data['medium'],
                                                          documents[index]
                                                              .data['low'],
                                                          documents[index]
                                                              .data['low+']);
                                                });
                                              },
                                            ),
                                          ),
                                          SliderTheme(
                                            data: SliderThemeData(
                                                thumbColor:
                                                    AppColors().highlight,
                                                trackHeight: 10,
                                                inactiveTrackColor:
                                                    AppColors().inactive,
                                                activeTrackColor:
                                                    AppColors().highlight,
                                                inactiveTickMarkColor:
                                                    AppColors().highlight,
                                                activeTickMarkColor:
                                                    AppColors().orange1,
                                                thumbShape:
                                                    RoundSliderThumbShape(
                                                        enabledThumbRadius:
                                                            15)),
                                            child: Slider(
                                              min: 0.0,
                                              max: 5.0,
                                              divisions: 5,
                                              value:
                                                  documents[index].data['low'],
                                              onChanged: (double l) {
                                                documents[index].data['low'] =
                                                    l;
                                                setState(() {
                                                  documents[index].data['low'] =
                                                      l;
                                                  programController
                                                      .updateProgram(
                                                          documents[index]
                                                              .data['name'],
                                                          documents[index]
                                                              .data['high'],
                                                          documents[index]
                                                              .data['high+'],
                                                          documents[index]
                                                              .data['medium'],
                                                          documents[index]
                                                              .data['low'],
                                                          documents[index]
                                                              .data['low+']);
                                                });
                                              },
                                            ),
                                          ),
                                          SliderTheme(
                                            data: SliderThemeData(
                                                thumbColor:
                                                    AppColors().highlight,
                                                trackHeight: 10,
                                                inactiveTrackColor:
                                                    AppColors().inactive,
                                                activeTrackColor:
                                                    AppColors().highlight,
                                                inactiveTickMarkColor:
                                                    AppColors().highlight,
                                                activeTickMarkColor:
                                                    AppColors().orange1,
                                                thumbShape:
                                                    RoundSliderThumbShape(
                                                        enabledThumbRadius:
                                                            15)),
                                            child: Slider(
                                              min: 0.0,
                                              max: 5.0,
                                              divisions: 5,
                                              value:
                                                  documents[index].data['low+'],
                                              onChanged: (double ll) {
                                                print(documents[index].data);
                                                documents[index].data['low+'] =
                                                    ll;
                                                setState(() {
                                                  documents[index]
                                                      .data['low+'] = ll;
                                                  programController
                                                      .updateProgram(
                                                          documents[index]
                                                              .data['name'],
                                                          documents[index]
                                                              .data['high'],
                                                          documents[index]
                                                              .data['high+'],
                                                          documents[index]
                                                              .data['medium'],
                                                          documents[index]
                                                              .data['low'],
                                                          documents[index]
                                                              .data['low+']);
                                                });
                                              },
                                            ),
                                          ),
                                        ]),
                                    padding: EdgeInsets.fromLTRB(0, 5, 0, 0),
                                  ),
                                ),
                                ExpandableButton(
                                  child: Container(
                                    margin: EdgeInsets.only(top: 20),
                                    padding: EdgeInsets.fromLTRB(10, 5, 10, 5),
                                    child: Text(
                                      'Done',
                                      style: TextStyle(
                                          color: AppColors().text,
                                          fontSize: 20),
                                    ),
                                    decoration: BoxDecoration(
                                        borderRadius:
                                            new BorderRadius.circular(10.0),
                                        color: AppColors().bar2),
                                  ),
                                )
                              ],
                            ),
                            width: MediaQuery.of(context).size.width,
                            padding: EdgeInsets.only(top: 5, bottom: 5),
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            );
          }),
    );
  }
}
