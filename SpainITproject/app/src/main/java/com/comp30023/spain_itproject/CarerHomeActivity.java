package com.comp30023.spain_itproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The home activity for carer which displays on the carers screen after login and
 * will remain as the home screen for carers in the application.
 * Contains a list of dependents, upon clicking the dependents, allowing the carer to perform
 * various different activities in order to observe or contact the dependent
 */
public class CarerHomeActivity extends AppCompatActivity {

    private ListView dependentsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_home);

        // Remove the action bar
        getSupportActionBar().hide();

        dependentsList = findViewById(R.id.carerHome_dependentsList);

        displayDependentsList();
    }

    private void displayDependentsList() {

        // TODO Change the string to dependents
        ArrayList<String> dependents = new ArrayList<>();

        // TODO Populate the arraylist
        dependents.add("asdf");
        dependents.add("basdf");

        // Set array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, dependents);

        // Set the on click listener to display a popup of options to call, message, or
        // other activities to monitor the dependent

        dependentsList.setAdapter(adapter);
    }
}
