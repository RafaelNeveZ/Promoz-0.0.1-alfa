package com.example.rafae.promoz_001_alfa.interfaces;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by vallux on 28/02/17.
 */

public interface Markers {
    void resetMarker();
    void moveCamera(LatLng coord);
}
