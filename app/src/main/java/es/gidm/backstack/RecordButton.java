package es.gidm.backstack;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

class RecordButton extends Button {
    boolean mStartRecording = true;
    MediaRecorder recorder;
    String fileName;

    OnClickListener clicker = new OnClickListener() {
        public void onClick(View v) {
            onRecord(mStartRecording);
            if (mStartRecording) {
                setText("Stop recording");
            } else {
                setText("Start recording");
            }
            mStartRecording = !mStartRecording;
        }
    };

    public RecordButton(Context ctx, String fileName) {
        super(ctx);
        this.fileName = fileName;
        setText("Start recording");
        setOnClickListener(clicker);
    }
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }


    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("Record Button", "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}