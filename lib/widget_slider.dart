import 'package:flutter/services.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:volume/volume.dart';
import 'dart:async';



  class slidertest extends StatefulWidget {
    @override

  @override
  State<StatefulWidget> createState() {
    SliderTheme(
      data: SliderThemeData(
          thumbColor: Color(0xff38E2CF),
          trackHeight: 10,
          thumbShape: RoundSliderThumbShape(enabledThumbRadius: 12.5)),
    child: Slider(
    min: 0.0,
    max: 5 + 0.0,
    // divisions: maxVol,
    value: 2 / 1.0,
    activeColor: Color(0xff38E2CF),
    onChanged: (double d) {

    },)
        );
  }


  }

