import 'package:audientes/view/HearingTest.dart';
import 'package:audientes/view/ResultScreen.dart';
import 'package:audientes/view/TestComplete.dart';
import 'package:audientes/view/settings.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:audientes/view/createProgram.dart';
import 'package:audientes/view/startscreen.dart';



class Router{

  static Route<dynamic> generateRoute(RouteSettings settings){
    switch(settings.name) {
      case '/':
        return CupertinoPageRoute(builder: (_) => MyApp());
      case '/settings':
        return CupertinoPageRoute(builder: (_) => Settings());
      case '/createProgram':
        return CupertinoPageRoute(builder: (_) => createProgram());
      case '/hear':
        return CupertinoPageRoute(builder: (_) => HearingTest());
      case '/complete':
        return CupertinoPageRoute(builder: (_) => TestComplete());
      case '/result':
        return CupertinoPageRoute(builder: (_) => ResultScreen());
      default:
        return CupertinoPageRoute(
            builder: (_) => Scaffold(
              body: Center(
                  child: Text('Fejl!')),
            ));
    }
  }

}
