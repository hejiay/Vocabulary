package com.example.hejiayuan.vocabulary.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hejiayuan.vocabulary.databases.DataBaseHelperDict;
import com.example.hejiayuan.vocabulary.entities.Dict;
import com.example.hejiayuan.vocabulary.adapters.DictSentenceListAdapter;
import com.example.hejiayuan.vocabulary.utils.Mp3Player;
import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.entities.WordValue;

import java.util.ArrayList;
import java.util.HashMap;

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

    public Button buttonDictDialogConfirm = null;
    public Button buttonDictDialogCancel = null;

    public EditText editTextDictSearch = null;

    public Dict dict = null;

    public WordValue w = null;

    public DataBaseHelperDict dbGlossaryHelper = null;//需注意
    public SQLiteDatabase dbGlossaryR = null;
    public SQLiteDatabase dbGlossaryW = null;

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
        mp3Box = new Mp3Player(DictAvtivity.this, "dict");
        //需注意
        dbGlossaryHelper = new DataBaseHelperDict(DictAvtivity.this, "glossary");
        dbGlossaryR = dbGlossaryHelper.getReadableDatabase();
        dbGlossaryW = dbGlossaryHelper.getWritableDatabase();

        dictHandler = new Handler(Looper.getMainLooper());

        //对searchedWord进行初始化
        Intent intent = this.getIntent();
        searchedWord = intent.getStringExtra("word");
        if (searchedWord == null) {
            searchedWord = null;
        }
        //设置查找的文本
        textDictWord.setText(searchedWord);
    }

    /**
     * 该方法可能需要访问网络，因此放在线程里进行
     * @param word
     */
    public void searchWord(String word) {
        //首先初始化界面
        dictHandler.post(new RunnableDictSetTextInterface(searchedWord, "", "", "", null, null));

        w = null;//对w进行初始化
        if (dict.isWordExist(word) == false) {//数据库中没有单词记录，从网络上同步
            if ((w = dict.getWordFromIntrenet(word)) == null || "".equals(w.getWord())) {
                return;
            }

            //错词不添加进词典
            dict.insertWordToDict(w, true);//默认添加到词典
        }//走到这一步说明从网上同步成功，数据库中一定存在单词记录
        w = dict.getWordFromIntrenet(word);
        if (w == null) { //若是词典中还没有用空字符串代替
            w = new WordValue();
        }
        String searchStr = w.getWord();
        String phoSymEng = w.getPsE();
        String phoSymUSA = w.psA;
        String interpret = w.getInterpret();
        ArrayList<String> sentList = w.getOrigList();
        ArrayList<String> sentInChineseList = w.getTransList();
        dictHandler.post(new RunnableDictSetTextInterface(searchedWord,
                phoSymEng, phoSymUSA, interpret, sentList, sentInChineseList));
        if (phoSymEng.equals("") == false && phoSymUSA.equals("") == false) { //只有有音标时才去下载
            mp3Box.playMusicByWord(searchedWord, Mp3Player.ENGLISH_ACCENT, true, false);
            mp3Box.playMusicByWord(searchedWord, Mp3Player.USA_ACCENT, true, false);

        }

    }

    public void setOnClickLis() {
        imageBtnDictBackToGeneral.setOnClickListener(new IBDictBackToGeneralClickLis());
        imageBtnDictHornEng.setOnClickListener(new IBDictPlayMusicByAccentClickLis(Mp3Player.ENGLISH_ACCENT));
        imageBtnDictHornUSA.setOnClickListener(new IBDictPlayMusicByAccentClickLis(Mp3Player.USA_ACCENT));
        // imageBtnDictAddToWordList.setOnClickListener(new IBDictAddWordToGlossaryClickLis());
        imageBtnDictDeleteEditText.setOnClickListener(new IBDictDeleteEditTextClickLis());
        imageBtnDictSearch.setOnClickListener(new IBDictSearchClickLis());
    }

//    public void showAddDialog() {
//        if (searchedWord == null)
//            return;
//        AlertDialog dialog = new AlertDialog.Builder(DictAvtivity.this).create();
//        dialog.show();
//        Window window = dialog.getWindow();
//        //window.setContentView(R.layout.dia);
//    }

    /**
     * 有问题存在，待改善
     */
    public void insertWordToGlossary() {
        if (w == null || w.getInterpret().equals("")) {
            Toast.makeText(DictAvtivity.this, "单词格式错误", Toast.LENGTH_SHORT).show();
            return;//若不是有效单词则不能添加单词本
        }
        //boolean isSuccess = dbGlossaryHelper.
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);

        initial();
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
        ArrayList<String> sentList = null;
        ArrayList<String> sentInChineseList = null;

        public RunnableDictSetTextInterface(String searchStr, String phoSymEng, String phoSymUSA, String interpret, ArrayList<String> sentList, ArrayList<String> sentInChineseList) {
            super();
            this.searchStr = searchStr;
            this.phoSymEng = "英[" + phoSymEng + "]";
            this.phoSymUSA = "美[" + phoSymUSA + "]";
            this.interpret = interpret;
            this.sentList = sentList;
            this.sentInChineseList = sentInChineseList;
        }

        @Override
        public void run() {
            textDictWord.setText(searchStr);
            textDictPhonSymbolEng.setText(phoSymEng);
            textDictPhonSymbolUSA.setText(phoSymUSA);
            textDictInterpret.setText(interpret);
            if (sentList == null || sentInChineseList == null) {
                return;
            }
            int count = 0;
            if (sentList.size() <= sentInChineseList.size()) {
                count = sentList.size();
            } else {
                count = sentInChineseList.size();//保护机制，这里取两者长度最小者，一般相等
            }
            ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            for (int i=0; i<count; i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("sentence", sentList.get(i) + "\n" + sentInChineseList);
                list.add(map);
            }
            DictSentenceListAdapter adapter=new DictSentenceListAdapter(DictAvtivity.this, R.layout.dict_sentence_list_item,
                    list, new String[]{"sentence"}, new int[]{R.id.text_dict_sentence_list_item});
            listViewDictSentence.setAdapter(adapter);
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

    class IBDictAddWordToGlossaryClickLi implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    class IBDictDeleteEditTextClickLis implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            editTextDictSearch.setText("");
        }
    }

    class BDictDialogConfirmClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    class BDictDialogCancelClickLis implements View.OnClickListener {
        @Override
        public void onClick(View v) {

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
}