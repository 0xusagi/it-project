package com.comp30023.spain_itproject.ui.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.comp30023.spain_itproject.ChatService;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.ChatMessage;

public class TextInputFragment extends MessageInputFragment {

    private EditText inputText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_text_input, container, false);

        inputText = (EditText) view.findViewById(R.id.textInput_editText);

        return view;
    }

    @Override
    public void sendInput(ChatService chatService) throws Exception {

        //Get text and create ChatMessage instance
        String text = inputText.getText().toString();

        if (text == null || text.isEmpty() || text.equals("")) {
            return;
        }

        final ChatMessage newMessage = new ChatMessage(getCurrentUserId(), getChatPartnerId(), text, null);

        //Send message
        chatService.sendMessage(newMessage);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //If no error thrown, clear the text
                //inputText.getText().clear();
                inputText.setText("");
            }
        });
    }
}
