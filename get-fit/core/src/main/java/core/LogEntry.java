package core;

import java.time.Duration;
import java.time.LocalDate;

public class LogEntry {

    private final String id;
    private String title;
    private String comment;
    private LocalDate date;
    private Duration duartion;

    public LogEntry(String id, String title, String comment, LocalDate date, Duration duration) {

        if (id == null || title == null || comment == null || date == null || duration == null){
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
        this.duartion = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public Duration getDuartion() {
        return duartion;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        
        if (title.length() < 1) {
            throw new IllegalArgumentException("Title should not be empty");
        }

        this.title = title;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(LocalDate date) {

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Entry cannot be set to be after current time");
        }

        this.date = date;
    }

    public void setDuartion(Duration duration) {

        if (duration.isNegative() || duration.isZero()) {
            throw new IllegalArgumentException("Entry duration must be positive");
        }

        this.duartion = duration;
    }
}