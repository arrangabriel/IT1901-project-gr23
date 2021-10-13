package core;

import java.time.Duration;
import java.time.LocalDate;

import core.LogEntry.EXERCISECATEGORIES;

/** Builder class for EntrLog. */
public class EntryBuilder {

    /**
     * Required fields.
     */

    /** Title to be built. */
    private final String title;

    /** Date to be built. */
    private final LocalDate date;

    /** Duration to be built. */
    private final Duration duration;

    /** Exercise category to be built. */
    private final EXERCISECATEGORIES exerciseCategory;

    /** Feeling of the exercise. */
    private final int feeling;
}
