package com.example.hejiayuan.vocabulary.chartutils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.hejiayuan.vocabulary.databases.DataBaseHelperReview;
import com.example.hejiayuan.vocabulary.utils.Log;
import com.example.hejiayuan.vocabulary.utils.MyApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class AWeekChartDataHelper {
    List<String> xValues = new ArrayList<>();
    List<Float> yValues = new ArrayList<>();

    DataBaseHelperReview dbHelper;
    SQLiteDatabase dbR;
    SQLiteDatabase dbW;

    String tableName;
    public AWeekChartDataHelper(String tableName) {

        this.tableName = tableName;

        dbHelper = new DataBaseHelperReview(MyApplication.getContext(), tableName);
        dbR = dbHelper.getReadableDatabase();
        dbW = dbHelper.getWritableDatabase();

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        Calendar calendar = Calendar.getInstance();

        //六天前
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -6);
        Date d6 = calendar.getTime();
        String d6Str = formatter.format(d6);
        xValues.add(d6Str);
        Log.log(d6Str + d6.getTime());

        //五天前
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -5);
        Date d5 = calendar.getTime();
        String d5Str = formatter.format(d5);
        xValues.add(d5Str);
        Log.log(d5Str + d5.getTime());

        //四天前
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -4);
        Date d4 = calendar.getTime();
        String d4Str = formatter.format(d4);
        xValues.add(d4Str);
        Log.log(d4Str + d4.getTime());

        //三天前
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -3);
        Date d3 = calendar.getTime();
        String d3Str = formatter.format(d3);
        xValues.add(d3Str);
        Log.log(d3Str  + d3.getTime());

        //两天前
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -2);
        Date d2 = calendar.getTime();
        String d2Str = formatter.format(d2);
        xValues.add(d2Str);
        Log.log(d2Str + d2.getTime());

        //一天前
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        Date d1 = calendar.getTime();
        String d1Str = formatter.format(d1);
        xValues.add(d1Str);
        Log.log(d1Str + d1.getTime());

        //今天
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 0);
        Date today = calendar.getTime();
        String todayStr = formatter.format(today);
        xValues.add(todayStr);
        Log.log(todayStr);
    }

    public List<String> getxValues() {
        return xValues;
    }

    public List<Float> getyValues() {
        return getCountGroupByLastLearnTime();
    }

    public List<Float> getCountGroupByLastLearnTime() {

        //查询六天前的单词学习数量
        long startTimeDay6 = getCal(-6).getTimeInMillis();
        long endTimeDay6 = startTimeDay6 + 24 * 3600 * 1000;

        Cursor cursorDay6 = dbR.query(tableName, new String[]{ "word" }, "lastLearntime > ? and lastLearntime < ?",
                new String[]{ String.valueOf(startTimeDay6), String.valueOf(endTimeDay6)}, null, null, null);
        Log.log( "6 " + String.valueOf(cursorDay6.getCount()));
        yValues.add((float)cursorDay6.getCount());
        cursorDay6.close();

        //五天前
        long startTimeDay5 = getCal(-5).getTimeInMillis();
        long endTimeDay5 = startTimeDay5 + 24 * 3600 * 1000;

        Cursor cursorDay5 = dbR.query(tableName, new String[]{ "word" }, "lastLearntime >= ? and lastLearntime <= ?",
                new String[]{ String.valueOf(startTimeDay5), String.valueOf(endTimeDay5)}, null, null, null);
        Log.log("5 " + String.valueOf(cursorDay5.getCount()));
        yValues.add((float)cursorDay5.getCount());
        cursorDay5.close();

        //四天前
        long startTimeDay4 = getCal(-4).getTimeInMillis();
        long endTimeDay4 = startTimeDay4 + 24 * 3600 * 1000;

        Cursor cursorDay4 = dbR.query(tableName, new String[]{ "word" }, "lastLearntime > ? and lastLearntime < ?",
                new String[]{ String.valueOf(startTimeDay4), String.valueOf(endTimeDay4)}, null, null, null);
        Log.log("4 " + String.valueOf(cursorDay4.getCount()));
        yValues.add((float)cursorDay4.getCount());
        cursorDay4.close();

        //三天前
        long startTimeDay3 = getCal(-3).getTimeInMillis();
        long endTimeDay3 = startTimeDay3 + 24 * 3600 * 1000;

        Cursor cursorDay3 = dbR.query(tableName, new String[]{ "word" }, "lastLearntime > ? and lastLearntime < ?",
                new String[]{ String.valueOf(startTimeDay3), String.valueOf(endTimeDay3)}, null, null, null);
        Log.log("3 " + String.valueOf(cursorDay3.getCount()));
        yValues.add((float)cursorDay3.getCount());
        cursorDay3.close();

        // 两天天前
        long startTimeDay2 = getCal(-2).getTimeInMillis();
        long endTimeDay2 = startTimeDay2 + 24 * 3600 * 1000;

        Cursor cursorDay2 = dbR.query(tableName, new String[]{ "word" }, "lastLearntime > ? and lastLearntime < ?",
                new String[]{ String.valueOf(startTimeDay2), String.valueOf(endTimeDay2)}, null, null, null);
        Log.log("2 " + String.valueOf(cursorDay2.getCount()));
        yValues.add((float)cursorDay2.getCount());
        cursorDay2.close();

        // 一天前
        long startTimeDay1 = getCal(-1).getTimeInMillis();
        long endTimeDay1 = startTimeDay1 + 24 * 3600 * 1000;

        Cursor cursorDay1 = dbR.query(tableName, new String[]{ "word" }, "lastLearntime > ? and lastLearntime < ?",
                new String[]{ String.valueOf(startTimeDay1), String.valueOf(endTimeDay1)}, null, null, null);
        Log.log("1 " +String.valueOf(cursorDay1.getCount()));
        yValues.add((float)cursorDay1.getCount());
        cursorDay1.close();

        //今天
        long startTimeToday = getCal(0).getTimeInMillis();
        long endTimeToday = startTimeToday + 24 * 3600 * 1000;

        Cursor cursorToday = dbR.query(tableName, new String[]{ "word" }, "lastLearntime > ? and lastLearntime < ?",
                new String[]{ String.valueOf(startTimeToday), String.valueOf(endTimeToday)}, null, null, null);
        Log.log("today " + String.valueOf(cursorToday.getCount()));
        yValues.add((float)cursorToday.getCount());
        cursorToday.close();
        return yValues;
    }

    public GregorianCalendar getCal(int date) {
        Date now = new Date();//获取当前时间
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTime(now);
        cal.add(Calendar.DATE, date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        //毫秒可根据系统需要清除或不清除
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }
}
