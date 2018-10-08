package com.comp30023.spain_itproject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.firebase.DataMessage;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.NoConnectionException;
import com.comp30023.spain_itproject.uicontroller.AccountController;

public class HelpRequestActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "MESSAGE";

    TextView senderText;

    DependentUser requester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_request);

        final DataMessage message = (DataMessage) getIntent().getExtras().getSerializable(EXTRA_MESSAGE);

        senderText = (TextView) findViewById(R.id.help_req_sender_text);
        senderText.setText(message.getSenderName());

        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                try {
                    requester = AccountController.getInstance().getDependent(message.getSenderId());
                } catch (BadRequestException e) {
                    e.printStackTrace();
                } catch (NoConnectionException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

    }
}
