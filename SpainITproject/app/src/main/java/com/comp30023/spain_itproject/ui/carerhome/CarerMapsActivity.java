package com.comp30023.spain_itproject.ui.carerhome;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.comp30023.spain_itproject.R;

import com.comp30023.spain_itproject.ui.maps.GpsMapsFragment;
import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CarerMapsActivity extends AppCompatActivity {

    private GpsMapsFragment fragment;
    private static String selectedDependentId;
    private PlaceAutocompleteFragment autocompleteFragment;

    public static String getSelectedDependent() {
        return selectedDependentId;
    }

    public static void setSelectedDependent(String selectedDependentId) {
        CarerMapsActivity.selectedDependentId = selectedDependentId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fragment = new GpsMapsFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.carerMapsActivity_mapFrame, fragment);
        transaction.commit();


        createAutocompleteFragment();

    }

    private void createAutocompleteFragment() {

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("AU")
                .build();

        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(final Place place) {

                final LatLng placeLatLng = place.getLatLng();

                fragment.map.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng,15));
                // Zoom in, animating the camera.
                fragment.map.animateCamera(CameraUpdateFactory.zoomIn());
                fragment.map.animateCamera(CameraUpdateFactory.zoomTo(15), 5000, null);

                fragment.map.addMarker(new MarkerOptions()
                        .position(placeLatLng)
                        .title(place.getName().toString()));

                @SuppressLint("StaticFieldLeak")
                AsyncTask task = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {



                        AccountController accountController = AccountController.getInstance();
                        Log.i("HELP", selectedDependentId + place.getId() + placeLatLng.latitude + placeLatLng.longitude + place.getName().toString());

                        try {
                            accountController.addLocationToDependent(selectedDependentId, place.getId(), placeLatLng.latitude, placeLatLng.longitude, place.getName().toString());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("MapsFragment","From within the task " + place.getId());

                                    LatLng placeLatLng = place.getLatLng();

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

}