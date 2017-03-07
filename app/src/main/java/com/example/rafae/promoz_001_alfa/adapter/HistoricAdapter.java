package com.example.rafae.promoz_001_alfa.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafae.promoz_001_alfa.R;
import com.example.rafae.promoz_001_alfa.model.HistoricCoin;
import com.example.rafae.promoz_001_alfa.util.DateUtil;

import java.util.List;

/**
 * Created by vallux on 05/03/17.
 */

public class HistoricAdapter extends CustomAdapter {

    public HistoricAdapter(Context context, List<HistoricCoin> lst) {
        super(context, lst);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        HistoricCoin object = (HistoricCoin) list.get(i);
        Log.e("TAG", "LIST = " + list.size());

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.history_layout_line, null);
        }

        TextView date = (TextView) view.findViewById(R.id.historic_date);
        date.setText(DateUtil.SQLiteDateFormatToBrazilFormat(object.getHistoricDateOperation()));

        TextView desc = (TextView) view.findViewById(R.id.historic_description);
        desc.setText(object.getOperationDescription());

        TextView amount = (TextView) view.findViewById(R.id.historic_value);
        Integer value = object.getAmountCoin();
        ImageView coin = (ImageView) view.findViewById(R.id.historic_coin);

        if(value < 0){
            amount.setTextColor(view.getResources().getColor(R.color.colorPrimary));
            coin.setImageDrawable(view.getResources().getDrawable(R.drawable.moeda_gasto));
        }else{
            amount.setTextColor(view.getResources().getColor(R.color.colorVerdeMoeda));
            coin.setImageDrawable(view.getResources().getDrawable(R.drawable.moeda));
        }
        amount.setText(value.toString());

        return view;
    }
}
