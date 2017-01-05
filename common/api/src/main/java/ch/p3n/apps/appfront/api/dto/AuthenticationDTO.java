package ch.p3n.apps.appfront.api.dto;

/**
 * Data transfer object for authentication data.
 *
 * @author deluc1
 * @author zempm3
 */
public class AuthenticationDTO implements BaseDTO {

    private String clientId;

    private String clientRandom;

    private String clientPublicKey;

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientRandom(String clientRandom) {
        this.clientRandom = clientRandom;
    }

    public String getClientRandom() {
        return clientRandom;
    }

    public void setClientPublicKey(String clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
    }

    public String getClientPublicKey() {
        return clientPublicKey;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AuthenticationDTO[clientId=").append(clientId);
        sb.append(", clientRandom=").append(clientRandom);
        sb.append(", clientPublicKey=").append(clientPublicKey).append("]");
        return sb.toString();
    }

}
