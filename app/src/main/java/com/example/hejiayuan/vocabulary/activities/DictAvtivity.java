package com.example.hejiayuan.vocabulary.activities;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hejiayuan.vocabulary.databases.DataBaseHelperReview;
import com.example.hejiayuan.vocabulary.databases.WordList;
import com.example.hejiayuan.vocabulary.entities.Dict;
import com.example.hejiayuan.vocabulary.utils.Mp3Player;
import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.entities.WordValue;
import com.example.hejiayuan.vocabulary.utils.MyApplication;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class DictAvtivity extends AppCompatActivity {

    public TextView textDictWord = null;
    public TextView textDictPhonSymbolEng = null;
    public TextView textDictPhonSymbolUSA = null;
    public TextView textDictInterpret = null;
    public ListView listViewDictSentence = null;
    public ImageButton imageBtnDictAddToWordList = null;
    public ImageButton imageBtnDictHornEng = null;
    public ImageButton imageBtnDictHornUSA = null;
    public ImageButton imageBtnDictSearch = null;
    public ImageButton imageBtnDictBackToGeneral= null;
    public ImageButton imageBtnDictDeleteEditText = null;

//    public Button buttonDictDialogConfirm = null;
//    public Button buttonDictDialogCancel = null;

    public EditText editTextDictSearch = null;

    public Dict dict = null;

    public WordValue w = null;

    public DataBaseHelperReview dbReviewHelper = null;//需注意
    public SQLiteDatabase dbReviewR = null;
    public SQLiteDatabase dbReviewW = null;

//    public WordList wordList = null;

    public Mp3Player mp3Box = null;

    public static String searchedWord = null;

    public Handler dictHandler = null;

    public void initial() {
        textDictWord = (TextView) findViewById(R.id.text_dict_word);
        textDictInterpret = (TextView) findViewById(R.id.text_dict_interpret);
        textDictPhonSymbolEng = (TextView) findViewById(R.id.text_dict_phosymbol_eng);
        textDictPhonSymbolUSA = (TextView) findViewById(R.id.text_dict_phosymbol_usa);
        listViewDictSentence = (ListView) findViewById(R.id.listview_dict_sentence);

        imageBtnDictAddToWordList = (ImageButton) findViewById(R.id.image_btn_dict_add_to_wordlist);

        imageBtnDictBackToGeneral = (ImageButton) findViewById(R.id.image_btn_dict_back_to_general);

        imageBtnDictHornEng = (ImageButton) findViewById(R.id.image_btn_horn_accent_eng);
        imageBtnDictHornUSA = (ImageButton) findViewById(R.id.image_btn_horn_accent_usa);
        imageBtnDictSearch = (ImageButton) findViewById(R.id.image_btn_dict_search);
        imageBtnDictDeleteEditText = (ImageButton) findViewById(R.id.image_btn_dict_delete_all);


        editTextDictSearch = (EditText) findViewById(R.id.edit_text_dict);
        editTextDictSearch.setOnEditorActionListener(new EditTextDictEditActionLis());
        dict = new Dict(DictAvtivity.this, "dict");
//        wordList = new WordList();
        mp3Box = new Mp3Player(DictAvtivity.this, "dict");
        //需注意

        dbReviewHelper = new DataBaseHelperReview(DictAvtivity.this, "reviewlist");
        dbReviewR = dbReviewHelper.getReadableDatabase();
        dbReviewW = dbReviewHelper.getWritableDatabase();

        dictHandler = new Handler(Looper.getMainLooper());


    }

    /**
     * 该方法可能需要访问网络，因此放在线程里进行
     * @param word
     */
    public void searchWord(String word) {
        //首先初始化界面
        dictHandler.post(new RunnableDictSetTextInterface(searchedWord, "", "", "", null, null));

        dict = new Dict(MyApplication.getContext(), "dict");
        w = null;//对w进行初始化
        if (dict.isWordExist(word) == false) {//数据库中没有单词记录，从网络上同步
            if ((w = dict.getWordFromIntrenet(word)) == null || "".equals(w.getWord())) {
                return;
            }

            //错词不添加进词典
            dict.insertWordToDict(w, true);//默认添加到词典
        }//走到这一步说明从网上同步成功，数据库中一定存在单词记录
        w = dict.getWordFromDict(word);
        if (w == null) { //若是词典中还没有用空字符串代替
            w = new WordValue();
        }
        String searchStr = w.getWord();
        String phoSymEng = w.getPsE();
        String phoSymUSA = w.psA;
        String interpret = w.getInterpret();
//        ArrayList<String> sentList = w.getOrigList();
//        ArrayList<String> sentInChineseList = w.getTransList();
        String sentOrig = w.getSentOrig();
        String sentTrans = w.getSentTrans();
        dictHandler.post(new RunnableDictSetTextInterface(searchedWord,
                phoSymEng, phoSymUSA, interpret, sentOrig, sentTrans));
        if (phoSymEng.equals("") == false && phoSymUSA.equals("") == false) { //只有有音标时才去下载
            mp3Box.playMusicByWord(searchedWord, Mp3Player.ENGLISH_ACCENT, true, false);
            mp3Box.playMusicByWord(searchedWord, Mp3Player.USA_ACCENT, true, false);

        }

    }

    public void setOnClickLis() {
        imageBtnDictBackToGeneral.setOnClickListener(new IBDictBackToGeneralClickLis());
        imageBtnDictHornEng.setOnClickListener(new IBDictPlayMusicByAccentClickLis(Mp3Player.ENGLISH_ACCENT));
        imageBtnDictHornUSA.setOnClickListener(new IBDictPlayMusicByAccentClickLis(Mp3Player.USA_ACCENT));
        imageBtnDictAddToWordList.setOnClickListener(new IBDictAddWordToGlossaryClickLis());
        imageBtnDictDeleteEditText.setOnClickListener(new IBDictDeleteEditTextClickLis());
        imageBtnDictSearch.setOnClickListener(new IBDictSearchClickLis());
    }

    public void showAddDialog() {
        if (searchedWord == null)
            return;
        AlertDialog.Builder dialog = new AlertDialog.Builder(DictAvtivity.this);
        dialog.setIcon(R.mipmap.dialog);
        dialog.setTitle("添加");
        dialog.setMessage("确定把[" + searchedWord + "]添加到单词本么？");
        dialog.setPositiveButton("确定", new BDictDialogConfirmClickLis());
        dialog.setNegativeButton("取消", null);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    /**
     * 有问题存在，待改善
     */
    public void insertWordToGlossary() {
        if (w == null || w.getInterpret().equals("")) {
            Toast.makeText(DictAvtivity.this, "单词格式错误", Toast.LENGTH_SHORT).show();
            return;//若不是有效单词则不能添加单词本
        }
//        List<WordList> queryWords = LitePal.select("word")
//                .where("word = ?", searchedWord)
//                .find(WordList.class);
        Cursor cursor = dbReviewR.query("reviewlist", new String[] { "word "}, "word = ?", new String[]{searchedWord}, null, null, null);
       int isSuccess = cursor.getCount();
        if (isSuccess == 0) { // 判断若单词本中已有该单词就不再加入
            ContentValues values = new ContentValues();
            values.put("word", w.getWord());
            values.put("interpret", w.getInterpret());
            values.put("right", 0);
            values.put("wrong", 0);
            values.put("grasp", 0);
            values.put("learned", 0);
            dbReviewW.insert("reviewlist", null, values);
            Toast.makeText(DictAvtivity.this, "添加成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DictAvtivity.this, searchedWord + " 已存在", Toast.LENGTH_SHORT).show();
            return;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);

        initial();
        getIntentFromWordList();
        setOnClickLis();

        new ThreadDictSearchWordAndSetInterface().start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mp3Box.isMusicPermitted = true;
    }

    @Override
    protected void onPause() {
        mp3Box.isMusicPermitted = false;
        super.onPause();
    }

    public class ThreadDictSearchWordAndSetInterface extends Thread {
        @Override
        public void run() {
            super.run();
            searchWord(searchedWord);
        }
    }

    public class RunnableDictSetTextInterface implements Runnable{
        String searchStr = null;
        String phoSymEng = null;
        String phoSymUSA = null;
        String interpret = null;
//        ArrayList<String> sentList = null;
//        ArrayList<String> sentInChineseList = null;
        String sentOrig = null;
        String sentTrans = null;

        public RunnableDictSetTextInterface(String searchStr, String phoSymEng, String phoSymUSA, String interpret, String sentOrig, String sentTrans) {
            super();
            this.searchStr = searchStr;
            this.phoSymEng = "英[" + phoSymEng + "]";
            this.phoSymUSA = "美[" + phoSymUSA + "]";
            this.interpret = interpret;
            this.sentOrig = sentOrig;
            this.sentTrans = sentTrans;
        }

        @Override
        public void run() {
            if (searchStr == null) {
                textDictWord.setHint("请在此输入要查找的单词");
                return;
            } else {
                textDictWord.setText(searchStr);
                textDictPhonSymbolEng.setText(phoSymEng);
                textDictPhonSymbolUSA.setText(phoSymUSA);
                textDictInterpret.setText(interpret);
                if (sentOrig == null || sentTrans == null) {
                    return;
                }
                String sentorig[] = sentOrig.split("[\\. \\? \\!][\\n]");
                String senttrans[] = sentTrans.split("[\\.\\? \\!][\\n]");
                Log.d(MyApplication.getContext().toString(), sentorig.length + "英文长度");
                Log.d(MyApplication.getContext().toString(), senttrans.length + "中文长度");
                ArrayList<String> sentenceLists = new ArrayList<>();
                ArrayList<String> origLists = new ArrayList<String>();
                ArrayList<String> transLists = new ArrayList<String>();
                for (int i = 0; i < (sentorig.length >= senttrans.length ? senttrans.length : sentorig.length); i++) {
                    int index = i + 1;
                    origLists.add("( " + index + " )" + sentorig[i] + ".");
                    transLists.add(senttrans[i] + ".");
                    sentenceLists.add(origLists.get(i));
                    sentenceLists.add(transLists.get(i));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        MyApplication.getContext(), android.R.layout.simple_list_item_1, sentenceLists
                );
                listViewDictSentence.setAdapter(adapter);
            }
        }
    }

    class IBDictBackToGeneralClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            DictAvtivity.this.finish();
        }
    }

    class IBDictPlayMusicByAccentClickLis implements View.OnClickListener {

        public int accentTemp = 0;

        public IBDictPlayMusicByAccentClickLis(int accentTemp) {
            super();
            this.accentTemp = accentTemp;
        }

        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mp3Box.playMusicByWord(searchedWord, accentTemp, true, true);
                }
            }).start();
            //Log.d("DictActivity.this", "播放成功");
        }
    }

    class IBDictAddWordToGlossaryClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showAddDialog();
        }
    }

    class IBDictDeleteEditTextClickLis implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            editTextDictSearch.setText("");
        }
    }

    class BDictDialogConfirmClickLis implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d("DictActivity.this", "添加成功");
            insertWordToGlossary();
        }
    }

    class EditTextDictEditActionLis implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                startSearch();
                return true;
            }
            return false;
        }
    }

    class IBDictSearchClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            startSearch();
        }
    }

    public void startSearch() {
        String str = editTextDictSearch.getText().toString();
        if (str == null || str.equals(""))
            return;
        searchedWord = str;
        new ThreadDictSearchWordAndSetInterface().start();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextDictSearch.getWindowToken(), 0);
    }

    public void getIntentFromWordList() {
        //对searchedWord进行初始化
        Intent intent = this.getIntent();
        searchedWord = intent.getStringExtra("searchedword");
        if (searchedWord == null) {
            searchedWord = "";
        }
        //设置查找的文本
        textDictWord.setText(searchedWord);
    }
}
