package com.example.hejiayuan.vocabulary.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import com.example.hejiayuan.vocabulary.utils.MyApplication;

import java.util.Date;

public class ClockManager {
    private static ClockManager clockInstance = new ClockManager();

    public ClockManager() {
    }

    public static ClockManager getInstance() {
        return clockInstance;
    }

    /**
     * 获取闹钟服务
     * @return
     */
    private static AlarmManager getAlarmManager() {
        return (AlarmManager) MyApplication.getContext().getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * 取消闹钟
     * @param pendingIntent
     */
    public void cancelAlarm(PendingIntent pendingIntent) {
        getAlarmManager().cancel(pendingIntent);
    }

    /**
     * t添加闹钟
     * @param pendingIntent 执行动作
     * @param performTime 执行时间
     */
    public void addAlarm(PendingIntent pendingIntent, Date performTime) {
        cancelAlarm(pendingIntent);
        getAlarmManager().set(AlarmManager.RTC_WAKEUP, performTime.getTime(), pendingIntent );
    }
}
