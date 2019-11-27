import 'package:audientes/AppColors.dart';
import 'package:audientes/view/widgets/mixer.dart';
import 'package:audientes/view/widgets/settingOption.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:expandable/expandable.dart';
import 'package:flutter/material.dart';

class Settings extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          backgroundColor: AppColors().background,
          title: Text(
            "Settings",
            style: TextStyle(color: Colors.white, fontSize: 25),
          ),
        ),
        body:Container(
            width: double.infinity,
            alignment: Alignment.center,
            color: AppColors().background,
            child:
            new StreamBuilder<QuerySnapshot>(
                stream: Firestore.instance.collection('Programs').snapshots(),
                builder:
                    (BuildContext context, AsyncSnapshot<QuerySnapshot> snapshot) {
                  if (snapshot.hasError)
                    return new Text('Error: ${snapshot.error}');
                  switch (snapshot.connectionState) {
                    case ConnectionState.waiting:
                      return new Text('Loading...');
                    default:
                      return new ListView.builder(
                          itemCount: snapshot.data.documents.length,
                          itemBuilder: (BuildContext ctxt, int index) {
                            return new ExpandableNotifier(
                              child: Column(
                                children: <Widget>[
                                  Expandable(
                                    collapsed: ExpandableButton(
                                      child: settingOption(Colors.red,
                                          Icons.radio, snapshot.data.documents[index]['optiontext'], false),
                                    ),
                                    expanded: Column(
                                      children: <Widget>[
                                        settingOption(Colors.red, Icons.radio,
                                            snapshot.data.documents[index]['optiontext'], true),
                                        Container(
                                          child: Column(
                                            children: <Widget>[
                                              mixer('High+', 0.0),
                                              mixer('High', 0.0),
                                              ExpandableButton(
                                                child: Text(
                                                  'Done',
                                                  style: TextStyle(
                                                      color: Colors.white,
                                                      fontSize: 20),
                                                ),
                                              )
                                            ],
                                          ),
                                          color: Color(0xff303030),
                                          width: MediaQuery.of(context)
                                              .size
                                              .width *
                                              0.95,
                                          margin:
                                          EdgeInsets.fromLTRB(5, 0, 5, 10),
                                          padding:
                                          EdgeInsets.fromLTRB(0, 0, 0, 5),
                                        )
                                      ],
                                    ),
                                  ),
                                ],
                              ),
                            );
                          });
                  }
                }))
    );}
}