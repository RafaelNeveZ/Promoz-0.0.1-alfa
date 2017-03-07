package com.example.rafae.promoz_001_alfa.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rafae.promoz_001_alfa.R;
import com.example.rafae.promoz_001_alfa.dao.db.AppDatabase;
import com.example.rafae.promoz_001_alfa.dao.db.PromozContract;
import com.example.rafae.promoz_001_alfa.model.Coupon;
import com.example.rafae.promoz_001_alfa.util.MessageDialogs;
import com.example.rafae.promoz_001_alfa.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vallux on 05/03/17.
 */

public class CouponDAO extends PromozContract.Coupon {

    private AppDatabase dbHelper;
    private SQLiteDatabase database;
    private Context context;
    private Cursor cursor;

    public CouponDAO(Context context) {
        this.context = context;
        dbHelper = new AppDatabase(context);
        database = dbHelper.getDatabase();
    }

    private Coupon populate(Cursor cursor) { // Popula o objeto "Coupon" com os dados do cursor
        Coupon model = new Coupon(
                cursor.getInt(cursor.getColumnIndex(_ID)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_WALLET_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CPN_TITLE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CPN_SUBTITLE)),
                //cursor.getBlob(cursor.getColumnIndex(COLUMN_CPN_IMG)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_CPN_IMG)), // TODO modificado apenas para protótipo
                cursor.getString(cursor.getColumnIndex(COLUMN_CPN_INFO)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CPN_DT_USE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CPN_DT_EXP)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_CPN_PRICE)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_CPN_IND_VALID)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_CPN_STR_ID))
        );
        return model;
    }

    /**
     * Save or update Coupon
     * @param coupon
     * @return long Util.Constants.ERROR_BD if error
     */
    public long save(Coupon coupon){ // salva os campos do objeto na tabela - atualiza ou cria um novo caso não exista

        long result = Util.Constants.ERROR_BD;

        ContentValues values = new ContentValues();
        values.put(COLUMN_WALLET_ID, coupon.getWalletId());
        values.put(COLUMN_CPN_TITLE, coupon.getTitle());
        values.put(COLUMN_CPN_SUBTITLE, coupon.getSubTitle());
        values.put(COLUMN_CPN_IMG, coupon.getImg());
        values.put(COLUMN_CPN_INFO, coupon.getInfo());
        values.put(COLUMN_CPN_DT_USE, coupon.getDateUse());
        values.put(COLUMN_CPN_DT_EXP, coupon.getDateExp());
        values.put(COLUMN_CPN_PRICE, coupon.getPrice());
        values.put(COLUMN_CPN_STR_ID, coupon.getStoreId());
        values.put(COLUMN_CPN_IND_VALID, coupon.getValid());

        try {
            if(coupon.get_id() != null)
                result = database.update(TABLE_NAME, values, "_id = ?", new String[]{ coupon.get_id().toString() });
            else
                result = database.insert(TABLE_NAME, null, values);
        } catch (Exception ex){
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_funny_db), ex);
        }

        return result;
    }

    public Coupon couponById(Integer id){
        Coupon cpn = new Coupon();

        try {
            cursor = database.query(TABLE_NAME, allFields, _ID+"=?", new String[]{id.toString()}, null, null, null);
            if(cursor.moveToFirst()) cpn = populate(cursor);

        } catch (Exception ex){
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_save_or_update), ex);
        } finally {
            cursor.close();
        }

        return cpn;
    }
    //TODO never used
    /*public List<Coupon> listById(Integer walletID){
        return listById(walletID, "");
    }*/

    public List<Coupon> listById(Integer walletID, String order){
        List<Coupon> lst = new ArrayList<Coupon>();

        try {
            cursor = database.query(TABLE_NAME, allFields, COLUMN_WALLET_ID + "=?", new String[]{walletID.toString()}, null, null, order);
            if (cursor.moveToFirst())
                do {
                    lst.add(populate(cursor));
                }while (cursor.moveToNext());
        } catch (Exception ex){
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_save_or_update), ex);
        } finally {
            cursor.close();
        }

        return lst;
    }

    //TODO never used
    /*public List<Coupon> list(){
        return list("");
    }*/

    public List<Coupon> list(String order){
        List<Coupon> lst = new ArrayList<Coupon>();

        try {
            cursor = database.query(TABLE_NAME, allFields, null, null, null, null, order);
            if (cursor.moveToFirst())
                do {
                    lst.add(populate(cursor));
                }while (cursor.moveToNext());

        } catch (Exception ex){
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_save_or_update), ex);
        } finally {
            cursor.close();
        }

        return lst;
    }

    public void closeDataBase(){
        if(database.isOpen()) database.close();
    }

    //TODO never used
    /*public boolean remove(int id){
        return database.delete(TABLE_NAME, "_id = ?", new String[]{ Integer.toString(id) }) > 0;
    }*/
}
