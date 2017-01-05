package ch.p3n.apps.appfront.test.util;

import ch.p3n.apps.appfront.api.dto.MatchType;
import ch.p3n.apps.appfront.domain.entity.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Utility class to create entities for testing purposes.
 *
 * @author deluc1
 * @author zempm3
 */
public class TestUtil {

    private TestUtil() {
        // private constructor as all methods are static.
    }

    public static ActivityEntity createActivityEntity() {
        final ActivityEntity ae = new ActivityEntity();
        ae.setName(UUID.randomUUID().toString());
        return ae;
    }

    public static ActivityEntity createActivityEntity(int id) {
        final ActivityEntity ae = new ActivityEntity();
        ae.setId(id);
        ae.setName(UUID.randomUUID().toString());
        return ae;
    }

    public static AuthenticationEntity createAuthenticationEntity() {
        final AuthenticationEntity ae = new AuthenticationEntity();
        ae.setClientId(UUID.randomUUID().toString());
        ae.setClientPublicKey(UUID.randomUUID().toString());
        return ae;
    }

    public static InterestActivityEntity createInterestActivityEntity(final ActivityEntity ae, final InterestEntity ie) {
        final InterestActivityEntity iae = new InterestActivityEntity();
        iae.setActivity(ae);
        iae.setInterest(ie);
        return iae;
    }

    public static InterestEntity createInterestEntity(final MatchType matchType, final LocalDateTime visibilityEndDate) {
        final InterestEntity ie = new InterestEntity();
        ie.setClientPushToken("PUSH_TOKEN");
        ie.setInterestId(UUID.randomUUID().toString());
        ie.setVerifyHash("VERIFY_HASH");
        ie.setVisibilityType(matchType.getTypeId());
        ie.setVisibilityEndDate(visibilityEndDate);
        return ie;
    }

    public static InterestEntity createInterestEntity(int id) {
        final InterestEntity ie = new InterestEntity();
        ie.setId(id);
        ie.setInterestId(UUID.randomUUID().toString());
        return ie;
    }

    public static MatchRequestEntity createMatchRequestEntity(final InterestEntity interest, final InterestEntity otherInterest) {
        final MatchRequestEntity mre = new MatchRequestEntity();
        mre.setInterest(interest);
        mre.setOtherInterest(otherInterest);
        return mre;
    }

}
