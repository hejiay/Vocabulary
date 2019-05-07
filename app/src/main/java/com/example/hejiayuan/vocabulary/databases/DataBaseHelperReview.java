package com.example.hejiayuan.vocabulary.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
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

}
