import 'package:flutter/material.dart';

class TestCompleteWrapper extends StatefulWidget {
  final Function onInit;
  final Widget child;

  const TestCompleteWrapper({Key key, this.onInit, this.child}) : super(key: key);

  @override
  _StatefulWrapperState createState() => _StatefulWrapperState();
}
class _StatefulWrapperState extends State<TestCompleteWrapper> {
  @override
  void initState() {
    if(widget.onInit != null) {
      widget.onInit();
    }
    super.initState();
  }
  @override
  Widget build(BuildContext context) {
    return widget.child;
  }
}