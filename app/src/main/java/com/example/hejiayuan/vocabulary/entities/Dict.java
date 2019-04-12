package com.example.hejiayuan.vocabulary.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hejiayuan.vocabulary.databases.DataBaseHelperDict;
import com.example.hejiayuan.vocabulary.utils.ContentHandler;
import com.example.hejiayuan.vocabulary.network.NetOperator;
import com.example.hejiayuan.vocabulary.utils.MyApplication;
import com.example.hejiayuan.vocabulary.utils.XMLParser;

import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Dict {
    public Context context = null;
    public String tableName = null;
    private DataBaseHelperDict dbHelper = null;
    private SQLiteDatabase dbR = null, dbW = null;

    public Dict(Context context, String tableName) {
        this.context = context;
        this.tableName = tableName;
        dbHelper = new DataBaseHelperDict(context,tableName);
        //在Dict类的构造方法中实例化此类
        //并且调用下面两个方法获得dbR和dbW用于完成对数据库的增删改查操作
        //这里吧dbR dbW作为成员变量的目的是避免反复实例化，造成数据库指针泄露
        dbR = dbHelper.getReadableDatabase();
        dbW = dbHelper.getWritableDatabase();
    }

    /**
     * 对象销毁时释放资源
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        dbR.close();;
        dbW.close();
        dbHelper.close();
        super.finalize();
    }

    public void insertWordToDict(WordValue w, boolean isOverWrite) {
        if (w == null) { //避免空指针异常
            return;
        }
        Cursor cursor = null;
        try {
            ContentValues values = new ContentValues();
            values.put("word", w.getWord());
            values.put("pse", w.getPsE());
            values.put("prone", w.getPronE());
            values.put("psa", w.getPsA());
            values.put("prona", w.getPronA());
            values.put("interpret", w.getInterpret());
            values.put("sentorig", w.getSentOrig());
            values.put("senttrans", w.getSentTrans());
            cursor = dbR.query(tableName, new String[]{"word"}, "word=?",
                                new String[]{ w.getWord() }, null, null, null);
            if (cursor.getCount() > 0) {
                if (isOverWrite == false) {
                    return;//先看看数据库中有没有这个单词，若有不在操作
                } else {
                    dbW.update(tableName, values, "word=?", new String[] { w.getWord() });
                }
            } else {
                dbW.insert(tableName, null, values);
                //这里可能会发生空指针异常
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 判断是否存在某个单词
     * @param word
     * @return
     */
    public boolean isWordExist(String word) {
        Cursor cursor = null;
        try {
            cursor = dbR.query(tableName, new String[]{ "word" }, "word=?",
                    new String[]{ word }, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            } else {
                cursor.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    /**
     * 从单词库中获得某个单词的信息,如果没有返回null
     * @param searchedWord
     * @return
     */
    public WordValue getWordFromDict(String searchedWord) {
        WordValue w = null;

        String[] columus = new String[] { "word",
                "pse","prone","psa","prona","interpret","sentorig","senttrans"};
        String[] strArray = new String[8];
        Log.d(MyApplication.getContext().toString(), searchedWord + "1 ");
        Log.d(MyApplication.getContext().toString(), searchedWord + "1 " + tableName);
        Cursor cursor = dbR.query(tableName, columus, "word = ?",
                new String[]{searchedWord}, null,null, null);
        while(cursor.moveToNext()) {
            for (int i = 0; i < strArray.length; i++) {
                strArray[i] = cursor.getString(cursor.getColumnIndex(columus[i]));
            }
            w = new WordValue(strArray[0], strArray[1], strArray[2], strArray[3], strArray[4],
                    strArray[5], strArray[6],strArray[7]);
        }
        cursor.close();
        return w;
    }

    /**
     * 从网络上获得某个单词
     * @param searchedWord
     * @return
     */
    public WordValue getWordFromIntrenet(String searchedWord) {
        WordValue wordValue = null;
        String tempWord = searchedWord;
        if (tempWord == null && "".equals(tempWord))
            return null;

//        char[] array = tempWord.toCharArray();
//        if (array[0] > 256) {
//            tempWord = "_" + URLEncoder.encode(tempWord);
//        }
        InputStream in = null;
        //String str = null;
        try {
            String url = NetOperator.iCiBaURL + tempWord + NetOperator.API;
            in = NetOperator.getInputStreamByUrl(url);//从网络上获得输入流
            if (in != null) {
                XMLParser xmlParser = new XMLParser();
                InputStreamReader reader = new InputStreamReader(in, "utf-8");
                //最终目的获得一个InputSourxe对象用于传入形参
                ContentHandler contentHandler = new ContentHandler();
                xmlParser.parseXml(contentHandler, new InputSource(reader));
                wordValue = contentHandler.getWordValue();
                wordValue.setWord(searchedWord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wordValue;
    }

    /**
     * 获取发音文件地址
     * @param searchedWord
     * @return
     */
    public String getPronEngUrl(String searchedWord) {
       Cursor cursor = dbR.query(tableName, new String[]{ "prone"}, "word=?",
               new String[]{ searchedWord }, null,null, null);
       if (cursor.moveToNext() == false) {
           cursor.close();
           return null;
       }
       String str = cursor.getString(cursor.getColumnIndex("prone"));
       cursor.close();
       return str;
    }

    public String getPronUSAUrl(String searchedWord) {
        Cursor cursor = dbR.query(tableName, new String[]{ "prona"}, "word=?",
                new String[]{searchedWord}, null,null, null);
        if (cursor.moveToNext() == false) {
            cursor.close();
            return null;
        }
        String str = cursor.getString(cursor.getColumnIndex("prona"));
        cursor.close();
        return str;
    }

    //获取音标
    public String getPsEng(String searchedWord) {
        Cursor cursor = dbR.query(tableName, new String[]{ "psa"}, "word=?",
                new String[]{searchedWord}, null,null, null);
        if (cursor.moveToNext() == false) {
            cursor.close();
            return null;
        }
        String str = cursor.getString(cursor.getColumnIndex("psa"));
        cursor.close();
        return str;
    }

    //获取翻译
    public String getInterpret(String searchedWord) {
        Cursor cursor = dbR.query(tableName, new String[]{ "interpret"}, "word=?",
                new String[]{searchedWord}, null,null, null);
        if (cursor.moveToNext() == false) {
            cursor.close();
            return null;
        }
        String str = cursor.getString(cursor.getColumnIndex("interpret"));
        cursor.close();
        return str;
    }

}
