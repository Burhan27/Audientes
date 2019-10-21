import 'package:audientes/settingsOptionDropDown.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class settingOption extends StatefulWidget {
  String optionText;

  settingOption(this.optionText);

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return settingOptionState(optionText);
  }

}

class settingOptionState extends State<settingOption> {
  String optionText;
  bool isChecked = false;
  settingOptionState(this.optionText);

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Container(
      child: Row(
        children: <Widget>[
          Expanded(
            child: settingOptionDropDown(optionText),
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
      margin: EdgeInsets.fromLTRB(5, 10, 5, 10),
      width: double.infinity,
    color: Color(0xff202020),);
  }
}