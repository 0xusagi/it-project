package com.comp30023.spain_itproject.ui.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public interface DisplayMarker {

    public void addMarker(String title, float lat, float lng);

    public void setMarkerOnClickListeners(GoogleMap.OnMarkerClickListener listener);

    public void clearMarkers();

    public void removeMarkerByTitle(String title);

}
