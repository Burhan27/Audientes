import 'package:audientes/AppColors.dart';
import 'package:flutter/material.dart';
import 'package:fl_chart/fl_chart.dart';

class ResultScreen extends StatefulWidget {
  @override
  _TestHomeScreen createState() => _TestHomeScreen();
}

class _TestHomeScreen extends State<ResultScreen> {
  bool test = true;
  List<List<double>> listing = new List<List<double>>();
  List<double> results = new List<double>();
  List<double> results2 = new List<double>();

  proceedDialog(BuildContext context) {
    return showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            title: Text("Warning!",
              style: TextStyle(
                  color: AppColors().plainText),
            ),
            backgroundColor: AppColors().bar1,
            content: Text("You got an ongoing test, "
                "are you sure, that you want to proceed?",
            style: TextStyle(
                color: AppColors().plainText),),
            actions: <Widget>[
              MaterialButton(
                elevation: 5.0,
                child: Text("Yes, start a new test"),
                color: AppColors().red1,
                textColor: Colors.white,
                onPressed: () => Navigator.popAndPushNamed(context, '/hear'),
              ),
              MaterialButton(
                elevation: 5.0,
                color: AppColors().green1,
                textColor: Colors.white,
                child: Text("Go back"),
                onPressed: () {
                  Navigator.pop(context);
                },
              )
            ],
          );
        });
  }

  @override
  void initState() {
    super.initState();
    results.add(2);
    results.add(4);
    results.add(6);
    results.add(7);
    results.add(4);
    results.add(7);
    results.add(6);
    results.add(1);
    results.add(9);
    results.add(2);
    results.add(5);
    results.add(7);

    results2.add(2);
    results2.add(4);
    results2.add(6);
    results2.add(7);
    results2.add(4);
    results2.add(7);
    results2.add(6);
    results2.add(1);
    results2.add(9);
    results2.add(2);
    results2.add(5);
    results2.add(7);

    listing.add(results);
    listing.add(results2);
  }

  LineChartData sampleData2(List<double> data) {
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

  List<LineChartBarData> linesBarData2(List<double> data) {
    return [
      LineChartBarData(
        spots: [
          FlSpot(2, data.elementAt(0)),
          FlSpot(3, data.elementAt(1)),
          FlSpot(4, data.elementAt(2)),
          FlSpot(5, data.elementAt(3)),
          FlSpot(6, data.elementAt(4)),
          FlSpot(7, data.elementAt(5)),
        ],
        isCurved: true,
        curveSmoothness: 0,
        colors: [
          AppColors().green1,Colors.greenAccent,Colors.green,
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
          FlSpot(2, data.elementAt(6)),
          FlSpot(3, data.elementAt(7)),
          FlSpot(4, data.elementAt(8)),
          FlSpot(5, data.elementAt(9)),
          FlSpot(6, data.elementAt(10)),
          FlSpot(7, data.elementAt(11)),
        ],
        isCurved: true,
        curveSmoothness: 0,
        colors: [
          AppColors().red1, Colors.redAccent,
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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          backgroundColor: AppColors().bar,
          title: Text('Audientes'),
        ),
        body: Container(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              Container(
                margin: EdgeInsets.symmetric(vertical: 10.0, horizontal: 5),
                padding: EdgeInsets.symmetric(horizontal: 10),
                //Tjek lige det her
                height: ((MediaQuery.of(context).size.height) / 2),
                width: MediaQuery.of(context).size.width,
                child: ListView.builder(
                  itemCount: listing.length,
                  scrollDirection: Axis.horizontal,
                  itemBuilder: (BuildContext ctxt, int index) {
                    return new Container(
                      decoration: new BoxDecoration(
                        borderRadius: new BorderRadius.circular(16.0),
                        color: AppColors().bar,
                      ),
                      margin:
                          EdgeInsets.symmetric(vertical: 10.0, horizontal: 10),
                      padding: EdgeInsets.symmetric(horizontal: 10),
                      //Tjek lige det her
                      width: MediaQuery.of(context).size.width * 0.8,
                      height: MediaQuery.of(context).size.height * 0.6,
                      child: Column(
                        children: <Widget>[
                          Text(
                            '03/11/2019',
                            style: TextStyle(
                              color: AppColors().text,
                              fontSize: 16,
                            ),
                          ),
                          FlChart(
                              swapAnimationDuration:
                                  Duration(milliseconds: 250),
                              chart: LineChart(
                                  sampleData2(listing.elementAt(index))),
                          )
                        ],
                      ),
                    );
                  },
                ),
              ),

              /*

            FlChart(
              swapAnimationDuration: Duration(milliseconds: 250),
              chart: LineChart(
                  sampleData2()
              ),
            ),
            ],
          ),

        ),

        Container(
          decoration: new BoxDecoration(
            borderRadius: new BorderRadius.circular(16.0),
            color: Colors.white,
          ),
          margin: EdgeInsets.symmetric(vertical: 10.0, horizontal: 10),
          padding: EdgeInsets.symmetric(horizontal: 10), //Tjek lige det her
          width: MediaQuery.of(context).size.width*0.8,
          child: Column(
            children: <Widget>[
              Text(
                '03/11/2019',
                style: TextStyle(
                  color: const Color(0xff827daa),
                  fontSize: 16,
                ),
              ),
              FlChart(
                swapAnimationDuration: Duration(milliseconds: 250),
                chart: LineChart(
                    sampleData2()
                ),
              ),
            ],
          ),

        ),
        Container(
          width: 460.0,
          color: Colors.blue,
        ),
      ],
    ),
    ),

*/

              RaisedButton(
                child: Text('Start Hearing Test',
                  style: TextStyle(
                      color:AppColors().text ),),
                onPressed: () {
                  proceedDialog(context);
                },
                color: AppColors().bar1,
                highlightColor: AppColors().highlight,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(6.0)),
              ),
              RaisedButton(
                child: Text('Resume Hearing Test',
                style: TextStyle(
                    color:Colors.grey ),
                ),
                onPressed: test ? null : null,
                //() => whatToDoOnPressed i stedet for sidste null
                color: Colors.white,
                disabledColor: AppColors().bar,
                highlightColor: AppColors().highlight,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(6.0)),
              ),
            ],
          ),
          color: AppColors().background,
          width: MediaQuery.of(context).size.width,
          height: MediaQuery.of(context).size.height,
        ));
  }
}
