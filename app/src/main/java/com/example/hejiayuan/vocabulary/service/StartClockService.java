package com.example.hejiayuan.vocabulary.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import com.example.hejiayuan.vocabulary.utils.Log;

import java.util.Calendar;

public class StartClockService extends Service {

    SharedPreferences timeSP;

    SharedPreferences switchSP;

    Calendar nowTime;

    Calendar clockTime;

    int hour, minute;

    int compareHour, compareMinute;

    public StartClockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        setTime();
        boolean flag = compareTime();
        if (flag == true) {
            if (isSwitch() == true) {
                Log.log("闹钟开关已打开");
                Intent i = new Intent(this, ClockService.class);
                this.startService(i);
            } else {
                setClock();

                Log.log("闹钟开关已关闭");
            }
        } else {
            if (isSwitch() == true) {
                setClock();
                Log.log("闹钟时间未到");
            } else {
                stopSelf();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void init() {
        timeSP = getSharedPreferences("time", 0);
        switchSP = getSharedPreferences("switch", 0);
        nowTime = Calendar.getInstance();
        clockTime = Calendar.getInstance();
    }

    public void setTime() {
        hour = timeSP.getInt("hour", 0);
        minute = timeSP.getInt("minute", 0);
        clockTime.set(Calendar.HOUR_OF_DAY, hour);
        clockTime.set(Calendar.MINUTE, minute);
        clockTime.set(Calendar.SECOND, 0);
        clockTime.set(Calendar.MILLISECOND, 0);
    }

    public boolean compareTime() {
        compareHour = nowTime.getTime().getHours();
        compareMinute = nowTime.getTime().getMinutes();
        if (compareHour == hour && compareMinute == minute) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSwitch() {
        boolean isCheck = switchSP.getBoolean("isChecked", false);
        return isCheck;
    }

    public void setClock() {
        Intent i = new Intent(this, StartClockService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, nowTime.getTimeInMillis(), pi);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.log("闹钟启动服务已关闭 onDestory");
        Intent intent = new Intent(this, ClockService.class);
        this.stopService(intent);
    }
}
