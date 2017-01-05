package ch.p3n.apps.appfront.domain.entity;

/**
 * Entity which represents an entry in database table {@code af_activity}.
 *
 * @author deluc1
 * @author zempm3
 */
public class ActivityEntity {

    private int id;

    private String name;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}