package com.example.hejiayuan.vocabulary.pages;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.statisticsfragment.AWeekLearning;
import com.example.hejiayuan.vocabulary.statisticsfragment.GraspStatis;

public class Statistics extends Fragment implements View.OnClickListener, TextWatcher {

    private TabLayout statisticsTabLayout = null;

    private ViewPager statisticsViewPager;

    private Fragment [] statisFragment = new Fragment[2];

    private String[] mTabTitles = new String[2];

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.statistics, null);
        initView(layout);
        init();
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
        mTabTitles[0] = "一周学习情况";
        mTabTitles[1] = "掌握程度统计";
        statisticsTabLayout.setTabMode(TabLayout.MODE_FIXED);
        statisFragment[0] = new AWeekLearning();
        statisFragment[1] = new GraspStatis();

        PagerAdapter pagerAdapter = new StatisViewPagerAdapter(getFragmentManager());
        statisticsViewPager.setAdapter(pagerAdapter);
        statisticsTabLayout.setupWithViewPager(statisticsViewPager);
    }

    private void initView(View layout) {
        statisticsTabLayout = (TabLayout) layout.findViewById(R.id.id_statistics_tablayout);
        statisticsViewPager = (ViewPager) layout.findViewById(R.id.id_statistics_viewpager);
    }

    @Override
    public void onStart() {
        super.onStart();
        onCreate(new Bundle());
    }

    final class StatisViewPagerAdapter extends FragmentPagerAdapter {

        public StatisViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return statisFragment[i];
        }

        @Override
        public int getCount() {
            return statisFragment.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);
        }
    }
}
