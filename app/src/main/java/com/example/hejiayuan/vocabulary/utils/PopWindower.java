package com.example.hejiayuan.vocabulary.utils;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.databases.DataBaseHelperReview;

public class PopWindower {

    View view;

    View contentView;

    LinearLayout familiarLayout;
    LinearLayout vagueLayout;
    LinearLayout unfamiliarLayout;

    DataBaseHelperReview dbHelper;

    Button garspBtn;
    //SQLiteDatabase dbW;

    String word;

    public PopWindower(View view, String word, Button garspBtn) {
        this.view = view;
        this.word = word;
        this.garspBtn = garspBtn;
        contentView = LayoutInflater.from(MyApplication.getContext()).inflate(
                R.layout.popwindow_layout, null);
        familiarLayout = contentView.findViewById(R.id.popwindow_layout_familiar);
        vagueLayout = contentView.findViewById(R.id.popwindow_layout_vague);
        unfamiliarLayout = contentView.findViewById(R.id.popwindow_layout_unfamiliar);
        dbHelper = new DataBaseHelperReview(MyApplication.getContext(), "reviewlist");
        //dbW = dbHelper.getWritableDatabase();
    }

    public void showPopupWindow() {

        // 一个自定义的布局，作为显示的内容

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //监听点击熟悉事件
        familiarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                dbHelper.updateGrasp(word, 8);
                garspBtn.setText("熟悉");
                Toast.makeText(MyApplication.getContext(), "熟悉", Toast.LENGTH_SHORT).show();
            }
        });

        vagueLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.updateGrasp(word, 5);
                popupWindow.dismiss();
                garspBtn.setText("模糊");
                Toast.makeText(MyApplication.getContext(), "模糊", Toast.LENGTH_SHORT).show();
            }
        });

        unfamiliarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.updateGrasp(word, 2);
                popupWindow.dismiss();
                garspBtn.setText("陌生");
                Toast.makeText(MyApplication.getContext(), "陌生", Toast.LENGTH_SHORT).show();
            }
        });
        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

               // Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(MyApplication.getContext().getResources().getDrawable(
               R.drawable.tab_btn_forget_bg));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }
}
