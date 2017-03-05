package com.example.rafae.promoz_001_alfa.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.rafae.promoz_001_alfa.R;
import com.example.rafae.promoz_001_alfa.dao.db.AppDatabase;
import com.example.rafae.promoz_001_alfa.dao.db.PromozContract;
import com.example.rafae.promoz_001_alfa.model.Wallet;
import com.example.rafae.promoz_001_alfa.util.MessageDialogs;


/**
 * Created by vallux on 05/03/17.
 */

public class WalletDAO extends PromozContract.Wallet {
    private AppDatabase dbHelper;
    private SQLiteDatabase database;
    private Context context;
    private Cursor cursor;

    public WalletDAO(Context context) {
        this.context = context;
        dbHelper = new AppDatabase(context);
        database = dbHelper.getDatabase();
    }

    private Wallet populate(){ // Popula o objeto "Wallet" com os dados do cursor
        Wallet model = new Wallet(
                cursor.getInt(cursor.getColumnIndex(_ID)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_AMOUNT_COIN))
        );
        return model;
    }

    //TODO never used
    /*public long save(Wallet hist){ // salva os campos do objeto na tabela - atualiza ou cria um novo caso não exista

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, hist.getUserId());
        values.put(COLUMN_AMOUNT_COIN, hist.getAmountCoin());

        if(hist.get_id() != null){
            return database.update(TABLE_NAME, values, "_id = ?", new String[]{ hist.get_id().toString() });
        }
        return database.insert(TABLE_NAME, null, values);
    }*/

    public Wallet walletById(int id){

        try {
            cursor = database.query(TABLE_NAME, allFields, _ID+"=?", new String[]{String.valueOf(id)}, null, null, null);
            if(cursor.moveToFirst())
                return populate();

        } catch (Exception ex){
           MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_funny_db), ex);
        } finally {
            cursor.close();
        }
        return null;
    }

    public Integer getAmountByWalletId(Integer id){
        Integer result = -1;

        try {
            cursor = database.query(TABLE_NAME, new String[]{COLUMN_AMOUNT_COIN}, COLUMN_USER_ID+"=?", new String[]{id.toString()}, null, null, null);
            if(cursor.moveToFirst())
                result = cursor.getInt(cursor.getColumnIndex(COLUMN_AMOUNT_COIN));

        } catch (Exception ex){
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_funny_db), ex);
        } finally {
            cursor.close();
        }

        return result;
    }

    public Integer walletIdByUserId(Integer id){
        Integer result = -1;

        try {
            cursor = database.query(TABLE_NAME, new String[]{_ID}, COLUMN_USER_ID+"=?", new String[]{id.toString()}, null, null, null);
            if(cursor.moveToFirst())
                result = cursor.getInt(cursor.getColumnIndex(_ID));

        } catch (Exception ex){
            MessageDialogs.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_funny_db), ex);
        } finally {
            cursor.close();
        }

        return result;
    }

    //TODO never used
    /*public List<Wallet> list(){
        List<Wallet> lst = new ArrayList<Wallet>();

        try {
            cursor = database.query(TABLE_NAME, allFields, null, null, null, null, null);
            while (cursor.moveToNext())
                lst.add(populate());

        } catch (Exception ex){
            Message.msgErrorDB(context, context.getString(R.string.tag_error_db), context.getString(R.string.error_funny_db), ex);
        } finally {
            cursor.close();
        }

        return lst;
    }*/

    //TODO never used
    /*public boolean remove(int id){
        return database.delete(TABLE_NAME, "_id = ?", new String[]{ Integer.toString(id) }) > 0;
    }*/

    public void closeDataBase(){
        if(database.isOpen()) database.close();
    }
}
