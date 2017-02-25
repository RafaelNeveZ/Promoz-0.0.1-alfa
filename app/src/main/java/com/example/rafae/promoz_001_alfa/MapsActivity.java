package com.example.rafae.promoz_001_alfa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.rafae.promoz_001_alfa.model.Advertising;
import com.example.rafae.promoz_001_alfa.util.HttpResponseHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, HttpResponseHandler.onFinishResponse {

    private GoogleMap mMap;
    Bitmap bmp;
    private String serverURL = "http://192.168.1.8/advertising/";
    private HttpResponseHandler responseHandler;
    private AsyncHttpClient client;
    private byte pos = 0;

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

        client.get(serverURL + "?lat=-12.9790&long=-38.4532&dist=160", responseHandler);

        LatLng tamari = new LatLng(-12.9650861, -38.4314455);
        LatLng extra = new LatLng(-12.9629824, -38.4322472);
     //   LatLng unifacs = new LatLng(-12.9611188, -38.4321055);
        LatLng ruy = new LatLng(-12.9604738, -38.4317371);
        LatLngBounds Imbui = new LatLngBounds(extra, ruy);

        mMap.addMarker(new MarkerOptions().position(tamari).title("Marker no Tamari").icon(BitmapDescriptorFactory.fromBitmap(bmp))).setTag(1);
    //    mMap.addMarker(new MarkerOptions().position(extra).title("Marker no Extra").icon(BitmapDescriptorFactory.fromBitmap(bmp))).setTag(2);
    //    mMap.addMarker(new MarkerOptions().position(unifacs).title("Marker no Facs").icon(BitmapDescriptorFactory.fromBitmap(bmp))).setTag(3);
    //    mMap.addMarker(new MarkerOptions().position(ruy).title("Marker no Ruy").icon(BitmapDescriptorFactory.fromBitmap(bmp))).setTag(4);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Imbui.getCenter(), 18));
       // client.get(serverURL + "?lat=-12.9790&long=-38.4532&dist=160", responseHandler);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this,marker.getTitle() + " TAG = " + marker.getTag(),Toast.LENGTH_SHORT).show();
        client.get(serverURL + "?lat=-12.9790&long=-38.4532&dist=160", responseHandler);
        return false;
    }

    @Override
    public void finished() {
        Log.e("HTTP","RESPOSTA");
        if(pos == 0) {
            List<Advertising> advertisings = responseHandler.getAdvertisings();

            LatLng ljAmeric = new LatLng(-12.979469, -38.453691);
            LatLng bomPrec = new LatLng(-12.977275, -38.455833);
            //LatLng cia = new LatLng(-12.979073, -38.453343);
            //LatLng centauro = new LatLng(-12.978765, -38.45451);

//        Advertising add = new Advertising();

            for (Advertising add : advertisings) {
                LatLng coordLoja = new LatLng(add.getLat(), add.getLng());
                Log.e("HTTP", add.getLat() + " x " + add.getLng());
                mMap.addMarker(new MarkerOptions().position(coordLoja).title("Marker no Centauro").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
            }

            LatLngBounds salvadorShopping = new LatLngBounds(ljAmeric, bomPrec);
            //CameraPosition camPos = new CameraPosition();
            pos = 1;
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(salvadorShopping, 0));

          //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(salvadorShopping.getCenter(), 18));
        }
    }
}
