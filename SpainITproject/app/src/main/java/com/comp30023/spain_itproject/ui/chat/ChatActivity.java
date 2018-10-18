package com.comp30023.spain_itproject.ui.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.comp30023.spain_itproject.external_services.ChatService;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.external_services.ServiceFactory;
import com.comp30023.spain_itproject.domain.ChatMessage;
import com.comp30023.spain_itproject.ui.BroadcastActivity;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BroadcastActivity {

    public static final String EXTRA_CHAT_PARTNER_USER_ID = "PARTNER";

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private String currentUserId;
    private String currentUserName;
    private String chatPartnerId;

    //The messages between the users
    private List<ChatMessage> messages;

    //UI elements
    private RecyclerView messageRecycler;
    private MessageListAdapter messageListAdapter;

    private FragmentManager fragmentManager;
    private MessageInputFragment currentFragment;

    private Button sendMessageButton;
    private ImageButton changeFragmentButton;

    private ChatService chatService;

    private boolean permissionToRecordAccepted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messages = new ArrayList<ChatMessage>();

        currentUserId = LoginSharedPreference.getId(this);
        currentUserName = LoginSharedPreference.getName(this);

        Intent intent = getIntent();
        chatPartnerId = intent.getStringExtra(EXTRA_CHAT_PARTNER_USER_ID);

        messageRecycler = (RecyclerView) findViewById(R.id.recyclerview_message_list);

        //Set the adapter for the RecyclerView so that the messages can be displayed
        messageListAdapter = new MessageListAdapter(this, messages, currentUserId, chatPartnerId);
        messageRecycler.setAdapter(messageListAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        //Display the chat room from the bottom (most recent messages)
        layoutManager.setStackFromEnd(true);
        messageRecycler.setLayoutManager(layoutManager);

        changeFragmentButton = (ImageButton) findViewById(R.id.chat_changeFragmentButton);
        setChangeFragmentButtonListener();

        changeFragment();

        sendMessageButton = (Button) findViewById(R.id.button_chatbox_send);
        setSendMessageButtonListener();

        //Create the listener for the chat service
        chatService = ServiceFactory.getInstance().chatService(currentUserId, currentUserName, chatPartnerId);

        /*chatService.getMessageHistory().observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(@Nullable List<ChatMessage> chatMessages) {

                for (ChatMessage message : chatMessages) {
                    messages.add(message);
                }
                messageListAdapter.notifyDataSetChanged();
            }
        });*/

        //Listen to incoming messages
        chatService.getLatestMessageLiveData().observe(this, new Observer<ChatMessage>() {
            @Override
            public void onChanged(@Nullable ChatMessage chatMessage) {

                //Add latest message to list, then update UI to show the bottom
                messages.add(chatMessage);
                messageListAdapter.notifyDataSetChanged();
                messageRecycler.scrollToPosition(messageListAdapter.getItemCount()-1);
            }
        });
    }

    private void setSendMessageButtonListener() {
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentFragment != null) {

                    @SuppressLint("StaticFieldLeak")
                    AsyncTask task = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] objects) {

                            try {
                                currentFragment.sendInput(chatService);

                            } catch (Exception e) {
                                displayExceptionToast(e);
                            }

                            return null;
                        }
                    };
                    task.execute();
                }
            }
        });
    }

    /**
     * Displays the message from the exception as a toast
     * @param e The exception
     */
    private void displayExceptionToast(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setChangeFragmentButtonListener() {
        changeFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment();
            }
        });
    }

    private void changeFragment() {

        if (currentFragment == null) {
            currentFragment = new TextInputFragment();
            changeFragmentButton.setImageResource(R.drawable.ic_mic_black_24dp);

        } else if (currentFragment instanceof TextInputFragment) {

            if (permissionToRecordAccepted) {
                currentFragment = new VoiceInputFragment();
                changeFragmentButton.setImageResource(R.drawable.ic_keyboard_black_24dp);

            } else {
                String [] permissions = {Manifest.permission.RECORD_AUDIO};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
            }
        } else {
            currentFragment = new TextInputFragment();
            changeFragmentButton.setImageResource(R.drawable.ic_mic_black_24dp);
        }

        setFragment();
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                changeFragment();
                break;
        }
    }

    private void setFragment() {

        if (currentFragment != null) {

            if (fragmentManager == null) {
                fragmentManager = getSupportFragmentManager();
            }

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.chat_inputFragment, currentFragment);
            transaction.commit();
        }
    }
}
