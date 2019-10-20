import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class mixer extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
        appBar: AppBar(
          backgroundColor: Color(0xff131313),
          title: Text("Mixer"),
        ),
        body: Container(
          child: Column(
            children: <Widget>[
              Slider(
                value: 1,
              ),
              Slider(
                value: 1,
              ),
              Slider(
                value: 1,
              ),
              Slider(
                value: 1,
              ),
            ],
          ),
          margin: EdgeInsets.fromLTRB(5, 20, 5, 10),
        ));
  }
}
