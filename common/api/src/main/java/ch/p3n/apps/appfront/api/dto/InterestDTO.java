package ch.p3n.apps.appfront.api.dto;

import java.util.Collection;

/**
 * Data transfer object for interest data.
 *
 * @author deluc1
 * @author zempm3
 */
public class InterestDTO implements BaseDTO {

    private String interestId;

    private Collection<ActivityDTO> interests;

    public void setInterestId(String interestId) {
        this.interestId = interestId;
    }

    public String getInterestId() {
        return interestId;
    }

    public void setInterests(Collection<ActivityDTO> interests) {
        this.interests = interests;
    }

    public Collection<ActivityDTO> getInterests() {
        return interests;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("InterestDTO[interestId=").append(interestId);
        sb.append(", interests=").append(interests).append("]");
        return sb.toString();
    }

}
