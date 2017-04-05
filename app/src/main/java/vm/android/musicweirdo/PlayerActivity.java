package vm.android.musicweirdo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vm.android.musicweirdo.adapters.TrackListAdapter;
import vm.android.musicweirdo.data.Track;
import vm.android.musicweirdo.services.PlayerService;
/**
 * PlayerActivity is the MainActivity of the application handling the list view with all songs
 * together with the player controls, is implementing onClickListener and onItemClickListener,
 * for handling click events in the buttons and in the list.
 * */
public class PlayerActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private static final String TAG = PlayerActivity.class.getSimpleName();

    // variables controlling the service and the list of songs
    private PlayerService mPlayerService;
    private boolean mIsBound = false;
    private static ArrayList<Integer> mPlayList = new ArrayList<>();
    private static List<Track> mTrackList = new ArrayList<>();

    // views from the layout
    private Button mBtnPlay;
    private Button mBtnPause;
    private Button mBtnStop;
    private Button mBtnPrev;
    private Button mBtnNext;
    private ListView listView;

    // static methods for the adapter and the service accessing the song list
    public static ArrayList<Integer> getmPlayList() { return mPlayList; }
    public static List<Track> getmTrackList() {
        return mTrackList;
    }

    // service binding definition
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            mPlayerService = binder.getService();
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mIsBound = false;
        }
    };

    // activity lifecycle onCreate method creates the views and setup the playlist
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        setPlayList();

        this.mBtnPlay = (Button) findViewById(R.id.btn_play_player);
        this.mBtnPlay.setOnClickListener(this);

        this.mBtnPause = (Button) findViewById(R.id.btn_pause_player);
        this.mBtnPause.setOnClickListener(this);

        this.mBtnStop = (Button) findViewById(R.id.btn_stop_player);
        this.mBtnStop.setOnClickListener(this);

        this.mBtnPrev = (Button) findViewById(R.id.btn_prev_player);
        this.mBtnPrev.setOnClickListener(this);

        this.mBtnNext = (Button) findViewById(R.id.btn_next_player);
        this.mBtnNext.setOnClickListener(this);

        this.listView = (ListView) findViewById(R.id.track_list_view);
        this.listView.setAdapter(new TrackListAdapter(this,0,this.mTrackList));
        this.listView.setOnItemClickListener(this);

    }

    // activity lifecycle onStart bind the service to the activity
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    // activity lifecycle onDestroy clear the song list, unbind and terminate the service
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTrackList.clear();

        if (isFinishing()) {
            unbindService(serviceConnection);
            mIsBound = false;
            mPlayerService.stopSelf();
        }
    }

    // interface onClick handling the buttons
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_play_player:
                Log.d(PlayerActivity.TAG,"Button Play Clicked");
                this.mPlayerService.playTrack();
                break;
            case R.id.btn_pause_player:
                Log.d(PlayerActivity.TAG,"Button Pause Clicked");
                this.mPlayerService.pauseTrack();
                break;
            case R.id.btn_stop_player:
                Log.d(PlayerActivity.TAG,"Button Stop Clicked");
                this.mPlayerService.stopTrack();
                break;
            case R.id.btn_prev_player:
                Log.d(PlayerActivity.TAG,"Button Prev Clicked");
                this.mPlayerService.prevTrack();
                break;
            case R.id.btn_next_player:
                Log.d(PlayerActivity.TAG,"Button Next Clicked");
                this.mPlayerService.nextTrack();
                break;
        }

    }

    // interface onItemClick handling the list view clicks
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(PlayerActivity.TAG,"Item " + position + " Clicked");

        if (mIsBound) {
            this.mPlayerService.playSelectedTrack(position);
        }
    }

    // setPlayList is the method used to setup the playlist for the songs
    private void setPlayList() {
        int[] rawValues = {
                R.raw.bensoundbrazilsamba,
                R.raw.bensoundcountryboy,
                R.raw.bensoundindia,
                R.raw.bensoundlittleplanet,
                R.raw.bensoundpsychedelic,
                R.raw.bensoundrelaxing,
                R.raw.bensoundtheelevatorbossanova
        };
        String[] countryList = {
                "Brazil",
                "USA",
                "India",
                "Iceland",
                "South Korea",
                "Indonesia",
                "Brazil"
        };
        String [] descriptions = {
                "Samba is a Brazilian musical genre and dance style, with its roots in Africa via the West African slave trade religious particularly of Angola and African traditions.",
                "Country music is a genre of American popular originated Southern States in the 1920s music that in the United",
                "The music of India includes multiple varieties of folk music, pop, and Indian classical music. India's classical music tradition, including Hindustani music and Carnatic, has a history spanning millennia and developed over several eras",
                "The music of Iceland includes vibrant folk and pop traditions. Well-known artists from Iceland include medieval music group Voces Thules, alternative rock band The Sugarcubes, singers Björk and Emiliana Torrini, post- rock band Sigur Rós and indie folk/indie pop band Of Monsters and Men",
                "The Music of South Korea has evolved over the course of the decades since the end of the Korean War, and has its roots in the music of the Korean people, who have inhabited the Korean peninsula for over a millennium. Contemporary South Korean music can be divided into three different categories: Traditional Korean folk music, popular music, or K- pop, and Western- influenced non-popular music",
                "The music of Indonesia demonstrates its cultural diversity, the local musical creativity, as well as subsequent foreign musical influences that shaped contemporary music scenes of Indonesia. Nearly thousands Indonesian having its own cultural and artistic history and character Nearly of islands",
                "Samba is a Brazilian musical genre and dance style, with its roots in Africa via the West African slave trade religious particularly of Angola"
        };

        for (int i = 0; i < rawValues.length; i++) {
            this.mPlayList.add(rawValues[i]);
            this.mTrackList.add(new Track(this.getResources().getResourceEntryName(rawValues[i]),
                    countryList[i],descriptions[i],rawValues[i]));
        }
    }
}
