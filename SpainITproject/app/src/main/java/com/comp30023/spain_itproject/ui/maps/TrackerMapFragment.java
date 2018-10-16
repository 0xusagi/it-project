package com.comp30023.spain_itproject.ui.maps;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.ServiceFactory;
import com.comp30023.spain_itproject.domain.Position;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A fragment that
 */
public class TrackerMapFragment extends MarkerMapsFragment {

    public static final String ARGUMENT_TRACK_USER_ID = "USER";
    public static final String ARGUMENT_CURRENT_USER = "CURRENT";
    public static final String ARGYMENT_TRACK_USER_NAME = "NAME";

    private String currentUserId;

    private String trackedUserId;
    private String trackedUserName;

    public LiveData<Position> positionLiveData;

    private Marker marker;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        View view = super.onCreateView(layoutInflater, viewGroup, bundle);

        marker = null;

        Bundle arguments = getArguments();
        currentUserId = arguments.getString(ARGUMENT_CURRENT_USER);
        trackedUserId = arguments.getString(ARGUMENT_TRACK_USER_ID);
        trackedUserName = arguments.getString(ARGYMENT_TRACK_USER_NAME);

        positionLiveData = ServiceFactory.getInstance().realTimeLocationSharingService().trackLocation(trackedUserId);

        //Update the marker on the map when the position is updated
        positionLiveData.observe(this, new Observer<Position>() {
            @Override
            public void onChanged(@Nullable Position position) {

                LatLng coordinates = new LatLng(position.getLat(), position.getLng());
                String title = trackedUserName;

                if (marker == null) {

                    MarkerOptions markerOptions = new MarkerOptions().position(coordinates).title(title);

                    GoogleMap map = getMap();
                    if (map != null) {
                        marker = map.addMarker(markerOptions);
                        marker.setTitle(title);
                        marker.showInfoWindow();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 17));
                    }
                } else {
                    marker.setPosition(coordinates);
                }

                /*clearMarkers();
                addMarker(trackedUserName + ", " + position.getTimeStamp(), position.getLat(), position.getLng());*/
            }
        });

        return view;
    }
}
