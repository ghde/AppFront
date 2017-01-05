package ch.p3n.apps.appfront.domain.entity;

/**
 * Entity which represents an entry in database table {@code af_interest_activity}.
 *
 * @author deluc1
 * @author zempm3
 */
public class InterestActivityEntity {

    private InterestEntity interest;

    private ActivityEntity activity;

    public void setInterest(InterestEntity interest) {
        this.interest = interest;
    }

    public InterestEntity getInterest() {
        return interest;
    }

    public void setActivity(ActivityEntity activity) {
        this.activity = activity;
    }

    public ActivityEntity getActivity() {
        return activity;
    }

}
