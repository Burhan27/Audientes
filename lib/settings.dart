import 'package:audientes/settingOption.dart';
import 'package:flutter/material.dart';

class Settings extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Color(0xff131313),
        title: Text("Settings"),
      ),
      body: Container(
        child: Column(
          children: <Widget>[
            settingOption('Noisy'),
            RaisedButton(
              onPressed: () {
                Navigator.pop(context);
              },
              child: Text('Back'),
            )
          ],
        ),
      width: double.infinity,
      color: Color.fromARGB(100, 200, 50, 20),
      alignment: Alignment.center,),
    );
  }
}
