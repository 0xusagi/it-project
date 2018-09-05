package com.comp30023.spain_itproject;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

    private void displayDependentsList() {

        // TODO Change the string to dependents
        ArrayList<String> dependents = new ArrayList<>();

        // TODO Populate the arraylist
        dependents.add("asdf");
        dependents.add("basdf");

        // Set array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, dependents);

        dependentsList.setAdapter(adapter);
    }
}
