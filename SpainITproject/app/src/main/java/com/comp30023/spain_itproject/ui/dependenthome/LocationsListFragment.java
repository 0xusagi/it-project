package com.comp30023.spain_itproject.ui.dependenthome;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
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
import java.util.List;

/**
 * A fragment that displays the list of the DependentUser's locations
 */
public class LocationsListFragment extends ListFragment<Location> {

    public static final String ARGUMENT_USER = "USER";

    public static final String TITLE = "Locations";

    private DependentUser user;

    private List<Location> locations;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);

        Bundle arguments = getArguments();
        user = (DependentUser) arguments.getSerializable(ARGUMENT_USER);

        setList();

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

    private void setList() {

        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                try {
                    locations = user.getLocations();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                setList(locations);
                setButtonListeners();
                setTitle(TITLE);
            }
        };
        task.execute();
    }
}
