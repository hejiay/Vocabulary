package com.example.hejiayuan.vocabulary.service;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.activities.MainActivity;
import com.example.hejiayuan.vocabulary.receiver.ClockRecevier;
import com.example.hejiayuan.vocabulary.utils.Log;

import java.util.Calendar;

public class ClockService extends Service {

    private long ONEDAY = 60 * 60 * 1000 * 24; //一天的毫秒数

    private SharedPreferences timeSP;

    public int hour;

    public int minute;

    public ClockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.log("触发");
                remind();
                //sendRemindMsg();
            }
        }).start();
        getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Intent i = new Intent(this, ClockService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY, pi);
        } else {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), ONEDAY, pi);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 获得设置的时间
     */
    public void getTime() {
        timeSP = getSharedPreferences("time", 0);
        hour = timeSP.getInt("hour", 0);
        minute = timeSP.getInt("minute", 0);
    }

    /**
     * 到点提醒
     */
    public void remind() {
            String channelId = "remind";
            String channelName = "提醒";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
            sendRemindMsg();
    }

    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    public void sendRemindMsg() {
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "remind")
                .setContentTitle("提醒")
                .setContentText("该复习单词啦！")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.clock)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setVibrate(new long[] { 0, 1000, 1000, 1000})
                .setLights(Color.GREEN, 1000, 1000)
                .build();
        manager.notify(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.log("闹钟服务已关闭 onDestory");
    }
}
