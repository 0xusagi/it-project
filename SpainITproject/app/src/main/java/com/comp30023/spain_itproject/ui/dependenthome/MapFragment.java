package com.comp30023.spain_itproject.ui.dependenthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.Location;

//Fragment to be developed for when a journey is started
//Currently just displays the tag of the location
public class MapFragment extends Fragment {

    public static final String ARGUMENT_LOCATION = "LOCATION";

    private Location location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        location = (Location) getArguments().getSerializable(ARGUMENT_LOCATION);
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        return view;
    }




}


