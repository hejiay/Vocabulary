package com.example.hejiayuan.vocabulary.activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.utils.Log;
import com.example.hejiayuan.vocabulary.utils.MyApplication;

import java.util.Calendar;

public class ClockActivity extends AppCompatActivity {

    private Switch remindSwitch;

    private TextView timePickerText;

    private Context context;

    public int hour, myMinute;

    public StringBuffer timeStr; //在text上显示的字符

    TimePicker timePicker;

    private SharedPreferences timeSP;

    private SharedPreferences switchSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        initWidget();
        init();
        setOnClick();
    }

    private void init() {
        context = this;
        timeStr = new StringBuffer();
        timeSP = getSharedPreferences("time", 0);//获取共享属性的操作工具
        switchSP = getSharedPreferences("switch", 0);
        boolean isChecked = switchSP.getBoolean("isChecked", false);
        remindSwitch.setChecked(isChecked);
        if (isChecked) {
            timePickerText.setClickable(true);
        } else {
            timePickerText.setClickable(false);
        }
        initTime();
    }

    /**
     * 获取当前的时间和日期
     */
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        myMinute = calendar.get(Calendar.MINUTE);
    }

    private void initWidget() {
        remindSwitch = (Switch)findViewById(R.id.id_clock_switch);
        timePickerText = (TextView) findViewById(R.id.id_setting_time_picker);
        timePicker = (TimePicker) findViewById(R.id.id_layout_timePicker);
    }

    public void setOnClick() {
        remindSwitch.setOnCheckedChangeListener(new clickRemindSwitch());
        timePickerText.setOnClickListener(new clickToTimePicker());
    }

    class clickRemindSwitch implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                storeSwitch(true);
                timePickerText.setClickable(true);
            } else {
                storeSwitch(false);
                timePickerText.setClickable(false);
            }
        }
    }

    class clickToTimePicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (timeStr.length() > 0) {
                        timeStr.delete(0, timeStr.length());
                    }
                    String strMinute;
                    if (minute <= 9) {
                        strMinute = "0" + String.valueOf(minute);
                    } else {
                        strMinute = String.valueOf(minute);
                    }
                    timePickerText.setText(timeStr.append(String.valueOf(hourOfDay)).append(":").append(strMinute));
                    SharedPreferences.Editor editor = timeSP.edit();
                    editor.putString("timer", timePickerText.getText().toString());
                    editor.commit();
                }
            }, hour, myMinute, true).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        remindSwitch.setChecked(switchSP.getBoolean("isChecked", false));
        timePickerText.setText(timeSP.getString("timer", "不提醒"));
    }

    public void storeSwitch(boolean isChecked) {
        SharedPreferences.Editor editor = switchSP.edit();
        editor.putBoolean("isChecked", isChecked);
        editor.commit();
    }
}
