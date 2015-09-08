package com.anddevbg.lawa.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by adri.stanchev on 03/09/2015.
 */
public interface IDatabaseTable {

    void onCreate(SQLiteDatabase database);

    void onUpgrade(SQLiteDatabase database);
}
