package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.comp30023.spain_itproject.LoginHandler;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;

public class LoginActivity extends AppCompatActivity {

    private Button cancelButton;

    private ToggleButton dependentButton;
    private ToggleButton carerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        setCancelButtonListener(this);

        dependentButton = (ToggleButton) findViewById(R.id.dependentButton);
        dependentButton.setChecked(true);
        dependentButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Toggles dependentButton to checked, carerButton to unchecked
             * @param v
             */
            @Override
            public void onClick(View v) {
                dependentButton.setChecked(true);
                carerButton.setChecked(false);
            }
        });

        carerButton = (ToggleButton) findViewById(R.id.carerButton);
        carerButton.setChecked(false);
        carerButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Toggles carerButton to checked, dependentButton to unchecked
             * @param v
             */
            @Override
            public void onClick(View v) {
                dependentButton.setChecked(false);
                carerButton.setChecked(true);
            }
        });
    }

    private void setCancelButtonListener(final Context context) {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When button clicked, closes this Activity and starts the StartActivity
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Same functionality as if the cancel button
     */
    public void onBackPressed() {
        cancelButton.performClick();
    }
}
