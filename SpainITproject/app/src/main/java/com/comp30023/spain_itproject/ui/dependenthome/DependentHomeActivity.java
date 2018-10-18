package com.comp30023.spain_itproject.ui.dependenthome;

import android.annotation.SuppressLint;
import android.content.Context;


import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import android.widget.PopupWindow;
import android.widget.Toast;

import com.comp30023.spain_itproject.ui.NetworkActivity;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.external_services.ServiceFactory;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.NoConnectionException;
import com.comp30023.spain_itproject.ui.LoginHandler;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.uicontroller.AccountController;

import java.util.ArrayList;

/**
 * Activity that opens when a DependentUser is logged in
 * Manages the fragments of the
 */
public class DependentHomeActivity extends NetworkActivity {

    public static final String LIST_NAME_LOCATION = "Locations";
    public static final String LIST_NAME_CARERS = "Carers";

    public static final String CONFIRM_GET_HELP_TITLE = "Get Help";
    public static final String CONFIRM_GET_HELP_MESSAGE = "Send a help request to your carers?";
    public static final String CONFIRM_GET_HELP_POSITIVE = "Yes";
    public static final String CONFIRM_GET_HELP_NEGATIVE = "No";


    private DrawerLayout drawerLayout;

    private FragmentManager fragmentManager;
    private Fragment locationsFragment;

    private PopupWindow helpWindow;

    private Button messagesButton;
    private Button callsButton;
    private Button locationsButton;
    private Button helpButton;
    private Button signOutButton;

    //The logged in DependentUser
    private DependentUser user;

    //Reference to signed in user's list of locations
    private ArrayList<Location> locations;

    private boolean responding;

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

        //Retrieve the logged in account from the server
        new DownloadDependentTask().execute(LoginSharedPreference.getId(this));

        messagesButton = (Button) findViewById(R.id.messagesButton);
        setMessagesButtonListener(this);

        callsButton = (Button) findViewById(R.id.callButton);
        setCallsButtonListener(this);

        locationsButton = (Button) findViewById(R.id.locationsButton);
        setLocationsButtonListener();

        signOutButton = findViewById(R.id.tempSignOutButton);
        setSignOutButtonListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("");
        actionbar.setSubtitle("");

        helpButton = (Button) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*ViewGroup group = (ViewGroup) getWindow().getDecorView().getRootView();
                helpWindow = new HelpPopupWindow(context, user, group);

                //Show at centre of screen
                helpWindow.showAtLocation(drawerLayout, Gravity.CENTER, 0, 0);*/


                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setMessage(CONFIRM_GET_HELP_MESSAGE)
                        .setTitle(CONFIRM_GET_HELP_TITLE);

                builder.setPositiveButton(CONFIRM_GET_HELP_POSITIVE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                @SuppressLint("StaticFieldLeak")
                                NetworkTask task = new NetworkTask() {
                                    @Override
                                    protected Object doInBackground(Object[] objects) {
                                        super.doInBackground(objects);

                                        try {
                                            ServiceFactory.getInstance().notificationSendingService().sendHelp(user, null);
                                            System.out.println("Notification sent");
                                        } catch (BadRequestException e) {
                                            e.printStackTrace();
                                        } catch (NoConnectionException e) {
                                            e.printStackTrace();
                                        }

                                        return null;
                                    }
                                };
                                task.execute();
                            }
                });

                builder.setNegativeButton(CONFIRM_GET_HELP_NEGATIVE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();

            }
        });
    }


    //Sets the listener for when the callsButton is pressed
    private void setCallsButtonListener(final Context context) {
        callsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

                if (!(currentFragment instanceof CallsListFragment)) {
                    Fragment carersFragment = new CallsListFragment();

                    Bundle arguments = new Bundle();
                    arguments.putSerializable(CallsListFragment.ARGUMENT_USER, user);

                    carersFragment.setArguments(arguments);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.replace(R.id.fragment_container, carersFragment);
                    transaction.commit();

                    messagesButton.setVisibility(View.VISIBLE);
                    callsButton.setVisibility(View.GONE);
                    locationsButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void setMessagesButtonListener(final Context context) {
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

                if (!(currentFragment instanceof MessagesListFragment)) {
                    Fragment carersFragment = new MessagesListFragment();

                    Bundle arguments = new Bundle();
                    arguments.putSerializable(MessagesListFragment.ARGUMENT_USER, user);

                    carersFragment.setArguments(arguments);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.replace(R.id.fragment_container, carersFragment);
                    transaction.commit();

                    messagesButton.setVisibility(View.GONE);
                    callsButton.setVisibility(View.VISIBLE);
                    locationsButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setLocationsButtonListener() {
        locationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

                if (!(currentFragment instanceof LocationsListFragment)) {
                    Fragment carersFragment = new LocationsListFragment();

                    Bundle arguments = new Bundle();
                    arguments.putSerializable(LocationsListFragment.ARGUMENT_USER, user);

                    carersFragment.setArguments(arguments);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.replace(R.id.fragment_container, carersFragment);
                    transaction.commit();

                    messagesButton.setVisibility(View.VISIBLE);
                    callsButton.setVisibility(View.VISIBLE);
                    locationsButton.setVisibility(View.GONE);
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
                // Stop the sinch client
                getSinchInterface().stopClient();
                finish();
            }
        });
    }

    //Downloads the logged in DependentUser from the database
    private class DownloadDependentTask extends NetworkTask<String, Void, DependentUser> {

        @Override
        protected DependentUser doInBackground(String... strings) {
            super.doInBackground(strings);

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

                LoginSharedPreference.setName(getApplicationContext(), user.getName());

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

                    locationsButton.setVisibility(View.VISIBLE);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                responding = false;
            }
        }
    }

    private void displayErrorToast(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        //If the helpbutton is on display, dismiss it
        if (helpWindow != null && helpWindow.isShowing()) {
            helpWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
