import 'package:audientes/HearingTest.dart';
import 'package:audientes/mixer.dart';
import 'package:flutter/material.dart';
import 'startscreen.dart';
import 'settings.dart';
import 'mixer.dart';


class Router{

  static Route<dynamic> generateRoute(RouteSettings settings){
    switch(settings.name) {
      case '/':
        return MaterialPageRoute(builder: (_) => MyApp());
      case '/settings':
        return MaterialPageRoute(builder: (_) => Settings());
      case '/mixer':
        return MaterialPageRoute(builder: (_) => mixer());
      case '/hearTest':
        return MaterialPageRoute(builder: (_) => HearingTest());
      default:
        return MaterialPageRoute(
            builder: (_) => Scaffold(
              body: Center(
                  child: Text('Fejl!')),
            ));
    }
  }

}
