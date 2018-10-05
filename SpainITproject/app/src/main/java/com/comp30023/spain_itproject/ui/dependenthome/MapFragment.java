package com.comp30023.spain_itproject.ui.dependenthome;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.ui.CarerMapsActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

//Fragment to be developed for when a journey is started
//Currently just displays the tag of the location
public class MapFragment extends Fragment {

    public static final String ARGUMENT_LOCATION = "LOCATION";

    private Location location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        location = (Location) getArguments().getSerializable(ARGUMENT_LOCATION);
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        return view;
    }



   /** private void setDirectionButtonListener(final Context context) {
        String serverKey = "AIzaSyAuz2NzVF-uJS1ztPEHjSw1Xq22wVRVOCM";

        double longitude = getCurrentLocation().getLongitude();
        double latitude = getCurrentLocation().getLatitude();
        LatLng origin = new LatLng(latitude, longitude);
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

                        Toast.makeText(CarerMapsActivity.this, "Distance = "
                                        + distance + ". This will take approx. " + duration,
                                Toast.LENGTH_LONG).show();

                        ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                        PolylineOptions polylineOptions = DirectionConverter.createPolyline( context, directionPositionList, 5, Color.RED);
                        mMap.addPolyline(polylineOptions);

                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                    }
                });
    }
    */
}


