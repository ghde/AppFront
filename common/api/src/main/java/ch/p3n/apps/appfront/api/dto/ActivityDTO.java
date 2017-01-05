package ch.p3n.apps.appfront.api.dto;

/**
 * Data transfer object for activity data.
 *
 * @author deluc1
 * @author zempm3
 */
public class ActivityDTO implements BaseDTO {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ActivityDTO[name=").append(name).append("]");
        return sb.toString();
    }

}
