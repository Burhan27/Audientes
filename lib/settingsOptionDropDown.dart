import 'package:flutter/material.dart';

class settingOptionDropDown extends StatefulWidget {

  String optionText;

  settingOptionDropDown(this.optionText);

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return settingOptionDropDownState(optionText);
  }


}

class settingOptionDropDownState extends State<settingOptionDropDown> {
  String optionText;

  settingOptionDropDownState(this.optionText);

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return GestureDetector(
      child: Row(children: <Widget>[
        Expanded(child: Text('Image'),
        flex: 1,),
        Expanded(child:Container( child: Text(optionText, style: TextStyle(fontSize: 20)),
        ),
        flex: 5,)],),
      onTap: ()=> Navigator.pushNamed(context, '/mixer'),
    );
  }

}