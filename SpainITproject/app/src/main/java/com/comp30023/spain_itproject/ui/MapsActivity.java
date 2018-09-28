package com.comp30023.spain_itproject.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;

import com.comp30023.spain_itproject.R;

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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    static public final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    private Button directionButton;
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

        //directionButton = (Button) findViewById(R.id.directionButton);
        //setDirectionButtonListener(this);

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("AU")
                .build();

        autocompleteFragment.setFilter(typeFilter);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
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
            mMap.setMyLocationEnabled(true);

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


            Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
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

    private void setDirectionButtonListener(final Context context) {
        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                                Toast.makeText(MapsActivity.this, "Distance = "
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
        });
    }

}