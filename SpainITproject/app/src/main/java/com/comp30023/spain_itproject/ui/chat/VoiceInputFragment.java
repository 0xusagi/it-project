package com.comp30023.spain_itproject.ui.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.comp30023.spain_itproject.ChatService;
import com.comp30023.spain_itproject.R;

public class VoiceInputFragment extends MessageInputFragment {

    private LinearLayout buttonsLayout;

    private ImageButton recordButton;
    private ImageButton stopRecordButton;
    private ImageButton playRecordingButton;
    private ImageButton stopRecordingButton;
    private ImageButton clearRecordingButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_voice_input, container, false);

        buttonsLayout = (LinearLayout) view.findViewById(R.id.voiceInput_buttonsLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);


        recordButton = new ImageButton(getContext());
        recordButton.setImageResource(R.drawable.ic_fiber_manual_record);
        recordButton.setLayoutParams(params);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.removeAllViews();
                buttonsLayout.addView(stopRecordButton);
            }
        });

        stopRecordButton = new ImageButton(getContext());
        stopRecordButton.setImageResource(R.drawable.ic_stop_black_24dp);
        stopRecordButton.setLayoutParams(params);
        stopRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.removeAllViews();
                buttonsLayout.addView(playRecordingButton);
                buttonsLayout.addView(clearRecordingButton);
            }
        });

        playRecordingButton = new ImageButton(getContext());
        playRecordingButton.setImageResource(R.drawable.ic_play_arrow);
        playRecordingButton.setLayoutParams(params);
        playRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.removeAllViews();
                buttonsLayout.addView(stopRecordingButton);
                buttonsLayout.addView(clearRecordingButton);
            }
        });

        stopRecordingButton = new ImageButton(getContext());
        stopRecordingButton.setImageResource(R.drawable.ic_stop_black_24dp);
        stopRecordingButton.setLayoutParams(params);
        stopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.removeAllViews();
                buttonsLayout.addView(playRecordingButton);
                buttonsLayout.addView(clearRecordingButton);
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
            }
        });

        buttonsLayout.addView(recordButton);

        getIdArguments();

        return view;
    }

    @Override
    public void sendInput(ChatService chatService) {

    }
}
