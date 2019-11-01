import 'package:flutter/material.dart';

class HeartinTest extends StatefulWidget {
  @override
  _TestHomeScreen createState() => _TestHomeScreen();
}

class _TestHomeScreen extends State<HeartinTest> {

  bool test = true;

  proceedDialog(BuildContext context) {
    return showDialog(context: context, builder: (context) {
      return AlertDialog(
        title: Text("Warning!"),
        backgroundColor: Colors.grey,
        content: Text("You got an ongoing test, "
            "are you sure, that you want to proceed?"),
        actions: <Widget>[
        MaterialButton(
            elevation: 5.0,
            child: Text("Yes, start a new test"),
            color: Colors.red,
            textColor: Colors.white,
            onPressed: () {},
          ),
          MaterialButton(
            elevation: 5.0,
            color: Colors.green,
            textColor: Colors.white,
            child: Text("Go back"),
            onPressed: () {
              Navigator.pop(context);
            },
      )
        ],
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
        backgroundColor: Color(0xff131313),
        title: Text('Audientes'),
         ),
    body: Container(
    child: Column(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: <Widget>[

     RaisedButton(
        child: Text('Start Hearing Test'),
        onPressed: () {
          proceedDialog(context);
        },
       color: Colors.white,
        highlightColor: Colors.green,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(6.0)),
      ),

     RaisedButton(
       child: Text('Resume Hearing Test'),
       onPressed:  test ? null : null,
       //() => whatToDoOnPressed i stedet for sidste null
       color: Colors.white,
       disabledColor: Colors.grey,
       highlightColor: Colors.green,
       shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(6.0)),
     ),



      ],
    ),
        color: Color(0xff131313),
        width: MediaQuery.of(context).size.width,
        height: MediaQuery.of(context).size.height,
    )

    );
  }
}