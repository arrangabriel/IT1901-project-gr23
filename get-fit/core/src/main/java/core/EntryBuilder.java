package core;

import java.time.Duration;
import java.time.LocalDate;

import core.LogEntry.EXERCISECATEGORY;
import core.LogEntry.Subcategory;

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

}
