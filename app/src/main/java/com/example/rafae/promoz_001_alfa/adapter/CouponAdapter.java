package com.example.rafae.promoz_001_alfa.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.rafae.promoz_001_alfa.R;
import com.example.rafae.promoz_001_alfa.model.Coupon;
import com.example.rafae.promoz_001_alfa.util.DateUtil;

import java.util.List;

/**
 * Created by vallux on 05/03/17.
 */

public class CouponAdapter extends CustomAdapter {

    //private Context context;
    //private List<Coupon> list;

    public CouponAdapter(Context context, List<Coupon> lst) {
        super(context, lst);
        //this.context = context;
        //this.list = lst;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Coupon object =  (Coupon) list.get(i);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.coupon_cel, null);
        }

        ImageView logo = (ImageView) view.findViewById(R.id.logo);
        logo.setTag(object.get_id());
        logo.setImageDrawable(ContextCompat.getDrawable(context,object.getImg())); // carrega logo

        ImageButton info = (ImageButton) view.findViewById(R.id.info_button);
        info.setTag(object.get_id()); // associa tag para buscar info do cupom adequado

        TextView title = (TextView) view.findViewById(R.id.cupom_title);
        title.setText(object.getTitle());

        TextView subTitle = (TextView) view.findViewById(R.id.cupom_subtitle);
        subTitle.setText(object.getSubTitle());

        TextView date = (TextView) view.findViewById(R.id.cupom_date);

        if(object.getValid() == 1)
            date.setText("Expira em: " + DateUtil.SQLiteDateFormatToBrazilFormat(object.getDateExp()));
        else
            date.setText("Usado em: " + DateUtil.SQLiteDateFormatToBrazilFormat(object.getDateUse()));

        TextView gostore = (TextView) view.findViewById(R.id.goto_loja);
        gostore.setTag(object.getStoreId());

        RadioButton use = (RadioButton) view.findViewById(R.id.cupom_use);
        use.setTag(object.get_id()); // associa tag para buscar status do cupom adequado
        use.setChecked(object.getValid()==0);

        return view;
    }
}