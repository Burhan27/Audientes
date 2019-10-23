import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class HearingTest extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        child: Column(
          children: <Widget>[
            Text(
              "press the button when you hear a sound",
              style: TextStyle(fontSize: 20, color: Color(0xff38E2CF)),
            ),
          ],
        ),
        padding: EdgeInsets.fromLTRB(5, 30, 5, 30),
        color: Color(0xff131313),
        width: double.infinity,
      ),
    );
  }
}
