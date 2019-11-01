import 'package:flutter/material.dart';
import 'router.dart';
import 'startscreen.dart';
import 'mixer.dart';
import 'settings.dart';
import 'hearingTest.dart';

void main()  => runApp(MainApp( ));
  //  onGenerateRoute: Router.generateRoute,
  // initialRoute: '/',

class MainApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
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
    HeartinTest(),
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
        backgroundColor: Color(0xff484848),
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
            title: Text('Volume Mixer'),
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

