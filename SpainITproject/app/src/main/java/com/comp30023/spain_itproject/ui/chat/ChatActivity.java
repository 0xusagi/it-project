package com.comp30023.spain_itproject.ui.chat;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp30023.spain_itproject.ChatService;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.ServiceFactory;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.domain.ChatMessage;
import com.comp30023.spain_itproject.ui.BroadcastActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BroadcastActivity {

    public static final String EXTRA_CURRENT_USER = "CURRENT";
    public static final String EXTRA_CHAT_PARTNER_USER = "PARTNER";

    private User currentUser;
    private User chatPartner;

    //The messages between the users
    private List<ChatMessage> messages;

    //UI elements
    private RecyclerView messageRecycler;
    private MessageListAdapter messageListAdapter;

    private EditText inputText;
    private Button sendMessageButton;

    private ChatService chatService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messages = new ArrayList<ChatMessage>();

        Intent arguments = getIntent();
        currentUser = (User) arguments.getSerializableExtra(EXTRA_CURRENT_USER);
        chatPartner = (User) arguments.getSerializableExtra(EXTRA_CHAT_PARTNER_USER);

        messageRecycler = (RecyclerView) findViewById(R.id.recyclerview_message_list);

        //Set the adapter for the RecyclerView so that the messages can be displayed
        messageListAdapter = new MessageListAdapter(this, messages, currentUser);
        messageRecycler.setAdapter(messageListAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        //Display the chat room from the bottom (most recent messages)
        layoutManager.setStackFromEnd(true);
        messageRecycler.setLayoutManager(layoutManager);

        inputText = (EditText) findViewById(R.id.edittext_chatbox);
        sendMessageButton = (Button) findViewById(R.id.button_chatbox_send);
        setSendMessageButtonListener();

        //Create the listener for the chat service
        chatService = ServiceFactory.getInstance().createChatService(currentUser, chatPartner);

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

                //Get text and create ChatMessage instance
                String text = inputText.getText().toString();
                final ChatMessage newMessage = new ChatMessage(currentUser.getId(), currentUser.getName(), chatPartner.getId(), text);

                @SuppressLint("StaticFieldLeak")
                AsyncTask task = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {

                        try {

                            //Send message
                            chatService.sendMessage(newMessage);

                            //If no error thrown, clear the text
                            //inputText.getText().clear();
                            inputText.setText("");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return null;
                    }
                };
                task.execute();
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
}
