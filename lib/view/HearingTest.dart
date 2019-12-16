import 'dart:math';
import 'dart:typed_data';

import 'package:audientes/AppColors.dart';
import 'package:avatar_glow/avatar_glow.dart';
import 'package:community_material_icon/community_material_icon.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class HearingTest extends StatefulWidget {
  @override

  HearingTestState createState() => HearingTestState();
}

class HearingTestState extends State<HearingTest> {
  List<String> results = new List<String>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors().background,
      body: Container(
        width: double.infinity,
        alignment: Alignment.center,
        padding: EdgeInsets.only(left: 20, right: 20,
            top:MediaQuery.of(context).size.height/3),
        child: Column(
          children: <Widget>[
            Text(
              "Press the button when you hear a sound!",
              style: TextStyle(
                fontSize: 15,
                color: Colors.white,
              ),
            ),
            AvatarGlow(
              endRadius: MediaQuery.of(context).size.width / 3,
              child: FloatingActionButton(
                child: Icon(CommunityMaterialIcons.ear_hearing),
                  backgroundColor: AppColors().inactive,
                  focusColor: AppColors().highlight,
                  onPressed: addResult),
            ),

            RaisedButton(child: Text("Stop test"
            , style: TextStyle(color: Colors.white),),
            color: Colors.red,
            onPressed:() => Navigator.pop(context),)
          ],
        ),
      ),
    );
  }

  void addResult(){
    while(results.length < 12){
      results.add(_generateRandom().toString());
    }
    print("aaaa " + results.length.toString());
    _saveResult();
    Navigator.popAndPushNamed(context, '/complete');
  }

  int _generateRandom(){
    var random = new Random();
    return random.nextInt(11) -1;
  }

  _saveResult() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setStringList('results', results);
  }

}
