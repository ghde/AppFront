package ch.p3n.apps.appfront.api.dto;

/**
 * Data transfer object for interest match data.
 *
 * @author deluc1
 * @author zempm3
 */
public class MatchDTO implements BaseDTO {

    private MatchType reason;

    private String latitude;

    private String longitude;

    public void setReason(MatchType reason) {
        this.reason = reason;
    }

    public MatchType getReason() {
        return reason;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MatchDTO[reason=").append(reason);
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude).append("]");
        return sb.toString();
    }

}
