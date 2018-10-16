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

    public MessageHolder(@NonNull View itemView) {
        super(itemView);

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

                    final StorageReference storageRef = FirebaseStorageService.getStorage().getReference().child(message.getResourceLink());

                    try {
                        final File localFile = File.createTempFile("recording", "3gp");
                        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                // Local temp file has been created
                                System.out.println("Get file success");

                                final MediaPlayer player = new MediaPlayer();
                                try {
                                    player.setDataSource(localFile.getPath());
                                    player.prepare();
                                    player.start();
                                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            player.reset();
                                            player.release();
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });

        } else {
            playButton.setVisibility(View.GONE);
        }
    }
}
