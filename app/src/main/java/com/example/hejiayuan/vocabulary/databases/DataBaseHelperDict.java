package com.example.hejiayuan.vocabulary.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class
DataBaseHelperDict extends SQLiteOpenHelper {

    public Context mContext = null;
    public String tableName = null;
    public static int VERSION = 1;

    String SQL = "create table dict(" +
            "word text, " + //单词
            "pse text," + //英式发音
            "prone text," + //英式发音地址
            "psa text," + //美式音标
            "prona text," + //美式发音地址
            "interpret text," + //翻译
            "sentorig text," + //例句英文
            "senttrans text)";//例句中文

    public DataBaseHelperDict(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
        tableName = name;
    }

    public DataBaseHelperDict(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory, VERSION);
        mContext = context;
        tableName = name;
    }

    public DataBaseHelperDict(Context context, String name) {
        this(context, name, null);
        mContext = context;
        tableName = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
