package com.example.rafae.promoz_001_alfa.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import com.example.rafae.promoz_001_alfa.R;
import com.example.rafae.promoz_001_alfa.dao.db.AppDatabase;
import com.example.rafae.promoz_001_alfa.dao.db.PromozContract;
import com.example.rafae.promoz_001_alfa.model.TempAdvertising;
import com.example.rafae.promoz_001_alfa.util.MessageDialogs;
import com.example.rafae.promoz_001_alfa.util.Util;

/**
 * Created by vallux on 08/03/17.
 */

public class TempAdvertisingDAO extends PromozContract.TempAdvertising {

    private AppDatabase dbHelper;
    private SQLiteDatabase database;
    private Context context;
    private Cursor cursor;

    public TempAdvertisingDAO(Context context) {
        this.context = context;
        dbHelper = new AppDatabase(context);
        database = dbHelper.getDatabase();
    }

    private TempAdvertising populate(Cursor cursor) { // Popula o objeto "TempAdvertising" com os dados do cursor
        TempAdvertising model = new TempAdvertising(
                this.context,
                cursor.getInt(cursor.getColumnIndex(_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TMP_ADD_IMG_URL)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_TMP_ADD_QTD_COIN)),
                cursor.getDouble(cursor.getColumnIndex(COLUMN_TMP_ADD_LAT)),
                cursor.getDouble(cursor.getColumnIndex(COLUMN_TMP_ADD_LONG)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_TMP_ADD_REGION_ID))
        );

        model.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_TMP_ADD_IMG))); // TODO: modificar classe para aceitar parâmetro IMAGE

        return model;
    }

    public long save(TempAdvertising tempAdvertising){ // salva os campos do objeto na tabela - atualiza ou cria um novo caso não exista

        long result = Util.Constants.ERROR_BD;

        ContentValues values = new ContentValues();
        values.put(_ID, tempAdvertising.get_id());
        values.put(COLUMN_TMP_ADD_IMG_URL, tempAdvertising.getImageURL());
        values.put(COLUMN_TMP_ADD_QTD_COIN, tempAdvertising.getQtdCoin());
        values.put(COLUMN_TMP_ADD_LAT, tempAdvertising.getLat());
        values.put(COLUMN_TMP_ADD_LONG, tempAdvertising.getLng());
        values.put(COLUMN_TMP_ADD_REGION_ID, tempAdvertising.getRegId());


        try {
            /*if(tempAdvertising.get_id() != null) {
                result = database.update(TABLE_NAME, values, _ID + " = ?", new String[]{tempAdvertising.get_id().toString()});
                Log.e("SAVE", "ATUALIZOU");
            }else{*/
                result = database.insert(TABLE_NAME, null, values);
            //}
        } catch (Exception ex){
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_funny_db), ex);
        }
        return result;
    }

    public long saveImageById(byte[] img, Integer id) {

        long result = Util.Constants.ERROR_BD;
        ContentValues values = new ContentValues();
        values.put(COLUMN_TMP_ADD_IMG, img);
        try {
            result = database.update(TABLE_NAME, values, _ID + " = ?", new String[]{id.toString()});
        } catch (Exception ex) {
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_funny_db), ex);
        }
        return result;
    }

    public List<TempAdvertising> list() {
        List<TempAdvertising> lst = new ArrayList<TempAdvertising> ();

        try {
            cursor = database.query(TABLE_NAME, allFields, null, null, null, null, null);
            if (cursor.moveToFirst())
                do {
                    lst.add(populate(cursor));
                }while (cursor.moveToNext());

        } catch (Exception ex){
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_save_or_update), ex);
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return lst;
    }

    public boolean isAdvertisingIdAdded(Integer advertisingId) {

        String selection = _ID + " = ? ";
        String[] selectionArgs = { String.valueOf(advertisingId)};
        Cursor cursor = database.query(TABLE_NAME, new String[]{_ID}, selection, selectionArgs, null, null, null);
        Integer qtd = cursor.getCount();
        cursor.close();

        return qtd != 0;
    }

    public List<TempAdvertising> listByRegionId(Integer regionId) {
        List<TempAdvertising> lst = new ArrayList<TempAdvertising> ();

        try {
            cursor = database.query(TABLE_NAME, allFields, COLUMN_TMP_ADD_REGION_ID + " = ? ", new String[]{regionId.toString()}, null, null, null);

            if (cursor.moveToFirst())
                do {
                    lst.add(populate(cursor));
                }while (cursor.moveToNext());

        } catch (Exception ex){
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_save_or_update), ex);
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return lst;
    }

    /*public TempAdvertising addvertisingById(Integer addId) {
        TempAdvertising result = null;

        try {
            cursor = database.query(TABLE_NAME, allFields, " _id = ? ", new String[]{addId.toString()}, null, null, null);
            Log.e("BYID","COUNT = " + cursor.getCount());
            if(cursor.moveToFirst())
                result = populate(cursor);

        } catch (Exception ex){
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db),  context.getString(R.string.error_funny_db), ex);
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return result;
    }*/

    public boolean remove(int id){
        return database.delete(TABLE_NAME, _ID + " = ?", new String[]{ Integer.toString(id) }) > 0;
    }

    public void closeDataBase(){
        if(database.isOpen()) database.close();
    }
}