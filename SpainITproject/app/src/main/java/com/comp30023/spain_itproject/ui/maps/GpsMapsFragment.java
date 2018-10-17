package com.comp30023.spain_itproject.ui.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.external_services.ServiceFactory;
import com.comp30023.spain_itproject.domain.Position;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Extension of the SupportMapFagment with handling of device GPS permissions and device location pinpointing
 */
public class GpsMapsFragment extends MarkerMapsFragment {

    static public final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final String ARGUMENT_USER = "USER";

    // This is a google location, not our Location object.
    private Location currentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private int locationCallbackCount = 0;

    private String userId;

    private boolean locationUpdatesOn;
    private boolean initial = true;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;

        Position position = new Position((float) currentLocation.getLatitude(), (float) currentLocation.getLongitude());
        ServiceFactory.getInstance().realTimeLocationSharingService().updateLocation(userId, position);
    }

    public int getLocationCallbackCount() {
        return locationCallbackCount;
    }

    public void setLocationCallbackCount(int locationCallbackCount) {
        this.locationCallbackCount = locationCallbackCount;
    }

    // This gets called periodically depending upon what's set in mLocationRequest.
    LocationCallback mLocationCallback;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        View view = super.onCreateView(layoutInflater, viewGroup, bundle);

        Bundle arguments = getArguments();
        userId = LoginSharedPreference.getId(getContext());

        locationUpdatesOn = false;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mLocationCallback = new MyLocationCallback();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!locationUpdatesOn) {
            enableMyLocation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (locationUpdatesOn) {
            disableMyLocation();
        }
    }

    public class MyLocationCallback extends LocationCallback {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            setLocationCallbackCount(getLocationCallbackCount() + 1);
            Location location = locationResult.getLastLocation();


            Log.i("MapsFragment", "Location: " + location.getLatitude() + " " + location.getLongitude());
            setCurrentLocation(location);

            // Only center the map the first time.
            if (getLocationCallbackCount() == 1) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        }
    }

    private void enableMyLocation() {

        if (map == null) {
            return;
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(12000); // 20 second interval
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //Check if my location permission is granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            map.setMyLocationEnabled(true);
            locationUpdatesOn = true;
        }
        else {

            //Request my location permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void disableMyLocation() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        locationUpdatesOn = false;
        System.out.println("Location updates removed");
    }

    public void setLocationCallback(LocationCallback locationCallback) {
        mLocationCallback = locationCallback;

        if (map != null) {
            enableMyLocation();
        }
    }

    /**
     * Called when map is retrieved
     * @param googleMap The retrieved map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        super.onMapReady(googleMap);

        enableMyLocation();
    }

    public GoogleMap getMap() {
        return map;
    }
}
