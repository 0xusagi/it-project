package com.comp30023.spain_itproject.ui.dependenthome;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.comp30023.spain_itproject.NetworkFragment;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.User;

/**
 * Fragment in which a dependent can respond to a request
 */
public class ResponseFragment extends NetworkFragment {

    public static final String ARGUMENT_USER = "USER";
    public static final String ARGUMENT_REQUESTING_USER = "REQUSER";

    private TextView carerName;
    private TextView carerPhoneNumber;

    private Button acceptButton;
    private Button rejectButton;

    DependentUser user;
    CarerUser requester;

    //If a response has already been sent, cannot respond again
    private boolean responding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_response, container, false);

        Bundle arguments = getArguments();
        user = (DependentUser) arguments.getSerializable(ARGUMENT_USER);
        requester = (CarerUser) arguments.getSerializable(ARGUMENT_REQUESTING_USER);

        carerName = view.findViewById(R.id.response_carer_name);
        carerName.setText(requester.getDisplayName());

        carerPhoneNumber = view.findViewById(R.id.response_carer_number);
        carerPhoneNumber.setText(requester.getPhoneNumber());

        acceptButton = (Button) view.findViewById(R.id.response_accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!responding) {
                    new RespondTask().execute(true);
                    responding = true;
                }
            }
        });

        rejectButton = (Button) view.findViewById(R.id.response_reject);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!responding) {
                    new RespondTask().execute(false);
                    responding = true;
                }
            }
        });

        responding = false;

        return view;
    }

    //Sends the response to the server and locally adjusts the lists of pending and confirmed carers
    private class RespondTask extends NetworkTask<Boolean, Void, Void> {

        @Override
        //First argument is whether the request is being accepted
        protected Void doInBackground(Boolean... booleans) {
            super.doInBackground(booleans);

            boolean response = booleans[0];

            try {
                user.respondToRequest(requester, response);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            responding = false;
            getActivity().onBackPressed();
        }
    }
}
