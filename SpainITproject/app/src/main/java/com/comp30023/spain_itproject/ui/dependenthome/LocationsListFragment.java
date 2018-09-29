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

public class LocationsListFragment extends ListFragment<Location> {

    public static final String ARGUMENT_USER = "USER";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);

        Bundle arguments = getArguments();
        DependentUser user = (DependentUser) arguments.getSerializable(ARGUMENT_USER);

        ArrayList<Location> locations = user.getLocations();

        setList(locations);
        setButtonListeners();

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

    /*
    //When an item button is pressed, starts the next activity as determined by the class
    //passed in the constructor
    private View.OnClickListener itemButtonListener = new View.OnClickListener() {

        @Override
        //When clicked, starts the next fragment and passes the location stored within the button
        public void onClick(View v) {


        }
    };*/
}
