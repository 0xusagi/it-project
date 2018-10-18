package com.comp30023.spain_itproject.ui.dependenthome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.calls.videoCalls.sinch.SinchClientService;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.ui.calls.VideoCallActivity;
import com.comp30023.spain_itproject.ui.calls.VoiceCallActivity;
import com.comp30023.spain_itproject.ui.views.ItemButton;
import com.sinch.android.rtc.calling.Call;


import java.util.List;

/**
 *
 */
public class CallsListFragment extends ListFragment<CarerUser> {
    // Store the parent activity
    private DependentHomeActivity activity;

    public static final String ARGUMENT_USER = "USER";

    public static final String TITLE = "Calls";

    private DependentUser user;

    private static boolean added = false;

    List<CarerUser> carers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        activity = (DependentHomeActivity) getActivity();

        Bundle arguments = getArguments();
        user = (DependentUser) arguments.getSerializable(ARGUMENT_USER);

        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                try {
                    carers = user.getCarers();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                setList(carers);
                setButtonListeners();
                setTitle(TITLE);

            }
        };
        task.execute();

        return view;
    }


    public void setButtonListeners() {
        super.setButtonListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //The item button that is pressed
                ItemButton itemButton = (ItemButton) v;
                CarerUser requester = (CarerUser) itemButton.getItem();

                setupAlertDialog(requester);
            }
        });
    }

    private void setupAlertDialog(final CarerUser carerUser) {
        String[] options = {"Call", "Video Call", "Internet Call"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on options[which]
                Intent intent;
                switch (which) {
                    case 0:
                        // Get the phone number of the dependent
                        String phoneNumber = carerUser.getPhoneNumber();

                        // Make a new calling intent
                        intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+phoneNumber));

                        // Check for permission
                        if (ContextCompat.checkSelfPermission(getActivity().getApplication(), Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CALL_PHONE}, 1);
                        } else {
                            startActivity(intent);
                        }
                        break;

                    // Video call
                    case 1:
                        startCall(carerUser.getId(), true);
                        break;

                    // Voice call
                    case 2:
                        startCall(carerUser.getId(), false);
                        break;

                    // Default case
                    default:
                        break;
                }
            }
        });
        builder.show();
    }

    private void startCall(String id, boolean isVideo) {
        // Video call
        Call call;
        Intent intent;
        if (isVideo) {
            call = activity.getSinchInterface().callUserVideo(id);
            intent = new Intent(getActivity(), VideoCallActivity.class);
        }
        else {
            call = activity.getSinchInterface().callUserVoice(id);
            intent = new Intent(getActivity(), VoiceCallActivity.class);
        }

        // Start the next screen
        String callId = call.getCallId();

        // Star tthe intent
        intent.putExtra(SinchClientService.CALL_ID, callId);
        startActivity(intent);
    }
}

