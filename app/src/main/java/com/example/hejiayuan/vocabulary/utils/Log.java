package com.example.hejiayuan.vocabulary.utils;

public class Log {
    static String TAG = MyApplication.getContext().toString();

    public static void log(String info) {
        android.util.Log.d(TAG, "调试信息："+ info);
    }
}
