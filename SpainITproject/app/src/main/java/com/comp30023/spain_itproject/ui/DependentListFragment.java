package com.comp30023.spain_itproject.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.LoginHandler;

import java.util.ArrayList;

/**
 * Display fragment of the dependent's list of locations
 */
public class DependentListFragment extends Fragment {

    /**
     * The maximum number of locations buttons that are viewed within the frame
     */
    public static final int LOCATIONS_PER_PAGE = 4;

    /**
     * The spacing between the location buttons
     */
    public static final float BUTTON_SPACING_WEIGHT = 0.2f;

    //Reference to signed in user's list of locations
    private ArrayList<Location> locations;
    private LinearLayout locationsFrame;

    private Button previousPageButton;
    private Button nextPageButton;

    private LocationButton[] locationButtons;

    //Index in the list of the location that is at the top of the frame
    private int topLocationsIndex;

    private User user;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_locations_list, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra(LoginHandler.PASSED_USER);
        locations = ((DependentUser) user).getLocations();

        locationsFrame = (LinearLayout) view.findViewById(R.id.locationsFrame);

        locationButtons = new LocationButton[LOCATIONS_PER_PAGE];
        addButtonsToFrame();

        previousPageButton = (Button) view.findViewById(R.id.previousPageButton);
        previousPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocationButtons(-1);
            }
        });

        nextPageButton = (Button) view.findViewById(R.id.nextPageButton);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocationButtons(1);
            }
        });

        setLocationButtons(0);

        // Inflate the layout for this fragment
        return view;
    }

    //Evenly distribute and instantiate the buttons about the frame
    private void addButtonsToFrame() {

        boolean first = true;

        for (int i = 0; i < LOCATIONS_PER_PAGE; i++) {

            if (!first) {
                Space space = new Space(view.getContext());
                space.setLayoutParams(new LinearLayout.LayoutParams(0,1, BUTTON_SPACING_WEIGHT));
                locationsFrame.addView(space);
            }

            locationButtons[i] = new LocationButton(view.getContext());

            locationButtons[i].setOnClickListener(locationButtonsListener);

            locationsFrame.addView(locationButtons[i]);
            locationButtons[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));
            first = false;
        }
    }

    //Changes the buttons viewed on the screen
    //If direction > 0, gives next page
    //If direction < 0, gives previous page
    private void setLocationButtons(int direction) {

        //Set what the topLocationsIndex is to be after execution of this method
        if (topLocationsIndex == 0) {
            topLocationsIndex = 1;

        } else if (direction > 0 && topLocationsIndex + LOCATIONS_PER_PAGE <= locations.size()) {
            topLocationsIndex += LOCATIONS_PER_PAGE;

        } else if (direction < 0 && topLocationsIndex - LOCATIONS_PER_PAGE >= 0) {
            topLocationsIndex -= LOCATIONS_PER_PAGE;
        }

        for (int i = topLocationsIndex; i < topLocationsIndex + LOCATIONS_PER_PAGE; i++) {

            //Attach a location to the button
            if (i - 1 < locations.size()) {
                Location location = locations.get(i - 1);
                locationButtons[i - topLocationsIndex].setLocation(location);
                locationButtons[i - topLocationsIndex].setVisibility(View.VISIBLE);

            } else {

                //Remove button from view if no location to correspond to
                locationButtons[i - topLocationsIndex].setVisibility(View.INVISIBLE);
            }
        }

        setNavigationButtons();
    }

    //Displays the navigation buttons if required
    private void setNavigationButtons() {

        //If on first page, hide the previousPageButton from view
        if (topLocationsIndex < LOCATIONS_PER_PAGE) {
            previousPageButton.setVisibility(View.INVISIBLE);
        } else {
            previousPageButton.setVisibility(View.VISIBLE);
        }

        //If on last page, hide the nextPageButton from view
        if (topLocationsIndex + LOCATIONS_PER_PAGE > locations.size()) {
            nextPageButton.setVisibility(View.INVISIBLE);
        } else {
            nextPageButton.setVisibility(View.VISIBLE);
        }
    }

    //Location buttons listener
    private View.OnClickListener locationButtonsListener = new View.OnClickListener() {
        @Override
        /**
         * When clicked, starts the next fragment and passes the location stored within the button
         */
        public void onClick(View v) {

            LocationButton locationButton = (LocationButton) v;
            Location location = locationButton.getLocation();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            Bundle arguments = new Bundle();
            arguments.putSerializable(MapFragment.LOCATION_ARGUMENT, location);

            MapFragment mapFragment = new MapFragment();
            mapFragment.setArguments(arguments);

            transaction.replace(R.id.fragment_container, mapFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    };

}
