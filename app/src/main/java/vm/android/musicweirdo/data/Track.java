package vm.android.musicweirdo.data;

/**
 * Created by vitormiguel on 04/04/17.
 */

public class Track {

    private String trackName;
    private String trackCountry;
    private String trackDescription;
    private int trackId;

    public Track(String trackName, String trackCountry, String trackDescription, int trackId) {
        this.trackName = trackName;
        this.trackCountry = trackCountry;
        this.trackDescription = trackDescription;
        this.trackId = trackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackCountry() {
        return trackCountry;
    }

    public void setTrackCountry(String trackCountry) {
        this.trackCountry = trackCountry;
    }

    public String getTrackDescription() {
        return trackDescription;
    }

    public void setTrackDescription(String trackDescription) {
        this.trackDescription = trackDescription;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Track{");
        sb.append("trackName='").append(trackName).append('\'');
        sb.append(", trackCountry='").append(trackCountry).append('\'');
        sb.append(", trackDescription='").append(trackDescription).append('\'');
        sb.append(", trackId=").append(trackId);
        sb.append('}');
        return sb.toString();
    }
}

