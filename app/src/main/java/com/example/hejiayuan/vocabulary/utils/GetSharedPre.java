package com.example.hejiayuan.vocabulary.utils;

import android.content.SharedPreferences;

public class GetSharedPre {

    static SharedPreferences switchSP;

    public static boolean getSharedOfSwitch() {
        switchSP = MyApplication.getContext().getSharedPreferences("switch", 0);
        return switchSP.getBoolean("isChecked", false);
    }
}
