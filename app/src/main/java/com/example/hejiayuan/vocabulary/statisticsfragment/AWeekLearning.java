package com.example.hejiayuan.vocabulary.statisticsfragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.chartutils.AWeekChartDataHelper;
import com.example.hejiayuan.vocabulary.utils.BarChartManager;
import com.example.hejiayuan.vocabulary.utils.MyApplication;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AWeekLearning extends Fragment implements OnChartValueSelectedListener {

    private BarChart aWeekLearningChart;
    BarChartManager barChartManager;
    AWeekChartDataHelper aWeekChartDataHelper;


    List<String> xValuesList = new ArrayList<>();
    List<Float> yValuesList = new ArrayList<>();
    public AWeekLearning() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_aweek_learning, null);
        init(layout);
        getData();
        return layout;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    public void init(View layout) {
        aWeekLearningChart = (BarChart) layout.findViewById(R.id.id_statis_aweek_learning_chart);
    }


    public void getData() {
        aWeekChartDataHelper = new AWeekChartDataHelper("reviewlist");
        xValuesList = aWeekChartDataHelper.getxValues();
        yValuesList = aWeekChartDataHelper.getyValues();
        barChartManager = new BarChartManager(aWeekLearningChart, xValuesList);
        barChartManager.showBarChart(xValuesList, yValuesList, "数量", 0xff00aaff);
    }
}
