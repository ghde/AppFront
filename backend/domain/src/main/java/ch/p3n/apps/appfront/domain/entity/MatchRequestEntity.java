package ch.p3n.apps.appfront.domain.entity;

/**
 * Entity which represents an entry in database table {@code af_match_request}.
 *
 * @author deluc1
 * @author zempm3
 */
public class MatchRequestEntity {

    private int id;

    private InterestEntity interest;

    private InterestEntity otherInterest;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setInterest(InterestEntity interest) {
        this.interest = interest;
    }

    public InterestEntity getInterest() {
        return interest;
    }

    public void setOtherInterest(InterestEntity otherInterest) {
        this.otherInterest = otherInterest;
    }

    public InterestEntity getOtherInterest() {
        return otherInterest;
    }

}
