package com.comp30023.spain_itproject.ui.chat;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.external_services.ServiceFactory;
import com.comp30023.spain_itproject.domain.ChatMessage;

/**
 * A view holder for messages that display the same attributes
 * Adapted from tutorial: https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
 */
public abstract class MessageHolder extends RecyclerView.ViewHolder {

    private View view;

    private TextView messageText;
    private TextView timeText;
    private LinearLayout layout;
    private ImageButton playButton;

    private String currentUserId;
    private String chatPartnerId;

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
        timeText.setText(message.getTimeStamp());

        if (message.getAudioResourceLink() != null) {

            playButton.setVisibility(View.VISIBLE);

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    @SuppressLint("StaticFieldLeak")
                    AsyncTask task = new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] objects) {

                            ServiceFactory.getInstance().chatService(currentUserId, null, chatPartnerId).playAudioMessage(message.getAudioResourceLink());

                            return null;
                        }
                    };
                    task.execute();

                }
            });

        } else {
            playButton.setVisibility(View.GONE);
        }
    }
}
