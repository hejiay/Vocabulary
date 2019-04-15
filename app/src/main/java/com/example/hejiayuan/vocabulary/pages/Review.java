package com.example.hejiayuan.vocabulary.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import com.example.hejiayuan.vocabulary.R;

public class Review extends Fragment implements View.OnClickListener, TextWatcher {


    private android.support.v7.widget.Toolbar toolbar;

    private RelativeLayout relativeLayout = null;

    private Button btnChangeToWordImformation;
    public Review() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.review, null);

        toolbar = layout.findViewById(R.id.id_review_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //init(layout);
        initWidget(layout);
        setOnClickLis();
        return layout;
    }

    private void init(View layout) {

    }

    @Override
    public void onClick(View v) {

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

    public void clearCount() {

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.review_toolbar_item, menu);
    }

    public void initWidget(View layout) {
        btnChangeToWordImformation = (Button) layout.findViewById(R.id.id_review_btn_change_wordinformation);
        relativeLayout = (RelativeLayout) layout.findViewById(R.id.id_review_layout_wordinformation);
    }

    public void setOnClickLis() {
        btnChangeToWordImformation.setOnClickListener(new ChangeToWordImformation());
    }

    class ChangeToWordImformation implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            relativeLayout.setVisibility(View.VISIBLE);
            btnChangeToWordImformation.setVisibility(View.GONE);
        }
    }
}
