package com.example.rafae.promoz_001_alfa.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rafae.promoz_001_alfa.R;
import com.example.rafae.promoz_001_alfa.dao.db.AppDatabase;
import com.example.rafae.promoz_001_alfa.dao.db.PromozContract;
import com.example.rafae.promoz_001_alfa.model.VirtualStore;
import com.example.rafae.promoz_001_alfa.util.MessageDialogs;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by vallux on 26/01/17.
 */

public class VirtualStoreDAO extends PromozContract.VirtualStore {

    private AppDatabase dbHelper;
    private SQLiteDatabase database;
    private Context context;
    private Cursor cursor;

    public VirtualStoreDAO(Context context) {
        this.context = context;
        dbHelper = new AppDatabase(context);
        database = dbHelper.getDatabase();
    }

    private VirtualStore populate(){ // Popula o objeto "VirtualStore" com os dados do cursor
        VirtualStore model = new VirtualStore(
                cursor.getInt(cursor.getColumnIndex(_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_VRT_STR_TITLE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_VRT_STR_INFO)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_VRT_STR_IMG)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_VRT_STR_PRICE)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_VRT_STR_STR_ID)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_VRT_STR_IND_VALID))
        );
        return model;
    }

    public List<VirtualStore> list(){

        List<VirtualStore> lst = new ArrayList<VirtualStore>();

        try {
            cursor = database.query(TABLE_NAME, allFields, null, null, null, null, null);

            while (cursor.moveToNext())
                lst.add(populate());

        } catch (Exception ex){
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_funny_db), ex);
        } finally {
            cursor.close();
        }

        return lst;
    }

    //TODO never used
    /*public void closeDatabase(){

        dbHelper.closeConnection();

        if(database.isOpen())
            database.close();
    }*/

    //TODO never used
    /*public boolean remove(int id){
        return database.delete(TABLE_NAME, "_id = ?", new String[]{ Integer.toString(id) }) > 0;
    }*/

    public void closeDataBase(){
        if(database.isOpen()) database.close();
    }
}
