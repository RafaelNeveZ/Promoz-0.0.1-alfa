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
import com.example.rafae.promoz_001_alfa.model.HistoricCoin;
import com.example.rafae.promoz_001_alfa.util.MessageDialogs;
import com.example.rafae.promoz_001_alfa.util.Util;

/**
 * Created by vallux on 05/03/17.
 */

public class HistoricCoinDAO extends PromozContract.HistoricCoin {

    private AppDatabase dbHelper;
    private SQLiteDatabase database;
    private Cursor cursor;
    private Context context;

    public HistoricCoinDAO(Context context) {
        this.context = context;
        dbHelper = new AppDatabase(context);
        database = dbHelper.getDatabase();
    }

    private HistoricCoin populate() { // Popula o objeto com os dados do cursor
        HistoricCoin model = new HistoricCoin(
                cursor.getInt(cursor.getColumnIndex(_ID)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_WALLET_ID)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_HST_TP_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_HST_DT_OPER)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_AMOUNT_COIN)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_COIN_ID)),
                cursor.getString(cursor.getColumnIndex(HistoricTypeCoinDAO.COLUMN_HST_TP_DESC))
        );
        return model;
    }

    /**
     * Save or update historic coin
     * @param historic
     * @return long Util.Constants.ERROR_BD if error
     */
    public long save(HistoricCoin historic){ // salva os campos do objeto na tabela - atualiza ou cria um novo caso não exista
        long result = Util.Constants.ERROR_BD;

        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLET_ID, historic.getWalletId());
        values.put(COLUMN_HST_TP_ID, historic.getHistoricTypeId());
        values.put(COLUMN_HST_DT_OPER, historic.getHistoricDateOperation());
        values.put(COLUMN_AMOUNT_COIN, historic.getAmountCoin());
        values.put(COLUMN_COIN_ID, historic.getCoinId());

        try {
            if(historic.get_id() != null){
                result = database.update(TABLE_NAME, values, "_id = ?", new String[]{ historic.get_id().toString() });
            }
            result = database.insert(TABLE_NAME, null, values);
        }catch (Exception ex){
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_save_or_update), ex);
        }

        return result;
    }

    public List<HistoricCoin> listByDate(Integer walletId, Integer daysBefore) {
        List<HistoricCoin> lst = new ArrayList<HistoricCoin>();

        String selection =  COLUMN_WALLET_ID + "=? and " + COLUMN_HST_DT_OPER + " > date('now','"+daysBefore.toString()+" day') and " + COLUMN_HST_TP_ID +" = "+HistoricTypeCoinDAO.TABLE_NAME + "." + HistoricTypeCoinDAO._ID;
        String[] selectionArgs = { String.valueOf(walletId)};

        String orderBy = COLUMN_HST_DT_OPER + " DESC";

        String fields[] = new String[]{TABLE_NAME + "." + allFields[0],allFields[1],allFields[2],allFields[3],allFields[4],allFields[5], HistoricTypeCoinDAO.TABLE_NAME + "." + HistoricTypeCoinDAO.COLUMN_HST_TP_DESC};

        try {
            cursor = database.query(TABLE_NAME + ", " + HistoricTypeCoinDAO.TABLE_NAME, fields, selection, selectionArgs, null, null, orderBy);

            if(cursor.moveToFirst())
                do {
                    lst.add(populate());
                } while (cursor.moveToNext());

        } catch (Exception ex){
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_funny_db), ex);
        } finally {
            cursor.close();
        }
        return lst;
    }

    public boolean isCoinIdAdded(Integer coinId, Integer walletId) {

        String selection = COLUMN_COIN_ID + " = ? and " + COLUMN_WALLET_ID + " = ?";
        String[] selectionArgs = { String.valueOf(coinId), String.valueOf(walletId)};
        Cursor cursor = database.query(TABLE_NAME, allFields, selection, selectionArgs, null, null, null);
        Integer qtd = cursor.getCount();
        cursor.close();

        return qtd != 0;
    }

    //TODO never used
    public List<HistoricCoin> list(){
        cursor = database.query(TABLE_NAME, allFields, null, null, null, null, null);

        List<HistoricCoin> lst = new ArrayList<HistoricCoin>();
        if(cursor.moveToFirst())
            do {
                lst.add(populate());
            }while (cursor.moveToNext());

        cursor.close();
        return lst;
    }

    public void closeDataBase(){
        if(database.isOpen()) database.close();
    }
}