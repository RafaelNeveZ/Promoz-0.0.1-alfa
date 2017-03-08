package com.example.rafae.promoz_001_alfa;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.rafae.promoz_001_alfa.adapter.ShopAdapter;
import com.example.rafae.promoz_001_alfa.dao.CouponDAO;
import com.example.rafae.promoz_001_alfa.dao.VirtualStoreDAO;
import com.example.rafae.promoz_001_alfa.dao.WalletDAO;
import com.example.rafae.promoz_001_alfa.model.Coupon;
import com.example.rafae.promoz_001_alfa.model.User;
import com.example.rafae.promoz_001_alfa.model.VirtualStore;
import com.example.rafae.promoz_001_alfa.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class LojaActivity extends AppCompatActivity {
    ListView listShop;
    List<VirtualStore> virtualStoreList;
    VirtualStoreDAO virtualStoreDAO;
    ShopAdapter shopAdapter;
    Integer walletId;
    Integer walletAmount;
    WalletDAO walletDAO;
    CouponDAO couponDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loja);
        listShop = (ListView) findViewById(R.id.lstShop);
        walletDAO = new WalletDAO(this);
        walletId = walletDAO.walletIdByUserId(getIntent().getIntExtra(User.getChave_ID(),0));
        walletAmount = walletDAO.getAmountByWalletId(walletId);
        walletDAO.closeDataBase();
        updateStoreList();
    }

    public void  info(View view){
        com.example.rafae.promoz_001_alfa.util.MessageDialogs.msgInfo(this,((VirtualStore) view.getTag()).getTitle(),((VirtualStore) view.getTag()).getInformation(),android.R.drawable.ic_dialog_info);
    }

    public void buy(View view){
        VirtualStore virtualStore = (VirtualStore) view.getTag();
        Integer price = virtualStore.getPrice();
        if(price <= walletAmount) {
            walletAmount -= price;

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 10);

            String date = new SimpleDateFormat(DateUtil.YYYYMMDD_HHmmss).format(new Date(cal.getTimeInMillis()));
            String desc = virtualStore.getTitle();

            couponDAO = new CouponDAO(this);
            Coupon coupon = new Coupon();
            coupon.setDateExp(date);
            coupon.setTitle(desc);
            coupon.setSubTitle("Compra na Loja Virtual");
            coupon.setPrice(price);
            coupon.setInfo(virtualStore.getInformation());
            coupon.setImg(virtualStore.getImg());
            coupon.setValid(1);
            coupon.setWalletId(walletId);
            coupon.setStoreId(virtualStore.getStoreId());
            couponDAO.save(coupon);
            couponDAO.closeDataBase();

            Snackbar.make(view, "Comprou " + desc, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } else {
            Snackbar.make(view,getResources().getString(R.string.saldoInsuficiente) + ": " + walletAmount, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    private void updateStoreList(){
        virtualStoreDAO = new VirtualStoreDAO(this);
        virtualStoreList = virtualStoreDAO.list();
        shopAdapter = new ShopAdapter(this,virtualStoreList);
        listShop.setAdapter(shopAdapter);
        virtualStoreDAO.closeDataBase();
    }
}