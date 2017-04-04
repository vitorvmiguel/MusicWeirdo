package vm.android.musicweirdo.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vm.android.musicweirdo.data.Track;

import static vm.android.musicweirdo.PlayerActivity.getmPlayList;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener {
    private static final String TAG = PlayerService.class.getSimpleName();

    private Uri path;
    private IBinder mBinder = new LocalBinder();
    private MediaPlayer mPlayer;
    private ArrayList<Integer> mPlayList = new ArrayList<>();
    public int musicIndex = 0;

    public class LocalBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    public PlayerService() {}

    public PlayerService(IBinder serviceInfo) {
        this.mBinder = serviceInfo;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mPlayList = getmPlayList();
        initializePlayer();
        this.mPlayer.setOnPreparedListener(this);
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(PlayerService.TAG, "In-onPrepared");
        this.mPlayer.start();
    }

    private void initializePlayer() {

        this.path = Uri.parse("android.resource://"
                        + getPackageName()
                        + "/raw/"
                        + this.mPlayList.get(musicIndex)
        );

        Log.d(PlayerService.TAG, "The raw path: " + this.path);

        this.mPlayer = new MediaPlayer();

        this.mPlayer.reset();

        this.mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(this,path);
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }
        this.mPlayer.prepareAsync();


    }

    public void changeSong(int songIdx) {
        this.mPlayer.reset();
        try {
            this.mPlayer.setDataSource(this,
                    Uri.parse("android.resource://"
                            + getPackageName()
                            + "/raw/"
                            + this.mPlayList.get(songIdx))
            );
            this.musicIndex = songIdx;
            this.mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startPlayer(int songIdx) {
        this.mPlayer.reset();
        changeSong(songIdx);
    }

    public void playTrack() {
        if (this.mPlayer != null && !this.mPlayer.isPlaying()) {
            this.mPlayer.start();
        }
    }

    public void pauseTrack() {
        if (this.mPlayer != null && this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
        }
    }

    public void stopTrack() {
        if (this.mPlayer != null) {
            this.mPlayer.stop();
        }
    }

    public void prevTrack() {
        this.musicIndex--;
        if(this.musicIndex < 0) {
            this.musicIndex = this.mPlayList.size() - 1;
            startPlayer(this.musicIndex);
            this.mPlayer.start();
        }
        else {
            startPlayer(this.musicIndex);
            this.mPlayer.start();
        }
    }

    public void nextTrack() {
        this.musicIndex++;
        if(this.musicIndex > this.mPlayList.size() - 1) {
            this.musicIndex = 0;
            changeSong(this.musicIndex);
            this.mPlayer.start();
        }
        else {
            changeSong(this.musicIndex);
            this.mPlayer.start();
        }
    }

    public void playSelectedTrack(int position) {
        Log.d(PlayerService.TAG, "In-playSelectedTrack");

        if (this.mPlayer != null && this.mPlayer.isPlaying()) {
            this.mPlayer.stop();
        }

        this.startPlayer(position);
    }
}