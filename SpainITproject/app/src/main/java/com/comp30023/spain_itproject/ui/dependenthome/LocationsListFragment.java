package com.comp30023.spain_itproject.ui.dependenthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.ui.views.ItemButton;

import java.util.ArrayList;

/**
 * A fragment that displays the list of the DependentUser's locations
 */
public class LocationsListFragment extends ListFragment<Location> {

    public static final String ARGUMENT_USER = "USER";

    public static final String TITLE = "Locations";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);

        Bundle arguments = getArguments();
        DependentUser user = (DependentUser) arguments.getSerializable(ARGUMENT_USER);

        ArrayList<Location> locations = user.getLocations();

        setList(locations);
        setButtonListeners();

        setTitle(TITLE);

        return view;
    }


    private void setButtonListeners() {

        super.setButtonListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //The item button that is pressed
                ItemButton itemButton = (ItemButton) v;
                Location location = (Location) itemButton.getItem();

                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                Bundle arguments = new Bundle();
                arguments.putSerializable(MapFragment.ARGUMENT_LOCATION, location);

                //Create new instance of the following class and pass the item
                Fragment nextFragment = new MapFragment();

                nextFragment.setArguments(arguments);

                transaction.replace(R.id.fragment_container, nextFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
