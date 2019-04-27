package com.example.hejiayuan.vocabulary.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.hejiayuan.vocabulary.utils.MyApplication;

public class ClockService extends Service {

    private long ONEDAY = 60 * 60 * 1000 * 24; //一天的毫秒数

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
        Toast.makeText(MyApplication.getContext(), "处罚成功", Toast.LENGTH_SHORT).show();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + ONEDAY;
        Intent i = new Intent(this, ClockService.class);
        PendingIntent pi = PendingIntent.getService(this, 0 , i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
