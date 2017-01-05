package ch.p3n.apps.appfront.domain.entity;

/**
 * Entity which represents an entry in database table {@code af_authentication}.
 *
 * @author deluc1
 * @author zempm3
 */
public class AuthenticationEntity {

    private int id;

    private String clientId;

    private String clientPublicKey;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientPublicKey(String clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
    }

    public String getClientPublicKey() {
        return clientPublicKey;
    }

}
