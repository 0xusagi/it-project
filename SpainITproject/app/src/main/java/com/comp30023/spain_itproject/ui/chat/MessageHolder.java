package com.comp30023.spain_itproject.ui.chat;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.ServiceFactory;
import com.comp30023.spain_itproject.domain.ChatMessage;
import com.comp30023.spain_itproject.firebase.storage.FirebaseStorageService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * A view holder for messages that display the same attributes
 * Adapted from tutorial: https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
 */
public abstract class MessageHolder extends RecyclerView.ViewHolder {

    private View view;

    TextView messageText;
    TextView timeText;
    LinearLayout layout;
    ImageButton playButton;

    String currentUserId;
    String chatPartnerId;

    public MessageHolder(@NonNull View itemView, String currentUserId, String chatPartnerId) {
        super(itemView);

        this.currentUserId = currentUserId;
        this.chatPartnerId = chatPartnerId;

        view = itemView;
        messageText = (TextView) itemView.findViewById(R.id.text_message_body);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        layout = (LinearLayout) view.findViewById(R.id.text_message_layout);
        playButton = (ImageButton) view.findViewById(R.id.text_play_button);
    }

    void bind(final ChatMessage message) {
        messageText.setText(message.getMessage());

        try {
            timeText.setText(message.getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (message.getResourceLink() != null) {

            playButton.setVisibility(View.VISIBLE);

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ServiceFactory.getInstance().chatService(currentUserId, chatPartnerId).playAudioMessage(message.getResourceLink());

                }
            });

        } else {
            playButton.setVisibility(View.GONE);
        }
    }
}
