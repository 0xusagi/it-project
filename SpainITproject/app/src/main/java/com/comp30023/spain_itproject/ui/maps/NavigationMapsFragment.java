package com.comp30023.spain_itproject.ui.maps;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
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
import com.comp30023.spain_itproject.domain.Position;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.external_services.ServiceFactory;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/** This fragment uses the user's current location (obtained from GpsMapsFragment)
 *  to provide on-screen walking directions. It is used primarly on the Depedent side
 *  to provide directions for locations.
 *
 *  Directions rendering API sourced from:
 *  http://www.akexorcist.com/2015/12/google-direction-library-for-android-en.html
 *
 */
public class NavigationMapsFragment extends GpsMapsFragment {

    public static final String ARGUMENT_LOCATION = "LOCATION";

    private Location destination;
    private boolean first;
    private String userId;


    @Override
    public void setCurrentLocation(android.location.Location currentLocation) {
        super.setCurrentLocation(currentLocation);

        Position position = new Position((float) currentLocation.getLatitude(),
                (float) currentLocation.getLongitude(), destination.getGoogleId());
        ServiceFactory.getInstance().realTimeLocationSharingService().updateLocation(userId, position);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = super.onCreateView(layoutInflater, viewGroup, bundle);

        Bundle arguments = getArguments();
        userId = LoginSharedPreference.getId(getContext());

        destination = (Location) arguments.getSerializable(ARGUMENT_LOCATION);

        setLocationCallback(new NavigationMapsLocationCallback());
        first = true;

        return view;
    }


    /** Main method used by the class, takes an origin and a destination and draws the resulting
     * to the map.
     * @param origin Starting location.
     * @param location Destination.
     */
    public void setDestination(LatLng origin, Location location) {

        Double destinationLat = location.getLatitude();
        Double destinationLon = location.getLongitude();
        LatLng destination = new LatLng(destinationLat, destinationLon);

        GoogleDirection.withServerKey(getString(R.string.serverKey))
         .from(origin)
         .to(destination)
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
                            Toast.makeText(activity,
                                    "Distance = " + distance + ". This will take approx. " + duration,
                                    Toast.LENGTH_LONG).show();

                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(activity,
                                    directionPositionList, 5, Color.RED);
                            map.addPolyline(polylineOptions);
                        }
                    }
                }

                @Override
                public void onDirectionFailure(Throwable t) {
                // Do something here
                    System.out.println("Direction failure");
                }
        });
    }

    public class NavigationMapsLocationCallback extends MyLocationCallback {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);


            double longitude = getCurrentLocation().getLongitude();
            double latitude = getCurrentLocation().getLatitude();
            LatLng origin = new LatLng(latitude, longitude);

            if (first) {
                setDestination(origin, destination);
                first = false;
            }
        }
    }
}
