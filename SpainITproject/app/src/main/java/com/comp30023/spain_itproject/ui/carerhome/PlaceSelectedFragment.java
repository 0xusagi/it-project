package com.comp30023.spain_itproject.ui.carerhome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.R;

public class PlaceSelectedFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {

        View view = inflater.inflate(R.layout.fragment_place_selected, container, false);

        Bundle arguments = getArguments();

        return view;
    }
}
