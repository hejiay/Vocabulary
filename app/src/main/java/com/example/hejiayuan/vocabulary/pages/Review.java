package com.example.hejiayuan.vocabulary.pages;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.core.WordBox;
import com.example.hejiayuan.vocabulary.databases.DataBaseHelperDict;
import com.example.hejiayuan.vocabulary.databases.DataBaseHelperReview;
import com.example.hejiayuan.vocabulary.entities.WordInfo;
import com.example.hejiayuan.vocabulary.utils.Mp3Player;
import com.example.hejiayuan.vocabulary.utils.MyApplication;

import java.util.ArrayList;

public class Review extends Fragment implements View.OnClickListener, TextWatcher {


    private android.support.v7.widget.Toolbar toolbar;

    private ImageButton imBtnBackToLast;

    private TextView textWord;

    private TextView textPs;
    private ImageButton imBtnPlay;

    private RelativeLayout relativeLayout = null;

    private Button btnChangeToWordImformation;

    private TextView textInterpret;

    private ListView listSentence;

    private Button btnRemember;
    private Button btnForget;

    private WordBox wordBox = null;
    private WordInfo wordInfo = null;

    String reviewWord;
    String psE;
    String psU;
    String interpret;
    String sentenceE;
    String sentenceC;
    String prone;
    String prona;

    public DataBaseHelperDict dbHelperDict = null;
    public SQLiteDatabase dbRDict = null;
    public SQLiteDatabase dbWDict = null;

    public DataBaseHelperReview dbHelperReview = null;
    public SQLiteDatabase dbRReview = null;
    public SQLiteDatabase dbWReview = null;

    public Mp3Player mp3Player = null;

    public ArrayList<WordInfo> learnedLists;

    private int arrayListPosition = 0;

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
        init();
        initWidget(layout);
        initWord();
        setOnClickLis(layout);
        return layout;
    }

    private void init() {
        wordBox = new WordBox(MyApplication.getContext(), "reviewlist");
        dbHelperDict = new DataBaseHelperDict(MyApplication.getContext(), "dict");
        dbRDict = dbHelperDict.getReadableDatabase();
        dbWDict = dbHelperDict.getWritableDatabase();
        dbHelperReview = new DataBaseHelperReview(MyApplication.getContext(), "reviewlist");
        dbRReview = dbHelperReview.getReadableDatabase();
        dbWReview = dbHelperReview.getWritableDatabase();
        mp3Player = new Mp3Player(MyApplication.getContext(), "dict");
        learnedLists = new ArrayList<>();
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

    @Override
    protected void finalize() throws Throwable {
        dbHelperDict.close();
        dbWReview.close();
        dbRReview.close();
        dbHelperReview.close();
        dbWReview.close();
        dbRReview.close();
        super.finalize();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.review_toolbar_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_word:
                dbWReview.delete("reviewlist", "word = ?", new String[]{ reviewWord + "" });
                Toast.makeText(MyApplication.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(View.INVISIBLE);
                btnChangeToWordImformation.setVisibility(View.VISIBLE);
                wordBox.feedBack(wordInfo, true);
                if (learnedLists.contains(wordInfo) == false) {
                    learnedLists.add(wordInfo);
                    Log.d(MyApplication.getContext().toString(), arrayListPosition + "记住位置");
                    arrayListPosition ++;
                }
                wordInfo = wordBox.popWord();
                if (wordInfo == null) {
                    Toast.makeText(MyApplication.getContext(), "复习列表中没有单词", Toast.LENGTH_SHORT).show();
                    return false;
                }
                wordInfoHandler(wordInfo);
                showContent();
                return true;
            case R.id.change_sound:
                if (textPs.getText().equals("英[" + psE + "]")) {
                    textPs.setText("美[" + psU + "]");
                } else {
                    textPs.setText("英[" + psE + "]");
                }
                return true;
                default:
                    return false;
        }
    }

    public void initWidget(View layout) {
        btnChangeToWordImformation = (Button) layout.findViewById(R.id.id_review_btn_change_wordinformation);
        relativeLayout = (RelativeLayout) layout.findViewById(R.id.id_review_layout_wordinformation);
        imBtnBackToLast = (ImageButton) layout.findViewById(R.id.id_review_toolbar_back);
        textWord = (TextView) layout.findViewById(R.id.id_review_text_word);
        textPs = (TextView) layout.findViewById(R.id.id_review_text_word_ps);
        imBtnPlay = (ImageButton) layout.findViewById(R.id.id_review_btn_player);
        textInterpret = (TextView) layout.findViewById(R.id.id_review_text_interpret);
        listSentence = (ListView) layout.findViewById(R.id.id_review_listview_sentence);
        btnRemember = (Button) layout.findViewById(R.id.id_review_btn_remember);
        btnForget = (Button) layout.findViewById(R.id.id_review_btn_forget);
    }

    public void setOnClickLis(View layout) {
        btnChangeToWordImformation.setOnClickListener(new ChangeToWordImformation());
        imBtnBackToLast.setOnClickListener(new ClickBackToLastBtn());
        imBtnPlay.setOnClickListener(new ClickPlyerBtn());
        btnRemember.setOnClickListener(new ClickRememberBtn(layout));
        btnForget.setOnClickListener(new ClickForgetBtn(layout));
    }

    class ChangeToWordImformation implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            relativeLayout.setVisibility(View.VISIBLE);
            btnChangeToWordImformation.setVisibility(View.GONE);
        }
    }

    class ClickBackToLastBtn implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (arrayListPosition == 0) {
                Toast.makeText(MyApplication.getContext(), "已经到达今天学习的第一个单词了哟！", Toast.LENGTH_SHORT).show();
                return;
            }
            arrayListPosition --;
            WordInfo wordInfo = learnedLists.get(arrayListPosition);
            wordInfoHandler(wordInfo);
            showContent();
        }
    }

    class ClickPlyerBtn implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (textPs.getText().equals("英[" + psE + "]")) {
                        mp3Player.playMusicByWord(reviewWord, Mp3Player.ENGLISH_ACCENT, true, true);
                    } else {
                        mp3Player.playMusicByWord(reviewWord, Mp3Player.USA_ACCENT, true, true);
                    }
                }
            }).start();
        }
    }

    class ClickRememberBtn implements View.OnClickListener {
        private View layout;

        public ClickRememberBtn(View layout) {
            this.layout = layout;
        }
        @Override
        public void onClick(View v) {
            relativeLayout.setVisibility(View.INVISIBLE);
            btnChangeToWordImformation.setVisibility(View.VISIBLE);
            wordBox.feedBack(wordInfo, true);
            if (learnedLists.contains(wordInfo) == false) {
                learnedLists.add(wordInfo);
                Log.d(MyApplication.getContext().toString(), arrayListPosition + "记住位置");
                arrayListPosition ++;
            }
            wordInfo = wordBox.popWord();
            if (wordInfo == null) {
                Toast.makeText(MyApplication.getContext(), "复习列表中没有单词", Toast.LENGTH_SHORT).show();
                return;
            }
            wordInfoHandler(wordInfo);
            showContent();
        }
    }


    class ClickForgetBtn implements View.OnClickListener {

        private View layout;

        public ClickForgetBtn(View layout) {
            this.layout = layout;
        }
        @Override
        public void onClick(View v) {
            relativeLayout.setVisibility(View.INVISIBLE);
            btnChangeToWordImformation.setVisibility(View.VISIBLE);
            wordBox.feedBack(wordInfo, false);
            if (learnedLists.contains(wordInfo) == false) {
                learnedLists.add(wordInfo);
                Log.d(MyApplication.getContext().toString(), arrayListPosition + "错误位置");
                arrayListPosition ++;
            }
            wordInfo = wordBox.popWord();
            if (wordInfo == null) {
                Toast.makeText(MyApplication.getContext(), "复习列表中没有单词", Toast.LENGTH_SHORT).show();
                return;
            }
            wordInfoHandler(wordInfo);
            showContent();
        }
    }

    public void wordInfoHandler(WordInfo wordInfo) {
        if (wordInfo == null) {
            return;
        } else {
            reviewWord = wordInfo.getWord();
            Cursor cursor = dbWDict.query("dict", null, "word = ?", new String[]
                    { reviewWord }, null, null, null);
            if (cursor.moveToNext()) {
                psE = cursor.getString(cursor.getColumnIndex("pse"));
                psU = cursor.getString(cursor.getColumnIndex("psa"));
                prona = cursor.getString(cursor.getColumnIndex("prona"));
                prone = cursor.getString(cursor.getColumnIndex("prone"));
                interpret = cursor.getString(cursor.getColumnIndex("interpret"));
                sentenceE = cursor.getString(cursor.getColumnIndex("sentorig"));
                sentenceC = cursor.getString(cursor.getColumnIndex("senttrans"));
            } else {
                        return;
            }

        }
    }

    public void showContent() {
        textWord.setText(reviewWord);
        textPs.setText("英[" + psE + "]");
        textInterpret.setText(interpret);
        handleSentence();
    }


    public void handleSentence() {
        if (sentenceE == null || sentenceC == null) {
            return;
        }
        String sentorig[] = sentenceE.split("[\\. \\? \\!][\\n]");
        String senttrans[] = sentenceC.split("[\\.\\? \\!][\\n]");
        Log.d(MyApplication.getContext().toString(), sentorig.length + "英文长度");
        Log.d(MyApplication.getContext().toString(), senttrans.length + "中文长度");
        ArrayList<String> sentenceLists = new ArrayList<>();
        ArrayList<String> origLists = new ArrayList<String>();
        ArrayList<String> transLists = new ArrayList<String>();
        for (int i=0; i<(sentorig.length >= senttrans.length ? senttrans.length : sentorig.length); i++) {
            int index = i + 1;
            origLists.add("( " + index + " )" + sentorig[i] + ".");
            transLists.add( senttrans[i] + ".");
            sentenceLists.add(origLists.get(i));
            sentenceLists.add(transLists.get(i));
    }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MyApplication.getContext(), android.R.layout.simple_list_item_1, sentenceLists
        );
        listSentence.setAdapter(adapter);
    }

    public void initWord() {
        WordInfo wordInfo = wordBox.getWordByGraspByRandom(8, 10, 1);
        wordInfoHandler(wordInfo);
        showContent();
    }
}
