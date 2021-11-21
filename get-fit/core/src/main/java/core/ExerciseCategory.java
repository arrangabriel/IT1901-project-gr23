package core;

public enum ExerciseCategory {
    /**
     * Represents strength exercise.
     */
    STRENGTH(StrengthSubCategory.values()),
    /**
     * Represents a running exercise.
     */
    RUNNING(CardioSubCategory.values()),
    /**
     * Represents a cycling exercise.
     */
    CYCLING(CardioSubCategory.values()),
    /**
     * Represents a swimming exercise.
     */
    SWIMMING(CardioSubCategory.values());

    /**
     * Array of Subcategories.
     */
    private final Subcategory[] subcategories;

    /**
     * Populates the subcategories array.
     *
     * @param subcategoriesArray The subcategories to populate with.
     */
    ExerciseCategory(final Subcategory[] subcategoriesArray) {
        this.subcategories = subcategoriesArray;
    }

    /**
     * Returns the subcategories.
     *
     * @return the subcategories.
     */
    public Subcategory[] getSubcategories() {
        return this.subcategories.clone();
    }
}
