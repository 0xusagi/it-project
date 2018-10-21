package com.comp30023.spain_itproject.ui.dependenthome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;


import android.content.DialogInterface;
import android.support.annotation.NonNull;
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

import android.widget.ImageButton;
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

    private FragmentManager fragmentManager;

    private Button messagesButton;
    private Button callsButton;
    private Button locationsButton;
    private Button helpButton;
    private Button signOutButton;
    private ImageButton refreshButton;

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

        fragmentManager = getSupportFragmentManager();

        responding = false;

        //Retrieve the logged in account from the server
        new DownloadDependentTask(this).execute(LoginSharedPreference.getId(this));

        setMessagesButton();
        setCallsButton();
        setLocationsButton();
        setRefreshButton(this);
        setSignOutButton(this);
        setToolbar();
        setHelpButton(this);
    }

    //Finds the view and sets the button to display a AlertDialog when pressed
    private void setHelpButton(final Context context) {
        helpButton = (Button) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpDialog builder = new HelpDialog(context, user);
                builder.show();
            }
        });
    }

    //Finds the view and sets the content within the toolbar
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("");
        actionbar.setSubtitle("");
    }

    //Finds the view and reloads the current user when pressed
    public void setRefreshButton(final Context context) {
        refreshButton = (ImageButton) findViewById(R.id.dependentHome_refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!responding) {
                    new DownloadDependentTask(context).execute(LoginSharedPreference.getId(context));
                }
            }
        });
    }

    //Finds the view and replaces the current fragment with the CallsListFragment when pressed
    private void setCallsButton() {
        callsButton = (Button) findViewById(R.id.callButton);
        callsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

                //Replace the current fragment with a CallsListFragment
                if (!responding && !(currentFragment instanceof CallsListFragment)) {

                    Fragment carersFragment = new CallsListFragment();

                    Bundle arguments = new Bundle();
                    arguments.putSerializable(CallsListFragment.ARGUMENT_USER, user);

                    carersFragment.setArguments(arguments);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.replace(R.id.fragment_container, carersFragment);
                    transaction.commit();

                    //Change which buttons are displayed
                    messagesButton.setVisibility(View.VISIBLE);
                    callsButton.setVisibility(View.GONE);
                    locationsButton.setVisibility(View.VISIBLE);
                    signOutButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //Finds the view and replaces the current fragment with the MessagesListFragment when pressed
    private void setMessagesButton() {
        messagesButton = (Button) findViewById(R.id.messagesButton);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

                //Replace current fragment to MessagesListFragment
                if (!responding && !(currentFragment instanceof MessagesListFragment)) {

                    Fragment carersFragment = new MessagesListFragment();

                    Bundle arguments = new Bundle();
                    arguments.putSerializable(MessagesListFragment.ARGUMENT_USER, user);

                    carersFragment.setArguments(arguments);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.replace(R.id.fragment_container, carersFragment);
                    transaction.commit();

                    //Change which buttons are displayed
                    messagesButton.setVisibility(View.GONE);
                    callsButton.setVisibility(View.VISIBLE);
                    locationsButton.setVisibility(View.VISIBLE);
                    signOutButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //Finds the view and replaces the current fragment with the LocationsListFragment when pressed
    private void setLocationsButton() {
        locationsButton = (Button) findViewById(R.id.locationsButton);
        locationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

                //Replaces the current fragment with the LocationsListFragment
                if (!responding && !(currentFragment instanceof LocationsListFragment)) {
                    Fragment carersFragment = new LocationsListFragment();

                    Bundle arguments = new Bundle();
                    arguments.putSerializable(LocationsListFragment.ARGUMENT_USER, user);

                    carersFragment.setArguments(arguments);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.replace(R.id.fragment_container, carersFragment);
                    transaction.commit();

                    //Changes which buttons are displayed
                    messagesButton.setVisibility(View.VISIBLE);
                    callsButton.setVisibility(View.VISIBLE);
                    locationsButton.setVisibility(View.GONE);
                    signOutButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //Finds the view and signs out the user when pressed
    private void setSignOutButton(final Context context) {
        signOutButton = findViewById(R.id.tempSignOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When the button is clicked, logs out closes the activity
             * @param v
             */
            @Override
            public void onClick(View v) {

                if (!responding) {
                    new SignOutDialog(context).show();
                }
            }
        });
    }

    //Downloads the logged in DependentUser from the database
    @SuppressLint("StaticFieldLeak")
    private class DownloadDependentTask extends NetworkTask<String, Void, DependentUser> {

        private boolean downloaded;
        private final Context context;
        private String message;

        public DownloadDependentTask(Context context) {
            this.context = context;
        }

        @Override
        protected DependentUser doInBackground(String... strings) {
            super.doInBackground(strings);

            responding = true;
            downloaded = false;
            message = null;
            try {

                user = AccountController.getInstance().getDependent(strings[0]);
                downloaded = true;
            }

            // Exception when can't connect to the server
            catch (Exception e) {
                displayMessageToast(e.getMessage());
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

            responding = false;

            if (!downloaded) {

                RetryDialog  builder = new RetryDialog(context, message);
                builder.show();

                return;
            }

            if (user != null) {

                //Save the name
                LoginSharedPreference.setName(getApplicationContext(), user.getName());

                setFirstFragment();
            }
        }
    }

    //Displays the initial fragment
    private void setFirstFragment() {
        //View the fragment
        try {
            Fragment fragment;

            //If there are no pending requests, display the locations
            if (!user.hasPendingCarers()) {
                fragment = new LocationsListFragment();

                locationsButton.setVisibility(View.GONE);
                signOutButton.setVisibility(View.VISIBLE);

            } else {
                fragment = new CarerRequestsListFragment();

                locationsButton.setVisibility(View.VISIBLE);
                signOutButton.setVisibility(View.GONE);
            }

            Bundle arguments = new Bundle();
            arguments.putSerializable(LocationsListFragment.ARGUMENT_USER, user);
            fragment.setArguments(arguments);

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();

            messagesButton.setVisibility(View.VISIBLE);
            callsButton.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Dialog providing the ability to retry downloading the account
    private class RetryDialog extends AlertDialog.Builder {

        public static final String RETRY_TITLE = "Failed to download";
        public static final String RETRY_MESSAGE = "Please retry";

        public static final String RETRY_POSITIVE = "Retry";
        public static final String RETRY_NEGATIVE = "Cancel";

        public RetryDialog(final @NonNull Context context, String message) {
            super(context);

            setTitle(RETRY_TITLE);

            if (message != null) {
                setMessage(message);
            } else {
                setMessage(RETRY_MESSAGE);
            }

            //Download the account again
            setPositiveButton(RETRY_POSITIVE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new DownloadDependentTask(context).execute();
                }
            });

            //Close the application
            setNegativeButton(RETRY_NEGATIVE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ((Activity) context).finish();
                }
            });
        }
    }

    //Dialog for signing out
    private class SignOutDialog extends AlertDialog.Builder {

        public static final String SIGN_OUT_TITLE = "Sign out";
        public static final String SIGN_OUT_POSITIVE = "Confirm";
        public static final String SIGN_OUT_NEGATIVE = "Cancel";

        public SignOutDialog(final @NonNull Context context) {
            super(context);

            setTitle(SIGN_OUT_TITLE);

            setPositiveButton(SIGN_OUT_POSITIVE, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    @SuppressLint("StaticFieldLeak")
                    NetworkTask task = new NetworkTask() {

                        private boolean success;

                        @Override
                        protected Object doInBackground(Object[] objects) {
                            super.doInBackground(objects);

                            responding = true;
                            success = false;

                            try {

                                LoginHandler.getInstance().logout(context);
                                success = true;

                            } catch (Exception e) {
                                displayMessageToast(e.getMessage());
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            super.onPostExecute(o);

                            responding = false;
                            if (success) {

                                // Stop the sinch client
                                getSinchInterface().stopClient();
                                finish();
                            }
                        }
                    };
                    task.execute();
                }
            });

            setNegativeButton(SIGN_OUT_NEGATIVE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
    }

    //Display a toast
    private void displayMessageToast(final String message) {

        if (message != null) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
