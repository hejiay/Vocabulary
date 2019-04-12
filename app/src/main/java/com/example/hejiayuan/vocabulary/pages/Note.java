package com.example.hejiayuan.vocabulary.pages;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.activities.MainActivity;
import com.example.hejiayuan.vocabulary.adapters.WordListAdapter;
import com.example.hejiayuan.vocabulary.databases.WordList;
import com.example.hejiayuan.vocabulary.utils.MyApplication;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class Note extends Fragment implements View.OnClickListener, TextWatcher {
    private List<WordList> wordLists = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;

    WordListAdapter adapter;

    Context context;

    LinearLayoutManager layoutManager;

    RecyclerView recyclerView;

    View layout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.note, null);
        initWordList();//初始化单词本数据
        recyclerView = (RecyclerView) layout.findViewById(R.id.id_note_recyclerview);
        layoutManager = new LinearLayoutManager(null);
        context = MyApplication.getContext();
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WordListAdapter(wordLists, context);
        recyclerView.setAdapter(adapter);
        refreshWordList(layout);//刷新操作
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

    private void initWordList() {
        if (wordLists != null)
            wordLists.clear();
        wordLists = LitePal.select("word", "interpret")
                .find(WordList.class);//从数据库中查询单词本中已有的数据
    }

    private void refreshWordList(View layout) {
        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.id_note_swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initWordList();//重新查询数据
                if (adapter != null)
                    adapter = null;
                adapter = new WordListAdapter(wordLists, context);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);///刷新结束，进度条隐藏
            }
        });
    }
}
