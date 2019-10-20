import 'package:audientes/settingOption.dart';
import 'package:flutter/material.dart';

class Settings extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Color(0xff131313),
        title: Text(
          "Settings",
          style: TextStyle(color: Colors.white, fontSize: 25),
        ),
        actions: <Widget>[
          GestureDetector(
            child: Text(
              '+',
              style: TextStyle(fontSize: 20, color: Color(0xffFFB20A)),
            ),
          onTap: ()=> Navigator.pushNamed(context, '/mixer'),)
        ],
      ),
      body: Container(
        child: Column(
          children: <Widget>[
            settingOption('Noisy'),
            settingOption('TV'),
            settingOption('Movie'),
            settingOption('Outdoor'),
            RaisedButton(
              onPressed: () {
                Navigator.pop(context);
              },
              child: Text('Back'),
            )
          ],
        ),
        width: double.infinity,
        alignment: Alignment.center,
        color: Color(0xff131313),
      ),
    );
  }
}
