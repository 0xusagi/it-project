package com.comp30023.spain_itproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.comp30023.spain_itproject.controller.CarerController;
import com.comp30023.spain_itproject.domain.Dependent;

import java.util.ArrayList;

/**
 * The home activity for carer which displays on the carers screen after login and
 * will remain as the home screen for carers in the application.
 * Contains a list of dependents, upon clicking the dependents, allowing the carer to perform
 * various different activities in order to observe or contact the dependent
 */
public class CarerHomeActivity extends AppCompatActivity {

    private ListView dependentsList;

    private CarerController carerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_home);

        // Remove the action bar
        getSupportActionBar().hide();

        carerController = new CarerController();

        dependentsList = findViewById(R.id.carerHome_dependentsList);

        // Initialise the listView
        displayDependentsList();

        // Set the on click listener
        dependentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DependentsOnClickDialog dialog = new DependentsOnClickDialog(CarerHomeActivity.this);
                dialog.show();
            }
        });

    }

    /**
     * Display the list of dependents that the carer currently has
     */
    private void displayDependentsList() {

        // TODO Get the string of the current user
        String phoneNumber = "";

        // TODO Change the string to dependents
        ArrayList<Dependent> dependents = carerController.getDependentsOfCarer(phoneNumber);
        ArrayList<String> dependentsName = new ArrayList<>();

        // TODO Populate the arraylist
        dependentsName.add("asdf");
        dependentsName.add("basdf");


        // Set array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, dependentsName);

        dependentsList.setAdapter(adapter);
    }

    /**
     * Takes the carer to the next screen where the carer can input a mobile number to search
     * for a dependent to be added
     * @param view
     */
    public void displayAddDependentActivity(View view) {
        Intent intent = new Intent(this, AddDependentActivity.class);

        startActivity(intent);
    }
}
