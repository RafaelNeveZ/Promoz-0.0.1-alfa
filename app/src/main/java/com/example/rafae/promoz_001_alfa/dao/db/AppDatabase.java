package com.example.rafae.promoz_001_alfa.dao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vallux on 28/02/17.
 */

public class AppDatabase {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public AppDatabase(Context context) {
        this.dbHelper = new DatabaseHelper(context);
        this.database = dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public class DatabaseHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "promoz.db";

        public DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            //     Log.v("SQL", "\n\nCRIANDO DB\n\n");

            for(int i = 0; i < PromozContract.tablesCreationList.length; i++) {
                //     Log.e("CREATE", PromozContract.tablesCreationList[i]);
                db.execSQL(PromozContract.tablesCreationList[i]);
            }

            //Log.v("SQL", "\n\nPOPULANDO TABELAS\n\n");

            for(int i = 0; i < PromozContract.valuesToPopulate.length; i++) {
                //   Log.e("INSERT", PromozContract.valuesToPopulate[i]);
                db.execSQL(PromozContract.valuesToPopulate[i]);
            }
            //Log.e("TRIGGER", PromozContract.Triger.TRIGER_WALLET_BALANCE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //TODO onUpgrade
        }
    }
}
