package com.comp30023.spain_itproject.ui.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.external_services.ChatService;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.NoConnectionException;

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
     * @throws BadRequestException Thrown if either user does not exist on the database
     * @throws NoConnectionException Thrown if there is a connection issue
     * @throws NoInputException Thrown if there is no input entered yet
     */
    public abstract void sendInput(ChatService chatService) throws NoInputException, BadRequestException, NoConnectionException;

}
