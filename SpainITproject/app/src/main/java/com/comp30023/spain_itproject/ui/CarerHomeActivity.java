package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.comp30023.spain_itproject.LoginHandler;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.DependentUser;

import java.util.ArrayList;

/**
 * The home activity for carer which displays on the carers screen after login and
 * will remain as the home screen for carers in the application.
 * Contains a list of dependents, upon clicking the dependents, allowing the carer to perform
 * various different activities in order to observe or contact the dependent
 */
public class CarerHomeActivity extends AppCompatActivity {

    // Dependents list
    private RecyclerView dependentsListView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // Settings button
    private ImageButton settingsButton;

    // Add Dependent button
    private Button addDependentsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_home);

        displayDependentsList();

        setupAddDependentButton();

        setupSettingsButton(this);
    }


    /**
     * Setup displaying the dependents list
     * Dynamically gets the list of dependents from the server
     */
    private void displayDependentsList() {
        // TODO Get dependents list from server
        // For now display a fake list
        ArrayList<DependentUser> dependentUsers = new ArrayList<>();
        dependentUsers.add(new DependentUser("John","0402849382", "1234", 1));

        /* Create the list to display dependent users */
        // Set the dependents list recycler view
        dependentsListView = (RecyclerView)findViewById(R.id.carerHome_dependentsList);

        // Use this setting to improve performance
        dependentsListView.setHasFixedSize(true);

        // Use linear layout manager
        layoutManager = new LinearLayoutManager(this);
        dependentsListView.setLayoutManager(layoutManager);

        // Set an adapter
        adapter = new DependentsListAdapter(dependentUsers);
        dependentsListView.setAdapter(adapter);
    }

    /**
     * Setup the add dependents button so that it displays the next activity on click
     */
    private void setupAddDependentButton() {
        addDependentsButton = (Button) findViewById(R.id.carerHome_addDependentButton);

        // When pressed the add dependents button the next activity to be shown is the
        // add dependent activity
        addDependentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display the next activity
                Intent intent = new Intent(getApplicationContext(), AddDependentActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupSettingsButton(final Context context) {
        settingsButton = (ImageButton) findViewById(R.id.carerHome_settingsButton);

        // When pressed, the user is logged out
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginHandler.logout(context);
                finish();
            }
        });
    }
}
