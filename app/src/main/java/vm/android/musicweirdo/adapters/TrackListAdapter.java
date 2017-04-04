package vm.android.musicweirdo.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vm.android.musicweirdo.PlayerActivity;
import vm.android.musicweirdo.R;
import vm.android.musicweirdo.data.Track;

/**
 * TrackListAdapter is the adapter that inflates the track_list layout into a list view
 * */

public class TrackListAdapter extends ArrayAdapter<Track> {
    private static final String TAG = TrackListAdapter.class.getSimpleName();
    private Context mContext;

    public TrackListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Track> objects) {
        super(context, resource, objects);
        this.mContext = context;
        Log.w(TrackListAdapter.TAG, "" + objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.track_list,null);

        TextView titleTextView = (TextView) rowView.findViewById(R.id.tv_row_track_title);
        titleTextView.setText(PlayerActivity.getmTrackList().get(position).getTrackName());

        TextView bodyTextView = (TextView) rowView.findViewById(R.id.tv_row_track_country);
        bodyTextView.setText(PlayerActivity.getmTrackList().get(position).getTrackCountry());

        return rowView;
    }
}
