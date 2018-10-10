package com.comp30023.spain_itproject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.firebase.FirebaseQueryLiveData;
import com.comp30023.spain_itproject.firebase.realtime_database.ChatMessage;
import com.comp30023.spain_itproject.firebase.realtime_database.FirebaseChatService;
import com.comp30023.spain_itproject.ui.BroadcastActivity;
import com.comp30023.spain_itproject.ui.views.MessageListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ChatActivity extends BroadcastActivity {

    public static final String EXTRA_CURRENT_USER = "CURRENT";
    public static final String EXTRA_CHAT_PARTNER_USER = "PARTNER";

    private User currentUser;
    private User chatPartner;

    private List<ChatMessage> messages;

    private RecyclerView messageRecycler;
    private MessageListAdapter messageListAdapter;

    private DatabaseReference dbReference;

    private EditText inputText;
    private Button sendMessageButton;

    private ChildEventListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messages = new ArrayList<ChatMessage>();

        messageRecycler = (RecyclerView) findViewById(R.id.recyclerview_message_list);
        messageListAdapter = new MessageListAdapter(this, messages);
        messageRecycler.setAdapter(messageListAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        messageRecycler.setLayoutManager(layoutManager);

        inputText = (EditText) findViewById(R.id.edittext_chatbox);
        sendMessageButton = (Button) findViewById(R.id.button_chatbox_send);
        setSendMessageButtonListener();

        Intent arguments = getIntent();
        currentUser = (User) arguments.getSerializableExtra(EXTRA_CURRENT_USER);
        chatPartner = (User) arguments.getSerializableExtra(EXTRA_CHAT_PARTNER_USER);

        final String path = FirebaseChatService.getReferenceName(currentUser, chatPartner.getId());

        dbReference = FirebaseDatabase.getInstance().getReference().child("chats").child(path);

        listener = dbReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                final ChatMessage message = dataSnapshot.getValue(ChatMessage.class);

                messages.add(message);
                messageListAdapter.notifyDataSetChanged();
                messageRecycler.scrollToPosition(messageListAdapter.getItemCount()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setSendMessageButtonListener() {
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = inputText.getText().toString();
                ChatMessage newMessage = new ChatMessage(currentUser.getId(), currentUser.getName(), chatPartner.getId(), text);

                dbReference.push().setValue(newMessage);
                inputText.getText().clear();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbReference.removeEventListener(listener);
    }
}
