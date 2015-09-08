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

    private static final String TABLE_NAME = "city_information_table";
    private static final String DATABASE_NAME = "city_database";
    private static final int DATABASE_VERSION = 3;
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String CITY_ID = "CITY_ID";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + CITY_ID + " VARCHAR(10), "
            + LATITUDE + " VARCHAR(20), " + LONGITUDE + " VARCHAR(20));";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static WeatherDatabaseManager sInstance;
    //    private CityTable cityTable;
    private SQLiteDatabase mSQLiteDatabase;
    private WeatherSQLiteOpenHelper mSQLiteHelper;
//    private IDatabaseTable[] mTables;

    private WeatherDatabaseManager(Context context) {
        mSQLiteHelper = new WeatherSQLiteOpenHelper(context);
        mSQLiteDatabase = getDatabase();
//        cityTable = new CityTable(context);
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

    public void insertData(int id, double latitude, double longitude) {
        Log.d("db", "inserting into WeatherDatabaseManager");
        String INSERT = "INSERT INTO " + TABLE_NAME + " VALUES(" + id + "," + latitude + "," + longitude + ");";
        mSQLiteDatabase.execSQL(INSERT);
    }

//    public void deleteTable() {
//        mSqLiteDatabase.execSQL("DELETE * FROM " + TABLE_NAME);
//    }

    public void deleteData(int id) {
        String DELETE = "DELETE FROM" + TABLE_NAME + " WHERE " + CITY_ID + "= " + id + ";";
        mSQLiteDatabase.execSQL(DELETE);
    }

    public List<WeatherData> showAll() {
        List<WeatherData> list = new ArrayList<>();
        String SHOWALL = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = mSQLiteDatabase.rawQuery(SHOWALL, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            WeatherData weatherData = new WeatherData();
            weatherData.setId(cursor.getInt(0));
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
            Log.d("asd", "onCreate table in WeatherDatabaseManager");
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.d("asd", "onUpgrade table in WeatherDatabaseManager");
            sqLiteDatabase.execSQL(DROP_TABLE);
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }
    }


}
