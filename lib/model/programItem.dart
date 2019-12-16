import 'package:flutter/material.dart';

class ProgramItem {
  String name;
  int iconData;
  int color;
  bool isActive;
  double highPlus, high, medium, low, lowPlus;

   ProgramItem(String name, int iconData, int color,  bool isActive, double highPlus, double high, double medium, double low, double lowPlus) {
     this.name = name;
     this.iconData = iconData;
     this.color = color;
     this.isActive = false;
     this.highPlus = highPlus;
     this.high = high;
     this.medium = medium;
     this.low = low;
     this.lowPlus = lowPlus;
  }


  void setActive() {
     if(isActive) {
      this.isActive = false;
     } else {
       this.isActive = true;
     }
  }

}