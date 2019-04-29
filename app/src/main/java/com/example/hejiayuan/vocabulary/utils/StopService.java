package com.example.hejiayuan.vocabulary.utils;

import android.content.Context;
import android.content.Intent;

import com.example.hejiayuan.vocabulary.service.ClockService;
import com.example.hejiayuan.vocabulary.service.StartClockService;

public class StopService {
    Context context = null;

    public StopService(Context context) {
        this.context = context;
    }

    public void stopStartClockService() {
        Intent stopStartClockIntent = new Intent(context, StartClockService.class);
        context.stopService(stopStartClockIntent);

    }
    public void stopClockService() {
        Intent stopClockIntent = new Intent(context, ClockService.class);
        context.stopService(stopClockIntent);
    }
}
