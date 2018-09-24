package com.comp30023.spain_itproject.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.Location;

public class MapFragment extends Fragment {

    public static final String LOCATION_ARGUMENT = "LOCATION";

    private Location location;

    private TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        location = (Location) getArguments().getSerializable(LOCATION_ARGUMENT);
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        text = (TextView) view.findViewById(R.id.location_display_name);
        text.setText(location.getDisplayName());

        return view;
    }
}
