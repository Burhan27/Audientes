import 'package:audientes/AppColors.dart';
import 'package:audientes/view/widgets/settingsOptionDropDown.dart';
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

  settingOptionState(
      this.color, this.iconData, this.optionText, this.expanded) {
    if (expanded) {
      B = 0;
    } else {
      B = 10;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Card(
        color: AppColors().bar1,
        elevation: 50,
        child: Row(
          children: <Widget>[
            Expanded(
              child: Container(
                child: Icon(
                  iconData,
                  color: Colors.white,
                  size: 35,
                ),
                color: color,
                margin: EdgeInsets.fromLTRB(5, 0, 5, 0),
              ),
              flex: 1,
            ),
            Expanded(
              child: Container(
                child: Text(
                  optionText,
                  style: TextStyle(fontSize: 20, color: AppColors().text),
                ),
              ),
              flex: 5,
            )
          ],
        ),
      ),
      margin: EdgeInsets.fromLTRB(5, 20, 5, 0),
      width: double.infinity,
    );
  }
}
