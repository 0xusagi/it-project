package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.comp30023.spain_itproject.LoginHandler;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;

import java.util.ArrayList;

/**
 * Activity that opens when a DependentUser is logged in
 * Displays the stored locations of the DependentUser
 */
public class DependentHomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private DependentListFragment listFragment;

    private Button messagesButton;
    private Button callButton;

    private Button helpButton;

    private Button signOutButton;

    private DrawerLayout drawerLayout;

    //The currently signed in user
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

        Intent intent = getIntent();
        user = (DependentUser) intent.getSerializableExtra(LoginHandler.PASSED_USER);

        fragmentManager = getSupportFragmentManager();

        listFragment = new DependentListFragment();

        ViewGroup fragmentContainer = (ViewGroup) findViewById(R.id.fragment_container);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, listFragment);
        transaction.commit();

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

}
