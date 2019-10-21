import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class mixer extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return mixerState();
  }

}



class mixerState extends State<mixer> {
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
              SliderDesign("Low", 0.0),
              SliderDesign("Mid", 0.0),
              SliderDesign("High", 0.0),
              SliderDesign("High +", 0.0),
            ],
          ),
          margin: EdgeInsets.fromLTRB(5, 20, 5, 10),

        ),
    backgroundColor: Color(0xff131313),);
  }

  Widget SliderDesign(String text, double value) {
    return Container (
        child: Column(children: <Widget>[
     /*     Expanded(child: Text('Noise'),
            flex: 1,),
*/
     Text(text, style: TextStyle(color: Color(0xff38E2CF)),),

      SliderTheme(
        data: SliderThemeData(
            thumbColor: Color(0xff38E2CF),
            trackHeight: 10,
            thumbShape: RoundSliderThumbShape(enabledThumbRadius: 15)),

      child: Slider (
        min: 0.0,
        max: 5.0,
        divisions: 5,
        value: value,
        onChanged: (double q) {
          value = q;
          setState(() {

          });
        },
      ),
    ),
     // flex: 5,),
  ]
    ),
    color: Color(0xff202020),
    margin: EdgeInsets.fromLTRB(0, 2.5, 0, 5),
    padding: EdgeInsets.fromLTRB(0, 5, 0, 0),);

  }




}

