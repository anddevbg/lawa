package com.anddevbg.lawa.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * Created by adri.stanchev on 04/08/2015.
 */
//public class CityTable implements IDatabaseTable {
//
////    private static final String TABLE_NAME = "city_table";
////
////    private static final int DATABASE_VERSION = 15;
////    private static final String LATITUDE = "LATITUDE";
////    private static final String LONGITUDE = "LONGITUDE";
////    private static final String CITY_ID = "CITY_ID";
//    private WeatherDatabaseManager manager;
//
//    private IDatabaseTable[] mTables;
//
//    public CityTable(Context context) {
//        // TODO create tables
//        mTables = new CityTable[2];
//        Log.d("db", "weather table constructor");
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        for (IDatabaseTable table : mTables) {
//            table.onCreate(sqLiteDatabase);
//        }
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase database) {
//        for (IDatabaseTable table : mTables) {
//            table.onUpgrade(database);
//            table.onCreate(database);
//        }
//    }
//
//}
