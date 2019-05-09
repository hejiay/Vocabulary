package com.example.hejiayuan.vocabulary.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelperReview extends SQLiteOpenHelper {

    public Context mContext = null;
    public String tableName = null;
    public static int VERSION = 1;

    public SQLiteDatabase dbR = null;
    public SQLiteDatabase dbW = null;

    public DataBaseHelperReview(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
        this.tableName = name;
        dbR = this.getReadableDatabase();
        dbW = this.getWritableDatabase();
    }

    public DataBaseHelperReview(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        this(context, name, factory, VERSION);
        mContext = context;
        tableName = name;
    }

    public DataBaseHelperReview(Context context, String name) {
        this(context, name, null);
        mContext = context;
        tableName = name;
    }

    public boolean insertWordInfoToDataBase(String word, String intrepret, boolean overWrite) {
        Cursor cursor = null;

        cursor = dbR.query(tableName, new String[]{ "word" }, "word = ?", new String[]{ word }, null, null, "word");

        if (cursor.moveToNext()) {
            if (overWrite) {
                ContentValues values = new ContentValues();
                values.put("interpret", intrepret);
                values.put("right", 0);
                values.put("wrong", 0);
                values.put("grasp", 0);
                values.put("learned", 0);

                dbW.update(tableName, values, "word = ?", new String[] { word });
                cursor.close();
                return true;
            } else {
                cursor.close();
                return false;
            }
        } else {
            ContentValues values = new ContentValues();
            values.put("interpret", intrepret);
            values.put("right", 0);
            values.put("wrong", 0);
            values.put("grasp", 0);
            values.put("learned", 0);

            dbW.insert(tableName, null, values);
            cursor.close();
            return true;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table reviewlist(word text,interpret text," +
                "right int,wrong int,grasp int,learned int, addDate timestamp not null default (datetime('now','localtime'))," +
                "lastLearntime INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<WordList> queryWordByGrasp(int fromGrasp, int toGrasp) {
        List<WordList> wordLists = new ArrayList<>();
        Cursor cursor = dbR.query(tableName, new String[]{ "word", "interpret" }, "grasp >= ? and grasp <= ?",
                new String[]{ String.valueOf(fromGrasp), String.valueOf(toGrasp)}, null, null, null);
        while (cursor.moveToNext()) {
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String interpret = cursor.getString(cursor.getColumnIndex("interpret"));
            wordLists.add(new WordList(word, interpret));
        }
        return wordLists;
    }

    public List<WordList> queryWordOrderByAddDate() {
        List<WordList> wordLists = new ArrayList<>();
        Cursor cursor = dbR.query(tableName, new String[]{ "word", "interpret" }, null, null, null, null, "addDate");
        while (cursor.moveToNext()) {
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String interpret = cursor.getString(cursor.getColumnIndex("interpret"));
            wordLists.add(new WordList(word, interpret));
        }
        return wordLists;
    }

    public List<WordList> queryWordOrderByGrasp() {
        List<WordList> wordLists = new ArrayList<>();
        Cursor cursor = dbR.query(tableName, new String[]{ "word", "interpret" }, null, null, null,
                null, "grasp desc" );
        while (cursor.moveToNext()) {
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String interpret = cursor.getString(cursor.getColumnIndex("interpret"));
            wordLists.add(new WordList(word, interpret));
        }
        return wordLists;
    }

    public List<WordList> queryWordOrderByWord() {
        List<WordList> wordLists = new ArrayList<>();
        Cursor cursor = dbR.query(tableName, new String[]{ "word", "interpret" }, null, null, null, null, "word");
        while (cursor.moveToNext()) {
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String interpret = cursor.getString(cursor.getColumnIndex("interpret"));
            wordLists.add(new WordList(word, interpret));
        }
        return wordLists;
    }

    public void updateGrasp(String word, int graspInt) {
        ContentValues values = new ContentValues();
        values.put("grasp", graspInt);
        dbW.update(tableName, values, "word = ?", new String[]{ word });
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        dbW.close();
        dbR.close();
    }
}
