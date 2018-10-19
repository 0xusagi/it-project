package com.comp30023.spain_itproject.ui.carerhome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.tts.Voice;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.NoConnectionException;
import com.comp30023.spain_itproject.ui.NetworkActivity;
import com.comp30023.spain_itproject.ui.calls.BaseActivity;
import com.comp30023.spain_itproject.ui.calls.VoiceCallActivity;
import com.comp30023.spain_itproject.ui.chat.ChatActivity;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.calls.videoCalls.sinch.SinchClientService;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.ui.LoginHandler;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.ui.calls.VideoCallActivity;
import com.comp30023.spain_itproject.ui.maps.TrackerMapFragment;
import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.sinch.android.rtc.calling.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * The home activity for carer which displays on the carers screen after login and
 * will remain as the home screen for carers in the application.
 * Contains a list of dependents, upon clicking the dependents, allowing the carer to perform
 * various different activities in order to observe or contact the dependent
 */
public class CarerHomeActivity extends NetworkActivity {
    // String Constants
    private final String NO_DEPENDENTS_MSG = "You currently have no dependents :(";

    // The list of dependents of the carer which contains all information about the dependents
    // to be used when the carer wants to edit the dependent
    private List<DependentUser> dependents;

    private DependentUser dependentSelected;

    // Dependents list
    private ListView dependentsList;
    private ArrayAdapter<String> arrayAdapter;

    // Settings button
    private Button settingsButton;

    // Add Dependent button
    private Button addDependentsButton;

    // Refresh button
    private ImageButton refreshButton;

    private CarerUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_home);

        dependentsList = findViewById(R.id.carerHome_dependentsList);
        displayDependentsList();

        setupAddDependentButton();

        setupRefreshButton();

        setupSettingsButton(this);
    }

    /**
     * Setup displaying the dependents list
     * Dynamically gets the list of dependents from the server
     */
    private void displayDependentsList() {
        // Get the dependents list from the server
        new DisplayDependentsListTask().execute(LoginSharedPreference.getId(this));
    }

    private void setArrayAdapter(ArrayList<String> dependentNames) {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dependentNames);
    }

    /**
     * Setup the add dependents button so that it displays the next activity on click
     */
    private void setupAddDependentButton() {
        addDependentsButton = findViewById(R.id.carerHome_addDependentButton);

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

    /**
     * Setup the settings button
     * @param context
     */
    private void setupSettingsButton(final Context context) {
        settingsButton = findViewById(R.id.carerHome_settingsButton);

        // When pressed, the user is logged out
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("StaticFieldLeak")
                NetworkTask task = new NetworkTask() {

                    private boolean success;

                    @Override
                    protected Object doInBackground(Object[] objects) {
                        super.doInBackground(objects);

                        success = false;

                        try {

                            LoginHandler.getInstance().logout(context);
                            success = true;


                        } catch (BadRequestException e) {
                            e.printStackTrace();
                        } catch (NoConnectionException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

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
    }

    /**
     * Async task to run on background thread to get the carer user from the server
     */
    private class DisplayDependentsListTask extends NetworkTask<String, Void, List<DependentUser>> {
        private Exception exception;

        @Override
        protected List<DependentUser> doInBackground(String... strings) {
            super.doInBackground(strings);

            // Get the carer User from the server
            try {
                CarerUser carer = AccountController.getInstance().getCarer(strings[0]);
                user = carer;
                return carer.getDependents();
            } catch (Exception e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<DependentUser> dependents) {
            super.onPostExecute(dependents);

            if (user != null) {
                LoginSharedPreference.setName(getApplicationContext(), user.getName());
            }

            // If the carer User is null, then there is an error
            if (dependents == null) {
                // Make a toast for the error
                Toast.makeText(CarerHomeActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                storeDependents(dependents);
                setupList();
            }
        }
    }

    private void setupRefreshButton() {
        refreshButton = findViewById(R.id.carerHome_refreshButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupList();
            }
        });
    }
  
    /**
     * Helper method to store the dependents list into the class since cannot be accessed in the
     * inner field
     * @param dependents
     */
    private void storeDependents(List<DependentUser> dependents) {
        this.dependents = dependents;
    }

    /**
     * Makes a list of the dependent names that a carer has,
     * If carer has none, then display a message
     * @return
     */
    private void setupList() {
        boolean isSetOnClick;
        ArrayList<String> dependentNames = new ArrayList<>();

        // Get the dependents
        if (dependents == null) {
            finish();
        }

        // Handle where carer has no dependents
        if (dependents.size() == 0) {
            dependentNames.add(NO_DEPENDENTS_MSG);
            isSetOnClick = false;
        }
        // Carer has dependents
        else {
            // Get the carer names
            for (DependentUser dependent: dependents) {
                dependentNames.add(dependent.getName());
            }
            isSetOnClick = true;
        }

        // Set array adapter
        arrayAdapter = new ArrayAdapter<>(CarerHomeActivity.this, android.R.layout.simple_list_item_1, dependentNames);

        dependentsList.setAdapter(arrayAdapter);

        // Set on click listener only if the carer has dependents
        if (isSetOnClick) {
            dependentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    setupAlertDialog(i);
                }
            });
        }
    }

    /**
     * Setup the alert dialog which displays the options on what to do when a dependent is clicked
     * from the list view
     * @param i
     */
    private void setupAlertDialog(final int i) {
        String[] options = {"Call", "Message", "Locations", "Video Call", "Internet Call", "View Current Location"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CarerHomeActivity.this);
        builder.setTitle("Choose");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on options[which]
                Intent intent;
                dependentSelected = getDependentAt(i);
                switch (which) {
                    case 0:
                        // Get the phone number of the dependent
                        String phoneNumber = dependentSelected.getPhoneNumber();

                        // Make a new calling intent
                        intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+phoneNumber));

                        // Check for permission
                        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CarerHomeActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE}, 1);
                        } else {
                            startActivity(intent);
                        }
                        break;
                    // Edit button

                    case 1:
                        intent = new Intent(getApplicationContext(), ChatActivity.class);
                        intent.putExtra(ChatActivity.EXTRA_CHAT_PARTNER_USER_ID, dependentSelected.getId());

                        startActivity(intent);
                        break;

                    case 2:
                        // Make a new intent to display the edit dependents activity
                        intent = new Intent(getApplicationContext(), EditDependentsActivity.class);
                        // Pass through the dependent id so that can edit easily
                        intent.putExtra(EditDependentsActivity.EXTRA_DEPENDENT, getDependentAt(i));
                        startActivity(intent);
                        break;

                    case 3:
                        startCall(true);
                        break;

                    case 4:
                        startCall(false);
                        break;

                    case 5:
                        intent = new Intent(getApplicationContext(), ViewDependentLocationActivity.class);
                        intent.putExtra(ViewDependentLocationActivity.EXTRA_SENDER_NAME, getDependentAt(i).getName());
                        intent.putExtra(ViewDependentLocationActivity.EXTRA_SENDER_ID, getDependentAt(i).getId());
                        startActivity(intent);
                        break;

                        // Default case
                    default:
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * Helper method to get the DependentUser at the index i from the outer class from inner class
     * of the onClick method
     * @param i
     * @return
     */
    private DependentUser getDependentAt(int i) {
        return dependents.get(i);
    }

    private void startCall(boolean isVideo) {
        if (getSinchInterface() == null || !getSinchInterface().isStarted()) {
            return;
        }

        // Choose which call
        Call call;
        Intent intent;
        // VIdeo call
        if (isVideo) {
            call = getSinchInterface().callUserVideo(dependentSelected.getId());
            intent = new Intent(getApplicationContext(), VideoCallActivity.class);
        }
        // Internet call
        else {
            call = getSinchInterface().callUserVoice(dependentSelected.getId());
            intent = new Intent(getApplicationContext(), VoiceCallActivity.class);
        }

        String callId = call.getCallId();

        // Star tthe intent
        intent.putExtra(SinchClientService.CALL_ID, callId);
        startActivity(intent);
    }
}
