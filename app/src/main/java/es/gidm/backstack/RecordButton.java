package es.gidm.backstack;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import java.io.IOException;

class RecordButton extends Button {
    boolean mStartRecording = true;
    MediaRecorder recorder;
    String fileName;
    PopupWindow pw;

    OnClickListener clicker = new OnClickListener() {
        public void onClick(View v) {
            onRecord(mStartRecording);
            if (mStartRecording) {
                setText("Listo");
            } else {
                setText("Grabar");
            }
            mStartRecording = !mStartRecording;
        }
    };


    public RecordButton(Context ctx, String fileName, PopupWindow pw) {
        super(ctx);
        this.fileName = fileName;
        setText("Grabar");
        setOnClickListener(clicker);
        this.pw = pw;
    }
    private void startRecording() {
        recorder = new MediaRecorder();
        try{
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        }
        catch (Exception e) {
            Log.e("Recorder","failed",e);
            return;
        }

        try {
            recorder.prepare();
        } catch (Exception e) {
            Log.e("Record Button", "prepare() failed",e);
            return;
        }
        recorder.start();
    }
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    public void stopRecording() {
        try {
            recorder.stop();
            recorder.reset();    // set state to idle
            recorder.release();
        }
        catch(Exception e) {
            Log.e("StopRecording","failed",e);
        }
        recorder = null;
    }
}