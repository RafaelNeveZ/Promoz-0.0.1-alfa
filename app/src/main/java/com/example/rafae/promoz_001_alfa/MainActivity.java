package com.example.rafae.promoz_001_alfa;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafae.promoz_001_alfa.dao.HistoricCoinDAO;
import com.example.rafae.promoz_001_alfa.dao.UserDAO;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, HttpResponseHandler.onFinishResponse, Coin, Markers, PromozLocation.connected {

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
    private ImageView foto,fotoclick;
    final Context context =this;

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
           // Intent intent = new Intent(this,LojaActivity.class);
           // intent.putExtra(User.getChave_ID(), userID);
          //  this.startActivity(intent);

        } else if (id == R.id.nav_config) {
            //Context contexto = getApplicationContext();
            Intent intent = new Intent(context,SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            //String texto = "CONFIGURAÇÕES";
            //int duracao = Toast.LENGTH_SHORT;
            //Toast toast = Toast.makeText(contexto, texto,duracao);
            //toast.show();

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
        addCoin(qtd, ((Advertising) tempMarker.getTag()).getId());
        addedMarkers.remove(addedMarkers.indexOf(Integer.parseInt(tempMarker.getSnippet())));
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
    public void resetMarker() {
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
