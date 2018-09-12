package com.comp30023.spain_itproject.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

    private RecyclerView dependentsListView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_home);

        displayDependentsList();
    }


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
}
