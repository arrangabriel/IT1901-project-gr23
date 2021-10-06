package core;

import java.time.Duration;
import java.time.LocalDate;

public class LogEntry {

    private final String id;
    private String title;
    private String comment;
    private LocalDate date;
    private Duration duration;
    private EXCERCISE_CATEGORIES excerciseCategory;
    private Subcategories exerciseSubCategory;
    private Double fealing;

    //Parameters for endurance exercise:
    private Double distance;
    private Double maxHeartRate;

    // it is paramount that all sorting configurations are supported by all possible LogEntries.
    // Keep in mind when some LogEntry fields become optional.
    public enum SORT_CONFIGURATIONS {
        DATE,
        DURATION,
        TITLE
    }

    // It will become evident if this is needed later
    // public interface Category {}
    public interface Subcategories /* extends Category */ {}

    // expand these in the future
    public enum EXCERCISE_CATEGORIES {
        ALL,
        STRENGTH(STRENGTH_SUBCATEGORIES.values()),
        RUNNING(CARDIO_SUBCATEGORIES.values()),
        CYCLING(CARDIO_SUBCATEGORIES.values()),
        SWIMMING(CARDIO_SUBCATEGORIES.values());

        private final Subcategories[] subcategories;

        EXCERCISE_CATEGORIES(Subcategories[] subcategories) {
            this.subcategories = subcategories;
        }

        EXCERCISE_CATEGORIES() {
            this.subcategories = new Subcategories[]{};
        }

        Subcategories[] getSubcategories() {
            return this.subcategories;
        }
    }

    enum STRENGTH_SUBCATEGORIES implements Subcategories {
        ANY,
        PUSH,
        PULL,
        LEGS,
        FULL_BODY
    }

    enum CARDIO_SUBCATEGORIES implements Subcategories {
        ANY,
        SHORT,
        LONG,
        HIGH_INTENSITY,
        LOW_INTENSITY
    }

    /**
     * A logEntry instance represents a single workout-entry internally.
     * Has fields for the elements of a workout-entry, getters for them, and setters for those that should be mutable.
     * @param id a string id, is final and can be used to identify a specific logEntry.
     * @param title entry title.
     * @param comment entry text-body.
     * @param date entry date.
     * @param duration entry duration.
     * @param fealing a double entry fealing from 1-10.
     * @param distance a double entry distance in kilometers.
     * @param maxHeartRate a double entry for max heart rate.
     * @throws IllegalArgumentException if any of the arguments are null, duration is zero or negative, the date is ahead of now, or the title is empty.
     */
    public LogEntry(String id, String title, String comment, LocalDate date, Duration duration, double fealing, double distance, double maxHeartRate) throws IllegalArgumentException {

        if (id == null || title == null || comment == null || date == null || duration == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        if (duration.isNegative() || duration.isZero()) {
            throw new IllegalArgumentException("Entry duration must be positive");
        }

        if (fealing > 10 || fealing < 1) {
            throw new IllegalArgumentException("Fealing must be between 1 and 10");
        }

        if (distance < 0){
            throw new IllegalArgumentException("Duration cannot be set to a negative number");
        }

        if (maxHeartRate < 20 || maxHeartRate > 250){
            throw new IllegalArgumentException("heart rate must be between 20 and 250");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Entry cannot be set to be after current time");
        }

        if (title.length() < 1) {
            throw new IllegalArgumentException("Title should not be empty");
        }

        this.id = id;
        this.title = title;
        this.comment = comment;
        this.date = date;
        this.duration = duration;
    }

    /**
     * Returns the title field of this logEntry.
     * @return the title field as a string.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the comment field (main text body) of this logEntry.
     * @return the comment field as a string.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Returns the date of this logEntry.
     * @return the date field of this logEntry as a time.LocalDate instance.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the duration of this logEntry.
     * @return the duration field of this logEntry as a time.Duration instance.
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Returns the individual id of this logEntry.
     * @return the id field of this logEntry as a string.
     */
    public String getId() {
        return id;
    }
    /**
     * Returns the fealing field.
     * @return the fealing field of this logEntry as a Double.
     */
    public Double getFealing(){
        return fealing;
    }

    /**
     * Returns the distance field.
     * @return the distance fiels of this logEntry
     */
    public Double getDistance(){
        return distance;
    }
    /**
     * Returns the distance field.
     * @return the distance fiels of this logEntry
     */
    public Double getMaxHeartRate(){
        return maxHeartRate;
    }

    

    /**
     * Returns the main exercise category of this logEntry.
     * @return the EXERCISE_CATEGORIES for the category.
     */
    public EXCERCISE_CATEGORIES getExcerciseCategory() {
        return excerciseCategory;
    }

    /**
     * Returns the subcategories for the logEntry's main category
     * @return an array of Subcategories
     */
    public Subcategories[] getExcerciseSubCategories() {
        return excerciseCategory.getSubcategories();
    }

    /**
     * Sets the title of this logEntry if the title parameter is valid.
     * @param title a string to be set as title.
     * @throws IllegalArgumentException if title is blank or null.
     */
    public void setTitle(String title) throws IllegalArgumentException {
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Title should not be empty");
        }

        this.title = title;
    }

    /**
     * Sets the text body of this logEntry.
     * @param comment a string to be set as the comment field.
     * @throws IllegalArgumentException if comment is null
     */
    public void setComment(String comment) throws IllegalArgumentException{

        if (title == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }

        this.comment = comment;
    }

    /**
     * Sets the date of this logEntry if date parameter is valid.
     * @param date a time.LocalDate instance to be set as the date field.
     * @throws IllegalArgumentException if date is after the current system date or is null.
     */
    public void setDate(LocalDate date) throws IllegalArgumentException{
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Entry cannot be set to be after current time");
        }

        this.date = date;
    }

    /**
     * Sets the duration of this logEntry if the duration parameter is valid.
     * @param duration a time.Duration instance to be set as the duration field.
     * @throws IllegalArgumentException if duration is zero or negative.
     */
    public void setDuration(Duration duration) throws IllegalArgumentException {
        // TODO - possibly add maximum time restriction to reflect input possibilities
        if (duration == null) {
            throw new IllegalArgumentException("Duration cannot be null");
        }

        if (duration.isNegative() || duration.isZero()) {
            throw new IllegalArgumentException("Entry duration must be positive");
        }

        this.duration = duration;
    }

    /**
     * Sets the fealing of this logEntry if the fealing parameter is valid.
     * @param fealing a double intance to be set as the fealing field.
     * @throws IllegalArgumentException if the fealing is not between 1 and 10.
     */
    public void setFealing(double fealing){
        if (fealing > 10 || fealing < 1) {
            throw new IllegalArgumentException("Fealing must be between 1 and 10");
        }
        this.fealing = fealing;
    }
    
    /**
     * Sets the distance of this logEntry if the distance parameter is valid.
     * @param distance a double intance to be set as the distance field.
     * @throws IllegalArgumentException if the distance is negative.
     */
    public void setDistance(double distance){
        if (distance < 0){
            throw new IllegalArgumentException("Distance can not be negative");
        }
        this.distance = distance;
    }

    /**
     * Sets the distance of this logEntry if the distance parameter is valid.
     * @param distance a double intance to be set as the distance field.
     * @throws IllegalArgumentException if the distance is negative.
     */
    public void setMaxHeartRate(double maxHeartRate){
        if (maxHeartRate < 20 || maxHeartRate > 250){
            throw new IllegalArgumentException("Max heart rate must be between 20 and 250");
        }
        this.maxHeartRate = maxHeartRate;
    }

}