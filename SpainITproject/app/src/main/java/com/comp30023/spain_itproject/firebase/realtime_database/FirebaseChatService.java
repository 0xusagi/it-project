package com.comp30023.spain_itproject.firebase.realtime_database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseChatService implements ChildEventListener {


    public static final String BASE_CHAT_PATH = "";


    private static FirebaseChatService instance;
    public static FirebaseChatService getInstance() {
        if (instance == null) {
            instance = new FirebaseChatService();
        }

        return instance;
    }


    private User user;

    private Map<String, ChildEventListener> listeners;


    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference baseChatReference;

    private void checkDatabaseInstance() {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);

            baseChatReference = firebaseDatabase.getReference().child(BASE_CHAT_PATH);
        }
    }

    public void addChatListener(final User currentUser, String partnerId) {

        checkDatabaseInstance();

        if (listeners == null) {
            listeners = new HashMap<String, ChildEventListener>();
        }

        String referenceName = getReferenceName(currentUser, partnerId);

        //Get the reference from the database
        DatabaseReference chatReference = baseChatReference.child(referenceName);

        //Handle incoming messages
        ChildEventListener listener = chatReference.addChildEventListener(this);
    }


    public static String getReferenceName(User currentUser, String partnerId) {

        //Determine the reference from the agreed naming convention (DependentUser.id-CarerUser.id)
        String reference;

        if (currentUser instanceof DependentUser) {
            reference = currentUser.getId() + "-" + partnerId;
        } else {
            reference = partnerId + "-" + currentUser.getId();
        }

        return reference;
    }


    public void fetchMessages(final User currentUser, List<User> partners) {

        for (User partner : partners) {

            String referenceName = getReferenceName(currentUser, partner.getId());

            checkDatabaseInstance();

            DatabaseReference chatReference = baseChatReference.child(referenceName);

            //Retrieve chat history
            chatReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //Get children (messages) for the chat room instance in the database
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                    //Add each message to the current user
                    for (DataSnapshot child : children) {

                        ChatMessage message = child.getValue(ChatMessage.class);
                        currentUser.putMessage(message);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    public void sendMessage(User sender, User receiver, String body) {

        checkDatabaseInstance();

        ChatMessage message = new ChatMessage(sender.getId(), sender.getName(), receiver.getId(), body);

        String referenceName = getReferenceName(sender, receiver.getId());
        DatabaseReference chatReference = baseChatReference.child(BASE_CHAT_PATH + referenceName);

        //TODO
        //AccountController.getInstance().sendMessage(sender, receiver, body);

        chatReference.push().setValue(message);
    }


    public void clearListeners() {
        listeners.clear();
    }

    public void removeChatListener(String partnerId) {
        listeners.remove(partnerId);
    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
        user.putMessage(message);
    }


    public DatabaseReference getChatReference(User currentUser, String partnerId) {

        checkDatabaseInstance();

        String chatReferenceName = getReferenceName(currentUser, partnerId);

        DatabaseReference chatReference = baseChatReference.child(chatReferenceName);

        return chatReference;
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
}