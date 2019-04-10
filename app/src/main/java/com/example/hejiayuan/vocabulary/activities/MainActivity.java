package com.example.hejiayuan.vocabulary.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.adapters.MyPagerAdapter;
import com.example.hejiayuan.vocabulary.pages.Note;
import com.example.hejiayuan.vocabulary.pages.Review;
import com.example.hejiayuan.vocabulary.pages.Setting;
import com.example.hejiayuan.vocabulary.pages.Statistics;
import com.jpeng.jptabbar.BadgeDismissListener;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements BadgeDismissListener, OnTabSelectListener {


    @Titles
    private static final String[] mTitles = { "复习", "单词本", "统计", "设置"};

    @SeleIcons
    private static final int [] mSeleIcons = { R.mipmap.review_selected, R.mipmap.note_selected, R.mipmap.statistics_selected, R.mipmap.setting_selected };

    @NorIcons
    private static final int[] mNormalIcons = { R.mipmap.review_normal, R.mipmap.note_normal, R.mipmap.statistics_normal, R.mipmap.setting_normal };

    private List<Fragment> list = new ArrayList<>();

    private ViewPager mPager;

    private JPTabBar mTabbar;

    private Review reviewTab;
    private Note noteTab;
    private Statistics statisticsTab;
    private Setting settingTab;

    //View middleView = mTabbar.getMiddleView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onDismiss(int position) {
        reviewTab.clearCount();
    }

    @Override
    public void onTabSelect(int index) {

    }

    @Override
    public boolean onInterruptSelect(int index) {
        return false;
    }

    public void init() {
        mTabbar = (JPTabBar) findViewById(R.id.tabbar);
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mTabbar.setTabTypeFace("fonts/Jaden.ttf");


        reviewTab = new Review();
        noteTab = new Note();
        statisticsTab = new Statistics();
        settingTab = new Setting();

        mTabbar.setGradientEnable(true);
        mTabbar.setPageAnimateEnable(true);
        mTabbar.setTabListener(this);



        list.add(reviewTab);
        list.add(noteTab);
        list.add(statisticsTab);
        list.add(settingTab);

        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), list));
        mTabbar.setContainer(mPager);
        //设置Badge消失的代理
        mTabbar.setDismissListener(this);
        mTabbar.setTabListener(this);
        if (mTabbar.getMiddleView() != null) {
            mTabbar.getMiddleView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, DictAvtivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public JPTabBar getTabbar() {
        return mTabbar;
    }
}
