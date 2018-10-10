package com.comp30023.spain_itproject.ui.views;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.firebase.FirebaseQueryLiveData;
import com.comp30023.spain_itproject.firebase.realtime_database.ChatMessage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChatViewModel extends ViewModel {

    public static final String baseReference = "";

    private FirebaseQueryLiveData liveData;
    private DatabaseReference ref;

    private MediatorLiveData<ChatMessage> chatLiveData;

    public ChatViewModel() {

        checks();

        chatLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            chatLiveData.postValue(dataSnapshot.getValue(ChatMessage.class));
                        }
                    }).start();

                } else {
                    chatLiveData.setValue(null);
                }
            }
        });
    }

    private void checks() {
        if (liveData == null) {

            if (ref == null) {
                ref = FirebaseDatabase.getInstance().getReference(baseReference);
            }

            //liveData = new FirebaseQueryLiveData(ref);
        }

        if (chatLiveData == null) {
            chatLiveData = new MediatorLiveData<ChatMessage>();
        }
    }

    public LiveData<ChatMessage> getMessageLiveData(User currentUser, String partnerId) {
        checks();

        return chatLiveData;
    }

    private class Deserialiser implements Function<DataSnapshot, ChatMessage> {

        @Override
        public ChatMessage apply(DataSnapshot input) {
            return input.getValue(ChatMessage.class);
        }
    }

}
