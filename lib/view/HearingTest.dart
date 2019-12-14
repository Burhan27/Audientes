import 'dart:math';
import 'dart:typed_data';

import 'package:audientes/AppColors.dart';
import 'package:avatar_glow/avatar_glow.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class HearingTest extends StatefulWidget {
  @override

  HearingTestState createState() => HearingTestState();
}

class HearingTestState extends State<HearingTest> {
  List<int> results = new List<int>();

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
                  backgroundColor: Colors.deepPurple,
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
    if(results.length < 6){
      results.add(_generateRandom());
    }
    if(results.length >= 6){
      Navigator.pop(context);
    }
  }

  int _generateRandom(){
    var random = new Random();
    return random.nextInt(8) -1;
  }



}
