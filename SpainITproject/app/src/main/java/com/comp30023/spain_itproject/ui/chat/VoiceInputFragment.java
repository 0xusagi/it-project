package com.comp30023.spain_itproject.ui.chat;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
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
import com.comp30023.spain_itproject.VoiceRecorder;

import java.io.File;
import java.io.IOException;

/**
 * Records audio input to send as message
 */
public class VoiceInputFragment extends MessageInputFragment {

    private enum State {
        WAITING,
        RECORDING,
        RECORDED,
        PLAYINGBACK,
        SENDING,
        SENT
    }

    public static final String AUDIO_MESSAGE = "Voice message";
    public static final String OUTPUT_FILE_NAME = "/tempRecording.3gp";

    private LinearLayout buttonsLayout;

    private ImageButton recordButton;
    private ImageButton stopRecordButton;
    private ImageButton playbackRecordingButton;
    private ImageButton stopPlaybackButton;
    private ImageButton clearRecordingButton;

    private VoiceRecorder voiceRecorder;
    private MediaPlayer player;

    private String outputPath;

    private State currentState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_voice_input, container, false);

        buttonsLayout = (LinearLayout) view.findViewById(R.id.voiceInput_buttonsLayout);

        //LayoutParams are same for each new button (evenly share the entire panel)
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

        //Send the output to the cache
        outputPath = getActivity().getCacheDir().getAbsolutePath() + OUTPUT_FILE_NAME;
        currentState = State.WAITING;

        setRecorder();
        player = new MediaPlayer();

        recordButton = new ImageButton(getContext());
        recordButton.setImageResource(R.drawable.ic_fiber_manual_record);
        recordButton.setLayoutParams(params);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                voiceRecorder.start();

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

                voiceRecorder.stop();
                currentState = State.RECORDED;

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

                if (currentState != State.SENDING) {

                    buttonsLayout.removeAllViews();
                    buttonsLayout.addView(recordButton);

                    currentState = State.WAITING;

                    File file = new File(outputPath);
                    if (file.exists()) {
                        file.delete();
                    }

                    voiceRecorder.reset();
                    player.reset();
                    setRecorder();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Reset so that in WAITING state
        buttonsLayout.removeAllViews();
        buttonsLayout.addView(recordButton);
        currentState = State.WAITING;

        if (voiceRecorder == null) {
            setRecorder();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (currentState == State.RECORDING) {
            voiceRecorder.stop();
        }

        //Release the recorder
        if (voiceRecorder != null) {
            voiceRecorder.release();
            voiceRecorder = null;
        }

        //Release the player
        if (player != null) {
            player.release();
            player = null;
        }

        //Delete the temporary output file
        File file = new File(outputPath);
        if (file.exists()) {
            file.delete();
        }
    }

    //prepare the recorder
    private void setRecorder() {

        if (voiceRecorder == null) {
            voiceRecorder = new VoiceRecorder(outputPath);
        }

        try {
            voiceRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check that there is a recording waiting to be sent, and then send
     * @param chatService The ChatService used to send the recording
     * @throws Exception Thrown if there is a connection issue
     */
    @Override
    public void sendInput(final ChatService chatService) throws Exception {

        switch (currentState) {

            case RECORDED:

                final File file = new File(outputPath);

                //Ensure that there is a file containing the recording
                if (file.exists()) {

                    //Send the recording
                    currentState = State.SENDING;
                    chatService.sendAudioMessage(AUDIO_MESSAGE, file);

                    currentState = State.SENT;

                    //Clear the temp recording as it has been sent
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
