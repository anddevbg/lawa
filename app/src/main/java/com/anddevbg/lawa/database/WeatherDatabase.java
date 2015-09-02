package com.anddevbg.lawa.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by adri.stanchev on 04/08/2015.
 */
public class WeatherDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather_databse";
    private static final String TABLE_NAME = "weather_table";
    private static final int DATABASE_VERSION = 5;

    private static final String CREATE_TABLE = "CREATE TABLE " +TABLE_NAME+ " (LATITUDE VARCHAR(20), LONGITUDE VARCHAR(20));";

    public WeatherDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("db", "weather database constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE);
            Log.d("db", "db created" + sqLiteDatabase.toString());
        } catch (SQLiteException x) {
            x.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d("db", "onUpgrade");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }


}
