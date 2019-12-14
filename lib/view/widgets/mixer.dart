import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class mixer extends StatefulWidget {
  String text;
  double frequency;

  mixer(String text, double frequency) {
    this.text = text;
    this.frequency = frequency;
  }

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return mixerState(text, frequency);
  }

}

class mixerState extends State<mixer> {
  String text;
  double frequency;

  mixerState(String text, double frequency) {
    this.text = text;
    this.frequency = frequency;
  }


  @override
  Widget build(BuildContext context) {
    return Container (
      child: Column(children: <Widget>[
        /*     Expanded(child: Text('Noise'),
            flex: 1,),
*/
     //   Text(text, style: TextStyle(color: Color(0xff38E2CF)),),

        SliderTheme(
          data: SliderThemeData(
              thumbColor: Color(0xff38E2CF),
              trackHeight: 10,
              thumbShape: RoundSliderThumbShape(enabledThumbRadius: 15)),

          child: Slider (
            min: 0.0,
            max: 5.0,
            divisions: 5,
            value: frequency,
            onChanged: (double q) {
              frequency = q;
              print('test1' + frequency.toString());
              setState(() {
                frequency = q;
                print('test2' + frequency.toString());
              });
            },
          ),
        ),
        // flex: 5,),
      ]
      ),
      padding: EdgeInsets.fromLTRB(0, 5, 0, 0),);
  }





}

