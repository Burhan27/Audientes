import 'package:flutter/material.dart';

class ProgramItem {
  String optionText;
  IconData iconData;
  Color color;
  bool expanded, isChecked;
  int clicked;

   ProgramItem(String optionText, IconData iconData, Color color,  bool expanded, bool isChecked, int clicked) {
     this.optionText = optionText;
     this.iconData = iconData;
     this.color = color;
     this.expanded = false;
     this.isChecked = false;
     this.clicked = 0;
  }


   int compareTo(ProgramItem programItem) {

    if (this.clicked > programItem.clicked) {
      return -1;
    } else if (this.clicked < programItem.clicked) {
      return 1;
    } else {
      return 0;
    }
  }


  void setClicked() {
     if(isChecked) {
      this.isChecked = false;
     } else {
       this.isChecked = true;
     }
  }

}