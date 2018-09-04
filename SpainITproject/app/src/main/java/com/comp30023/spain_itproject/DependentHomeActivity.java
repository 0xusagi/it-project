package com.comp30023.spain_itproject;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        previousPageButton = (Button) findViewById(R.id.previousPageButton);
        nextPageButton = (Button) findViewById(R.id.nextPageButton);

        helpButton = (Button) findViewById(R.id.helpButton);

        Intent intent = getIntent();
        user = (DependentUser) intent.getSerializableExtra(AccountCreationActivity.PASSED_USER);

        locations = user.getLocations();
        topLocationsIndex = 0;

        previousPageButton.setVisibility(View.INVISIBLE);
        if (locations.size() <= LOCATIONS_PER_PAGE) {
            nextPageButton.setVisibility(View.INVISIBLE);
        }

        setLocationButtons(0);
    }

    private void setLocationButtons(int direction) {
        if (topLocationsIndex == 0) {

            for (int i = 0; i < LOCATIONS_PER_PAGE; i++) {

                if (i < locations.size()) {
                    Location location = locations.get(i);
                    locationButtons[i].setText(location.getTag());
                    locationButtons[i].setVisibility(View.VISIBLE);
                } else {
                    locationButtons[i].setVisibility(View.INVISIBLE);
                }

            }

            topLocationsIndex = 1;

            return;
        }
    }

}
