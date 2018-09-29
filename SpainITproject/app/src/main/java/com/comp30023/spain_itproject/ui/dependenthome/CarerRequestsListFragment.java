package com.comp30023.spain_itproject.ui.dependenthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.ui.views.ItemButton;

import java.util.ArrayList;

public class CarerRequestsListFragment extends ListFragment<CarerUser> {

    public static final String ARGUMENT_USER = "USER";

    public static final String TITLE = "Requesting Carers";

    private DependentUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        Bundle arguments = getArguments();
        user = (DependentUser) arguments.getSerializable(ARGUMENT_USER);

        ArrayList<CarerUser> pendingCarers = user.getPendingCarers();
        if (pendingCarers.isEmpty()) {
            getActivity().onBackPressed();
        }

        setList(user.getPendingCarers());
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
}
