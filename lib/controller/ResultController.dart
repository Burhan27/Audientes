import 'package:audientes/model/ResultItem.dart';
import 'package:cloud_firestore/cloud_firestore.dart';

class ResultController {
  void createProgram(ResultItem resultItem) {
    Firestore.instance.collection('Results').add({
      'l1': resultItem.l1,
      'l2': resultItem.l2,
      'l3': resultItem.l3,
      'l4': resultItem.l4,
      'l5': resultItem.l5,
      'l6': resultItem.l6,
      'r1': resultItem.r1,
      'r2': resultItem.r2,
      'r3': resultItem.r3,
      'r4': resultItem.r4,
      'r5': resultItem.r5,
      'r6': resultItem.r6,
    });

    Firestore.instance.collection('Results').document("test").setData({
      'l1': resultItem.l1,
      'l2': resultItem.l2,
      'l3': resultItem.l3,
      'l4': resultItem.l4,
      'l5': resultItem.l5,
      'l6': resultItem.l6,
      'r1': resultItem.r1,
      'r2': resultItem.r2,
      'r3': resultItem.r3,
      'r4': resultItem.r4,
      'r5': resultItem.r5,
      'r6': resultItem.r6,
    });
  }
}
