import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:audientes/model/programItem.dart';

class ProgramItemView extends StatefulWidget {
//  ProgramItemView(programItems);
  List<ProgramItem> programItems = new List<ProgramItem>();

  ProgramItemView(this.programItems);
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return ProgramItemViewState(programItems);
  }
}

class ProgramItemViewState extends State<ProgramItemView> {

  List<ProgramItem> programItems = new List<ProgramItem>();

  ProgramItemViewState(this.programItems);

  /* programItems.add(new ProgramItem(1, "test1", Icons.home, Colors.white, false, false, 0));
    programItems.add(new ProgramItem(2, "test2", Icons.home, Colors.white, false, false, 0));
    programItems.add(new ProgramItem(3, "test3", Icons.terrain, Colors.white, false, false, 0));
    programItems.add(new ProgramItem(4, "test3", Icons.home, Colors.white, false, false, 0));
*/

  //   programItems.sort((a, b) => a.clicked.compareTo(b.clicked));  }

  @override
  Widget build(BuildContext context) {
    return StreamBuilder<QuerySnapshot>(
        stream: Firestore.instance.collection('Programs').snapshots(),
        builder: (BuildContext context, AsyncSnapshot<QuerySnapshot> snapshot) {
          if (snapshot.hasError)
            return new Text('Error: ${snapshot.error}');
          switch (snapshot.connectionState) {
            case ConnectionState.waiting:
              return new Text('Loading...');
            default:
              return new Container(
                  decoration: new BoxDecoration(
                    borderRadius: new BorderRadius.circular(16.0),
                    color: Color(0xff303030),
                  ),
                  width: MediaQuery
                      .of(context)
                      .size
                      .width * 0.8,
                  height: MediaQuery
                      .of(context)
                      .size
                      .width * 0.3,
                  child: new ListView.builder(
                      scrollDirection: Axis.horizontal,
                      itemCount: snapshot.data.documents.length,
                      itemBuilder: (BuildContext ctxt, int index) {
                        return new Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              RawMaterialButton(
                                onPressed: () {
                                  //   programItems.elementAt(index).setClicked();
                                  setState(() {});
                                },
                                child: new Icon(
                                  Icons.home,
                                  color: Colors.black,
                                  size: MediaQuery
                                      .of(context)
                                      .size
                                      .width * 0.07,
                                ),
                                shape: new CircleBorder(),
                                elevation: 2.0,
                                fillColor: /* programItems
        .elementAt(index)
        .isChecked ? Colors.green : Colors.white, */
                                Colors.white,
                                padding: const EdgeInsets.all(15.0),
                              ),

                              Text(snapshot.data.documents[index]['optiontext'],
                                  style: TextStyle(
                                      color: Colors.teal
                                  )
                              )
                            ]
                        );
                      }
                  )
              );
          }
        }
    );
  }
}
