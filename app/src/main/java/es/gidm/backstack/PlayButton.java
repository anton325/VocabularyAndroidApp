package es.gidm.backstack;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

class PlayButton extends Button {
    boolean mStartPlaying = true;
    String fileName;
    MediaPlayer player;
    OnClickListener clicker = new OnClickListener() {
        public void onClick(View v) {
            onPlay(mStartPlaying);
            if (mStartPlaying) {
                setText("Stop playing");
            } else {
                setText("Start playing");
            }
            mStartPlaying = !mStartPlaying;
        }
    };

    public PlayButton(Context ctx, String filename) {
        super(ctx);
        this.fileName = filename;
        setText("Start playing");
        setOnClickListener(clicker);
    }
    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e("Play Button", "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }
}