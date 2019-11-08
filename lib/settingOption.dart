import 'package:audientes/settingsOptionDropDown.dart';
import 'package:expandable/expandable.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class settingOption extends StatefulWidget {
  String optionText;
  IconData iconData;
  Color color;
  bool expanded;

  settingOption(this.color, this.iconData, this.optionText, this.expanded);

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return settingOptionState(color, iconData, optionText, expanded);
  }
}

class settingOptionState extends State<settingOption> {
  String optionText;
  IconData iconData;
  Color color;
  bool isChecked = false;
  bool expanded;
  int B;

  settingOptionState(this.color, this.iconData, this.optionText, this.expanded){
    if(expanded){
      B = 0;
    }
    else{
       B = 10;
    }
  }



@override
  Widget build(BuildContext context) {
    return Container(
      child: Row(
        children: <Widget>[
          Expanded(
            child: settingOptionDropDown(color, iconData, optionText),
            flex: 8,
          ),
          Expanded(
            child: Container(child: CupertinoSwitch(
              value: isChecked,
              onChanged: (value) {
                setState(() {
                  isChecked = !isChecked;
                });
              },
            activeColor: Color(0xff38E2CF),),
              alignment: Alignment.centerRight,),
            flex: 2,
          )
        ],
      ),
      margin: EdgeInsets.fromLTRB(5, 20, 5, 0),
      width: double.infinity,
    color: Color(0xff202020),);
  }
}
