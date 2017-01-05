package ch.p3n.apps.appfront.domain.entity;

import java.time.LocalDateTime;

/**
 * Entity which represents an entry in database table {@code af_interest}.
 *
 * @author deluc1
 * @author zempm3
 */
public class InterestEntity {

    private int id;

    private String interestId;

    private String verifyHash;

    private String clientPushToken;

    private int visibilityType;

    private LocalDateTime visibilityEndDate;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setInterestId(String interestId) {
        this.interestId = interestId;
    }

    public String getInterestId() {
        return interestId;
    }

    public void setVerifyHash(String verifyHash) {
        this.verifyHash = verifyHash;
    }

    public String getVerifyHash() {
        return verifyHash;
    }

    public void setClientPushToken(String clientPushToken) {
        this.clientPushToken = clientPushToken;
    }

    public String getClientPushToken() {
        return clientPushToken;
    }

    public void setVisibilityType(int visibilityType) {
        this.visibilityType = visibilityType;
    }

    public int getVisibilityType() {
        return visibilityType;
    }

    public void setVisibilityEndDate(LocalDateTime visibilityEndDate) {
        this.visibilityEndDate = visibilityEndDate;
    }

    public LocalDateTime getVisibilityEndDate() {
        return visibilityEndDate;
    }

}