package vm.android.musicweirdo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vm.android.musicweirdo.data.Track;

/**
 * NotificationActivity used to display extra detail for the song played
 * */
public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = NotificationActivity.class.getSimpleName();
    private List<Track> mTrackList = new ArrayList<>();
    private int listIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        this.mTrackList = PlayerActivity.getmTrackList();

        Intent mIntent = getIntent();
        Log.i(NotificationActivity.TAG, mIntent.toString());
        listIndex = mIntent.getIntExtra("Index", -1);

        final TextView songTitle = (TextView) findViewById(R.id.tv_song_title);
        final TextView songCountry = (TextView) findViewById(R.id.tv_song_country);
        final TextView songDescription = (TextView) findViewById(R.id.tv_song_description);

        songTitle.setText(this.mTrackList.get(listIndex).getTrackName());
        songCountry.setText(this.mTrackList.get(listIndex).getTrackCountry());
        songDescription.setText(this.mTrackList.get(listIndex).getTrackDescription());
    }
}
