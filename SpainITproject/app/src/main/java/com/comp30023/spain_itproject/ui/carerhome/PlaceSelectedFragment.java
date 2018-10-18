package com.comp30023.spain_itproject.ui.carerhome;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.comp30023.spain_itproject.ui.NetworkFragment;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.uicontroller.AccountController;

public class PlaceSelectedFragment extends NetworkFragment {

    public static final String ARGUMENT_PLACE_ID = "PLACE_ID";
    public static final String ARGUMENT_PLACE_NAME = "PLACE_NAME";
    public static final String ARGUMENT_DEPENDENT = "DEPENDENT";
    public static final String ARGUMENT_PLACE_LAT = "PLACE_LAT";
    public static final String ARGUMENT_PLACE_LNG = "PLACE_LNG";

    private EditText inputText;
    private Button addLocationButton;

    private String dependentId;

    private String placeId;
    private String defaultPlaceName;
    private double placeLat;
    private double placeLng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {

        View view = inflater.inflate(R.layout.fragment_place_selected, container, false);

        inputText = (EditText) view.findViewById(R.id.placeSelected_editText);
        addLocationButton = (Button) view.findViewById(R.id.placeSelected_addButton);

        Bundle arguments = getArguments();
        dependentId = arguments.getString(ARGUMENT_DEPENDENT);
        placeId = arguments.getString(ARGUMENT_PLACE_ID);
        defaultPlaceName = arguments.getString(ARGUMENT_PLACE_NAME);
        placeLat = arguments.getDouble(ARGUMENT_PLACE_LAT);
        placeLng = arguments.getDouble(ARGUMENT_PLACE_LNG);

        inputText.setText(defaultPlaceName);

        inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = inputText.getText().toString();

                @SuppressLint("StaticFieldLeak")
                NetworkTask task = new NetworkTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        super.doInBackground(objects);

                        AccountController accountController = AccountController.getInstance();
                        try {
                            accountController.addLocationToDependent(dependentId, placeId, placeLat, placeLng, name);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                    }
                };
                task.execute();
            }
        });

        return view;
    }
}
