package com.example.hejiayuan.vocabulary.pages;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.activities.MainActivity;
import com.example.hejiayuan.vocabulary.adapters.WordListAdapter;
import com.example.hejiayuan.vocabulary.databases.DataBaseHelperReview;
import com.example.hejiayuan.vocabulary.databases.WordList;
import com.example.hejiayuan.vocabulary.utils.MyApplication;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class Note extends Fragment {
    private List<WordList> wordLists;

    private SwipeRefreshLayout swipeRefreshLayout;

    WordListAdapter adapter;

    Context context = null;

    LinearLayoutManager layoutManager;

    RecyclerView recyclerView;

    View layout;

    DataBaseHelperReview dbHelper;
    SQLiteDatabase dbR;
    SQLiteDatabase dbW;

    private Spinner selectedSpin;
    private Spinner orderSpin;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.note, null);
        dbHelper = new DataBaseHelperReview(MyApplication.getContext(), "reviewlist");
        dbR = dbHelper.getReadableDatabase();
        dbW = dbHelper.getWritableDatabase();
        wordLists = new ArrayList<>();
        selectedSpin = (Spinner) layout.findViewById(R.id.id_note_spinner_selected);
        selectedSpin.setOnItemSelectedListener(new SelectedSpinListener());
        orderSpin = (Spinner) layout.findViewById(R.id.id_note_spinner_order);
        orderSpin.setOnItemSelectedListener(new OrderSpinListener());
        initWordList();//初始化单词本数据
        recyclerView = (RecyclerView) layout.findViewById(R.id.id_note_recyclerview);
        layoutManager = new LinearLayoutManager(null);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WordListAdapter(wordLists, MyApplication.getContext() ,recyclerView);
        recyclerView.setAdapter(adapter);
        refreshWordList(layout);//刷新操作
        return layout;
    }

    private void initWordList() {
        if (wordLists != null)
            wordLists.clear();
        Cursor cursor = dbR.query("reviewlist", new String[]{ "word", "interpret"}, null, null, null, null,null);
        while (cursor.moveToNext()) {
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String interpret = cursor.getString(cursor.getColumnIndex("interpret"));
            wordLists.add(new WordList(word, interpret));
        }
        //wordLists = LitePal.select("word", "interpret")
         //       .find(WordList.class);//从数据库中查询单词本中已有的数据
    }

    private void refreshWordList(final View layout) {
        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.id_note_swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initWordList();//重新查询数据
                if (adapter != null)
                    adapter = null;
                adapter = new WordListAdapter(wordLists, MyApplication.getContext(), recyclerView);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);///刷新结束，进度条隐藏
            }
        });
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        dbHelper.close();;
        dbW.close();
        dbR.close();
    }

    class SelectedSpinListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            List<WordList> selectedWordLists = new ArrayList<>();
            switch (selected) {
                case "所有单词":
                    selectedWordLists = dbHelper.queryWordByGrasp(0, 10);
                    adapter = new WordListAdapter(selectedWordLists, context, recyclerView);
                    recyclerView.setAdapter(adapter);
                    break;
                case "陌生单词":
                    selectedWordLists = dbHelper.queryWordByGrasp(0, 3);
                    adapter = new WordListAdapter(selectedWordLists, context, recyclerView);
                    recyclerView.setAdapter(adapter);
                    break;
                case "模糊单词":
                    selectedWordLists = dbHelper.queryWordByGrasp(4, 6);
                    adapter = new WordListAdapter(selectedWordLists, context, recyclerView);
                    recyclerView.setAdapter(adapter);
                    break;
                case "熟悉单词":
                    selectedWordLists = dbHelper.queryWordByGrasp(7, 10);
                    adapter = new WordListAdapter(selectedWordLists, context, recyclerView);
                    recyclerView.setAdapter(adapter);
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class OrderSpinListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            List<WordList> selectedWordLists = new ArrayList<>();
            switch (selected) {
                case "日期":selectedWordLists = dbHelper.queryWordOrderByAddDate();
                    adapter = new WordListAdapter(selectedWordLists, context, recyclerView);
                    recyclerView.setAdapter(adapter);
                    break;
                case "字母":selectedWordLists = dbHelper.queryWordOrderByWord();
                    adapter = new WordListAdapter(selectedWordLists, context ,recyclerView);
                    recyclerView.setAdapter(adapter);
                    break;
                case "熟练度":selectedWordLists = dbHelper.queryWordOrderByGrasp();
                    adapter = new WordListAdapter(selectedWordLists, context, recyclerView);
                    recyclerView.setAdapter(adapter);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}
