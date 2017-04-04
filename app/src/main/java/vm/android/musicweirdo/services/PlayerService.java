package vm.android.musicweirdo.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import vm.android.musicweirdo.NotificationActivity;
import vm.android.musicweirdo.R;

import static vm.android.musicweirdo.PlayerActivity.getmPlayList;

/**
 * PlayerService is a bind service implementing onPreparedListener for each time that the
 * MediaPlayer method onPrepareAsync is called it triggers a callback
 * */

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener {
    private static final String TAG = PlayerService.class.getSimpleName();

    // class variables to handle the music player, track list and service binding
    private IBinder mBinder = new LocalBinder();
    private MediaPlayer mPlayer;
    private ArrayList<Integer> mPlayList = new ArrayList<>();
    private int mMusicIndex = 0;

    // local binder returns provides a getter to the service used in the activity
    public class LocalBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    // constructors
    public PlayerService() {}

    public PlayerService(IBinder serviceInfo) {
        this.mBinder = serviceInfo;
    }

    // service lifecycle onBind provides the binding with the service returning a Local Binder
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // service lifecylce onUnbind unbinds the service
    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    // service lifecycle onCreate initialize the media player and set up the play list
    @Override
    public void onCreate() {
        Log.d(PlayerService.TAG, "In-onCreate");
        super.onCreate();

        setupPlaylist();
        createPlayer();
    }

    // interface onPrepared starts the media player when the callback is triggered
    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(PlayerService.TAG, "In-onPrepared");
        mp.start();
        triggerNotification();
    }

    // create playlist
    private void setupPlaylist() {
        this.mPlayList = getmPlayList();
    }

    // create media player and assign onPrepareListener
    private void createPlayer() {
        Log.d(PlayerService.TAG, "In-createPlayer");
        this.mPlayer = new MediaPlayer();
        this.mPlayer.setOnPreparedListener(this);
    }

    // changeSong provides a try catch block for reseting and assigning a new song to the player
    public void changeSong(int songIdx) {
        this.mPlayer.reset();
        try {
            this.mPlayer.setDataSource(this,
                    Uri.parse("android.resource://"
                            + getPackageName()
                            + "/raw/"
                            + this.mPlayList.get(songIdx))
            );
            this.mMusicIndex = songIdx;
            this.mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // media player lifecycle starts the player and changes the song
    public void startPlayer(int songIdx) {
        Log.d(PlayerService.TAG, "In-startPlayer");
        this.mPlayer.reset();
        changeSong(songIdx);
    }

    // media player lifecycle start
    public void playTrack() {
        if (this.mPlayer != null && !this.mPlayer.isPlaying()) {
            this.mPlayer.start();
        }
    }

    // media player lifecycle pause
    public void pauseTrack() {
        if (this.mPlayer != null && this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
        }
    }

    // media player lifecycle stop
    public void stopTrack() {
        if (this.mPlayer != null) {
            this.mPlayer.stop();
        }
    }

    // media player lifecycle previous track
    public void prevTrack() {
        this.mMusicIndex--;
        if(this.mMusicIndex < 0) {
            this.mMusicIndex = this.mPlayList.size() - 1;
            startPlayer(this.mMusicIndex);
            this.mPlayer.start();
        }
        else {
            startPlayer(this.mMusicIndex);
            this.mPlayer.start();
        }
    }

    // media player lifecycle next track
    public void nextTrack() {
        this.mMusicIndex++;
        if(this.mMusicIndex > this.mPlayList.size() - 1) {
            this.mMusicIndex = 0;
            startPlayer(this.mMusicIndex);
            this.mPlayer.start();
        }
        else {
            startPlayer(this.mMusicIndex);
            this.mPlayer.start();
        }
    }

    // // media player lifecycle handling list view selection
    public void playSelectedTrack(int position) {
        Log.d(PlayerService.TAG, "In-playSelectedTrack");

        if (this.mPlayer != null && this.mPlayer.isPlaying()) {
            this.mPlayer.stop();
        }

        this.startPlayer(position);
    }

    // create notification for song play
    private void triggerNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Now Playing "
                        + this.getResources().getResourceEntryName(this.mPlayList.get(this.mMusicIndex)))
                .setContentText("Tap to Read more")
                .setAutoCancel(false);

        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        notificationIntent.putExtra("Index", this.mMusicIndex);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 ,notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notifManager = (NotificationManager) this.getSystemService(Service.NOTIFICATION_SERVICE);
        notifManager.notify(2, builder.build());
    }
}