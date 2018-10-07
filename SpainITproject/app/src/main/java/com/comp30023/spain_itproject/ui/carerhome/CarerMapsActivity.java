package com.comp30023.spain_itproject.ui.carerhome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import android.widget.Toast;

import com.comp30023.spain_itproject.R;

import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class CarerMapsActivity extends FragmentActivity
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    static public final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    public static String getSelectedDependent() {
        return selectedDependentId;
    }

    public static void setSelectedDependent(String selectedDependentId) {
        CarerMapsActivity.selectedDependentId = selectedDependentId;
    }

    private static String selectedDependentId;
    private GoogleMap mMap;
    private Location currentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    PlaceAutocompleteFragment autocompleteFragment;

    public int getLocationCallbackCount() {
        return locationCallbackCount;
    }

    public void setLocationCallbackCount(int locationCallbackCount) {
        this.locationCallbackCount = locationCallbackCount;
    }

    private int locationCallbackCount = 0;


    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("AU")
                .build();

        autocompleteFragment.setFilter(typeFilter);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(final Place place) {

                @SuppressLint("StaticFieldLeak")
                AsyncTask task = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {

                        LatLng placeLatLng = place.getLatLng();


                        AccountController accountController = AccountController.getInstance();
                        Log.i("HELP", selectedDependentId);

                        try {
                            accountController.addLocationToDependent(selectedDependentId, place.getId(), placeLatLng.latitude, placeLatLng.longitude, place.getName().toString());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("MapsFragment","From within the task " + place.getId());

                                    LatLng placeLatLng = place.getLatLng();

                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng,15));
                                    // Zoom in, animating the camera.
                                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 5000, null);

                                    mMap.addMarker(new MarkerOptions()
                                            .position(placeLatLng)
                                            .title(place.getName().toString()));
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                };
                task.execute();

            }

            @Override
            public void onError(Status status) {
            }
        });
    }

        @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(12000); // two minute interval
        mLocationRequest.setFastestInterval(12000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            Log.i("MapsFragment", "Permissions checked");

            mMap.setMyLocationEnabled(true);
        }
        else {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            setLocationCallbackCount(getLocationCallbackCount() + 1);
            Location location = locationResult.getLastLocation();


            Log.i("MapsFragment", "Location: " + location.getLatitude() + " " + location.getLongitude());
            setCurrentLocation(location);

            autocompleteFragment.setBoundsBias(new LatLngBounds(
                    new LatLng(getCurrentLocation().getLatitude() - 1, getCurrentLocation().getLongitude() - 1),
                    new LatLng(getCurrentLocation().getLatitude() + 1, getCurrentLocation().getLongitude() + 1)));

            // Only center the map the first time.
            if (getLocationCallbackCount() == 1) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        }
    };

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
        }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
}