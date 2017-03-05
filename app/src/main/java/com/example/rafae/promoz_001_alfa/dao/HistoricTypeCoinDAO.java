package com.example.rafae.promoz_001_alfa.dao;

import android.database.sqlite.SQLiteDatabase;

import com.example.rafae.promoz_001_alfa.dao.db.AppDatabase;
import com.example.rafae.promoz_001_alfa.dao.db.PromozContract;

/**
 * Created by vallux on 05/03/17.
 */

public class HistoricTypeCoinDAO extends PromozContract.HistoricTypeCoin {

    //TODO never used
    private AppDatabase dbHelper;

    //TODO never used
    private SQLiteDatabase database;

    //TODO never used
    /*public HistoricTypeCoinDAO(Context context) {
        dbHelper = new AppDatabase(context);
        database = dbHelper.getDatabase();
    }*/

    //TODO never used
    /*private HistoricTypeCoin populate(Cursor cursor){ // Popula o objeto "HistoricTypeCoin" com os dados do cursor
        HistoricTypeCoin model = new HistoricTypeCoin(
                cursor.getInt(cursor.getColumnIndex(_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_HST_TP_DESC))
        );
        return model;
    }*/

    //TODO never used
    /*public long save(HistoricTypeCoin hist){ // salva os campos do objeto na tabela - atualiza ou cria um novo caso não exista

        ContentValues values = new ContentValues();
        values.put(COLUMN_HST_TP_DESC, hist.getDescription());

        if(hist.get_id() != null){
            return database.update(TABLE_NAME, values, "_id = ?", new String[]{ hist.get_id().toString() });
        }
        return database.insert(TABLE_NAME, null, values);
    }*/

    //TODO never used
    /*public List<HistoricTypeCoin> list(){
        Cursor cursor = database.query(TABLE_NAME, allFields, null, null, null, null, null);

        List<HistoricTypeCoin> lst = new ArrayList<HistoricTypeCoin>();
        while (cursor.moveToNext())
            lst.add(populate(cursor));
        cursor.close();
        return lst;
    }*/

    //TODO never used
    /*public boolean remove(int id){
        return database.delete(TABLE_NAME, "_id = ?", new String[]{ Integer.toString(id) }) > 0;
    }*/
}
