package com.yanyining.redrockexamine.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YanYiNing on 2017/5/21.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String CREATE_DATA = "create table Data("
            + "love integer, "
            + "hate integer, "
            + "texts text, "
            + "weixin_url text, "
            + "video_uri text, "
            + "name text, "
            + "profile_image text, "
            + "id integer, "
            + "create_time text)";

    public static final String CREATE_HISTORY = "create table History("
            + "love integer, "
            + "hate integer, "
            + "texts text, "
            + "weixin_url text, "
            + "video_uri text, "
            + "name text, "
            + "profile_image text, "
            + "id integer, "
            + "progress integer, "
            + "create_time text)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATA);
        db.execSQL(CREATE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
