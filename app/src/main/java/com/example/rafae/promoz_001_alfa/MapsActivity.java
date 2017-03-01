package com.example.rafae.promoz_001_alfa;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.rafae.promoz_001_alfa.interfaces.Coin;
import com.example.rafae.promoz_001_alfa.interfaces.Markers;
import com.example.rafae.promoz_001_alfa.model.Advertising;
import com.example.rafae.promoz_001_alfa.model.User;
import com.example.rafae.promoz_001_alfa.util.HttpResponseHandler;
import com.example.rafae.promoz_001_alfa.util.MessageDialogs;
import com.example.rafae.promoz_001_alfa.util.PlayAudio;
import com.example.rafae.promoz_001_alfa.util.Singleton;
import com.example.rafae.promoz_001_alfa.util.TimerView;
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
import java.util.ArrayList;
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, HttpResponseHandler.onFinishResponse, Coin, Markers {

    Integer userID=-1;
    private GoogleMap mMap;
    Bitmap bmp;
    private HttpResponseHandler responseHandler;
    private AsyncHttpClient client;
    private List<Integer> addedMarkers = new ArrayList<Integer>();
    Marker tempMarker;
    final private Context context = this;

    private static final int TIMER_LENGTH = 5;

    private TimerView mTimerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        checkLogged();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.moeda_marker);
        mapFragment.getMapAsync(this);

        client = new AsyncHttpClient();
        responseHandler = new HttpResponseHandler(this);
        responseHandler.setCallback(this);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Imbui.getCenter(), 16));

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context,SettingsActivity.class);
                context.startActivity(intent);
            }
        });
        showDialog();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.hideInfoWindow();
        if(marker.getTag() == null) {
            String serverURL = "http://" + Singleton.getServerIp(getResources().getString(R.string.server_ip), getResources().getString(R.string.pref_default_ip_address), this) + "/advertising/";
            client.get(serverURL + "?lat=-12.9790&long=-38.4532&dist=160", responseHandler);
        }else{
            tempMarker = marker;
            MessageDialogs.msgAddvertising(this,R.layout.add_layout, ((Advertising) marker.getTag()).getImage(), R.id.img_advertising, 5000, 1);
        }
        return true;
    }

    @Override
    public void finished() {
        List<Advertising> advertisings = responseHandler.getAdvertisings();
        for (Advertising add : advertisings) {
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
    public void gainCoin(Integer qtd) {
        PlayAudio audio = new PlayAudio();
        audio.play(this,R.raw.smw_coin);

        // TODO: adicinar moeda na carteira

        addedMarkers.remove(addedMarkers.indexOf(Integer.parseInt(tempMarker.getSnippet())));
        tempMarker.remove();
    }

    @Override
    public void resetMarker() {
        tempMarker = null;
    }

    //TODO: Metodo do dialog custom
    private void showDialog(/* COLOCAR O QUE TIVER QUE RECEBER DO SERVER E MOSTRAR*/ ){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        ImageView Im = (ImageView)dialog.findViewById(R.id.imageView);
        mTimerView = (TimerView) dialog.findViewById(R.id.timer);

        //Coloca imagem da propaganda
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.sq_popup_papel);;
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
        new Thread(new Runnable() {
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
        }).start();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                //TODO DESENHAR ROTA
            }
        });
    }

    private void checkLogged(){
        userID = getDefaultSharedPreferences(this).getInt(getResources().getString(R.string.user_id),0);
        Log.e("USER","ID = " + userID);
        //if(userID == 0)
        //    finish();
    }
}
