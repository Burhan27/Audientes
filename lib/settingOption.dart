import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class settingOption extends StatefulWidget {
  String optionText;
  bool isChecked = false;

  settingOption(this.optionText);

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return settingOptionState();
  }

}

class settingOptionState extends State<settingOption> {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Container(
      child: Row(
        children: <Widget>[
          Expanded(
            child: Text('Image'),
            flex: 2,
          ),
          Expanded(
            child: Text(
              optionText,
              style: new TextStyle(fontSize: 20),
            ),
            flex: 6,
          ),
          Expanded(
            child: Container(child: CupertinoSwitch(
              value: isChecked,
              onChanged: (value) {
                isChecked = !isChecked;
              },
            ),
              alignment: Alignment.centerRight,),
            flex: 2,
          )
        ],
      ),
      margin: EdgeInsets.fromLTRB(5, 10, 5, 10),
      width: double.infinity,
    );
  }
}