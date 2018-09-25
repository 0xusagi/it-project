package com.comp30023.spain_itproject.ui.dependenthome;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.Toast;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.ui.LoginHandler;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.uicontroller.AccountController;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Activity that opens when a DependentUser is logged in
 * Displays the stored locations of the DependentUser
 */
public class DependentHomeActivity extends AppCompatActivity {

    public static final String LOCATION_LIST_NAME = "Locations";
    public static final String CARER_LIST_NAME = "Carers";

    private FragmentManager fragmentManager;

    private Button messagesButton;
    private Button callButton;

    private Button helpButton;

    private Button signOutButton;

    private DrawerLayout drawerLayout;

    // Store the dependent user
    DependentUser user;

    //Reference to signed in user's list of locations
    private ArrayList<Location> locations;

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

        // Make a call to the server
        new DownloadDependentTask().execute(LoginSharedPreference.getId(this));

        fragmentManager = getSupportFragmentManager();
        ViewGroup fragmentContainer = (ViewGroup) findViewById(R.id.fragment_container);

        messagesButton = (Button) findViewById(R.id.messagesButton);
        callButton = (Button) findViewById(R.id.callButton);
        setCallButtonListener(this);

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
        helpButton = (Button) findViewById(R.id.helpButton);
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

    private void setCallButtonListener(final Context context) {
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListFragment<CarerUser> carersFragment;
                carersFragment = new ListFragment<CarerUser>(CARER_LIST_NAME, user, user.getCarers(), CarerFragment.class);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, carersFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
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

    private class DownloadDependentTask extends AsyncTask<String, Void, DependentUser> {

        @Override
        protected DependentUser doInBackground(String... strings) {

            try {

                DependentUser dependent = AccountController.getInstance().getDependent(strings[0]);
                storeDependentUser(dependent);

                return null;
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

        /**
         * Load the list fragment of the dependent's locations
         * @param dependentUser
         */
        @Override
        protected void onPostExecute(DependentUser dependentUser) {
            super.onPostExecute(dependentUser);

            ListFragment<Location> locationsFragment;
            locationsFragment = new ListFragment<Location>(LOCATION_LIST_NAME, user, user.getLocations(), MapFragment.class);

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.add(R.id.fragment_container, locationsFragment);
            transaction.commit();
        }
    }
}
