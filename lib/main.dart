import 'package:audientes/AppColors.dart';
import 'package:audientes/BluetoothController.dart';
import 'package:flutter/material.dart';
import 'package:audientes/view/startscreen.dart';
import 'package:audientes/view/settings.dart';
import 'package:audientes/view/ResultScreen.dart';
import 'package:flutter/services.dart';
import 'package:flutter_crashlytics/flutter_crashlytics.dart';
import 'dart:async';

import 'controller/router.dart';


void main() async {
  bool isInDebugMode = false;

  FlutterError.onError = (FlutterErrorDetails details) {
    if (isInDebugMode) {
      // In development mode simply print to console.
      FlutterError.dumpErrorToConsole(details);
    } else {
      // In production mode report to the application zone to report to
      // Crashlytics.
      Zone.current.handleUncaughtError(details.exception, details.stack);
    }
  };

  await FlutterCrashlytics().initialize();

  runZoned<Future<Null>>(() async {
    SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
    runApp(MainApp( ));
  }, onError: (error, stackTrace) async {
    // Whenever an error occurs, call the `reportCrash` function. This will send
    // Dart errors to our dev console or Crashlytics depending on the environment.
    await FlutterCrashlytics().reportCrash(error, stackTrace, forceCrash: false);
  });
}








class MainApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      onGenerateRoute: Router.generateRoute,
      home: NaviBar(),
    );
  }
}

class NaviBar extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _NaviBarState();
  }
}

class _NaviBarState extends State<NaviBar> {
  int _selectedPage = 0;
  final _pageOptions = [
    MyApp(),
    Settings(),
    ResultScreen(),
  ];

  void _onTabTapped(int index) {
    setState(() {
      _selectedPage = index;
    });
  }
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _pageOptions[_selectedPage], // new
      bottomNavigationBar: BottomNavigationBar(
        backgroundColor: AppColors().NavBar,
        unselectedItemColor: Color(0xffffffff),
        onTap: _onTabTapped, // new
        currentIndex: _selectedPage, // new
        items: [
          new BottomNavigationBarItem(
            icon: Icon(Icons.home),
            title: Text('Home'),
          ),
          new BottomNavigationBarItem(
            icon: Icon(Icons.format_list_bulleted),
            title: Text('Setting'),
          ),
          new BottomNavigationBarItem(
            icon: Icon(Icons.hearing),
            title: Text('Test & Results'),
          )
        ],
      ),
    );

  }

}

