import 'package:audientes/BluetoothButton.dart';
import 'package:audientes/mixer.dart';
import 'package:audientes/settingOption.dart';
import 'package:expandable/expandable.dart';
import 'package:flutter/material.dart';

class Settings extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Color(0xff131313),
        title: Text(
          "Settings",
          style: TextStyle(color: Colors.white, fontSize: 25),
        ),
        actions: <Widget>[
          GestureDetector(
            child: Text(
              '+',
              style: TextStyle(fontSize: 20, color: Color(0xffFFB20A)),
            ),
            onTap: () => Navigator.pushNamed(context, '/mixer'),
          )
        ],
      ),
      body: Container(
        child: ExpandableNotifier(
          child: Column(
            children: <Widget>[
              Expandable(
                collapsed: ExpandableButton(
                  child: settingOption(Colors.red, Icons.radio, 'Radio', false),
                ),
                expanded: Column(
                  children: <Widget>[
                    settingOption(Colors.red, Icons.radio, 'Radio', true),
                    Container(
                        child: Column(
                      children: <Widget>[
                        mixer('High+', 0.0),
                        mixer('High', 0.0),
                        ExpandableButton(
                          child: Text(
                            'Done',
                            style: TextStyle(color: Colors.white, fontSize: 20),
                          ),
                        )
                      ],
                    ),
                    color: Color(0xff303030),
                    width: MediaQuery.of(context).size.width * 0.95 ,
                    margin: EdgeInsets.fromLTRB(5, 0, 5, 10),
                    padding: EdgeInsets.fromLTRB(0, 0, 0, 5),)
                  ],
                ),
              ),
              settingOption(Colors.cyan, Icons.home, 'Home', false),
              settingOption(Colors.amberAccent, Icons.work, 'Work', false),
            ],
          ),
        ),
        width: double.infinity,
        alignment: Alignment.center,
        color: Color(0xff131313),
      ),
    );
  }
}
