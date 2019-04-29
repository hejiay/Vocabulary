package com.example.hejiayuan.vocabulary.utils;

import android.content.Context;
import android.content.Intent;

import com.example.hejiayuan.vocabulary.service.StartClockService;

public class StartService {
    Context context = null;

    public StartService(Context context) {
        this.context = context;
    }

    public void startService() {
        Intent intent = new Intent(context, StartClockService.class);
        context.startService(intent);
        Log.log("启动服务");
    }
}
