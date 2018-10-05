package com.comp30023.spain_itproject.ui.dependenthome;

import android.content.Context;
import android.content.Intent;
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

import android.widget.ProgressBar;
import android.widget.Toast;

import com.comp30023.spain_itproject.DisplayHelpRequestActivity;
import com.comp30023.spain_itproject.R;
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
    private Button signOutButton;

    //The logged in DependentUser
    private DependentUser user;

    //Reference to signed in user's list of locations
    private ArrayList<Location> locations;

    private boolean responding;

    private ProgressBar spinner;

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

        final Context context = this;

        fragmentManager = getSupportFragmentManager();

        responding = false;

        spinner = (ProgressBar) findViewById(R.id.progressBar) ;
        spinner.setVisibility(View.GONE);

        //Retrieve the logged in account from the server
        new DownloadDependentTask().execute(LoginSharedPreference.getId(this));

        messagesButton = (Button) findViewById(R.id.messagesButton);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayHelpRequestActivity.class);
                startActivity(intent);
            }
        });

        callsButton = (Button) findViewById(R.id.callButton);
        setCallsButtonListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        signOutButton = findViewById(R.id.tempSignOutButton);
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

                Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

                if (!(currentFragment instanceof CarersListFragment)) {
                    Fragment carersFragment = new CarersListFragment();

                    Bundle arguments = new Bundle();
                    arguments.putSerializable(CarersListFragment.ARGUMENT_USER, user);

                    carersFragment.setArguments(arguments);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.replace(R.id.fragment_container, carersFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
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

                LoginHandler.getInstance().logout(context);
                finish();
            }
        });
    }

    //Downloads the logged in DependentUser from the database
    private class DownloadDependentTask extends AsyncTask<String, Void, DependentUser> {

        @Override
        protected DependentUser doInBackground(String... strings) {

            responding = true;

            displaySpinner(true);
            try {

                user = AccountController.getInstance().getDependent(strings[0]);
                return null;
            }

            // Exception when can't connect to the server
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Load the fragment to display
         * @param dependentUser
         */
        @Override
        protected void onPostExecute(DependentUser dependentUser) {
            super.onPostExecute(dependentUser);

            displaySpinner(false);

            if (user != null) {

                try {

                    Fragment fragment;

                    //If there are pending requests, display them
                    if (!user.hasPendingCarers()) {
                        fragment = new LocationsListFragment();

                        //Otherwise display the locations
                    } else {
                        fragment = new CarerRequestsListFragment();
                    }

                    Bundle arguments = new Bundle();
                    arguments.putSerializable(LocationsListFragment.ARGUMENT_USER, user);
                    fragment.setArguments(arguments);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.commit();



                } catch (Exception e) {
                    e.printStackTrace();
                }

                responding = false;
            }
        }
    }

    /**
     * Sets wehether the refresh button should be displayed
     * @param display
     */
    private void displaySpinner(final boolean display) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                int spinnerVisibility = display ? View.VISIBLE : View.GONE;
                spinner.setVisibility(spinnerVisibility);
            }
        });
    }

    private void displayErrorToast(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
