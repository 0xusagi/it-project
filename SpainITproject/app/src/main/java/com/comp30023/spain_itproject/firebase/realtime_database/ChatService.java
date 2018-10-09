package com.comp30023.spain_itproject.firebase.realtime_database;

import android.support.annotation.NonNull;

import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChatService {

    public static final String BASE_CHAT_PATH = "";

    private static ChatService instance;

    public static ChatService getInstance() {
        if (instance == null) {
            instance = new ChatService();
        }

        return instance;
    }

    private FirebaseDatabase firebaseDatabase;
    private Map<String, DatabaseReference> references;

    public void addChatListener(final User currentUser, String partnerId) {

        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
        }

        if (references == null) {
            references = new HashMap<String, DatabaseReference>();
        }

        String reference;
        if (currentUser instanceof DependentUser) {
            reference = currentUser.getId() + "-" + partnerId;
        } else {
            reference = partnerId + "-" + currentUser.getId();
        }

        DatabaseReference dbReference = firebaseDatabase.getReference(BASE_CHAT_PATH + reference);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {

                    ChatMessage message = child.getValue(ChatMessage.class);
                    currentUser.putMessage(message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        references.put(partnerId, dbReference);
    }

    public void sendMessage(User sender, User receiver, String body) {

        ChatMessage message = new ChatMessage(sender.getId(), sender.getName(), receiver.getId(), body);

        DatabaseReference dbReference = references.get(receiver.getId());

        //TODO
        //AccountController.getInstance().sendMessage(sender, receiver, body);

        dbReference.push().setValue(message);

    }
}