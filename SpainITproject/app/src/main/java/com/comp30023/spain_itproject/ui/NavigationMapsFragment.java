package com.comp30023.spain_itproject.ui;

import android.graphics.Color;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class NavigationMapsFragment extends GpsMapsFragment {

    private final String serverKey = "AIzaSyAuz2NzVF-uJS1ztPEHjSw1Xq22wVRVOCM";

    // This can easily take a location, for now it's hard-coded in.
    public void setDestination() {

         double longitude = getCurrentLocation().getLongitude();
         double latitude = getCurrentLocation().getLatitude();
         LatLng origin = new LatLng(latitude, longitude);
         // This is some random spot near Melbourne Uni I picked for testing.
         LatLng destination = new LatLng(-37.815238, 144.974881);
         GoogleDirection.withServerKey(serverKey)
         .from(origin)
         .to(destination)
         .execute(new DirectionCallback() {
        @Override
        public void onDirectionSuccess(Direction direction, String rawBody) {
        Route route = direction.getRouteList().get(0);
        Leg leg = route.getLegList().get(0);

        Info distanceInfo = leg.getDistance();
        Info durationInfo = leg.getDuration();
        String distance = distanceInfo.getText();
        String duration = durationInfo.getText();

        Toast.makeText(getActivity(), "Distance = " + distance + ". This will take approx. " + duration, Toast.LENGTH_LONG).show();

        ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
        PolylineOptions polylineOptions = DirectionConverter.createPolyline( getActivity(), directionPositionList, 5, Color.RED);
        map.addPolyline(polylineOptions);
        }

        @Override
        public void onDirectionFailure(Throwable t) {
        // Do something here
        }
        });
    }

    public void clearDestination() {
        map.clear();
    }
}
