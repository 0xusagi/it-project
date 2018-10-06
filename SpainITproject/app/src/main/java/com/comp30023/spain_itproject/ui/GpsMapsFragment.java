package com.comp30023.spain_itproject.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.DisplayMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static com.comp30023.spain_itproject.ui.carerhome.CarerMapsActivity.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

/**
 * Extension of the SupportMapFagment with handling of device GPS permissions and device location pinpointing
 */
public class GpsMapsFragment extends SupportMapFragment implements DisplayMarker, OnMapReadyCallback {

    GoogleMap map;

    List<MarkerOptions> pendingMarkers;
    List<Marker> markers;

    GoogleMap.OnMarkerClickListener pendingListener;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        View view = super.onCreateView(layoutInflater, viewGroup, bundle);

        //Set the map
        getMapAsync(this);

        return view;
    }

    private void enableMyLocation() {

        //Check if my location permission is granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            map.setMyLocationEnabled(true);
        }
        else {

            //Request my location permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Called when map is retrieved
     * @param googleMap The retrieved map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        //If pending markers or OnMarkerClickListener, set these
        if (pendingMarkers != null && !pendingMarkers.isEmpty()) {

            if (markers == null) {
                markers = new ArrayList<Marker>();
            }

            for (MarkerOptions marker : pendingMarkers) {
                markers.add(map.addMarker(marker));
            }
            pendingMarkers.clear();
        }

        if (pendingListener != null) {
            map.setOnMarkerClickListener(pendingListener);
        }

        enableMyLocation();
    }


    /**
     * Add marker to the map
     * @param title Title of the marker
     * @param lat Latitude coordinate
     * @param lng Longitude coordinate
     */
    @Override
    public void addMarker(String title, float lat, float lng) {

        //Initialise MarkerOptions instance
        LatLng coordinates = new LatLng(lat, lng);
        MarkerOptions marker = new MarkerOptions().position(coordinates).title(title);

        //If map not set yet, add to pending markers
        if (map == null) {

            if (pendingMarkers == null) {
                pendingMarkers = new ArrayList<MarkerOptions>();
            }

            pendingMarkers.add(marker);

        } else {

            if (markers == null) {
                markers = new ArrayList<Marker>();
            }

            //Add marker to map and list for later access
            markers.add(map.addMarker(marker));
        }
    }

    /**
     * Set the listener for when a marker is clicked
     * @param listener The listener interface
     */
    @Override
    public void setMarkerOnClickListeners(GoogleMap.OnMarkerClickListener listener) {

        if (map != null) {
            map.setOnMarkerClickListener(listener);

        } else {

            //If map is not yet set, store the listener
            pendingListener = listener;

        }
    }

    /**
     * Remove all markers
     */
    @Override
    public void clearMarkers() {

        if (markers != null) {

            //Remove all markers from map
            for (Marker marker : markers) {
                marker.remove();
            }

            markers.clear();
        }

        if (pendingMarkers != null) {
            pendingMarkers.clear();
        }
    }

    /**
     * Remove all instances of a marker matching input string
     * @param title The input string
     */
    @Override
    public void removeMarkerByTitle(String title) {

        //Check and remove matching existing markers
        if (markers != null) {
            for (int i = 0; i < markers.size(); i++) {

                Marker marker = markers.get(i);

                if (title.equals(marker.getTitle())) {

                    //Remove marker from map
                    marker.remove();

                    //Remove marker from list
                    markers.remove(marker);

                    //Reiterate on this position in list
                    i--;
                }
            }
        }

        //Check and remove matching pending markers
        if (pendingMarkers != null) {
            for (int i = 0; i < pendingMarkers.size(); i++) {

                MarkerOptions marker = pendingMarkers.get(i);

                if (title.equals(marker.getTitle())) {

                    //Remove marker from list
                    pendingMarkers.remove(marker);

                    //Reiterate on this position in list
                    i--;
                }
            }
        }
    }
}
