package ch.p3n.apps.appfront.api.dto;

/**
 * Data transfer object for activation data.
 *
 * @author deluc1
 * @author zempm3
 */
public class ActivationDTO implements BaseDTO {

    private AuthenticationDTO authentication;

    private InterestDTO interest;

    private MatchType visibilityType;

    private int visibilityDuration;

    private String clientRandom;

    private String clientPushToken;

    public void setAuthentication(AuthenticationDTO authentication) {
        this.authentication = authentication;
    }

    public AuthenticationDTO getAuthentication() {
        return authentication;
    }

    public void setInterest(InterestDTO interest) {
        this.interest = interest;
    }

    public InterestDTO getInterest() {
        return interest;
    }

    public void setVisibilityType(MatchType visibilityType) {
        this.visibilityType = visibilityType;
    }

    public MatchType getVisibilityType() {
        return visibilityType;
    }

    public void setVisibilityDuration(int visibilityDuration) {
        this.visibilityDuration = visibilityDuration;
    }

    public int getVisibilityDuration() {
        return visibilityDuration;
    }

    public void setClientRandom(String clientRandom) {
        this.clientRandom = clientRandom;
    }

    public String getClientRandom() {
        return clientRandom;
    }

    public void setClientPushToken(String clientPushToken) {
        this.clientPushToken = clientPushToken;
    }

    public String getClientPushToken() {
        return clientPushToken;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ActivationDTO[authentication=").append(authentication);
        sb.append(", interest=").append(interest);
        sb.append(", visibilityType=").append(visibilityType);
        sb.append(", visibilityDuration=").append(visibilityDuration);
        sb.append(", clientRandom=").append(clientRandom);
        sb.append(", clientPushToken=").append(clientPushToken).append("]");
        return sb.toString();
    }

}
