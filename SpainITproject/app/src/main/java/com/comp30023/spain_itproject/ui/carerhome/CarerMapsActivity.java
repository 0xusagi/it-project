package com.comp30023.spain_itproject.ui.carerhome;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;

import com.comp30023.spain_itproject.ui.maps.GpsMapsFragment;
import com.comp30023.spain_itproject.ui.maps.MarkerMapsFragment;
import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CarerMapsActivity extends AppCompatActivity {

    public static String getSelectedDependent() {
        return selectedDependentId;
    }

    public static void setSelectedDependent(String selectedDependentId) {
        CarerMapsActivity.selectedDependentId = selectedDependentId;
    }

    private boolean displayingPlace;
    private String placeId;
    private double placeLat;
    private double placeLng;

    private MarkerMapsFragment fragment;
    private static String selectedDependentId;
    private PlaceAutocompleteFragment autocompleteFragment;

    private FragmentManager fragmentManager;
    private FrameLayout placeSelectedFrame;

    private EditText inputText;
    private Button addLocationButton;
    private TextView enterNameText;

    private ConstraintLayout parentContainer;

    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        displayingPlace = false;

        enterNameText = (TextView) findViewById(R.id.placeSelected_textView);
        placeSelectedFrame = (FrameLayout) findViewById(R.id.carerMapsActivity_mapFrame);
        inputText = (EditText) findViewById(R.id.placeSelected_editText);
        addLocationButton = (Button) findViewById(R.id.placeSelected_addButton);
        parentContainer = (ConstraintLayout) findViewById(R.id.carerMaps_container);

        enterNameText.setVisibility(View.INVISIBLE);
        inputText.setVisibility(View.INVISIBLE);
        addLocationButton.setVisibility(View.INVISIBLE);

        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (displayingPlace) {

                    final String name = inputText.getText().toString();

                    @SuppressLint("StaticFieldLeak")
                    AsyncTask task = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] objects) {

                            AccountController accountController = AccountController.getInstance();
                            try {

                                accountController.addLocationToDependent(selectedDependentId, placeId, placeLat, placeLng, name);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onBackPressed();
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
            }
        });

        fragment = new GpsMapsFragment();

        fragmentManager = getSupportFragmentManager();
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

                placeId = place.getId();
                placeLat = place.getLatLng().latitude;
                placeLng = place.getLatLng().longitude;

                displayingPlace = true;

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(parentContainer);
                constraintSet.connect(R.id.carerMapsActivity_mapFrame, ConstraintSet.BOTTOM, R.id.carerMaps_placeSelectedFrame, ConstraintSet.TOP, 0);
                constraintSet.applyTo(parentContainer);

                enterNameText.setVisibility(View.VISIBLE);
                inputText.setVisibility(View.VISIBLE);
                addLocationButton.setVisibility(View.VISIBLE);

                inputText.setText(place.getName().toString());

                final LatLng placeLatLng = place.getLatLng();

                fragment.map.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng,15));
                // Zoom in, animating the camera.
                fragment.map.animateCamera(CameraUpdateFactory.zoomIn());
                fragment.map.animateCamera(CameraUpdateFactory.zoomTo(15), 5000, null);

                marker = fragment.map.addMarker(new MarkerOptions()
                        .position(placeLatLng)
                        .title(place.getName().toString()));

            }

            @Override
            public void onError(Status status) {
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (displayingPlace) {
            enterNameText.setVisibility(View.INVISIBLE);
            inputText.setVisibility(View.INVISIBLE);
            addLocationButton.setVisibility(View.INVISIBLE);
            marker.remove();
            marker = null;
            displayingPlace = false;

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(parentContainer);
            constraintSet.connect(R.id.carerMapsActivity_mapFrame, ConstraintSet.BOTTOM, R.id.carerMaps_container, ConstraintSet.BOTTOM, 0);
            constraintSet.applyTo(parentContainer);

        } else {
            super.onBackPressed();
        }
    }
}