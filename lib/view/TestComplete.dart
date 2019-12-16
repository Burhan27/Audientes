import 'package:audientes/AppColors.dart';
import 'package:audientes/TestCompleteWrapper.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:loading_animations/loading_animations.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:intl/intl.dart';

class TestComplete extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return TestCompleteState();
  }
}

class TestCompleteState extends State<TestComplete> {
  List<int> results = new List<int>();
  List<String> _stringResults = new List<String>();
  String today = "NA";

  @override
  void initState() {
    super.initState();
    populatelists(_stringResults).then((stringResults) {
      setState(() {
        _stringResults = stringResults;
      });
    });
    var now = new DateTime.now();
    today = DateFormat('dd-MM-yyyy').format(now);
  }

  Future<List<String>> populatelists(List<String> data) async {
    final prefs = await SharedPreferences.getInstance();
    data = prefs.getStringList('results');
    results = data.map(int.parse).toList();
    print("eeee " + results.length.toString());
    return data;
  }

  @override
  Widget build(BuildContext context) {
    if (_stringResults == null) {
      return LoadingBouncingGrid.square(
        backgroundColor: AppColors().highlight,
        borderColor: AppColors().inactive,
        borderSize: 3,
        size: 30,
        duration: Duration(milliseconds: 500),
      );
    }
    return Scaffold(
      backgroundColor: AppColors().background,
      body: Container(
        child: Column(
          children: <Widget>[
            Container(
              margin:
                  EdgeInsets.only(top: MediaQuery.of(context).size.height / 10),
              alignment: Alignment.center,
              child: Text(
                "GOOD JOB!",
                style: TextStyle(fontSize: 40, color: AppColors().text),
              ),
            ),
            Container(
              margin: EdgeInsets.only(top: 10),
              alignment: Alignment.center,
              child: Text(
                "You completed the hearing test! \n"
                "You can see your results on the graph below!",
                style: TextStyle(
                  color: AppColors().plainText,
                  fontSize: 15,
                ),
                textAlign: TextAlign.center,
              ),
            ),
            Container(
              decoration: new BoxDecoration(
                borderRadius: new BorderRadius.circular(16.0),
                color: AppColors().bar,
              ),
              margin: EdgeInsets.symmetric(vertical: 10.0, horizontal: 10),
              padding: EdgeInsets.symmetric(horizontal: 10),
              //Tjek lige det her
              width: MediaQuery.of(context).size.width * 0.8,
              height: MediaQuery.of(context).size.height * 0.45,

              child: Column(
                children: <Widget>[
                  Text(
                    today,
                    style: TextStyle(
                      color: AppColors().text,
                      fontSize: 16,
                    ),
                  ),
                  FlChart(
                    swapAnimationDuration: Duration(milliseconds: 250),
                    chart: LineChart(sampleData2(results)),
                  )
                ],
              ),
            ),
            Container(
              margin: EdgeInsets.only(top: 20),
              width: MediaQuery.of(context).size.width * 0.8,
              alignment: Alignment.center,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[
                  FlatButton(
                    color: AppColors().bar2,
                    child: Container(
                      padding: EdgeInsets.only(top: 3, bottom: 3),
                      child: Text(
                        'Adjust \ndevice',
                        style: TextStyle(color: AppColors().text, fontSize: 20),
                      ),
                    ),
                    shape: RoundedRectangleBorder(
                        borderRadius: new BorderRadius.circular(18.0),
                        side: BorderSide()),
                    onPressed: () {
                      Navigator.popAndPushNamed(context, '/');
                    },
                  ),
                  FlatButton(
                    color: AppColors().bar2,
                    child: Container(
                      alignment: Alignment.centerLeft,
                      padding: EdgeInsets.only(top: 3, bottom: 3),
                      child: Text(
                        'save \nresults',
                        style: TextStyle(color: AppColors().text, fontSize: 20),
                        textAlign: TextAlign.center,
                      ),
                    ),
                    shape: RoundedRectangleBorder(
                        borderRadius: new BorderRadius.circular(18.0),
                        side: BorderSide()),
                    onPressed: () {
                      Navigator.pop(context);
                    },
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  LineChartData sampleData2(List<int> data) {
    return LineChartData(
      lineTouchData: const LineTouchData(enabled: false),
      gridData: FlGridData(
        drawVerticalGrid: true,
        drawHorizontalGrid: true,
        show: true,
        getDrawingHorizontalGridLine: (value) {
          return const FlLine(
            color: Color(0xff2271bf),
            strokeWidth: 1,
          );
        },
        getDrawingVerticalGridLine: (value) {
          return const FlLine(
            color: Color(0xff37434d),
            strokeWidth: 1,
          );
        },
      ),
      titlesData: FlTitlesData(
        bottomTitles: SideTitles(
          showTitles: true,
          textStyle: TextStyle(
            color: AppColors().text,
            fontWeight: FontWeight.bold,
            fontSize: 16,
          ),
          getTitles: (value) {
            switch (value.toInt()) {
              case 1:
                return '125';
              case 2:
                return '250';
              case 3:
                return '500';
              case 4:
                return '1k';
              case 5:
                return '2k';
              case 6:
                return '4k';
              case 7:
                return '8k';
            }
            return '';
          },
          margin: 10,
        ),
        leftTitles: SideTitles(
          showTitles: true,
          textStyle: TextStyle(
            color: AppColors().text,
            fontWeight: FontWeight.bold,
            fontSize: 14,
          ),
          getTitles: (value) {
            switch (value.toInt()) {
              case -1:
                return '100';
              case 0:
                return '90';
              case 1:
                return '80';
              case 2:
                return '70';
              case 3:
                return '60';
              case 4:
                return '50';
              case 5:
                return '40';
              case 6:
                return '30';
              case 7:
                return '20';
              case 8:
                return '10';
              case 9:
                return '0';
              case 10:
                return '-10';
            }
            return '';
          },
          margin: 10,
        ),
      ),
      borderData: FlBorderData(
          show: true,
          border: Border(
            bottom: BorderSide(
              color: AppColors().inactive,
              width: 4,
            ),
            left: BorderSide(
              color: Colors.transparent,
            ),
            right: BorderSide(
              color: Colors.transparent,
            ),
            top: BorderSide(
              color: Colors.transparent,
            ),
          )),
      minX: 1,
      maxX: 7.3,
      maxY: 10.4,
      minY: -1,
      lineBarsData: linesBarData2(data),
    );
  }

  List<LineChartBarData> linesBarData2(List<int> data) {
    return [
      LineChartBarData(
        spots: [
          FlSpot(2, data.elementAt(0).toDouble()),
          FlSpot(3, data.elementAt(1).toDouble()),
          FlSpot(4, data.elementAt(2).toDouble()),
          FlSpot(5, data.elementAt(3).toDouble()),
          FlSpot(6, data.elementAt(4).toDouble()),
          FlSpot(7, data.elementAt(5).toDouble()),
        ],
        isCurved: true,
        curveSmoothness: 0,
        colors: [
          AppColors().green1,
          Colors.greenAccent,
          Colors.green,
        ],
        barWidth: 2,
        isStrokeCapRound: true,
        dotData: FlDotData(
          show: true,
        ),
        belowBarData: BarAreaData(
          show: false,
        ),
      ),
      LineChartBarData(
        spots: [
          FlSpot(2, data.elementAt(6).toDouble()),
          FlSpot(3, data.elementAt(7).toDouble()),
          FlSpot(4, data.elementAt(8).toDouble()),
          FlSpot(5, data.elementAt(9).toDouble()),
          FlSpot(6, data.elementAt(10).toDouble()),
          FlSpot(7, data.elementAt(11).toDouble()),
        ],
        isCurved: true,
        curveSmoothness: 0,
        colors: [
          AppColors().red1,
          Colors.redAccent,
        ],
        barWidth: 2,
        isStrokeCapRound: true,
        dotData: FlDotData(
          show: true,
        ),
        belowBarData: BarAreaData(
          show: false,
        ),
      ),
    ];
  }
}
