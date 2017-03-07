package com.example.rafae.promoz_001_alfa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.rafae.promoz_001_alfa.dao.HistoricCoinDAO;
import com.example.rafae.promoz_001_alfa.dao.WalletDAO;
import com.example.rafae.promoz_001_alfa.interfaces.Coin;
import com.example.rafae.promoz_001_alfa.interfaces.Markers;
import com.example.rafae.promoz_001_alfa.model.Advertising;
import com.example.rafae.promoz_001_alfa.model.HistoricCoin;
import com.example.rafae.promoz_001_alfa.model.User;
import com.example.rafae.promoz_001_alfa.util.DateUtil;
import com.example.rafae.promoz_001_alfa.util.HttpResponseHandler;
import com.example.rafae.promoz_001_alfa.util.MessageDialogs;
import com.example.rafae.promoz_001_alfa.util.PlayAudio;
import com.example.rafae.promoz_001_alfa.util.PromozLocation;
import com.example.rafae.promoz_001_alfa.util.Singleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, HttpResponseHandler.onFinishResponse, Coin, Markers, PromozLocation.connected {

    Integer userID=-1;
    int backButtonCount = 0;
    int countDown = 10000;
    private GoogleMap mMap;
    Bitmap bmp;
    private HttpResponseHandler responseHandler;
    private AsyncHttpClient client;
    private List<Integer> addedMarkers = new ArrayList<Integer>();
    Marker tempMarker;
    private PromozLocation promozLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        checkLogged();

        promozLocation = new PromozLocation(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.moeda_marker);
        mapFragment.getMapAsync(this);

        client = new AsyncHttpClient();
        responseHandler = new HttpResponseHandler(this);
        responseHandler.setCallback(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        promozLocation.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        promozLocation.disconnect();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng tamari = new LatLng(-12.964996, -38.431504);
        /*LatLng extra = new LatLng(-12.9629824, -38.4322472);
        LatLng unifacs = new LatLng(-12.9611188, -38.4321055);*/
        LatLng ruy = new LatLng(-12.960244, -38.431348);
        LatLngBounds Imbui = new LatLngBounds(tamari, ruy);
        mMap.addMarker(new MarkerOptions().position(tamari).title("Marker no Tamari").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        //mMap.addMarker(new MarkerOptions().position(extra).title("Marker no Extra").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        //mMap.addMarker(new MarkerOptions().position(unifacs).title("Marker no Facs").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        mMap.addMarker(new MarkerOptions().position(ruy).title("Marker no Ruy").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Imbui.getCenter(), 12));

        mMap.setOnMarkerClickListener(this);

        // ##################################### inicia activity Setting
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context,SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
       // showDialog();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.hideInfoWindow();
        if(marker.getTag() == null) {
            String serverURL = "http://" + Singleton.getServerIp(getResources().getString(R.string.server_ip), getResources().getString(R.string.pref_default_ip_address), this) + "/advertising/";
            client.get(serverURL + "?lat=-12.9790&long=-38.4532&dist=560", responseHandler);
        }else{
            tempMarker = marker;
            Advertising add = (Advertising) tempMarker.getTag();
            LatLng coordLoja = new LatLng(add.getLat(), add.getLng());
            MessageDialogs.msgAddvertising(this,R.layout.dialog, add.getImage(), R.id.imageView, 5000, add.getQtdCoin(), add.getId(), coordLoja);
            //MessageDialogs.msgAddvertising(this,R.layout.add_layout, ((Advertising) marker.getTag()).getImage(), R.id.img_advertising, 5000, 1);
        }
        return true;
    }

    @Override
    public void finished() {
        List<Advertising> advertisings = responseHandler.getAdvertisings();

        for (Advertising add : advertisings) {
        //for (int i=0;i<advertisings.size();i++) {
          //  Advertising add = null;
          //  add = advertisings.get(i);

            if (addedMarkers.indexOf(add.getId()) == -1) { // TODO: consultar carteira existencia de moeda já coletada
                addedMarkers.add(add.getId());
                LatLng coordLoja = new LatLng(add.getLat(), add.getLng());
                mMap.addMarker((new MarkerOptions().position(coordLoja).title(add.getTitle()).icon(BitmapDescriptorFactory.fromBitmap(bmp)).snippet(add.getId().toString()))).setTag(add);
                add.setImage();
                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(add.getLat(),add.getLng()), 16));
            }
        }

        responseHandler.clearAdvertisings();
/*        LatLng ljAmeric = new LatLng(-12.9770046, -38.4559200);
        LatLng bomPrec = new LatLng(-12.9798411, -38.4536884);
        LatLngBounds salvadorShopping = new LatLngBounds(bomPrec, ljAmeric);
        Log.e("LAT LONG","" + salvadorShopping.getCenter().toString());
        //CameraPosition camPos = new CameraPosition();
       //  mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(salvadorShopping, 18));
       //   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(salvadorShopping.getCenter(), 18));*/
    }

    @Override
    public void gainCoin(Integer qtd, Integer idCoin) {
        PlayAudio audio = new PlayAudio();
        //for(int i = 0; i< qtd; i++)
        audio.play(this,R.raw.smw_coin, 1);
        addCoin(qtd);
        addedMarkers.remove(addedMarkers.indexOf(Integer.parseInt(tempMarker.getSnippet())));
        tempMarker.remove();
    }

    private void addCoin(Integer amountCoin) {
        WalletDAO wallet = new WalletDAO(this);
        Integer walletId = wallet.walletIdByUserId(userID);

        Log.e("TAG","walletId - " + walletId);

        String date = new SimpleDateFormat(DateUtil.YYYYMMDD_HHmmss).format(new Date());
        HistoricCoin historicCoin = new HistoricCoin(walletId,1,date,amountCoin,0);
        HistoricCoinDAO historicCoinDAO = new HistoricCoinDAO(this);
        historicCoinDAO.save(historicCoin);
        wallet.closeDataBase();
        historicCoinDAO.closeDataBase();

        // ########################### TEMPORARIO
        Intent intent = new Intent(this,CarteiraActivity.class);
        intent.putExtra(User.getChave_ID(),userID);
        this.startActivity(intent);
    }

    @Override
    public void resetMarker() {
        tempMarker = null;
    }

    @Override
    public void moveCamera(LatLng coord) {
        mMap.setIndoorEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 17));
    }

    private boolean closeMenu(){
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            backButtonCount =0;
            drawer.closeDrawer(GravityCompat.START); // recolhe o menu caso esteja "aberto"
            return false;
        }*/
        return true;
    }

    @Override
    public void onBackPressed() {

        if (closeMenu()) {
            if (backButtonCount >= 1) {
                moveTaskToBack(true);
                backButtonCount = 0;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Pressione o botão voltar novamente para sair do aplicativo", Toast.LENGTH_SHORT).show();
                backButtonCount++;
                new CountDownTimer(countDown, 5000) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                        backButtonCount = 0;
                    }
                }.start();
            }
        }
    }

    /*//TODO: Metodo do dialog custom
    private void showDialog(*//* COLOCAR O QUE TIVER QUE RECEBER DO SERVER E MOSTRAR*//* ){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        ImageView Im = (ImageView)dialog.findViewById(R.id.imageView);
        mTimerView = (TimerView) dialog.findViewById(R.id.timer);

        //Coloca imagem da propaganda
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.sq_popup_papel);

        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(this.getResources(), bitmap);
        drawable.setCircular(true);
        Im.setImageDrawable(drawable);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();


        //Botão
        final Button bt = (Button)dialog.findViewById(R.id.bot);
        bt.setEnabled(false);

        //TODO implementar thread corretamente
        mTimerView.start(TIMER_LENGTH);
        *//*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bt.setEnabled(true);
                    }
                });
            }
        }).start();*//*

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                //TODO DESENHAR ROTA
            }
        });
    }*/
    private void checkLogged(){
        userID = getDefaultSharedPreferences(getApplicationContext()).getInt(getResources().getString(R.string.user_id),0);
        if(userID == 0)
            finish();
    }

    @Override
    public void onConnected() {
        Location local = promozLocation.getLocation();
        if(local != null) {
            LatLng latLng = new LatLng(local.getLatitude(),local.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            //if(promozLocation.checkPermission())
            //    mMap.setMyLocationEnabled(true);
            //CircleOptions circulo = new CircleOptions().center(latLng);
            //circulo.fillColor(Color.RED);
            //circulo.radius(25);
            //mMap.clear();
            //mMap.addCircle(circulo);
        }
    }

}
