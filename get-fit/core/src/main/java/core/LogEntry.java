package core;

import java.time.Duration;
import java.time.LocalDate;

public class LogEntry {

    private final String id;
    private String title;
    private String comment;
    private LocalDate date;
    private Duration duration;

    /**
     * A logEntry instance represents a single workout-entry internally.
     * Has fields for the elements of a workout-entry, getters for them, and setters for those that should be mutable.
     * @param id a string id, is final and can be used to identify a specific logEntry.
     * @param title entry title.
     * @param comment entry text-body.
     * @param date entry date.
     * @param duration entry duration.
     * @throws IllegalArgumentException if any of the arguments are null, duration is zero or negative, the date is ahead of now, or the title is empty.
     */
    public LogEntry(String id, String title, String comment, LocalDate date, Duration duration) throws IllegalArgumentException {

        if (id == null || title == null || comment == null || date == null || duration == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        if (duration.isNegative() || duration.isZero()) {
            throw new IllegalArgumentException("Entry duration must be positive");
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
}