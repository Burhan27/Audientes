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
  double d = 0.0;
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
              SliderDesign("Volume", d),
              SliderDesign("Filter", d),
              SliderDesign("Bass", d),
              SliderDesign("Noise", d),
            ],
          ),
          margin: EdgeInsets.fromLTRB(5, 20, 5, 10),
        ));
  }

  Widget SliderDesign(String text, double value) {

    return Container (
        child: Column(children: <Widget>[
     /*     Expanded(child: Text('Noise'),
            flex: 1,),
*/
     Text(text),

      SliderTheme(
        data: SliderThemeData(
            thumbColor: Colors.teal,
            trackHeight: 10,
            thumbShape: RoundSliderThumbShape(enabledThumbRadius: 15)),

      child: Slider (
        min: 0.0,
        max: 5.0,
        divisions: 5,
        value: d,
        onChanged: (double q) {
          d = q;
          setState(() {

          });
        },
      ),
    ),
     // flex: 5,),
  ]
    ));


  }




}

