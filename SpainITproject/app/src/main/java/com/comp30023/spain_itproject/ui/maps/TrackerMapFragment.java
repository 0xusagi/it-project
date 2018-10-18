package com.comp30023.spain_itproject.ui.maps;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.external_services.ServiceFactory;
import com.comp30023.spain_itproject.domain.Position;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

/**
 * A fragment that
 */
public class TrackerMapFragment extends MarkerMapsFragment {

    public static final String ARGUMENT_TRACK_USER_ID = "USER";
    public static final String ARGUMENT_CURRENT_USER = "CURRENT";
    public static final String ARGYMENT_TRACK_USER_NAME = "NAME";


    private GeoDataClient mGeoDataClient;

    private String currentUserId;

    private String trackedUserId;
    private String trackedUserName;

    private LiveData<Position> positionLiveData;

    private Map<LifecycleOwner, Observer<Position>> pendingObservers;

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

        mGeoDataClient = Places.getGeoDataClient(getActivity(), null);

        //Update the marker on the map when the position is updated
        positionLiveData.observe(this, new Observer<Position>() {
            @Override
            public void onChanged(@Nullable final Position position) {

                final LatLng coordinates = new LatLng(position.getLat(), position.getLng());
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

                if (position.getDestinationID() != null) {
                    mGeoDataClient.getPlaceById(position.getDestinationID()).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                            if (task.isSuccessful()) {
                                PlaceBufferResponse places = task.getResult();
                                Place dependentDestination = places.get(0);

                                Log.i(TAG, "Place found: " + dependentDestination.getName());

                                GoogleDirection.withServerKey(getString(R.string.serverKey))
                                        .from(coordinates)
                                        .to(dependentDestination.getLatLng())
                                        .transportMode(TransportMode.WALKING)
                                        .execute(new DirectionCallback() {
                                            @Override
                                            public void onDirectionSuccess(Direction direction, String rawBody) {

                                                if (direction.getRouteList().size() > 0) {
                                                    Route route = direction.getRouteList().get(0);
                                                    Leg leg = route.getLegList().get(0);

                                                    Info distanceInfo = leg.getDistance();
                                                    Info durationInfo = leg.getDuration();
                                                    String distance = distanceInfo.getText();
                                                    String duration = durationInfo.getText();

                                                    Activity activity = getActivity();
                                                    if (activity != null) {
                                                        Toast.makeText(activity, "Distance = " + distance + ". This will take approx. " + duration, Toast.LENGTH_LONG).show();

                                                        ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                                        PolylineOptions polylineOptions = DirectionConverter.createPolyline(activity, directionPositionList, 5, Color.RED);
                                                        GoogleMap map = getMap();
                                                        if (map != null) {
                                                            map.addPolyline(polylineOptions);
                                                        }
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onDirectionFailure(Throwable t) {
                                                // Do something here
                                                System.out.println("Direction failure");
                                            }
                                        });
                                places.release();
                            } else {
                                Log.e(TAG, "Place not found.");
                            }
                        }
                    });
                }

                /*clearMarkers();
                addMarker(trackedUserName + ", " + position.getTimeStamp(), position.getLat(), position.getLng());*/
            }
        });

        if (pendingObservers != null && !pendingObservers.isEmpty()) {
            for (LifecycleOwner owner : pendingObservers.keySet()) {

                addPositionListener(owner, pendingObservers.get(owner));
            }
            pendingObservers.clear();
            pendingObservers = null;
        }

        return view;
    }

    public void addPositionListener(LifecycleOwner owner, Observer<Position> observer) {

        if (positionLiveData == null) {

            if (pendingObservers == null) {
                pendingObservers = new HashMap<LifecycleOwner, Observer<Position>>();
            }

            pendingObservers.put(owner, observer);

        } else {
            positionLiveData.observe(owner, observer);
        }
    }
}
