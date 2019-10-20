import 'package:flutter/material.dart';

class settingOptionDropDown extends StatefulWidget {

  String optionText;

  settingOptionDropDown(this.optionText);

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return null;
  }


}

class settingOptionDropDownState extends State<settingOptionDropDown> {
  String optionText;

  settingOptionDropDownState(this.optionText);

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return GestureDetector(
      child: Row(children: <Widget>[Text('Image'), Text(optionText)],),
      onTap: ,
    );
  }

}