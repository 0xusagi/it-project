package com.comp30023.spain_itproject.ui.dependenthome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.ui.carerhome.CarerHomeActivity;
import com.comp30023.spain_itproject.ui.views.ItemButton;


import java.util.List;

/**
 *
 */
public class CallsListFragment extends ListFragment<CarerUser> {

    public static final String ARGUMENT_USER = "USER";

    public static final String TITLE = "Calls";

    private DependentUser user;

    private static boolean added = false;

    List<CarerUser> carers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        Bundle arguments = getArguments();
        user = (DependentUser) arguments.getSerializable(ARGUMENT_USER);

        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                try {
                    carers = user.getCarers();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                setList(carers);
                setButtonListeners();
                setTitle(TITLE);

            }
        };
        task.execute();

        return view;
    }


    public void setButtonListeners() {
        super.setButtonListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //The item button that is pressed
                ItemButton itemButton = (ItemButton) v;
                CarerUser requester = (CarerUser) itemButton.getItem();

                // Get the phone number of the dependent
                String phoneNumber = requester.getPhoneNumber();

                // Make a new calling intent
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+phoneNumber));

                // Check for permission
                if (ContextCompat.checkSelfPermission(getActivity().getApplication(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    startActivity(intent);
                }
            }
        });
    }
}
