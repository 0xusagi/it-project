package com.comp30023.spain_itproject.ui;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.R;

import java.util.ArrayList;

public class DependentHomeActivity extends AppCompatActivity {

    public static final int LOCATIONS_PER_PAGE = 4;

    private Button messagesButton;
    private Button callButton;

    private ConstraintLayout locationsFrame;

    private Button previousPageButton;
    private Button nextPageButton;

    private Button helpButton;

    private Button[] locationButtons;

    private DependentUser user;
    private ArrayList<Location> locations;
    private int topLocationsIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dependent_home);

        messagesButton = (Button) findViewById(R.id.messagesButton);
        callButton = (Button) findViewById(R.id.callButton);

        locationsFrame = (ConstraintLayout) findViewById(R.id.locationsFrame);

        locationButtons = new Button[LOCATIONS_PER_PAGE];

        locationButtons[0] = (Button) findViewById(R.id.locationOneButton);
        locationButtons[1] = (Button) findViewById(R.id.locationTwoButton);
        locationButtons[2] = (Button) findViewById(R.id.locationThreeButton);
        locationButtons[3] = (Button) findViewById(R.id.locationFourButton);

        Button button = new Button(this);
        button.setConst
        locationsFrame.addView(button);


        previousPageButton = (Button) findViewById(R.id.previousPageButton);
        nextPageButton = (Button) findViewById(R.id.nextPageButton);

        helpButton = (Button) findViewById(R.id.helpButton);

        Intent intent = getIntent();
        user = (DependentUser) intent.getSerializableExtra(AccountCreationActivity.PASSED_USER);

        locations = user.getLocations();
        topLocationsIndex = 0;
        setLocationButtons(0);
    }

    private void setLocationButtons(int direction) {

        if (topLocationsIndex == 0) {
            topLocationsIndex = 1;
        } else if (direction > 0) {
            topLocationsIndex += LOCATIONS_PER_PAGE;
        } else if (direction < 0) {
            topLocationsIndex -= LOCATIONS_PER_PAGE;
        }

        for (int i = topLocationsIndex; i < topLocationsIndex + LOCATIONS_PER_PAGE; i++) {

            if (i-1 < locations.size()) {
                Location location = locations.get(i-1);
                locationButtons[i - topLocationsIndex].setText(location.getTag());
                locationButtons[i - topLocationsIndex].setVisibility(View.VISIBLE);
            } else {
                locationButtons[i - topLocationsIndex].setVisibility(View.INVISIBLE);
            }
        }

        if (topLocationsIndex < LOCATIONS_PER_PAGE) {
            previousPageButton.setVisibility(View.INVISIBLE);
        } else {
            previousPageButton.setVisibility(View.VISIBLE);
        }

        if (topLocationsIndex + LOCATIONS_PER_PAGE > locations.size()) {
            nextPageButton.setVisibility(View.INVISIBLE);
        } else {
            nextPageButton.setVisibility(View.VISIBLE);
        }

    }

    public void nextPageButtonClick(View view) {
        if (nextPageButton.getVisibility() == View.VISIBLE) {
            setLocationButtons(1);
        }
    }

    public void previousPageButton(View view) {
        if (previousPageButton.getVisibility() == View.VISIBLE) {
            setLocationButtons(-1);
        }
    }

}
