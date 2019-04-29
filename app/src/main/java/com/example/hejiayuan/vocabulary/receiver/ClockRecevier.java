package com.example.hejiayuan.vocabulary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.hejiayuan.vocabulary.service.ClockService;
import com.example.hejiayuan.vocabulary.utils.Log;

public class ClockRecevier extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
       // throw new UnsupportedOperationException("Not yet implemented");
        Log.log("收到");
        Intent i = new Intent(context, ClockService.class);
        context.startService(i);
    }
}
