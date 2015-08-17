package com.anddevbg.lawa.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adri.stanchev on 04/08/2015.
 */
public class WeatherDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather_databse";
    private static final String TABLE_NAME = "weather_table";
    private static final String CITY_NAME = "city_name";
    private static final int DATABASE_VERSION = 1;
    private static final String UID = "_id";
    private double mCityLongitude;
    private double mCityLatitude;

    private static final String CREATE_TABLE = "CREATE TABLE " +TABLE_NAME+ " (" +UID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CITY_NAME + " VARCHAR(20));";


    public WeatherDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        } catch (SQLiteException x) {
            x.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }
}
