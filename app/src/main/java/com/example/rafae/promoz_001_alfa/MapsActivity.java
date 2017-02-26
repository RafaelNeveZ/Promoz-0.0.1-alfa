package com.example.rafae.promoz_001_alfa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.rafae.promoz_001_alfa.model.Advertising;
import com.example.rafae.promoz_001_alfa.util.HttpResponseHandler;
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
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, HttpResponseHandler.onFinishResponse {

    private GoogleMap mMap;
    Bitmap bmp;
    private String serverIP = "192.168.1.8";
    private String serverURL = "http://"+serverIP+"/advertising/";
    private HttpResponseHandler responseHandler;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.moeda_marker);
        mapFragment.getMapAsync(this);

        client = new AsyncHttpClient();
        responseHandler = new HttpResponseHandler();
        responseHandler.setCallback(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
      //  client.get(serverURL + "?lat=-12.9790&long=-38.4532&dist=160", responseHandler);
        LatLng tamari = new LatLng(-12.9650861, -38.4314455);
        LatLng extra = new LatLng(-12.9629824, -38.4322472);
        LatLng unifacs = new LatLng(-12.9611188, -38.4321055);
        LatLng ruy = new LatLng(-12.9604738, -38.4317371);
        LatLngBounds Imbui = new LatLngBounds(tamari, ruy);
        mMap.addMarker(new MarkerOptions().position(tamari).title("Marker no Tamari").icon(BitmapDescriptorFactory.fromBitmap(bmp))).setTag(1);
        mMap.addMarker(new MarkerOptions().position(extra).title("Marker no Extra").icon(BitmapDescriptorFactory.fromBitmap(bmp))).setTag(2);
        mMap.addMarker(new MarkerOptions().position(unifacs).title("Marker no Facs").icon(BitmapDescriptorFactory.fromBitmap(bmp))).setTag(3);
        mMap.addMarker(new MarkerOptions().position(ruy).title("Marker no Ruy").icon(BitmapDescriptorFactory.fromBitmap(bmp))).setTag(4);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Imbui.getCenter(), 18));
       // client.get(serverURL + "?lat=-12.9790&long=-38.4532&dist=160", responseHandler);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context,SettingsActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.hideInfoWindow();
        client.get(serverURL + "?lat=-12.9790&long=-38.4532&dist=160", responseHandler);
        marker.remove();
        return true;
    }

    @Override
    public void finished() {
        Log.e("HTTP","RESPOSTA");
        List<Advertising> advertisings = responseHandler.getAdvertisings();
        LatLng ljAmeric = new LatLng(-12.9770046, -38.4559200);
        LatLng bomPrec = new LatLng(-12.9798411, -38.4536884);
            //LatLng cia = new LatLng(-12.979073, -38.453343);
            //LatLng centauro = new LatLng(-12.978765, -38.45451);
        for (Advertising add : advertisings) {
            LatLng coordLoja = new LatLng(add.getLat(), add.getLng());
            Log.e("HTTP", add.getLat() + " x " + add.getLng());
            //if ()
            mMap.addMarker((new MarkerOptions().position(coordLoja).title(add.getTitle()).icon(BitmapDescriptorFactory.fromBitmap(bmp)).snippet(add.getId().toString())));
        }
        LatLngBounds salvadorShopping = new LatLngBounds(bomPrec, ljAmeric);
        //CameraPosition camPos = new CameraPosition();
        // mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(salvadorShopping, 18));
          mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(salvadorShopping.getCenter(), 18));
    }

    private String getPreference(String strPref){
        String pref = getSharedPreferences(strPref, Context.MODE_PRIVATE).getString(strPref,"");
        return pref;
    }

    @Override
    protected void onRestart() {
        Log.e("IP",getPreference(getResources().getString(R.string.server_ip)));
        super.onRestart();
    }
}
