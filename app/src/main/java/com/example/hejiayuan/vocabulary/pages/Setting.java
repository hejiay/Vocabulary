package com.example.hejiayuan.vocabulary.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.activities.ClockActivity;
import com.example.hejiayuan.vocabulary.activities.MainActivity;
import com.example.hejiayuan.vocabulary.utils.MyApplication;

public class Setting extends Fragment implements View.OnClickListener, TextWatcher {
    private LinearLayout clockLayout;

    private SharedPreferences timeSP;
    private SharedPreferences switchSP;

    private Context context;

    private TextView remindText;

    public boolean switchOn;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.setting, null);
        initWidget(layout);
        init();
        setOnClickLis();
        return layout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {

    }

    private void init() {
        context = MyApplication.getContext();
        timeSP = context.getSharedPreferences("time", 0);
        switchSP = context.getSharedPreferences("switch", 0);
        //setRemindText();
    }

    private void initWidget(View layout) {
        clockLayout = (LinearLayout) layout.findViewById(R.id.setting_layout_remind_timer);
        remindText = (TextView) layout.findViewById(R.id.id_setting_remind_time);
    }

    public void setOnClickLis() {
        clockLayout.setOnClickListener(new clickToClock());
    }

    class clickToClock implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MyApplication.getContext(), ClockActivity.class);
            MyApplication.getContext().startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setRemindText();
    }

    @Override
    public void onPause() {
        super.onPause();
        setRemindText();
    }

    public void setRemindText() {
        switchOn = switchSP.getBoolean("isChecked", false);
        if (switchOn == true) {
            String timeStr = timeSP.getString("timer", "");
            remindText.setText(timeStr);
        } else {
            remindText.setText("不提醒");
        }
    }
}
