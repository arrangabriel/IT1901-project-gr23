package core;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;

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
    public static final int MAXHEARTRATEHUMAN = 480; // World record
    /**
     * The min heart rate of a human being.
     */
    public static final int MINHEARTRATEHUMAN = 40;
    /**
     * Id of the LogEntry.
     */
    private String id;
    /**
     * Title of LogEntry.
     */
    private String title;
    /**
     * Description of LogEntry.
     */
    private String comment;
    /**
     * Date of LogEntry.
     */
    private LocalDate date;
    /**
     * Duration of LogEntry.
     */
    private Duration duration;
    /**
     * Category of LogEntry.
     */
    private EXERCISECATEGORY exerciseCategory;
    /**
     * Subcategory of LogEntry.
     */
    private Subcategory exerciseSubCategory;
    /**
     * Feeling of LogEntry.
     */
    private int feeling;
    /**
     * Distance of LogEntry.
     */
    private Double distance;
    /**
     * Maximum heart rate of LogEntry.
     */
    private Integer maxHeartRate;

    // it is paramount that all sorting configurations are
    // supported by all possible LogEntries.
    // Keep in mind when some LogEntry fields become optional.

    /**
     * A logEntry instance represents a single workout-entry internally.
     * Has fields for the elements of a workout-entry, getters for them,
     * and setters for those that should be mutable.
     *
     * @param builder the EntryBuilder used to build this LogEntry.
     * @throws IllegalArgumentException if any of the arguments are null,
     *                                  duration is zero or negative,
     *                                  the date is ahead of now,
     *                                  or the title is empty.
     * @see #validate
     */
    public LogEntry(
            final EntryBuilder builder)
            throws IllegalArgumentException {

        Validity validity = validate(builder);
        if (!validity.valid()) {
            throw new IllegalArgumentException(validity.reason());
        }

        this.title = builder.ctitle;
        this.comment = builder.ccomment;
        this.date = builder.cdate;
        this.duration = builder.cduration;
        this.distance = builder.cdistance;
        this.feeling = builder.cfeeling;
        this.maxHeartRate = builder.cmaxHeartRate;
        this.exerciseCategory = builder.cexerciseCategory;
        this.exerciseSubCategory = builder.cexerciseSubcategory;

    }

    // It will become evident if this is needed later
    // public interface Category {}

    /**
     * Validates a EntryBuilder.
     * Requirements:<br><br>
     * <b>id</b>                    must not be null and have a positive length.
     * <br><br>
     *
     * <b>title</b>                 must not be null and have a positive length.
     * <br><br>
     *
     * <b>date</b>                  must not be null and not be after current
     * time.<br><br>
     *
     * <b>duration</b>              must not be null and be positive.
     * <br><br>
     *
     * <b>feeling</b>               must be between 1 and 10.
     * <br><br>
     *
     * <b>exerciseCategory</b>      must not be null.
     * <br><br>
     *
     * <b>comment</b>               must either be null or of positive length.
     * <br><br>
     *
     * <b>exerciseSubcategory</b>   must either be null or a subcategory of
     * <b>exerciseCategory</b>
     * <br><br>
     *
     * <b>distance</b>              must either be null or be positive
     * <br><br>
     *
     * <b>maxHeartRate</b>          must either be null or be between 40 and 480
     * <br><br>
     *
     * @param builder the builder to validate.
     * @return true if builder is valid, otherwise false.
     */
    public static Validity validate(final EntryBuilder builder) {

        /* Required fields. */
        if (builder.ctitle == null
            || builder.ctitle.length() < 1) {

            return new Validity(false,
                    "Title cannot be empty or null");
        }

        if (builder.cdate == null
            || builder.cdate.isAfter(LocalDate.now())) {

            return new Validity(false,
                    "Date cannot be after now or null");
        }

        if (builder.cduration == null
            || builder.cduration.isNegative()
            || builder.cduration.isZero()) {

            return new Validity(false,
                    "Duration cannot be null and must be positive");
        }

        if (builder.cfeeling > LogEntry.MAXFEELING
            || builder.cfeeling < LogEntry.MINFEELING) {

            return new Validity(false, String.format(
                    "Feeling must be between %i and %i",
                    LogEntry.MAXFEELING, LogEntry.MINFEELING));
        }

        if (builder.cexerciseCategory == null) {

            return new Validity(false,
                    "Exercise category cannot be null");
        }

        /* Optional fields. */
        if (builder.ccomment != null
            && (builder.ccomment.length() < 1)) {

            return new Validity(false,
                    "Comment should not be empty, should be null instead");
        }


        if (builder.cexerciseSubcategory != null
            && (!Arrays.stream(builder.cexerciseCategory.getSubcategories())
                .anyMatch(builder.cexerciseSubcategory::equals))) {

            return new Validity(false,
                    "Subcategory must be part of exercise category");
        }

        if (builder.cdistance != null
            && (builder.cdistance < 1)) {

            return new Validity(false,
                    "Distance must be positive");
        }

        if (builder.cmaxHeartRate != null
            && (builder.cmaxHeartRate > LogEntry.MAXHEARTRATEHUMAN
            || builder.cmaxHeartRate < LogEntry.MINHEARTRATEHUMAN)) {

            return new Validity(false, String.format(
                    "Heart rate must be between %i and %i",
                    LogEntry.MAXHEARTRATEHUMAN, LogEntry.MINHEARTRATEHUMAN));
        }


        return new Validity(true, "ok");
    }

    // expand these in the future

    /**
     * Sets the id for the LogEntry. Can only be done once.
     * @param setId the id to set.
     * @throws IllegalStateException if the id has allready been set.
     */
    public void setId(final String setId) throws IllegalStateException {
        if (this.id == null) {
            this.id = setId;
        } else {
            throw new IllegalStateException("Id allready set");
        }
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

    // #region geters

    /**
     * Returns the distance field.
     *
     * @return the distance fiels of this logEntry
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * Returns the maximun heart rate.
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
    public EXERCISECATEGORY getExerciseCategory() {
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

    /**
     * Returns the subcategories for the logEntry's main category.
     *
     * @return an array of Subcategories
     */
    public Subcategory[] getExerciseSubCategories() {
        return exerciseCategory.getSubcategories();
    }

    /**
     * Configurations by which to sort LogEntries.
     */
    public enum SORTCONFIGURATIONS {
        /**
         * Sort by Date.
         */
        DATE,
        /**
         * Sort by Duration.
         */
        DURATION,
        /**
         * Sort by Title.
         */
        TITLE
    }

    /**
     * The categories an exercise can fall under.
     */
    public enum EXERCISECATEGORY {
        /**
         * Represents strength exercise.
         */
        STRENGTH(STRENGTHSUBCATEGORIES.values()),
        /**
         * Represents a running exercise.
         */
        RUNNING(CARDIOSUBCATEGORIES.values()),
        /**
         * Reoresents a cyclinc exercise.
         */
        CYCLING(CARDIOSUBCATEGORIES.values()),
        /**
         * Represents a swimming exercise.
         */
        SWIMMING(CARDIOSUBCATEGORIES.values());

        /**
         * Array of Subcategories.
         */
        private final Subcategory[] subcategories;

        /**
         * Populates the subcategories array.
         *
         * @param subcategoriesArray The subcategories to populate with.
         */
        EXERCISECATEGORY(final Subcategory[] subcategoriesArray) {
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

    /**
     * Subcategories for the Strength category.
     */
    public enum STRENGTHSUBCATEGORIES implements Subcategory {
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
            return STRENGTHSUBCATEGORIES.valueOf(name);
        }

    }

    /**
     * Subcategories for the Cardio category.
     */
    public enum CARDIOSUBCATEGORIES implements Subcategory {
        /**
         * Short exercises.
         */
        SHORT,
        /**
         * Long exercises.
         */
        LONG,
        /**
         * High intensity exercises.
         */
        HIGHINTENSITY,
        /**
         * Low intensity exercises.
         */
        LOWINTENSITY;

        @Override
        public Subcategory getValueOf(final String name) {
            return CARDIOSUBCATEGORIES.valueOf(name);
        }

    }

    /**
     * Exercise Subcategory.
     */
    public interface Subcategory /* extends Category */ {

        /**
         * Get the Subcategory value of a String.
         *
         * @param name The name of the Subcategory to retrieve
         * @return the Subcategory represented by the String
         */
        Subcategory getValueOf(String name);

    }

    // #endregion geters

    /**
     * Validity of a EntryBuilder.
     */
    public static class Validity {

        /**
         * Wether the builder is valid.
         */
        private boolean ivalid;

        /**
         * The reason for the validity of the builder.
         */
        private String ireason;

        /**
         * Object representing the validity of an EntryBuilder.
         *
         * @param valid  whether the builder is valid.
         * @param reason "ok" if valid,
         *               otherwise the reason the builder is not valid.
         */
        public Validity(final boolean valid, final String reason) {

            this.ivalid = valid;
            this.ireason = reason;
        }

        /**
         * Wether the builder is valid.
         *
         * @return the validity
         */
        public boolean valid() {
            return this.ivalid;
        }

        /**
         * The reason for the validity of the builder.
         *
         * @return the reason.
         */
        public String reason() {
            return this.ireason;
        }
    }

    /**
     * Builder class for EntrLog.
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
        private final EXERCISECATEGORY cexerciseCategory;

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
        private Subcategory cexerciseSubcategory;

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
         * @throws IllegalArgumentException if any of the input is invalid.
         * @see #validate
         */
        public EntryBuilder(
                final String title,
                final LocalDate date,
                final Duration duration,
                final EXERCISECATEGORY exerciseCategory,
                final int feeling)
                throws IllegalArgumentException {

            this.ctitle = title;
            this.cdate = date;
            this.cduration = duration;
            this.cexerciseCategory = exerciseCategory;
            this.cfeeling = feeling;

            Validity validity = validate(this);
            if (!validity.valid()) {
                throw new IllegalArgumentException(validity.reason());
            }

        }

        /**
         * Sets the comment for the builder.
         *
         * @param comment the comment to set.
         * @return this builder.
         * @throws IllegalArgumentException if the comment is illegal.
         */
        public EntryBuilder comment(final String comment)
                throws IllegalArgumentException {

            String old = this.ccomment;
            this.ccomment = comment;

            Validity validity = validate(this);
            if (!validity.valid()) {
                this.ccomment = old;
                throw new IllegalArgumentException(validity.reason());
            }

            return this;
        }

        /**
         * Sets the exercise subcategory for the builder.
         *
         * @param exerciseSubcategory the subcategory to set.
         * @return this builder.
         * @throws IllegalArgumentException if the subcategory is illegal.
         */
        public EntryBuilder exerciseSubcategory(
                final Subcategory exerciseSubcategory)
                throws IllegalArgumentException {

            Subcategory old = this.cexerciseSubcategory;
            this.cexerciseSubcategory = exerciseSubcategory;

            Validity validity = validate(this);
            if (!validity.valid()) {
                this.cexerciseSubcategory = old;
                throw new IllegalArgumentException(validity.reason());
            }

            return this;
        }

        /**
         * Sets the distance for the builder.
         *
         * @param distance the distance to set.
         * @return this builder.
         * @throws IllegalArgumentException if the distance is illegal.
         */
        public EntryBuilder distance(final Double distance)
                throws IllegalArgumentException {

            Double old = this.cdistance;
            this.cdistance = distance;

            Validity validity = validate(this);
            if (!validity.valid()) {
                this.cdistance = old;
                throw new IllegalArgumentException(validity.reason());
            }

            return this;
        }

        /**
         * Sets the max heart rate for the builder.
         *
         * @param maxHeartRate the heart rate to set.
         * @return this builder.
         * @throws IllegalArgumentException if the heart rate is illegal
         */
        public EntryBuilder maxHeartRate(final Integer maxHeartRate)
                throws IllegalArgumentException {

            Integer old = this.cmaxHeartRate;
            this.cmaxHeartRate = maxHeartRate;

            Validity validity = validate(this);
            if (!validity.valid()) {
                this.cmaxHeartRate = old;
                throw new IllegalArgumentException(validity.reason());
            }

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

            LogEntry logEntry = new LogEntry(this);
            return logEntry;
        }
    }
}
