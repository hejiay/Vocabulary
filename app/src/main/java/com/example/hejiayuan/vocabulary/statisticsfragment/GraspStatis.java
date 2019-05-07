package com.example.hejiayuan.vocabulary.statisticsfragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.chartutils.GraspStatisDataHelper;
import com.example.hejiayuan.vocabulary.utils.LineChartManager;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraspStatis extends Fragment {

    LineChart graspStatisChart;

    GraspStatisDataHelper garspDataHelper;

    List<String> xValuesList = new ArrayList<>();
    List<Integer> yValuesList = new ArrayList<>();
    public GraspStatis() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_grasp_statis, null);
        init(layout);
        garspDataHelper = new GraspStatisDataHelper("reviewlist");
        yValuesList = garspDataHelper.getyValues();
        for (int i=0; i<=10; i++) {
            xValuesList.add("" + i);
        }
        LineChartManager lineChartManager = new LineChartManager(graspStatisChart, xValuesList);
        lineChartManager.showLineChart(yValuesList, "掌握程度", 0xff00aaff);
        return layout;
    }

    public void init(View layout) {
        //aWeekLearningChart = (BarChart) layout.findViewById(R.id.id_statis_aweek_learning_chart);
        graspStatisChart = layout.findViewById(R.id.id_statis_grasp_linechart);
    }

}
