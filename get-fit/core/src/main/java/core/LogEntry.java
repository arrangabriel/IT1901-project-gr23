package core;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Representation of a workout or exercise.
 */
public class LogEntry {

    /** Title of LogEntry. */
    private String title;

    /** Description of LogEntry. */
    private String comment;

    /** Date of LogEntry. */
    private LocalDate date;

    /** Duration of LogEntry. */
    private Duration duration;

    /** Category of LogEntry. */
    private EXERCISECATEGORY exerciseCategory;

    /** Subcategory of LogEntry. */
    private Subcategory exerciseSubCategory;

    /** Feeling of LogEntry. */
    private int feeling;

    /** Distance of LogEntry. */
    private Double distance;

    /** Maximum heart rate of LogEntry. */
    private Integer maxHeartRate;

    /** The top end of the feeling scale. */
    private final int maxFeeling = 10;

    /** The bottom end of the feeling scale. */
    private final int minFeeling = 1;

    /** The max heart rate of a human being. */
    private final int maxHeartRateHuman = 480; // World record

    /** The min heart rate of a human being. */
    private final int minHeartRateHuman = 40;

    // it is paramount that all sorting configurations are
    // supported by all possible LogEntries.
    // Keep in mind when some LogEntry fields become optional.

    /**
     * Configurations by which to sort LogEntries.
     */
    public enum SORTCONFIGURATIONS {
        /** Sort by Date. */
        DATE,
        /** Sort by Duration. */
        DURATION,
        /** Sort by Title. */
        TITLE
    }

    // It will become evident if this is needed later
    // public interface Category {}

    /**
     * Exercise Subcategory.
     */
    public interface Subcategory /* extends Category */ {

        /** Get the Subcategory value of a String.
         * @param name The name of the Subcategory to retrieve
         * @return the Subcategory represented by the String
         */
        Subcategory getValueOf(String name);

    }

    // expand these in the future
    /** The categories an exercise can fall under. */
    public enum EXERCISECATEGORY {
        /** Represents any or generic exercise. */
        ANY,
        /** Represents strength exercise. */
        STRENGTH(STRENGTHSUBCATEGORIES.values()),
        /** Represents a running exercise. */
        RUNNING(CARDIOSUBCATEGORIES.values()),
        /** Reoresents a cyclinc exercise. */
        CYCLING(CARDIOSUBCATEGORIES.values()),
        /** Represents a swimming exercise. */
        SWIMMING(CARDIOSUBCATEGORIES.values());

        /** Array of Subcategories. */
        private final Subcategory[] subcategories;

        /** Populates the subcategories array.
         * @param subcategoriesArray The subcategories to populate with.
        */
        EXERCISECATEGORY(final Subcategory[] subcategoriesArray) {
            this.subcategories = subcategoriesArray;
        }

        /** Empties the subcategories array. */
        EXERCISECATEGORY() {
            this.subcategories = new Subcategory[] {};
        }

        /**
         * Returns the subcategories.
         * @return the subcategories.
         */
        public Subcategory[] getSubcategories() {
            return this.subcategories;
        }
    }

    /** Subcategories for the Strength category. */
    public enum STRENGTHSUBCATEGORIES implements Subcategory {
        /** Push exercises. */
        PUSH,
        /** Pull exercises. */
        PULL,
        /** Legs exercises. */
        LEGS,
        /** Full exercises. */
        FULL,
        /** Body exercises. */
        BODY;

        @Override
        public Subcategory getValueOf(final String name) {
            return STRENGTHSUBCATEGORIES.valueOf(name);
        }

    }

    /** Subcategories for the Cardio category. */
    public enum CARDIOSUBCATEGORIES implements Subcategory {
        /** Short exercises. */
        SHORT,
        /** Long exercises. */
        LONG,
        /** High intensity exercises. */
        HIGHINTENSITY,
        /** Low intensity exercises. */
        LOWINTENSITY;

        @Override
        public Subcategory getValueOf(final String name) {
            return CARDIOSUBCATEGORIES.valueOf(name);
        }

    }

    /**
     * A logEntry instance represents a single workout-entry internally.
     * Has fields for the elements of a workout-entry, getters for them,
     * and setters for those that should be mutable.
     * @param builder the EntryBuilder used to build this LogEntry.
     * @throws IllegalArgumentException if any of the arguments are null,
     *                                  duration is zero or negative,
     *                                  the date is ahead of now,
     *                                  or the title is empty.
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
        this.exerciseSubCategory = builder.cexerciseSubcategory;

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
     * Returns the individual id of this logEntry.
     *
     * @return the id field of this logEntry as a string.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the feeling field.
     *
     * @return the feeling field of this logEntry as an int.
     */
    public int getFeeling() {
        return feeling;
    }

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
     * Sets the title of this logEntry if the title parameter is valid.
     *
     * @param ititle a string to be set as title.
     * @throws IllegalArgumentException if title is blank or null.
     */
    public void setTitle(final String ititle)
        throws IllegalArgumentException {
        if (ititle == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }

        if (ititle.isEmpty()) {
            throw new IllegalArgumentException("Title should not be empty");
        }

        this.title = ititle;
    }

    /**
     * Sets the text body of this logEntry.
     *
     * @param icomment a string to be set as the comment field.
     * @throws IllegalArgumentException if comment is null
     */
    public void setComment(final String icomment)
        throws IllegalArgumentException {

        if (title == null) {
            throw new IllegalArgumentException(
                "Comment cannot be null");
        }

        this.comment = icomment;
    }

    /**
     * Sets the date of this logEntry if date parameter is valid.
     *
     * @param idate a time.LocalDate instance to be set as the date field.
     * @throws IllegalArgumentException if date is after the current system date
     *                                  or is null.
     */
    public void setDate(final LocalDate idate)
        throws IllegalArgumentException {
        if (idate == null) {
            throw new IllegalArgumentException(
                "Date cannot be null");
        }

        if (idate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                "Entry cannot be set to be after current time");
        }

        this.date = idate;
    }

    /**
     * Sets the duration of this logEntry if the duration parameter is valid.
     *
     * @param iduration a time.Duration instance to
     * be set as the duration field.
     * @throws IllegalArgumentException if duration is zero or negative.
     */
    public void setDuration(final Duration iduration)
        throws IllegalArgumentException {
// TODO - possibly add maximum time restriction to reflect input possibilities
        if (iduration == null) {
            throw new IllegalArgumentException("Duration cannot be null");
        }

        if (iduration.isNegative() || iduration.isZero()) {
            throw new IllegalArgumentException(
                "Entry duration must be positive");
        }

        this.duration = iduration;
    }

    /**
     * Sets the feeling of this logEntry if the feeling parameter is valid.
     *
     * @param ifeeling an int intance to be set as the feeling field.
     * @throws IllegalArgumentException if the feeling is not between 1 and 10.
     */
    public void setFeeling(final int ifeeling)
        throws IllegalArgumentException {

        if (ifeeling > this.minFeeling || ifeeling < this.minFeeling) {
            throw new IllegalArgumentException(
                "Feeling must be between 1 and 10");
        }
        this.feeling = ifeeling;
    }

    /**
     * Sets the distance of this logEntry if the distance parameter is valid.
     *
     * @param idistance a double intance to be set as the distance field.
     * @throws IllegalArgumentException if the distance is negative.
     */
    public void setDistance(final Double idistance)
        throws IllegalArgumentException {

        if (this.distance == null) {
            throw new IllegalArgumentException(
                "This workout does not support distance.");
        }
        if (idistance < 0) {
            throw new IllegalArgumentException("Distance can not be negative");
        }
        this.distance = idistance;
    }

    /**
     * Sets the distance of this logEntry if the distance parameter is valid.
     *
     * @param imaxHeartRate an Integer intance to be set as the distance field.
     * @throws IllegalArgumentException if the distance is negative.
     */
    public void setMaxHeartRate(final Integer imaxHeartRate)
        throws IllegalArgumentException {

        if (this.maxHeartRate == null) {
            throw new IllegalArgumentException(
                "This workout does not support maxHeartRate.");
        }
        if (imaxHeartRate < this.minHeartRateHuman
        || imaxHeartRate > maxHeartRateHuman) {
            throw new IllegalArgumentException(
                "Max heart rate must be between 20 and 250");
        }
        this.maxHeartRate = imaxHeartRate;
    }

    /** Builder class for EntrLog. */
    public class EntryBuilder {

        /**
         * Required fields.
         */

        /** Title to be built. */
        private final String ctitle;

        /** Date to be built. */
        private final LocalDate cdate;

        /** Duration to be built. */
        private final Duration cduration;

        /** Exercise category to be built. */
        private final EXERCISECATEGORY cexerciseCategory;

        /** Feeling of the exercise. */
        private final int cfeeling;

        /**
         * Optional fields.
         */

        /** Comment to be built. */
        private String ccomment;

        /** Subcategory to be built. */
        private Subcategory cexerciseSubcategory;

        /** Distance to be built. */
        private Double cdistance;

        /** Maximum heart rate to be built. */
        private Integer cmaxHeartRate;

        /**
         * Creates an EntryBuilder with all required fields.
         * @param title the title for the LogEntry.
         * @param date the date for the LogEntry.
         * @param duration the duration for the LogEntry.
         * @param exerciseCategory the exercise category for the LogEntry.
         * @param feeling the feeling for the LogEntry
         */
        public EntryBuilder(
            final String title,
            final LocalDate date,
            final Duration duration,
            final EXERCISECATEGORY exerciseCategory,
            final int feeling) {

            this.ctitle = title;
            this.cdate = date;
            this.cduration = duration;
            this.cexerciseCategory = exerciseCategory;
            this.cfeeling = feeling;

        }

        /**
         * Sets the comment for the builder.
         * @param comment the comment to set.
         * @return this builder.
         */
        public EntryBuilder comment(final String comment) {

            this.ccomment = comment;
            return this;
        }

        /**
         * Sets the exercise subcategory for the builder.
         * @param exerciseSubcategory the subcategory to set.
         * @return this builder.
         */
        public EntryBuilder exerciseSubcategory(
            final Subcategory exerciseSubcategory) {

            this.cexerciseSubcategory = exerciseSubcategory;
            return this;
        }

        /**
         * Sets the distance for the builder.
         * @param distance the distance to set.
         * @return this builder.
         */
        public EntryBuilder distance(final Double distance) {

            this.cdistance = distance;
            return this;
        }

        /**
         * Sets the max heart rate for the builder.
         * @param maxHeartRate the heart rate to set.
         * @return this builder.
         */
        public EntryBuilder maxHeartRate(final Integer maxHeartRate) {

            this.cmaxHeartRate = maxHeartRate;
            return this;
        }

        /**
         * Constructs the LogEntry object.
         * @return the LogEntry.
         * @throws IllegalArgumentException if any of the arguments are invalid.
         */
        public LogEntry build() throws IllegalArgumentException {
            LogEntry logEntry = new LogEntry(this);
            return logEntry;
        }



    }


}
