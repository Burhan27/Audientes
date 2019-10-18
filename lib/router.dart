import 'package:flutter/material.dart';
import 'startscreen.dart';
import 'settings.dart';


class Router{

  static Route<dynamic> generateRoute(RouteSettings settings){
    switch(settings.name) {
      case '/':
        return MaterialPageRoute(builder: (_) => StartScreen());
      case '/settings':
        return MaterialPageRoute(builder: (_) => Settings());
      default:
        return MaterialPageRoute(
            builder: (_) => Scaffold(
              body: Center(
                  child: Text('Fejl!')),
            ));
    }
  }

}
