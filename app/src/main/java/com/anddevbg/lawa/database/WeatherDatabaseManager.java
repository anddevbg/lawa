package com.anddevbg.lawa.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.anddevbg.lawa.model.WeatherData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adri.stanchev on 03/09/2015.
 */
public class WeatherDatabaseManager {

    private static final String TABLE_NAME = "city_information_table1";
    private static final String DATABASE_NAME = "city_database";
    private static final int DATABASE_VERSION = 5;
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String LOCATION_NAME = "NAME";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + LOCATION_NAME + " VARCHAR(20), "
            + LATITUDE + " VARCHAR(20), " + LONGITUDE + " VARCHAR(20));";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static WeatherDatabaseManager sInstance;
    private SQLiteDatabase mSQLiteDatabase;
    private WeatherSQLiteOpenHelper mSQLiteHelper;

    private WeatherDatabaseManager(Context context) {
        mSQLiteHelper = new WeatherSQLiteOpenHelper(context);
        mSQLiteDatabase = getDatabase();
    }

    public static WeatherDatabaseManager getInstance() {
        return sInstance;
    }

    public SQLiteDatabase getDatabase() {
        return mSQLiteHelper.getWritableDatabase();
    }

    /**
     * Should be called from the Application's onCreate method.
     *
     * @param context
     */
    public static void initialize(Context context) {
        sInstance = new WeatherDatabaseManager(context);
    }

    public void insertData(String locationName, double latitude, double longitude) {
        Log.d("db", "inserting into WeatherDatabaseManager");
        String INSERT = "INSERT INTO " + TABLE_NAME + " VALUES('" + locationName + "'," + latitude + "," + longitude + ");";
        Log.d("asd", "insert: " + INSERT);
        mSQLiteDatabase.execSQL(INSERT);
    }

    public void deleteData(String locationName) {
        String DELETE = "DELETE FROM " +TABLE_NAME+ " WHERE " +LOCATION_NAME+ " ='" +locationName+
                "';";
        mSQLiteDatabase.execSQL(DELETE);
        Log.d("asd", "deleting from database:  " + locationName);
    }

    public List<WeatherData> showAll() {
        List<WeatherData> list = new ArrayList<>();
        String SHOWALL = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = mSQLiteDatabase.rawQuery(SHOWALL, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            WeatherData weatherData = new WeatherData();
            weatherData.setCityName(cursor.getString(0));
            weatherData.setLatitude(cursor.getDouble(1));
            weatherData.setLongitude(cursor.getDouble(2));
            list.add(weatherData);
        }
        return list;
    }

    private static class WeatherSQLiteOpenHelper extends SQLiteOpenHelper {

        public WeatherSQLiteOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_TABLE);
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }
    }

}
