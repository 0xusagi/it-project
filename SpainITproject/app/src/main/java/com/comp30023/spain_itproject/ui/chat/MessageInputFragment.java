package com.comp30023.spain_itproject.ui.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.ChatService;

public abstract class MessageInputFragment extends Fragment {

    public static final String ARGUMENT_CURRENT_USER_ID = "CURRENT";
    public static final String ARGUMENT_CHAT_PARTNER_ID = "PARTNER";

    private String currentUserId;
    private String chatPartnerId;

    @Nullable
    @Override
    public abstract View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        currentUserId = arguments.getString(ARGUMENT_CURRENT_USER_ID);
        chatPartnerId = arguments.getString(ARGUMENT_CHAT_PARTNER_ID);

    }

    public abstract void sendInput(ChatService chatService) throws Exception;

    public String getCurrentUserId() {
        return currentUserId;
    }

    public String getChatPartnerId() {
        return chatPartnerId;
    }

}
