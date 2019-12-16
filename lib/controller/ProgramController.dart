import 'package:audientes/AppColors.dart';
import 'package:audientes/model/programItem.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'dart:async';

import 'package:flutter/material.dart';

class ProgramController {


/*
  void createProgram(ProgramItem programItem) {
    Firestore.instance.collection('Programs').add({ 'optiontext': programItem.optionText, 'expandend': programItem.expanded, 'clicked': programItem.clicked});
  }
*/

  void createProgram2(String name, int color, int icon, double high, double highPlus, double medium, double low, double lowPlus) {
    Firestore.instance.collection('Programs').document(name).setData(
        { 'name': name, 'color': color, 'icon': icon, 'high': high, 'high+': highPlus, 'medium': medium, 'low': low, 'low+':lowPlus});
  }

  IconData  getIcon(int iconIndex) {
    switch(iconIndex) {
      case 0:
        return Icons.airport_shuttle;
        break;
      case 1:
        return Icons.nature_people;
        break;
      case 2:
        return Icons.directions_run;
        break;
      case 3:
        return Icons.tv;
        break;
      case 4:
        return Icons.radio;
        break;
      case 5:
        return Icons.hotel;
        break;
      case 6:
        return Icons.phone_in_talk;
        break;
      case 7:
        return Icons.audiotrack;
        break;
      case 8:
        return Icons.restaurant;
        break;
      default: 0;
    }

  }

  Color getColor(int colorIndex) {
    switch(colorIndex) {
      case 0:
        return Colors.blueAccent;
        break;
      case 1:
        return Colors.pinkAccent;
        break;
      case 2:
        return Colors.redAccent;
        break;
      case 3:
        return Colors.lightBlueAccent;
        break;
      case 4:
        return Colors.orangeAccent;
        break;
      case 5:
        return Colors.purpleAccent;
        break;
      case 6:
        return Colors.limeAccent;
        break;
      case 7:
        return Colors.indigoAccent;
        break;
      case 8:
        return Colors.amberAccent;
        break;
      default: 0;
    }
  }
void updateMain(int icon, int color, String name) {
  Firestore.instance.collection('MainProgran').document('main').updateData({'color': color, 'name': name, 'icon': icon});


}

  void setFalse( String index) {
    Firestore.instance.collection('Programs').document(index).updateData({'isActive': false});
  }

  void setActive(String index) {

  Firestore.instance.collection('Programs').document(index).updateData({'isActive': true});
  }


  void deleteProgram() {
    //Todo in later updates
  }

  void updateProgram(String index, double high, double highPlus, double medium, double low, double lowPlus ) {
    Firestore.instance.collection('Programs').document(index).updateData({'high': high, 'high+': highPlus, 'medium': medium, 'low': low, 'low+':lowPlus});
  }

  void viewProgram() {
    //Todo in later updates

  }



  test(var documents) {
    Firestore.instance.collection("Programs").getDocuments().then((data){
      var list = data.documents;
      documents = list;
    });
    return documents;
  }

  List<ProgramItem> viewProgramList() {
    List<ProgramItem> programItems = new List<ProgramItem>();

    final snapshotData = Firestore.instance.collection('books').snapshots();


    AsyncSnapshot<QuerySnapshot> snapshot;



    return programItems;

  }


}