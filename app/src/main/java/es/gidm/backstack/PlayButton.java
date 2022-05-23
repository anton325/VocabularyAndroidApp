package es.gidm.backstack;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

class PlayButton extends Button {
    boolean mStartPlaying = true;
    String fileName;
    MediaPlayer player;
    OnClickListener clicker = new OnClickListener() {
        public void onClick(View v) {
            onPlay(mStartPlaying);
            if (mStartPlaying) {
                setText("Para");
            } else {
                setText("Reproducir");
            }
            mStartPlaying = !mStartPlaying;
        }
    };

    public PlayButton(Context ctx, String filename) {
        super(ctx);
        this.fileName = filename;
        setText("Reproducir");
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
            Toast.makeText(getContext(),"Todavía no existe una grabación",Toast.LENGTH_SHORT);
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }
}
