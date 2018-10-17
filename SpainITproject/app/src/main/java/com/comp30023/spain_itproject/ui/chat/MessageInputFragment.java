package com.comp30023.spain_itproject.ui.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.ChatService;

/**
 * Fragment for message input to send to another user
 */
public abstract class MessageInputFragment extends Fragment {

    @Nullable
    @Override
    public abstract View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * Send the current input to the chat partner via the ChatService
     * @param chatService The service that sends the message
     * @throws Exception Thrown if there is a connection issue
     */
    public abstract void sendInput(ChatService chatService) throws Exception;

}
