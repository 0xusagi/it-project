package com.comp30023.spain_itproject;

import android.media.MediaRecorder;

import java.io.IOException;

public class VoiceRecorder extends MediaRecorder {

    private String outputPath;

    public VoiceRecorder(String outputPath) {
        super();
        this.outputPath = outputPath;
    }

    @Override
    public void prepare() throws IOException, IllegalStateException {

        this.setAudioSource(MediaRecorder.AudioSource.MIC);
        this.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        this.setOutputFile(outputPath);
        this.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        super.prepare();
    }
}
