package com.example.hejiayuan.vocabulary.utils;

import android.graphics.Color;
import android.graphics.Matrix;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class BarChartManager {

    private BarChart barChart;
    private YAxis leftAxis;             //左侧Y轴
    private YAxis rightAxis;            //右侧Y轴
    private XAxis xAxis;                //X轴
    List<String> xValueList;


    public BarChartManager(BarChart mBarChart, List<String> xValueList) {
        this.barChart = mBarChart;
        this.xValueList = xValueList;
        initBarChart(mBarChart);
    }

    /**
     * 初始化BarChart图表
     */
    private void initBarChart(BarChart barChart) {
        /***图表设置***/
        //背景颜色
        barChart.setBackgroundColor(Color.WHITE);
        //不显示图表网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //显示边框
        barChart.setDrawBorders(true);
        //设置动画效果
        barChart.animateY(1000, Easing.Linear);
        barChart.animateX(1000, Easing.Linear);

        /***XY轴的设置***/
        //X轴设置显示位置在底部
        xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setGranularity(1f);

        leftAxis = barChart.getAxisLeft();
        rightAxis = barChart.getAxisRight();
        leftAxis.setDrawLabels(false);
        rightAxis.setDrawLabels(false);
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);

        rightAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return "" + (int) value;
            }
        });

        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        barChart.setDrawGridBackground(false);

        //不显示X轴网格线
        xAxis.setDrawGridLines(false);
        //右侧Y轴网格线设置为虚线
        rightAxis.enableGridDashedLine(10f, 10f, 0f);

        //xy同时缩放
        barChart.setPinchZoom(true);
//        barChart.setScaleXEnabled(false);
//        barChart.setScaleYEnabled(false);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xValueList.get((int)value);
            }
        });

    }

    /**
     * 柱状图始化设置 一个BarDataSet 代表一列柱状图
     *
     * @param barDataSet 柱状图
     * @param color      柱状图颜色
     */
    private void initBarDataSet(BarDataSet barDataSet, int color) {
        barDataSet.setColor(color);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);
        //显示柱状图顶部值
        barDataSet.setDrawValues(true);
        barDataSet.setValueTextSize(10f);
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + (int) value;
            }
        });
//        barDataSet.setValueTextColor(color);
    }

    public void showBarChart(List<String> xValueList,List<Float> yValueList, String name, int color) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        //ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < xValueList.size(); i++) {
            /**
             * 此处还可传入Drawable对象 BarEntry(float x, float y, Drawable icon)
             * 即可设置柱状图顶部的 icon展示
             */
           // XAxis xAxis = new XAxis();
            //xValues.add(xValueList.get(i));
            //BarData data = new BarData(xValueList.get(i).toString(), yValueList.get(i));
            BarEntry barEntry = new BarEntry((float)i, yValueList.get(i));
            entries.add(barEntry);
        }
        // 每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, name);
        initBarDataSet(barDataSet, color);

//        // 添加多个BarDataSet时
//        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//        dataSets.add(barDataSet);
//        BarData data = new BarData(dataSets);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.5f);
        barChart.setData(data);
    }
}
