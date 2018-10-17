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
import com.comp30023.spain_itproject.firebase.storage.FirebaseStorageService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class VoiceInputFragment extends MessageInputFragment {

    private enum State {
        WAITING,
        RECORDING,
        RECORDED,
        PLAYINGBACK,
        SENDING
    }

    public static final String AUDIO_MESSAGE = "Audio message";

    private LinearLayout buttonsLayout;

    private ImageButton recordButton;
    private ImageButton stopRecordButton;
    private ImageButton playbackRecordingButton;
    private ImageButton stopPlaybackButton;
    private ImageButton clearRecordingButton;

    private MediaRecorder recorder;
    private MediaPlayer player;

    private String outputPath;

    private State currentState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_voice_input, container, false);

        buttonsLayout = (LinearLayout) view.findViewById(R.id.voiceInput_buttonsLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

        currentState = State.WAITING;

        outputPath = getActivity().getCacheDir().getAbsolutePath() + "/tempRecording.3gp";

        setRecorder();

        recordButton = new ImageButton(getContext());
        recordButton.setImageResource(R.drawable.ic_fiber_manual_record);
        recordButton.setLayoutParams(params);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recorder.start();

                buttonsLayout.removeAllViews();
                buttonsLayout.addView(stopRecordButton);

                currentState = State.RECORDING;
            }
        });

        stopRecordButton = new ImageButton(getContext());
        stopRecordButton.setImageResource(R.drawable.ic_stop_black_24dp);
        stopRecordButton.setLayoutParams(params);
        stopRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recorder.stop();
                currentState = State.RECORDED;

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

                currentState = State.PLAYINGBACK;

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

                currentState = State.RECORDED;

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

                currentState = State.WAITING;

                File file = new File(outputPath);
                if (file.exists()) {
                    file.delete();
                }

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

        switch (currentState) {
            case RECORDING:
                recorder.stop();
                break;

            default:
                break;
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
    public void sendInput(final ChatService chatService) throws Exception {

        switch (currentState) {

            case RECORDED:

                final File file = new File(outputPath);
                if (file.exists()) {

                    currentState = State.SENDING;
                    chatService.sendAudioMessage(AUDIO_MESSAGE, file);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            clearRecordingButton.callOnClick();
                        }
                    });

                }
                break;

            default:
                break;

        }

    }
}
