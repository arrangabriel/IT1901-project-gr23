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
