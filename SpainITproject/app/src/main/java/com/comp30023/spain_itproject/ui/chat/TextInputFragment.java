package com.comp30023.spain_itproject.ui.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.comp30023.spain_itproject.external_services.ChatService;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.NoConnectionException;

/**
 * Records text input to send as message
 */
public class TextInputFragment extends MessageInputFragment {

    private EditText inputText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_text_input, container, false);

        inputText = (EditText) view.findViewById(R.id.textInput_editText);

        return view;
    }

    /**
     * Sends the text in the input field as a message
     * @param chatService The ChatService that the message is sent by
     * @throws NoConnectionException Thrown if there is a connection issue
     * @throws BadRequestException Thrown if either user does not exist on the database
     * @throws NoInputException Thrown if there is no input entered yet
     */
    @Override
    public void sendInput(ChatService chatService) throws NoInputException, BadRequestException, NoConnectionException {

        //Get text and create ChatMessage instance
        String text = inputText.getText().toString();

        if (text == null || text.isEmpty() || text.equals("")) {
            throw new NoInputException();
        }

        //Send message
        chatService.sendMessage(text);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //If no error thrown, clear the text
                inputText.setText("");
            }
        });
    }
}
