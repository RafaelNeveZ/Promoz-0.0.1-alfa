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
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, HttpResponseHandler.onFinishResponse {

    private GoogleMap mMap;
    Bitmap bmp;
    private HttpResponseHandler responseHandler;
    private AsyncHttpClient client;
    private List<Integer> addedMarkers = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
//        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.moeda_marker);
        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.diamante_marker);
        mapFragment.getMapAsync(this);

        client = new AsyncHttpClient();
        responseHandler = new HttpResponseHandler();
        responseHandler.setCallback(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng tamari = new LatLng(-12.9650861, -38.4314455);
        LatLng extra = new LatLng(-12.9629824, -38.4322472);
        LatLng unifacs = new LatLng(-12.9611188, -38.4321055);
        LatLng ruy = new LatLng(-12.9604738, -38.4317371);
        LatLngBounds Imbui = new LatLngBounds(tamari, ruy);
        mMap.addMarker(new MarkerOptions().position(tamari).title("Marker no Tamari").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        mMap.addMarker(new MarkerOptions().position(extra).title("Marker no Extra").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        mMap.addMarker(new MarkerOptions().position(unifacs).title("Marker no Facs").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        mMap.addMarker(new MarkerOptions().position(ruy).title("Marker no Ruy").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Imbui.getCenter(), 18));
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
        if(marker.getTag() == null) {
            String serverURL = "http://" + Singleton.getServerIp(getResources().getString(R.string.server_ip), getResources().getString(R.string.pref_default_ip_address), this) + "/advertising/";
            client.get(serverURL + "?lat=-12.9790&long=-38.4532&dist=160", responseHandler);
        }else{
            Log.e("IMG","http://" + Singleton.getServerIp(getResources().getString(R.string.server_ip), getResources().getString(R.string.pref_default_ip_address), this) + "/advertising/"  + ((Advertising) marker.getTag()).getImageURL());
        }
      //  marker.remove();
        return true;
    }

    @Override
    public void finished() {
        List<Advertising> advertisings = responseHandler.getAdvertisings();
     //   LatLng ljAmeric = new LatLng(-12.9770046, -38.4559200);
     //   LatLng bomPrec = new LatLng(-12.9798411, -38.4536884);
            //LatLng cia = new LatLng(-12.979073, -38.453343);
            //LatLng centauro = new LatLng(-12.978765, -38.45451);
        for (Advertising add : advertisings) {
            if (addedMarkers.indexOf(add.getId()) == -1) {
                addedMarkers.add(add.getId());
                LatLng coordLoja = new LatLng(add.getLat(), add.getLng());
                mMap.addMarker((new MarkerOptions().position(coordLoja).title(add.getTitle()).icon(BitmapDescriptorFactory.fromBitmap(bmp)).snippet(add.getId().toString()))).setTag(add);
            }
        }
        responseHandler.clearAdvertisings();
      //  LatLngBounds salvadorShopping = new LatLngBounds(bomPrec, ljAmeric);
        //CameraPosition camPos = new CameraPosition();
        // mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(salvadorShopping, 18));
        //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(salvadorShopping.getCenter(), 18));
    }
}
