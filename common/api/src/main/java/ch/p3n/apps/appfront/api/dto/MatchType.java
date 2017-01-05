package ch.p3n.apps.appfront.api.dto;

/**
 * Enum which describes the available match types.
 *
 * @author deluc1
 * @author zempm3
 */
public enum MatchType {

    BLUETOOTH(0), MAP(1);

    private final int typeId;

    MatchType(final int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    /**
     * Get {@link MatchType} given its id.
     * @param typeId type id.
     * @return instance of {@link MatchType}.
     */
    public static MatchType getByTypeId(final int typeId) {
        for (final MatchType type : values()) {
            if (type.getTypeId() == typeId) {
                return type;
            }
        }
        return null;
    }

}
