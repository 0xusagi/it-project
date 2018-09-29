package com.comp30023.spain_itproject.ui.dependenthome;

import android.content.Context;
import android.support.v4.app.Fragment;
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
import android.widget.Button;

import android.widget.Toast;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.ui.LoginHandler;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.uicontroller.AccountController;

import java.util.ArrayList;

/**
 * Activity that opens when a DependentUser is logged in
 * Manages the fragments of the
 */
public class DependentHomeActivity extends AppCompatActivity {

    public static final String LIST_NAME_LOCATION = "Locations";
    public static final String LIST_NAME_CARERS = "Carers";

    private DrawerLayout drawerLayout;

    private FragmentManager fragmentManager;
    private Fragment locationsFragment;

    private Button messagesButton;
    private Button callsButton;
    private Button helpButton;
    private Button refreshButton;
    private Button signOutButton;

    //The logged in DependentUser
    private DependentUser user;

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

        fragmentManager = getSupportFragmentManager();

        //Retrieve the logged in account from the server
        new DownloadDependentTask().execute(LoginSharedPreference.getId(this));

        messagesButton = (Button) findViewById(R.id.messagesButton);

        callsButton = (Button) findViewById(R.id.callButton);
        setCallsButtonListener(this);

        refreshButton = (Button) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadDependentTask().execute(LoginSharedPreference.getId(getApplicationContext()));
            }
        });

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

    //Sets the listener for when the callsButton is pressed
    private void setCallsButtonListener(final Context context) {
        callsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.fragment_container, carersFragment);
                transaction.addToBackStack(null);
                transaction.commit();*/
            }
        });
    }

    //Signs out the user and loads the StartActivity
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

    //Downloads the logged in DependentUser from the database
    private class DownloadDependentTask extends AsyncTask<String, Void, DependentUser> {

        @Override
        protected DependentUser doInBackground(String... strings) {

            displayRefreshButton(false);
            try {

                user = AccountController.getInstance().getDependent(strings[0]);
                return null;
            }

            // Exception when can't connect to the server
            catch (Exception e) {
                // When cannot get prompt whether to try again
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

            locationsFragment = new LocationsListFragment();


            Bundle arguments = new Bundle();
            arguments.putSerializable(LocationsListFragment.ARGUMENT_USER, user);
            locationsFragment.setArguments(arguments);

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.add(R.id.fragment_container, locationsFragment);
            transaction.commit();

            displayRefreshButton(true);
        }
    }

    /**
     * Sets wehether the refresh button should be displayed
     * @param display
     */
    private void displayRefreshButton(final boolean display) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                int visibility = display ? View.VISIBLE : View.INVISIBLE;
                refreshButton.setVisibility(visibility);
            }
        });
    }
}
