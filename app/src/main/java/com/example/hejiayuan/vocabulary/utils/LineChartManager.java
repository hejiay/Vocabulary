package com.example.hejiayuan.vocabulary.utils;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class LineChartManager {
    private LineChart lineChart;
    private YAxis leftAxis;             //左侧Y轴
    private YAxis rightAxis;            //右侧Y轴
    private XAxis xAxis;                //X轴
    List<String> xValueList;

    public LineChartManager(LineChart lineChart, List<String> xValueList) {
        this.lineChart = lineChart;
        this.xValueList = xValueList;
        //initLineChart(lineChart);
    }

    /**
     * 初始化折线表
     * @param lineChart
     */
    public void initLineChart(LineChart lineChart) {
        lineChart.setBackgroundColor(Color.WHITE);
        //显示边框
        lineChart.setDrawBorders(true);
        //动画设置
        lineChart.animateX(3000, Easing.EaseInCirc);
        //lineChart.animateY(2000, Easing.Linear);

        /**XY轴设置**/
        //X轴在底部
        xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setGranularity(1f);

        leftAxis = lineChart.getAxisLeft();
        rightAxis = lineChart.getAxisRight();

        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setDrawLabels(false);
        leftAxis.setDrawLabels(false);

        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xValueList.get((int)value);
            }
        });
    }

    public void showLineChart(List<Integer> yValueList, String name, int color) {
        initLineChart(lineChart);
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i=0; i<yValueList.size(); i++) {
            entries.add(new Entry(i, yValueList.get(i)));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, name);

        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + (int) value;
            }
        });
        lineDataSet.setValueTextSize(12f);

        LineData data = new LineData(lineDataSet);
        lineChart.setData(data);
    }

}
