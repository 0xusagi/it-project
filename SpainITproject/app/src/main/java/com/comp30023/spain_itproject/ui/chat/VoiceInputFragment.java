package com.comp30023.spain_itproject.ui.chat;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.comp30023.spain_itproject.ChatService;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.ChatMessage;
import com.comp30023.spain_itproject.firebase.storage.MyFirebaseStorage;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class VoiceInputFragment extends MessageInputFragment {

    //TODO
    //private final String OUTPUT_FILE_PATH = getActivity().getFilesDir().getPath();

    public static final String AUDIO_MESSAGE = "Audio message";

    private LinearLayout buttonsLayout;

    private ImageButton recordButton;
    private ImageButton stopRecordButton;
    private ImageButton playbackRecordingButton;
    private ImageButton stopPlaybackButton;
    private ImageButton clearRecordingButton;

    private MediaRecorder recorder;
    private MediaPlayer player;
    private boolean isRecording;

    private String outputPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_voice_input, container, false);

        buttonsLayout = (LinearLayout) view.findViewById(R.id.voiceInput_buttonsLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

        isRecording = false;

        outputPath = getActivity().getCacheDir().getAbsolutePath() + "/tempRecording.3gp";

        setRecorder();

        recordButton = new ImageButton(getContext());
        recordButton.setImageResource(R.drawable.ic_fiber_manual_record);
        recordButton.setLayoutParams(params);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recorder.start();
                isRecording = true;

                buttonsLayout.removeAllViews();
                buttonsLayout.addView(stopRecordButton);
                isRecording = true;
            }
        });

        stopRecordButton = new ImageButton(getContext());
        stopRecordButton.setImageResource(R.drawable.ic_stop_black_24dp);
        stopRecordButton.setLayoutParams(params);
        stopRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recorder.stop();
                isRecording = false;

                player = new MediaPlayer();
                try {
                    player.setDataSource(outputPath);
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                buttonsLayout.removeAllViews();
                buttonsLayout.addView(playbackRecordingButton);
                buttonsLayout.addView(clearRecordingButton);
            }
        });

        playbackRecordingButton = new ImageButton(getContext());
        playbackRecordingButton.setImageResource(R.drawable.ic_play_arrow);
        playbackRecordingButton.setLayoutParams(params);
        playbackRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.removeAllViews();
                buttonsLayout.addView(stopPlaybackButton);

                player.start();
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlaybackButton.callOnClick();
                    }
                });
            }
        });

        stopPlaybackButton = new ImageButton(getContext());
        stopPlaybackButton.setImageResource(R.drawable.ic_stop_black_24dp);
        stopPlaybackButton.setLayoutParams(params);
        stopPlaybackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.removeAllViews();
                buttonsLayout.addView(playbackRecordingButton);
                buttonsLayout.addView(clearRecordingButton);

                player.stop();
                try {
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        clearRecordingButton = new ImageButton(getContext());
        clearRecordingButton.setImageResource(R.drawable.ic_delete_forever_black_24dp);
        clearRecordingButton.setLayoutParams(params);
        clearRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.removeAllViews();
                buttonsLayout.addView(recordButton);

                recorder.reset();
                player.reset();
                setRecorder();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        buttonsLayout.removeAllViews();
        buttonsLayout.addView(recordButton);

        if (recorder == null) {
            setRecorder();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (isRecording) {
            recorder.stop();
            isRecording = false;
        }

        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }

        File file = new File(outputPath);
        if (file.exists()) {
            file.delete();
        }
    }

    private void setRecorder() {

        if (recorder == null) {
            recorder = new MediaRecorder();
        }

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(outputPath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendInput(final ChatService chatService) {

        if (isRecording) {
            return;
        }

        final File file = new File(outputPath);

        if (file.exists()) {

            TimeZone tz = TimeZone.getDefault();
            DateFormat df = SimpleDateFormat.getDateTimeInstance();
            df.setTimeZone(tz);
            String timeStamp = df.format(new Date());

            final StorageReference storageReference = MyFirebaseStorage.getStorage().getReference().child("audio_messages").child(getCurrentUserId()).child(timeStamp);

            Uri uri = Uri.fromFile(file);
            UploadTask task = storageReference.putFile(uri);
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ChatMessage newMessage = new ChatMessage(getCurrentUserId(), getChatPartnerId(), AUDIO_MESSAGE, storageReference.getPath());

                    try {
                        chatService.sendMessage(newMessage);
                        file.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
