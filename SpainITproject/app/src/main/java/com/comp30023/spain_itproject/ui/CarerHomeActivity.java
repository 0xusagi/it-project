package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.comp30023.spain_itproject.LoginHandler;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.uicontroller.AccountController;

import java.util.ArrayList;

/**
 * The home activity for carer which displays on the carers screen after login and
 * will remain as the home screen for carers in the application.
 * Contains a list of dependents, upon clicking the dependents, allowing the carer to perform
 * various different activities in order to observe or contact the dependent
 */
public class CarerHomeActivity extends AppCompatActivity {

    private ListView dependentsList;

    private AccountController carerController;

    private CarerUser carerUser;

    private ArrayList<DependentUser> dependents;

    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_home);

        carerUser = (CarerUser) getIntent().getSerializableExtra(LoginHandler.PASSED_USER);

        dependents = carerUser.getDependents();

        carerController = new AccountController();

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

        settingsButton = (ImageButton) findViewById(R.id.carerHome_settingsButton);

        setSettingsButtonListener(this);

    }

    private void setSettingsButtonListener(final Context context) {
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginHandler.logout(context);
                finish();
            }
        });


    }

    /**
     * Display the list of dependents that the carer currently has
     */
    private void displayDependentsList() {

        // TODO Get the string of the current user
        String phoneNumber = carerUser.getPhoneNumber();

        ArrayList<String> dependentsName = new ArrayList<>();

        for (DependentUser dependent : dependents) {
            dependentsName.add(dependent.getName());
        }

        // TODO Change the string to dependents
        //ArrayList<DependentUser> dependents = carerController.getDependentsOfCarer(phoneNumber);
        //ArrayList<String> dependentsName = new ArrayList<>();

        //// TODO Populate the arraylist
        //dependentsName.add("asdf");
        //dependentsName.add("basdf");

        //// Set array adapter
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
