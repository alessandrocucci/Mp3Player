package it.alessandrocucci.mp3player;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.media.MediaPlayer.OnPreparedListener;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.TextView;

import java.io.IOException;

public class PlaySong extends Activity implements OnPreparedListener, MediaController.MediaPlayerControl{

    private String uri ="";
    private Uri uri2;
    int media_length;

    private static final String TAG = "AudioPlayer";

    public String AUDIO_FILE_NAME = "audioFileName";

    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private String audioFile;

    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audioplayer);

        final Intent intent = getIntent();
        final String action = intent.getAction();
        mediaPlayer = new MediaPlayer();
        mediaController = new MediaController(this);

        if(Intent.ACTION_VIEW.equals(action)) {

            uri2 = intent.getData();
            uri = uri2.getEncodedPath() + "  complete: " + uri2.toString();
            AUDIO_FILE_NAME = uri2.getPath();
            this.getIntent().putExtra(AUDIO_FILE_NAME,AUDIO_FILE_NAME);
            audioFile = this.getIntent().getStringExtra(AUDIO_FILE_NAME);
            ((TextView)findViewById(R.id.now_playing_text)).setText(audioFile.substring(audioFile.lastIndexOf("/")+1));


            mediaPlayer.setOnPreparedListener(this);



            try {
                mediaPlayer.setDataSource(audioFile);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                Log.e(TAG, "Could not open file " + audioFile + " for playback.", e);
            }
        }


    }



    @Override
    protected void onStop() {
        super.onStop();
        mediaController.hide();
        mediaPlayer.pause();
        media_length = mediaPlayer.getCurrentPosition();
    }



    @Override

    public int getAudioSessionId() {

        return 0;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mediaController.show();
        return false;
    }

    //--Questi sono i metodi del MediaPlayerControl ----------------------------------------------------
    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }
    //--------------------------------------------------------------------------------

    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared");
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(findViewById(R.id.main_audio_view));

        handler.post(new Runnable() {
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        mediaPlayer.seekTo(media_length);
        mediaPlayer.start();
    }

}