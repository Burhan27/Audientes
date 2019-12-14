import 'package:audientes/AppColors.dart';
import 'package:audientes/view/widgets/mixer.dart';
import 'package:audientes/view/widgets/settingOption.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:expandable/expandable.dart';
import 'package:flutter/material.dart';
import  'package:audientes/controller/router.dart';
import 'package:audientes/view/widgets/ProgramList.dart';
import 'package:audientes/view/createProgram.dart';
import 'package:audientes/view/startscreen.dart';

class Settings extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          backgroundColor: AppColors().background,
          title: Text(
            "Settings",
            style: TextStyle(color: Colors.white, fontSize: 25),
          ),
        ),
        body:Container(
            width: double.infinity,
            alignment: Alignment.center,
            color: AppColors().background,
            child: Column(
                children: <Widget>[
                  Expanded(
                  child: ProgramList(),
                    flex: 8,
                  ),
                  Expanded(
                    child: RawMaterialButton(
                      onPressed: ()  {
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => createProgram()),
                        );
                      },
                      child: new Icon(
                        Icons.add,
                        color: Colors.blue,
                        size: 50.0,
                      ),
                      shape: new CircleBorder(),
                      constraints: BoxConstraints.tight(Size(65, 65)),
                      fillColor: Colors.white,
                    ),
                    flex: 2,
                  )
                ]
            )
        ),
    );}
}