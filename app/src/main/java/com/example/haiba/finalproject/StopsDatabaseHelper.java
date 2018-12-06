package com.example.haiba.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StopsDatabaseHelper extends SQLiteOpenHelper {

    protected  static String ACTIVITY_NAME = "stopsDatabaseHelper";

    public static final String DATABASE_NAME = "stops.db";
    public static final int VERSION_NUM = 18;
    public static final String KEY_ID = "ID";
    public static final String KEY_STOP = "stops";
    public static final String TABLE_NAME = "stopsTable";
    public static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_STOP + " TEXT not null);";

    public StopsDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i("stopsDatabaseHelper","Calling onUpgrade,oldVer="+oldVersion+"newVersion="+newVersion);
    }
}
