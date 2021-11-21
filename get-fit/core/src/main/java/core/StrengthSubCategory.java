package core;

public enum StrengthSubCategory implements Subcategory {
    /**
     * Push exercises.
     */
    PUSH,
    /**
     * Pull exercises.
     */
    PULL,
    /**
     * Legs exercises.
     */
    LEGS,
    /**
     * Full body exercises.
     */
    FULLBODY;

    @Override
    public Subcategory getValueOf(final String name) {
        return StrengthSubCategory.valueOf(name);
    }
}
