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

import java.util.ArrayList;
import java.util.List;

import vm.android.musicweirdo.adapters.TrackListAdapter;
import vm.android.musicweirdo.data.Track;
import vm.android.musicweirdo.services.PlayerService;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private static final String TAG = PlayerActivity.class.getSimpleName();

    private PlayerService mPlayerService;
    private boolean mIsBound = false;
    private static ArrayList<Integer> mPlayList = new ArrayList<>();
    private static List<Track> mTrackList = new ArrayList<>();

    private Button mBtnPlay;
    private Button mBtnPause;
    private Button mBtnStop;
    private Button mBtnPrev;
    private Button mBtnNext;
    private ListView listView;

    public static ArrayList<Integer> getmPlayList() { return mPlayList; }
    public static List<Track> getmTrackList() {
        return mTrackList;
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        setPlayList();

        this.listView = (ListView) findViewById(R.id.track_list_view);

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

        this.listView.setAdapter(new TrackListAdapter(this,0,this.mTrackList));
        this.listView.setOnItemClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsBound) {
            unbindService(serviceConnection);
            mIsBound = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", mIsBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mIsBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTrackList.clear();

        if (mIsBound) {
            unbindService(serviceConnection);
            mIsBound = false;
            mPlayerService.stopSelf();
        }
    }

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(PlayerActivity.TAG,"Item " + position + " Clicked");

        if (mIsBound) {
            this.mPlayerService.playSelectedTrack(position);
        }
    }

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

        for (int i = 0; i < rawValues.length; i++) {
            this.mPlayList.add(rawValues[i]);
            this.mTrackList.add(new Track(this.getResources().getResourceEntryName(rawValues[i]),
                    countryList[i],"description",rawValues[i]));
        }
    }
}
