import 'dart:async';
import 'package:audientes/AppColors.dart';
import 'package:audientes/controller/ProgramController.dart';
import 'package:flutter/material.dart';


class createProgram extends StatefulWidget {
  @override
  createProgramState createState() => createProgramState();
}



class createProgramState extends State<createProgram> {

  TextEditingController nameController = new TextEditingController();
  String name;
  Timer timer;
  Color test = Colors.blue;
  IconData standardIcon = Icons.face;
  int i = 0;
  double highPlus = 0.0, high = 0.0, low = 0.0, medium = 0.0, lowPlus = 0.0;
  int colorCode = 99, iconCode = 99;
  List<Color> colorList = new List<Color>();
  List<IconData> iconList = new List<IconData>();
  ProgramController programController = new ProgramController();
  var documents = [];



  createProgramstate(){

    nameController.addListener(nameListener);

  }


  fillIconList(List<IconData> iconList) {
    if(iconList.isEmpty) {
      iconList.add(Icons.airport_shuttle);
      iconList.add(Icons.nature_people);
      iconList.add(Icons.directions_run);
      iconList.add(Icons.tv);
      iconList.add(Icons.radio);
      iconList.add(Icons.hotel);
      iconList.add(Icons.phone_in_talk);
      iconList.add(Icons.audiotrack);
      iconList.add(Icons.restaurant);


    }
  }

  fillColorList(List<Color> colorList) {
    if(colorList.isEmpty) {
      colorList.add(Colors.blue);
      colorList.add(Colors.red);
      colorList.add(Colors.purple);
      colorList.add(Colors.yellow);
      colorList.add(Colors.green);
      colorList.add(Colors.teal);
      colorList.add(Colors.deepOrange);
      colorList.add(Colors.pink);
      colorList.add(Colors.tealAccent);
    }
  }

  @override
  void initState() {
    super.initState();

    fillIconList(iconList);
    fillColorList(colorList);

  }

  nameListener(){
    if (nameController.text.isEmpty) {
  name = "";
  } else {
  name = nameController.text;
  }
}

void selectColorDialog() {
    showDialog(
      context: context,
      builder: (BuildContext contex) {
       return AlertDialog(
         contentPadding: const EdgeInsets.all(10.0),
         title: new Text(
           'Pick a color!',
           style:
           new TextStyle(fontWeight: FontWeight.bold, color: Colors.black),
         ),
content: new Container(

  width: MediaQuery.of(context).size.width * .7,
  height: MediaQuery.of(context).size.width * .6,
  color: Colors.transparent,

      child:   GridView.count(crossAxisCount: 5,
           children: List.generate(colorList.length, (index) {
          return RawMaterialButton(
            onPressed: ()  {
              colorCode = index;
              test = colorList.elementAt(index);
              Navigator.pop(context);
              setState(() {});
            },
            shape: new CircleBorder(),
            fillColor: colorList.elementAt(index),
          );
       }),
         padding: const EdgeInsets.all(10),
         mainAxisSpacing: 10,
         crossAxisSpacing: 10,
       )


),
       );
      }
    );
}


void selectIconDialog() {
    showDialog(
        context: context,
        builder: (BuildContext contex) {
          return AlertDialog(
            contentPadding: const EdgeInsets.all(10.0),
            title: new Text(
              'Pick a icon!',
              style:
              new TextStyle(fontWeight: FontWeight.bold, color: Colors.black),
            ),
            content: new Container(

                width: MediaQuery.of(context).size.width * .7,
                height: MediaQuery.of(context).size.width * .6,
                color: Colors.black,

                child:   GridView.count(crossAxisCount: 5,
                  children: List.generate(iconList.length, (index) {
                    return RawMaterialButton(
                      onPressed: ()  {
                        standardIcon = iconList.elementAt(index);
                        iconCode = index;
                        Navigator.pop(context);
                        setState(() {});
                      },
                      child: new Icon(
                        iconList.elementAt(index),
                        color: Colors.grey,
                        size: 20.0,
                      ),
                      shape: new CircleBorder(),
                      fillColor: Colors.white,
                    );
                  }),
                  padding: const EdgeInsets.all(10),
                  mainAxisSpacing: 10,
                  crossAxisSpacing: 10,
                )


            ),
          );
        }
    );
  }






  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: AppColors().background,
      ),
      body:Container(
          width: double.infinity,
          alignment: Alignment.center,
          color: AppColors().background,
          child: Column(
            children: <Widget>[
              SizedBox(height: 10),

                TextField(
                style: TextStyle(color: Colors.redAccent),
                  onChanged: (text) {
                    name = text;
                  },
                decoration: new InputDecoration(
                    enabledBorder: OutlineInputBorder(
                        borderSide: BorderSide(color: Colors.teal),
                      borderRadius: BorderRadius.all(Radius.circular(20)),
                    ),
                    labelStyle: TextStyle(color: Colors.red),
                    labelText: 'Name',
                    fillColor: Colors.redAccent,
                  focusColor: Colors.redAccent,
                  border: OutlineInputBorder(
                    borderSide: BorderSide(color: Colors.purple),
                    borderRadius: BorderRadius.all(Radius.circular(4)),
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Colors.purple, ),
                    borderRadius: BorderRadius.all(Radius.circular(4)),
                  ),
                ),
              ),

                SizedBox(height: 10),



              Row(
                children: <Widget>[

                  FlatButton(

                    onPressed: () {
                      selectColorDialog();
                    },

                    color: test,
                  ),

                  RawMaterialButton(
                    onPressed: ()  {
                      selectIconDialog();
                    },
                    child: new Icon(
                      standardIcon,
                      color: Colors.grey,
                      size: 30.0,
                    ),
                    shape: new CircleBorder(),
                    fillColor: Colors.white,
                  ),
                ],
              ),

              SizedBox(height: 10),

              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[
                  Text(
                    'High+',
                    style: TextStyle(
                        color: Colors.white,
                        fontSize: 20),
                  ),
                  Text(
                    'High',
                    style: TextStyle(
                        color: Colors.white,
                        fontSize: 20),
                  ),
                  Text(
                    'Medium',
                    style: TextStyle(
                        color: Colors.white,
                        fontSize: 20),
                  ),
                  Text(
                    'Low',
                    style: TextStyle(
                        color: Colors.white,
                        fontSize: 20),
                  ),
                  Text(
                    'Low+',
                    style: TextStyle(
                        color: Colors.white,
                        fontSize: 20),
                  ),
                ],
              ),

              Container(
                width: MediaQuery.of(context).size.width,
                height: MediaQuery.of(context).size.width * .6,
              child: RotatedBox (
                quarterTurns: 3,
                child: Column(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children:<Widget>[

                      Container (
                child: Column(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: <Widget>[
                SliderTheme(
                data: SliderThemeData(
                    thumbColor: Color(0xff38E2CF),
                  trackHeight: 10,
                  thumbShape: RoundSliderThumbShape(enabledThumbRadius: 15)),

                child: Slider (
                  min: 0.0,
                  max: 5.0,
                  divisions: 5,
                  value: highPlus,
                  onChanged: (double h) {
                    highPlus = h;
                    setState(() {
                      highPlus = h;
                    });
                  },
                ),
              ),

                SliderTheme(
                  data: SliderThemeData(
                      thumbColor: Color(0xff38E2CF),
                      trackHeight: 10,
                      thumbShape: RoundSliderThumbShape(enabledThumbRadius: 15)),

                  child: Slider (
                    min: 0.0,
                    max: 5.0,
                    divisions: 5,
                    value: high,
                    onChanged: (double hh) {
                      high = hh;
                      setState(() {
                        high = hh;
                      });
                    },
                  ),
                ),

                SliderTheme(
                  data: SliderThemeData(
                      thumbColor: Color(0xff38E2CF),
                      trackHeight: 10,
                      thumbShape: RoundSliderThumbShape(enabledThumbRadius: 15)),

                  child: Slider (
                    min: 0.0,
                    max: 5.0,
                    divisions: 5,
                    value: medium,
                    onChanged: (double m) {
                      medium = m;
                      setState(() {
                        medium = m;
                      });
                    },
                  ),
                ),

                SliderTheme(
                  data: SliderThemeData(
                      thumbColor: Color(0xff38E2CF),
                      trackHeight: 10,
                      thumbShape: RoundSliderThumbShape(enabledThumbRadius: 15)),

                  child: Slider (
                    min: 0.0,
                    max: 5.0,
                    divisions: 5,
                    value: low,
                    onChanged: (double l) {
                      low = l;
                      setState(() {
                        low = l;
                      });
                    },
                  ),
                ),

                SliderTheme(
                  data: SliderThemeData(
                      thumbColor: Color(0xff38E2CF),
                      trackHeight: 10,
                      thumbShape: RoundSliderThumbShape(enabledThumbRadius: 15)),

                  child: Slider (
                    min: 0.0,
                    max: 5.0,
                    divisions: 5,
                    value: lowPlus,
                    onChanged: (double ll) {
                      lowPlus = ll;
                      setState(() {
                        lowPlus = ll;
                      });
                    },
                  ),
                ),
            ]
          ),
        padding: EdgeInsets.fromLTRB(0, 5, 0, 0),

      ),

                    ]
                ),
              ),
              ),

              FlatButton(
                color: Colors.green,

                onPressed: () {
                  setState(() {
                  });

                  if(colorCode == 99 || iconCode == 99) {
                    Navigator.pop(context);

                  } else {
                    programController.createProgram2(name, colorCode, iconCode, high, highPlus, medium, low, lowPlus);
                    Navigator.pop(context);
                  }
                },
              ),





            ],
          )
      ),
    );}

}