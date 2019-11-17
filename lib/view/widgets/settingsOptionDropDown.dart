import 'package:expandable/expandable.dart';
import 'package:flutter/material.dart';

class settingOptionDropDown extends StatefulWidget {
  String optionText;
  IconData iconData;
  Color color;

  settingOptionDropDown(this.color, this.iconData, this.optionText);

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return settingOptionDropDownState(color,iconData,optionText);
  }
}

class settingOptionDropDownState extends State<settingOptionDropDown> {
  String optionText;
  IconData iconData;
  Color color;
  settingOptionDropDownState(this.color,this.iconData,this.optionText);

  @override
  Widget build(BuildContext context) {
     return Row(
        children: <Widget>[
          Expanded(
            child:Container( child: Icon(
              iconData,
              color: Colors.white,
              size: 35,
            ),
            color: color,
            margin: EdgeInsets.fromLTRB(5, 0, 5, 0),),
            flex: 1,
          ),
          Expanded(
            child: Container(
              child: Text(
                optionText,
                style: TextStyle(fontSize: 20, color: Colors.white),
              ),
            ),
            flex: 5,
          )
        ],
    );
  }
}
