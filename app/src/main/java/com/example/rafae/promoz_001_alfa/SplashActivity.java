package com.example.rafae.promoz_001_alfa;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.rafae.promoz_001_alfa.services.GeofenceTrasitionService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient googleApiClient;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int REQ_PERMISSION = 1;
    private static final long GEO_DURATION = 24 * 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 1.0f; // in meters
    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;
//    private final LatLng latlong =new LatLng(-12.9649743,-38.4314574);
private final LatLng latlong =new LatLng(-12.717127,-38.312073);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        createGoogleApi();

    }
    private boolean checkLogged() {
        if(getDefaultSharedPreferences(getApplicationContext()).getInt(getResources().getString(R.string.user_id),0) != 0)
            return true;

        return false;
    }
    private void loadMain(){
        Intent intent = new Intent(this,StartScreenActivity.class);
        this.startActivity(intent);
        finish();
    }
    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder( this )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( this )
                    .addApi( LocationServices.API )
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(checkLogged()){
            loadMain();
        }else {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
       // Toast.makeText(this, "Conectei", Toast.LENGTH_SHORT).show();
        if ( checkPermission() ) {
            startGeofence();
            timerSplash();
        }
        else askPermission();
    }

    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION
        );
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    startGeofence();
                    timerSplash();

                } else {
                    permissionsDenied();
                }
                break;
            }
        }
    }

    public static Intent makeNotificationIntent(Context geofenceService, String msg)
    {
        Log.d(TAG,msg);
        return new Intent(geofenceService,StartScreenActivity.class);
    }

    // Create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius ) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion( latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration( GEO_DURATION )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT )
                .setNotificationResponsiveness(10000)
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(Geofence geofence ) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence( geofence )
                .build();
    }

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( this, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            );
    }
    // Start Geofence creation process
    private void startGeofence() {
        Log.i(TAG, "startGeofence()");
        Geofence geofence = createGeofence( latlong, GEOFENCE_RADIUS );
        GeofencingRequest geofenceRequest = createGeofenceRequest( geofence );
        addGeofence( geofenceRequest );
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged ["+location+"]");
    }

    public void timerSplash(){
        Thread splashThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(2000);
                } catch (Exception e) {

                } finally {
                    Log.d("TAG","Abri SPLASH");
                    Intent i = new Intent(SplashActivity.this,
                            StartScreenActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        splashThread.start();
    }
}
