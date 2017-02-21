package com.example.rafae.promoz_001_alfa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    Bitmap bmp;

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.moeda_marker);
        mapFragment.getMapAsync(this);

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

        LatLng tamari = new LatLng(-12.9650861, -38.4314455);
        LatLng extra = new LatLng(-12.9629824, -38.4322472);
        LatLng unifacs = new LatLng(-12.9611188, -38.4321055);
        LatLng ruy = new LatLng(-12.9604738, -38.4317371);
        LatLngBounds Imbui = new LatLngBounds(extra, ruy);

        mMap.addMarker(new MarkerOptions().position(tamari).title("Marker no Tamari").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        mMap.addMarker(new MarkerOptions().position(extra).title("Marker no Extra").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        mMap.addMarker(new MarkerOptions().position(unifacs).title("Marker no Facs").icon(BitmapDescriptorFactory.fromBitmap(bmp)));
        mMap.addMarker(new MarkerOptions().position(ruy).title("Marker no Ruy").icon(BitmapDescriptorFactory.fromBitmap(bmp)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Imbui.getCenter(), 18));

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        switch (marker.getTitle()) {
            case "Marker no Tamari":
                Toast.makeText(this,"TAMARI",Toast.LENGTH_SHORT);
                break;


        }
        return false;
    }
}
