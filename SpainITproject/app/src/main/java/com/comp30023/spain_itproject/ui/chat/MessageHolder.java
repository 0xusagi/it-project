package com.comp30023.spain_itproject.ui.chat;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.ChatMessage;
import com.comp30023.spain_itproject.firebase.storage.MyFirebaseStorage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

    public MessageHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;
        messageText = (TextView) itemView.findViewById(R.id.text_message_body);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time);

    }

    void bind(final Context context, final ChatMessage message) {
        messageText.setText(message.getMessage());

        try {
            timeText.setText(message.getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (message.getResourceLink() != null) {
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.text_message_layout);
            layout.removeAllViews();

            ImageButton button = new ImageButton(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            button.setLayoutParams(params);
            button.setImageResource(R.drawable.ic_play_arrow);

            final String destinationFilename = context.getCacheDir().getAbsolutePath() + message.getResourceLink();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    File file = new File(destinationFilename);

                    if (!file.exists()) {

                        FirebaseStorage storage = MyFirebaseStorage.getStorage();
                        StorageReference reference = storage.getReference().child(message.getResourceLink());

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String sourceFilename= uri.getPath();

                                System.out.println("URI path: " + sourceFilename);
                                BufferedInputStream bis = null;
                                BufferedOutputStream bos = null;

                                try {
                                    bis = new BufferedInputStream(new FileInputStream(sourceFilename));
                                    bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
                                    byte[] buf = new byte[1024];
                                    bis.read(buf);
                                    do {
                                        bos.write(buf);
                                    } while(bis.read(buf) != -1);

                                    System.out.println("File downloaded");

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        if (bis != null) bis.close();
                                        if (bos != null) bos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } else {

                        final MediaPlayer player = new MediaPlayer();
                        try {
                            player.setDataSource(destinationFilename);
                            player.prepare();
                            player.start();
                            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    try {
                                        player.prepare();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


            layout.addView(button);
        }
    }
}
