package com.comp30023.spain_itproject.ui.dependenthome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.Location;

public class CarerFragment extends Fragment {

    private CarerUser carer;

    private TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        carer = (CarerUser) getArguments().getSerializable(ListFragment.ITEM_ARGUMENT);
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        text = (TextView) view.findViewById(R.id.location_display_name);
        text.setText(carer.getDisplayName());

        return view;
    }
}
