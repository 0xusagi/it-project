package com.comp30023.spain_itproject.ui;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;

import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.R;

import java.util.ArrayList;

public class DependentHomeActivity extends AppCompatActivity {

    public static final int LOCATIONS_PER_PAGE = 4;
    public static final float BUTTON_SPACING_WEIGHT = 0.2f;

    private Button messagesButton;
    private Button callButton;

    private LinearLayout locationsFrame;

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

        locationsFrame = (LinearLayout) findViewById(R.id.locationsFrame);

        locationButtons = new Button[LOCATIONS_PER_PAGE];

        boolean first = true;
        for (int i = 0; i < LOCATIONS_PER_PAGE; i++) {

            if (!first) {
                Space space = new Space(this);
                space.setLayoutParams(new LinearLayout.LayoutParams(0,1, BUTTON_SPACING_WEIGHT));
                locationsFrame.addView(space);
            }

            locationButtons[i] = new Button(this);
            locationsFrame.addView(locationButtons[i]);
            locationButtons[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));
            first = false;
        }

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
