package com.example.hejiayuan.vocabulary.pages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class Note extends Fragment implements View.OnClickListener, TextWatcher {
    private List<WordList> wordLists = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.note, null);
        initWordList();//初始化单词本数据
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.id_note_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(null);
        recyclerView.setLayoutManager(layoutManager);
        WordListAdapter adapter = new WordListAdapter(wordLists);
        recyclerView.setAdapter(adapter);
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
        wordLists = LitePal.select("word", "interpret")
                .find(WordList.class);//从数据库中查询单词本中已有的数据
    }
}
