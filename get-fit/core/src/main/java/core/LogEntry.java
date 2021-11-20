package core;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Representation of a workout or exercise.
 */
public final class LogEntry {

    /**
     * The top end of the feeling scale.
     */
    public static final int MAXFEELING = 10;
    /**
     * The bottom end of the feeling scale.
     */
    public static final int MINFEELING = 1;
    /**
     * The max heart rate of a human being.
     */
    public static final int MAXHEARTRATEHUMAN = 300;
    /**
     * The min heart rate of a human being.
     */
    public static final int MINHEARTRATEHUMAN = 20;
    /**
     * Title of LogEntry.
     */
    private final String title;
    /**
     * Description of LogEntry.
     */
    private final String comment;
    /**
     * Date of LogEntry.
     */
    private final LocalDate date;
    /**
     * Duration of LogEntry.
     */
    private final Duration duration;
    /**
     * Category of LogEntry.
     */
    private final ExerciseCategory exerciseCategory;
    /**
     * Subcategory of LogEntry.
     */
    private final Subcategory exerciseSubCategory;
    /**
     * Feeling of LogEntry.
     */
    private final int feeling;
    /**
     * Distance of LogEntry.
     */
    private final Double distance;
    /**
     * Maximum heart rate of LogEntry.
     */
    private final Integer maxHeartRate;
    /**
     * ID of the LogEntry.
     */
    private String id;

    /**
     * A logEntry instance represents a single workout-entry internally.
     * Has fields for the elements of a workout-entry,
     * getters for them, and setters for those that should be mutable.
     *
     * @param builder the EntryBuilder used to build this LogEntry.
     */
    public LogEntry(final EntryBuilder builder)
            throws IllegalArgumentException {

        this.title = builder.ctitle;
        this.comment = builder.ccomment;
        this.date = builder.cdate;
        this.duration = builder.cduration;
        this.distance = builder.cdistance;
        this.feeling = builder.cfeeling;
        this.maxHeartRate = builder.cmaxHeartRate;
        this.exerciseCategory = builder.cexerciseCategory;
        this.exerciseSubCategory = builder.cexerciseSubCategory;

    }

    // It will become evident if this is needed later
    // public interface Category {}

    /**
     * Validates a EntryBuilder. Requirements:<br>
     * <br>
     * <b>id</b> must not be null and have a positive length. <br>
     * <br>
     *
     * <b>title</b> must not be null and have a positive length. <br>
     * <br>
     *
     * <b>date</b> must not be null and not be after current time.<br>
     * <br>
     *
     * <b>duration</b> must not be null and be positive. <br>
     * <br>
     *
     * <b>feeling</b> must be between 1 and 10. <br>
     * <br>
     *
     * <b>exerciseCategory</b> must not be null. <br>
     * <br>
     *
     * <b>comment</b> must either be null or of positive length. <br>
     * <br>
     *
     * <b>exerciseSubCategory</b> must either be null or a subcategory of
     * <b>exerciseCategory</b> <br>
     * <br>
     *
     * <b>distance</b> must either be null or be positive <br>
     * <br>
     *
     * <b>maxHeartRate</b> must either be null or be between 40 and 480 <br>
     * <br>
     *
     * @param builder the builder to validate.
     * @return true if builder is valid, otherwise false.
     */
    private static Validity validate(final EntryBuilder builder) {

        /* Required fields. */
        if (builder.ctitle == null || builder.ctitle.length() < 1) {

            return new Validity(false, "Title cannot be empty or null");
        }

        if (builder.cdate == null || builder.cdate.isAfter(LocalDate.now())) {

            return new Validity(false, "Date cannot be after now or null");
        }

        if (builder.cduration == null || builder.cduration.isNegative()
                || builder.cduration.isZero()) {

            return new Validity(false,
                    "Duration cannot be null and must be positive");
        }

        if (builder.cfeeling > LogEntry.MAXFEELING
                || builder.cfeeling < LogEntry.MINFEELING) {

            return new Validity(false,
                    String.format("Feeling must be between %i and %i",
                            LogEntry.MAXFEELING, LogEntry.MINFEELING));
        }

        if (builder.cexerciseCategory == null) {

            return new Validity(false, "Exercise category cannot be null");
        }

        /* Optional fields. */
        if (builder.ccomment != null && (builder.ccomment.length() < 1)) {

            return new Validity(false,
                    "Comment should not be empty, should be null instead");
        }

        if (builder.cexerciseSubCategory != null
                && (Arrays.stream(builder.cexerciseCategory.getSubcategories())
                .noneMatch(builder.cexerciseSubCategory::equals))) {

            return new Validity(false,
                    "Subcategory must be part of exercise category");
        }

        if (builder.cdistance != null && (builder.cdistance <= 0)) {

            return new Validity(false, "Distance must be positive");
        }

        if (builder.cmaxHeartRate != null
                && (builder.cmaxHeartRate > LogEntry.MAXHEARTRATEHUMAN
                ||
                builder.cmaxHeartRate < LogEntry.MINHEARTRATEHUMAN)) {

            return new Validity(false,
                    String.format("Heart rate must be between %d and %d",
                            LogEntry.MAXHEARTRATEHUMAN,
                            LogEntry.MINHEARTRATEHUMAN));
        }

        return new Validity(true, "ok");
    }

    // expand these in the future

    /**
     * Converts a string representation of a subcategory into a subcategory.
     *
     * @param category The string representation of the subcategory.
     * @return The actual subcategory or null if no match.
     */
    public static Subcategory stringToSubcategory(final String category) {

        Subcategory subCategory = null;
        outerLoop:
        for (ExerciseCategory exCategory : ExerciseCategory.values()) {
            for (Subcategory sub : exCategory.getSubcategories()) {
                try {
                    subCategory = sub.getValueOf(category);
                    if (subCategory != null) {
                        break outerLoop;
                    }
                } catch (Exception e) {
                    // NEQ
                }
            }
        }
        return subCategory;
    }

    /**
     * Parses a hashMap to create a logEntry.
     * @param map the map.
     * @return the parsed logEntry.
     */
    public static LogEntry fromHash(final HashMap<String, String> map) {
        String title = map.get("title");
        LocalDate date = LocalDate.parse(map.get("date"));
        String comment = null;
        Double distance = null;
        Integer maxHeartRate = null;
        int feeling = Integer.parseInt(map.get("feeling"));

        if (!map.get("distance").equals("null")) {
            distance = Double.parseDouble(map.get("distance"));
        }
        if (!map.get("maxHeartRate").equals("null")) {
            maxHeartRate = Integer.parseInt(map.get("maxHeartRate"));
        }
        if (!map.get("comment").equals("null")) {
            comment = map.get("comment");
        }

        Duration duration =
                Duration.ofSeconds(Long.parseLong(map.get("duration")));

        ExerciseCategory category =
                ExerciseCategory.valueOf(map.get("exerciseCategory"));

        Subcategory subCategory =
                stringToSubcategory(map.get("exerciseSubCategory"));

        return new EntryBuilder(title, date, duration, category,
                feeling).comment(comment).distance(distance)
                .exerciseSubCategory(subCategory).maxHeartRate(maxHeartRate)
                .build();
    }

    /**
     * Returns the id field of this logEntry.
     *
     * @return the id field as a string.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id for the LogEntry. Can only be done once.
     *
     * @param setId the id to set.
     * @throws IllegalStateException if the id has already been set.
     */
    public void setId(final String setId) throws IllegalStateException {
        if (this.id == null) {
            this.id = setId;
        } else {
            throw new IllegalStateException("Id already set");
        }
    }

    /**
     * Represents this LogEntry as a hashmap with values converted to strings.
     *
     * @return The hashMap representing this LogEntry.
     */
    public HashMap<String, String> toHash() {

        HashMap<String, String> map = new HashMap<>();

        map.put("title", this.getTitle());
        if (this.getComment() != null) {
            map.put("comment", this.getComment());
        } else {
            map.put("comment", "null");
        }

        map.put("id", this.getId());
        map.put("date", this.getDate().toString());
        map.put("feeling", Integer.toString(this.getFeeling()));
        map.put("duration", Long.toString(this.getDuration().toSeconds()));

        if (this.getDistance() != null) {
            map.put("distance", Double.toString(this.getDistance()));
        } else {
            map.put("distance", "null");
        }

        if (this.getMaxHeartRate() != null) {
            map.put("maxHeartRate", Integer.toString(this.getMaxHeartRate()));
        } else {
            map.put("maxHeartRate", "null");
        }

        map.put("exerciseCategory", this.getExerciseCategory().toString());

        if (this.getExerciseSubCategory() != null) {
            map.put("exerciseSubCategory",
                    this.getExerciseSubCategory().toString());
        } else {
            map.put("exerciseSubCategory", "null");
        }

        return map;
    }

    /**
     * Returns the title field of this logEntry.
     *
     * @return the title field as a string.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the comment field (main text body) of this logEntry.
     *
     * @return the comment field as a string.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Returns the date of this logEntry.
     *
     * @return the date field of this logEntry as a time.LocalDate instance.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the duration of this logEntry.
     *
     * @return the duration field of this logEntry as a time.Duration instance.
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Returns the feeling field.
     *
     * @return the feeling field of this logEntry as an int.
     */
    public int getFeeling() {
        return feeling;
    }

    // #region getters

    /**
     * Returns the distance field.
     *
     * @return the distance field of this logEntry
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * Returns the maximum heart rate.
     *
     * @return the maxHeartRate field of this logEntry
     */
    public Integer getMaxHeartRate() {
        return maxHeartRate;
    }

    /**
     * Returns the main exercise category of this logEntry.
     *
     * @return the EXERCISE_CATEGORIES for the category.
     */
    public ExerciseCategory getExerciseCategory() {
        return exerciseCategory;
    }

    /**
     * Returns the subcategory of this logEntry.
     *
     * @return the Subcategory for the category.
     */
    public Subcategory getExerciseSubCategory() {
        return exerciseSubCategory;
    }

    // #endregion getters


    /**
     * Builder class for LogEntry.
     */
    public static class EntryBuilder {

        /*
         * Required fields.
         */

        /**
         * Title to be built.
         */
        private final String ctitle;

        /**
         * Date to be built.
         */
        private final LocalDate cdate;

        /**
         * Duration to be built.
         */
        private final Duration cduration;

        /**
         * Exercise category to be built.
         */
        private final ExerciseCategory cexerciseCategory;

        /**
         * Feeling of the exercise.
         */
        private final int cfeeling;

        /*
         * Optional fields.
         */

        /**
         * Comment to be built.
         */
        private String ccomment;

        /**
         * Subcategory to be built.
         */
        private Subcategory cexerciseSubCategory;

        /**
         * Distance to be built.
         */
        private Double cdistance;

        /**
         * Maximum heart rate to be built.
         */
        private Integer cmaxHeartRate;

        /**
         * Creates an EntryBuilder with all required fields.
         *
         * @param title            the title for the LogEntry.
         * @param date             the date for the LogEntry.
         * @param duration         the duration for the LogEntry.
         * @param exerciseCategory the exercise category for the LogEntry.
         * @param feeling          the feeling for the LogEntry
         * @see #validate
         */
        public EntryBuilder(final String title, final LocalDate date,
                            final Duration duration,
                            final ExerciseCategory exerciseCategory,
                            final int feeling) throws IllegalArgumentException {

            this.ctitle = title;
            this.cdate = date;
            this.cduration = duration;
            this.cexerciseCategory = exerciseCategory;
            this.cfeeling = feeling;

        }

        /**
         * Sets the comment for the builder.
         *
         * @param comment the comment to set.
         * @return this builder.
         */
        public EntryBuilder comment(final String comment) {

            this.ccomment = comment;
            return this;
        }

        /**
         * Sets the exercise subcategory for the builder.
         *
         * @param exerciseSubCategory the subcategory to set.
         * @return this builder.
         */
        public EntryBuilder exerciseSubCategory(
                final Subcategory exerciseSubCategory) {

            this.cexerciseSubCategory = exerciseSubCategory;
            return this;
        }

        /**
         * Sets the distance for the builder.
         *
         * @param distance the distance to set.
         * @return this builder.
         */
        public EntryBuilder distance(final Double distance) {

            this.cdistance = distance;
            return this;
        }

        /**
         * Sets the max heart rate for the builder.
         *
         * @param maxHeartRate the heart rate to set.
         * @return this builder.
         */
        public EntryBuilder maxHeartRate(final Integer maxHeartRate) {

            this.cmaxHeartRate = maxHeartRate;
            return this;
        }

        /**
         * Constructs the LogEntry object.
         *
         * @return the LogEntry.
         * @throws IllegalArgumentException if any of the arguments are invalid.
         * @see #validate
         */
        public LogEntry build() throws IllegalArgumentException {
            Validity valid = validate(this);
            if (valid.invalid()) {
                throw new IllegalArgumentException(valid.reason());
            } else {
                return new LogEntry(this);
            }
        }
    }
}
