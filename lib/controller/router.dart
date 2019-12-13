import 'package:audientes/view/settings.dart';
import 'package:audientes/view/widgets/mixer.dart';
import 'package:flutter/material.dart';
import 'package:audientes/view/createProgram.dart';
import 'package:audientes/view/startscreen.dart';



class Router{

  static Route<dynamic> generateRoute(RouteSettings settings){
    switch(settings.name) {
      case '/':
        return MaterialPageRoute(builder: (_) => MyApp());
      case '/settings':
        return MaterialPageRoute(builder: (_) => Settings());
      case '/createProgram':
        return MaterialPageRoute(builder: (_) => createProgram());
      default:
        return MaterialPageRoute(
            builder: (_) => Scaffold(
              body: Center(
                  child: Text('Fejl!')),
            ));
    }
  }

}
