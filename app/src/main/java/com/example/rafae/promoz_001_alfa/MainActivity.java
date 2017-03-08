package com.example.rafae.promoz_001_alfa;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafae.promoz_001_alfa.dao.HistoricCoinDAO;
import com.example.rafae.promoz_001_alfa.dao.TempAdvertisingDAO;
import com.example.rafae.promoz_001_alfa.dao.UserDAO;
import com.example.rafae.promoz_001_alfa.dao.WalletDAO;
import com.example.rafae.promoz_001_alfa.interfaces.Coin;
import com.example.rafae.promoz_001_alfa.interfaces.Markers;
import com.example.rafae.promoz_001_alfa.model.Advertising;
import com.example.rafae.promoz_001_alfa.model.HistoricCoin;
import com.example.rafae.promoz_001_alfa.model.TempAdvertising;
import com.example.rafae.promoz_001_alfa.model.User;
import com.example.rafae.promoz_001_alfa.util.DateUtil;
import com.example.rafae.promoz_001_alfa.util.HttpResponseHandler;
import com.example.rafae.promoz_001_alfa.util.MessageDialogs;
import com.example.rafae.promoz_001_alfa.util.PlayAudio;
import com.example.rafae.promoz_001_alfa.util.PromozLocation;
import com.example.rafae.promoz_001_alfa.util.Singleton;
import com.example.rafae.promoz_001_alfa.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener, HttpResponseHandler.onFinishResponse, Coin, Markers,
        PromozLocation.connected, GoogleMap.OnMapClickListener {

    private Integer userID=-1;
    private String serverURL;
    private int backButtonCount = 0;
    private int countDown = 10000;
    private GoogleMap mMap;
    private Bitmap bmp;
    private HttpResponseHandler responseHandler;
    private AsyncHttpClient client;
    private List<Integer> addedMarkers = new ArrayList<Integer>();
    private Marker tempMarker;
    private PromozLocation promozLocation;
    private ImageView foto;
    private final Context context =this;
    private LatLng latLngAtual;
    private LatLng latLngAv7;
    private Integer raioAv7;
    private LatLng latLngShopSalv;
    private Integer raioShopSalv;
    private Circle circuloCoord = null;
    private Polyline linhaRota;
    private String keyShop = "shopSalv";
    private String keyAv7 = "av7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLogged();
        promozLocation = new PromozLocation(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.moeda_marker);
        mapFragment.getMapAsync(this);

        latLngAv7 = new LatLng(-12.982541677928948, -38.51487658917903);
        raioAv7 = 560;
        latLngShopSalv = new LatLng(-12.978495718050622, -38.45488730818033);
        raioShopSalv = 250;

        client = new AsyncHttpClient();
        responseHandler = new HttpResponseHandler(this);
        responseHandler.setCallback(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CarteiraActivity.class);
                intent.putExtra(User.getChave_ID(),userID);
                MainActivity.this.startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerClosed(View view) {
                backButtonCount =0;
                super.onDrawerClosed(view);
            }
            public void onDrawerOpened(View drawerView) {
                backButtonCount =0;
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        foto =  (ImageView) hView.findViewById(R.id.foto_nav);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PerfilActivity.class);
                closeMenu();
                context.startActivity(intent);
            }
        });

        setMenu();
    }

    private void setMenu(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        UserDAO userDAO = new UserDAO(this);
        User user = userDAO.userById(userID);

        if(user != null) {
            byte[] bitmapdata;
            bitmapdata = user.getImg();
            TextView name = (TextView) hView.findViewById(R.id.navDrawerNome);
            name.setText(user.getNome());
            if(bitmapdata != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                if (bitmap != null){
                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                    drawable.setCircular(true);
                    foto.setImageDrawable(drawable);
                }
            } else {
                Resources res = getResources();
                Bitmap src = BitmapFactory.decodeResource(res, R.drawable.default_photo);
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), src);
                drawable.setCircular(true);
                foto.setImageDrawable(drawable);
            }
        }
        userDAO.closeDataBase();
    }

    @Override
    public void onBackPressed() {
        if (closeMenu()) {
            if (backButtonCount >= 1) {
                moveTaskToBack(true);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Intent intent = new Intent(this,PerfilActivity.class);
            intent.putExtra(User.getChave_ID(),userID);
            this.startActivity(intent);

        } else if (id == R.id.nav_wallet) {
            Intent intent = new Intent(this,CarteiraActivity.class);
            intent.putExtra(User.getChave_ID(),userID);
            this.startActivity(intent);
        } else if (id == R.id.nav_missions) {
            Context contexto = getApplicationContext();
            String texto = "MISSÕES";
            int duracao = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(contexto, texto,duracao);
            toast.show();

        } else if (id == R.id.nav_shop) {
            Intent intent = new Intent(this,LojaActivity.class);
            intent.putExtra(User.getChave_ID(), userID);
            this.startActivity(intent);

        } else if (id == R.id.nav_config) {
            Intent intent = new Intent(context,SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        } else if (id == R.id.nav_help) {
            Context contexto = getApplicationContext();
            String texto = "AJUDA";
            int duracao = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(contexto, texto,duracao);
            toast.show();

        } else if (id == R.id.nav_feedback) {
            Context contexto = getApplicationContext();
            String texto = "FEEDBACK";
            int duracao = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(contexto, texto,duracao);
            toast.show();

        } else if (id == R.id.nav_terms) {
            Context contexto = getApplicationContext();
            String texto = "TERMO DE SERVOÇO";
            int duracao = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(contexto, texto,duracao);
            toast.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        LatLng ruy = new LatLng(-12.960244, -38.431348);
        LatLngBounds Imbui = new LatLngBounds(tamari, ruy);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Imbui.getCenter(), 12));

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        makeFence(latLngAv7,raioAv7);
        makeFence(latLngShopSalv,raioShopSalv);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.hideInfoWindow();

        if(linhaRota != null)
            linhaRota.remove();

        if(marker.getTag() != null) {
            tempMarker = marker;
            Advertising add = (Advertising) tempMarker.getTag();
            LatLng coordLoja = new LatLng(add.getLat(), add.getLng());
            MessageDialogs.msgAddvertising(this,R.layout.dialog, add.getImage(), R.id.imageView, 5000, add.getQtdCoin(), add.getId(), coordLoja);
        }
        return true;
    }

    @Override
    public void finished() {
        List<Advertising> advertisings = responseHandler.getAdvertisings();

        TempAdvertisingDAO tempAdvertisingDAO = new TempAdvertisingDAO(this);

        for (Advertising add : advertisings) {
            HistoricCoinDAO historicCoinDAO = new HistoricCoinDAO(this);
            WalletDAO wallet = new WalletDAO(this);
            Integer walletId = wallet.walletIdByUserId(userID);
            wallet.closeDataBase();

            if (!historicCoinDAO.isCoinIdAdded(add.getId(),walletId)) { // verificando existencia de moeda já coletada
                Integer iaddId = add.getId();

                if(!tempAdvertisingDAO.isAdvertisingIdAdded(iaddId)){
                    TempAdvertising tempAdvertising = new TempAdvertising(iaddId, add.getImageURL(), add.getQtdCoin(), add.getLat(), add.getLng());
                    tempAdvertisingDAO.save(tempAdvertising);
                 //   Log.e("SAVE","SALVO NO BANCO " + iaddId);
                }


               // addedMarkers.add(add.getId());
                //LatLng coordLoja = new LatLng(add.getLat(), add.getLng());
                //mMap.addMarker((new MarkerOptions().position(coordLoja).title(add.getTitle()).icon(BitmapDescriptorFactory.fromBitmap(bmp)).snippet(add.getId().toString()))).setTag(add);
                //add.setImage(); // adiquire imagem no webserver
            }
        }
        tempAdvertisingDAO.closeDataBase();
        responseHandler.clearAdvertisings();
    }

    @Override
    public void gainCoin(Integer qtd, Integer idCoin) { // invocado ao final da temporização
        PlayAudio audio = new PlayAudio();
        audio.play(this,R.raw.smw_coin, 1);
        addCoin(qtd, ((Advertising) tempMarker.getTag()).getId());

        Integer idx = addedMarkers.indexOf(Integer.parseInt(tempMarker.getSnippet()));
        if (idx != -1)
            addedMarkers.remove(idx);

        tempMarker.remove();
    }

    private void addCoin(Integer amountCoin, Integer coinId) {
        WalletDAO wallet = new WalletDAO(this);
        Integer walletId = wallet.walletIdByUserId(userID);
        String date = new SimpleDateFormat(DateUtil.YYYYMMDD_HHmmss).format(new Date());
        HistoricCoin historicCoin = new HistoricCoin(walletId,1,date,amountCoin,coinId);
        HistoricCoinDAO historicCoinDAO = new HistoricCoinDAO(this);
        historicCoinDAO.save(historicCoin);
        wallet.closeDataBase();
        historicCoinDAO.closeDataBase();
    }

    @Override
    public void resetMarker() { // metodo invocado somente no onDismiss do dialog
        tempMarker = null;
    }

    @Override
    public void moveCamera(LatLng coord) {
        mMap.setIndoorEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 17));
    }

    private boolean closeMenu(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            backButtonCount =0;
            drawer.closeDrawer(GravityCompat.START); // recolhe o menu caso esteja "aberto"
            return false;
        }
        return true;
    }
    private void checkLogged(){
        userID = getDefaultSharedPreferences(getApplicationContext()).getInt(getResources().getString(R.string.user_id),0);
        if(userID == 0)
            finish();
    }

    @Override
    protected void onResume() {
        serverURL = "http://" + Singleton.getServerIp(getResources().getString(R.string.server_ip), getResources().getString(R.string.pref_default_ip_address), this) + "/advertising/";
        super.onResume();
    }

    @Override
    protected void onRestart() {
        checkLogged();
        super.onRestart();
        setMenu();
    }

    @Override
    public void onConnected() {
        Location local = promozLocation.getLocation();
        if(local != null) {
            LatLng latLng = new LatLng(local.getLatitude(),local.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }
    }

    @Override
    public void makeRoute(LatLng coordLoja) {
        PolylineOptions linha = new PolylineOptions();
        linha.add(latLngAtual);
        linha.add(coordLoja);
        linha.color(Color.argb(85, 200, 100,100));
        if(linhaRota != null)
            linhaRota.remove();
        linhaRota = mMap.addPolyline(linha);
        linhaRota.setGeodesic(true);
    }

    private void makeFence(LatLng latLng, Integer raio){
        CircleOptions circleOptions = new CircleOptions().center(latLng);
        circleOptions.fillColor(Color.argb(60,200,150,150)).strokeWidth(2);
        circleOptions.radius(raio);
        mMap.addCircle(circleOptions);
    }

    private boolean isInsideFence(LatLng de, LatLng para, Integer raio) {

        double lat_de = de.latitude;
        double long_de = de.longitude;

        double lat_para = para.latitude;
        double long_para = para.longitude;


        double dist =
        6371000*Math.acos(Math.cos(Math.PI*(90-lat_para)/180)*Math.cos((90-lat_de)*Math.PI/180)+Math.sin((90-lat_para)*Math.PI/180)*Math.sin((90-lat_de)*Math.PI/180)*Math.cos((long_para-long_de)*Math.PI/180));

        return (dist <= raio);
    }

    @Override
    public void onMapClick(LatLng latLng) {
      //  mMap.clear();
        latLngAtual = latLng;
        addedMarkers.clear();
        Integer raio = 120;

        if(linhaRota != null)
            linhaRota.remove();

        if (circuloCoord != null)
            circuloCoord.remove();
        CircleOptions opt = new CircleOptions().center(latLng);
        opt.fillColor(Color.argb(80,200,150,150)).strokeWidth(1);
        opt.radius(raio);
        circuloCoord = mMap.addCircle(opt);

        if(isInsideFence(latLng,latLngShopSalv,raioShopSalv) && getDefaultSharedPreferences(getApplicationContext()).getInt(keyShop,0) == 0) { // verifica se está dentro da região e se as moedas já foram baixadas
            client.get(serverURL + "?lat="+latLngShopSalv.latitude+"&long="+latLngShopSalv.longitude+"&dist="+raioShopSalv, responseHandler);
            Util.setSharedPreferencesRegion(this,keyShop,1);
        } else if(isInsideFence(latLng,latLngAv7,raioAv7) && getDefaultSharedPreferences(getApplicationContext()).getInt(keyAv7,0) == 0) {
            client.get(serverURL + "?lat="+latLngAv7.latitude+"&long="+latLngAv7.longitude+"&dist="+raioAv7, responseHandler);
            Util.setSharedPreferencesRegion(this,keyAv7,1);
        }
    }
}