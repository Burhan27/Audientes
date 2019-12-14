import 'package:audientes/model/programItem.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'dart:async';

import 'package:flutter/material.dart';

class ProgramController {



  void createProgram(ProgramItem programItem) {
    Firestore.instance.collection('Programs').add({ 'optiontext': programItem.optionText, 'expandend': programItem.expanded, 'clicked': programItem.clicked});

    Firestore.instance.collection('Programs').document("test")
        .setData({ 'optiontext': programItem.optionText, 'expandend': programItem.expanded, 'clicked': programItem.clicked, 'color': programItem.color, 'icon': programItem.iconData });
  }

  void deleteProgram() {

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


  void updateProgram() {

  }

  void viewProgram() {

  }

  List<ProgramItem> viewProgramList() {
    List<ProgramItem> programItems = new List<ProgramItem>();

    final snapshotData = Firestore.instance.collection('books').snapshots();


    AsyncSnapshot<QuerySnapshot> snapshot;



    return programItems;

  }


}