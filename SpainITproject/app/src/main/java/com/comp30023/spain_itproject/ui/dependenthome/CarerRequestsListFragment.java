package com.comp30023.spain_itproject.ui.dependenthome;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.ui.views.ItemButton;

import java.util.List;

public class CarerRequestsListFragment extends ListFragment<CarerUser> {

    public static final String ARGUMENT_USER = "USER";

    public static final String TITLE = "Requesting Carers";

    private DependentUser user;

    private List<CarerUser> pendingCarers = null;

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        setList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

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
                CarerUser requester = (CarerUser) itemButton.getItem();

                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                Bundle arguments = new Bundle();
                arguments.putSerializable(ResponseFragment.ARGUMENT_USER, user);
                arguments.putSerializable(ResponseFragment.ARGUMENT_REQUESTING_USER, requester);

                //Create new instance of the following class and pass the item
                Fragment nextFragment = new ResponseFragment();

                nextFragment.setArguments(arguments);

                transaction.replace(R.id.fragment_container, nextFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void startNextFragment() {

        FragmentManager fragmentManager = getFragmentManager();

        Fragment fragment = new LocationsListFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(LocationsListFragment.ARGUMENT_USER, user);
        fragment.setArguments(arguments);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void setList() {
        @SuppressLint("StaticFieldLeak")
        NetworkTask task = new NetworkTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                super.doInBackground(objects);

                try {
                    pendingCarers = user.getPendingCarers();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if (pendingCarers == null || pendingCarers.isEmpty()) {
                    startNextFragment();

                } else {
                    setList(pendingCarers);
                    setButtonListeners();
                    setTitle(TITLE);
                }
            }
        };
        task.execute();
    }
}
