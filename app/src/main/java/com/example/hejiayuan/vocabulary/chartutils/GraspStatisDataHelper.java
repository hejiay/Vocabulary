package com.example.hejiayuan.vocabulary.chartutils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.example.hejiayuan.vocabulary.databases.DataBaseHelperReview;
import com.example.hejiayuan.vocabulary.utils.Log;
import com.example.hejiayuan.vocabulary.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class GraspStatisDataHelper {
    List<String> xValues = new ArrayList<>();
    List<Integer> yValues = new ArrayList<>();

    DataBaseHelperReview dbHelper;
    SQLiteDatabase dbR;
    SQLiteDatabase dbW;

    String tableName;

    public GraspStatisDataHelper(String tableName) {
        this.tableName = tableName;
        dbHelper = new DataBaseHelperReview(MyApplication.getContext(), tableName);
        dbR = dbHelper.getReadableDatabase();
        dbW = dbHelper.getWritableDatabase();
    }

    public List<Integer> getyValues() {
        return getCountListByGrasp();
    }

    public List<Integer> getCountListByGrasp() {

        //按照掌握程度统计个数
        for (int i=0; i<=10; i++) {
            yValues.add(getCountByGrasp(i));
        }
        return  yValues;
    }

    public int getCountByGrasp(int grasp) {
        Cursor cursor = dbR.query(tableName, new String[] {"word"}, "grasp = ?", new String[] { String.valueOf(grasp) }, null, null, null);
        return cursor.getCount();
    }
}
