package com.comp30023.spain_itproject.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;

import com.comp30023.spain_itproject.LoginHandler;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.uicontroller.AccountController;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Activity that opens when a DependentUser is logged in
 * Displays the stored locations of the DependentUser
 */
public class DependentHomeActivity extends AppCompatActivity {

    /**
     * The maximum number of locations buttons that are viewed within the frame
     */
    public static final int LOCATIONS_PER_PAGE = 4;

    /**
     * The spacing between the location buttons
     */
    public static final float BUTTON_SPACING_WEIGHT = 0.2f;

    private Button messagesButton;
    private Button callButton;

    private LinearLayout locationsFrame;

    private Button previousPageButton;
    private Button nextPageButton;

    private Button helpButton;

    private Button[] locationButtons;

    private Button signOutButton;

    private DrawerLayout drawerLayout;

    //The currently signed in user
    private DependentUser user;

    //Reference to signed in user's list of locations
    private ArrayList<Location> locations;

    //Index in the list of the location that is at the top of the frame
    private int topLocationsIndex;

    /**
     * References the objects to the corresponding views
     * Sets up and displays the layout
     * Sets listeners for buttons
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dependent_home);

        messagesButton = (Button) findViewById(R.id.messagesButton);
        callButton = (Button) findViewById(R.id.callButton);

        drawerLayout = findViewById(R.id.drawer_layout);
        signOutButton = findViewById(R.id.signOutButton);
        setSignOutButtonListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("");
        actionbar.setSubtitle("");

        Intent intent = getIntent();
        user = (DependentUser) intent.getSerializableExtra(LoginHandler.PASSED_USER);

        System.out.println(user.getId());

        locationsFrame = (LinearLayout) findViewById(R.id.locationsFrame);

        locationButtons = new Button[LOCATIONS_PER_PAGE];
        addButtonsToFrame();

        previousPageButton = (Button) findViewById(R.id.previousPageButton);
        previousPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocationButtons(-1);
            }
        });

        nextPageButton = (Button) findViewById(R.id.nextPageButton);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocationButtons(1);
            }
        });

        helpButton = (Button) findViewById(R.id.helpButton);

        // Make a call to the server
        new DownloadDependentTask().execute(user.getId());

        locations = user.getLocations();

        topLocationsIndex = 0;
        setLocationButtons(0);

        System.out.println("User ID once started: " + user.getId());

    }

    /**
     * Respond to the menu button press
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Changes the buttons viewed on the screen
    //If direction > 0, gives next page
    //If direction < 0, gives previous page
    private void setLocationButtons(int direction) {

        //Set what the topLocationsIndex is to be after execution of this method
        if (topLocationsIndex == 0) {
            topLocationsIndex = 1;

        } else if (direction > 0 && topLocationsIndex+LOCATIONS_PER_PAGE <= locations.size()) {
            topLocationsIndex += LOCATIONS_PER_PAGE;

        } else if (direction < 0 && topLocationsIndex-LOCATIONS_PER_PAGE >= 0) {
            topLocationsIndex -= LOCATIONS_PER_PAGE;
        }

        for (int i = topLocationsIndex; i < topLocationsIndex + LOCATIONS_PER_PAGE; i++) {

            //Attach a location to the button
            if (i-1 < locations.size()) {
                Location location = locations.get(i-1);
                locationButtons[i - topLocationsIndex].setText(location.getDisplayName());
                locationButtons[i - topLocationsIndex].setVisibility(View.VISIBLE);

            } else {

                //Remove button from view if no location to correspond to
                locationButtons[i - topLocationsIndex].setVisibility(View.INVISIBLE);
            }
        }

        //If on first page, hide the previousPageButton from view
        if (topLocationsIndex < LOCATIONS_PER_PAGE) {
            previousPageButton.setVisibility(View.INVISIBLE);
        } else {
            previousPageButton.setVisibility(View.VISIBLE);
        }

        //If on last page, hide the nextPageButton from view
        if (topLocationsIndex + LOCATIONS_PER_PAGE > locations.size()) {
            nextPageButton.setVisibility(View.INVISIBLE);
        } else {
            nextPageButton.setVisibility(View.VISIBLE);
        }

    }

    //Evenly distribute the buttons about the frame
    private void addButtonsToFrame() {
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
    }

    private void setSignOutButtonListener(final Context context) {
        signOutButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When the button is clicked, logs out closes the activity
             * @param v
             */
            @Override
            public void onClick(View v) {
                LoginHandler.logout(context);
                finish();
            }
        });
    }

    private void storeDependentUser(DependentUser dependentUser) {
        this.user = dependentUser;
    }

    private class DownloadDependentTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                DependentUser dependent = AccountController.getDependent(strings[0]);

                // The null body so the carer doesn't exist
                if (dependent == null) {
                    return null;
                }

                storeDependentUser(dependent);
            }
            // Exception when can't connect to the server
            catch (IOException e) {
                // When cannot get prompt whether to try again
                Toast.makeText(getApplicationContext(), "Cannot get dependents. Please try again", Toast.LENGTH_SHORT).show();
            }
            // Exception when using invalid request
            catch (BadRequestException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
