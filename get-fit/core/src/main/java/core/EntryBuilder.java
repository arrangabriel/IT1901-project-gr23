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
    private final String title;

    /** Date to be built. */
    private final LocalDate date;

    /** Duration to be built. */
    private final Duration duration;

    /** Exercise category to be built. */
    private final EXERCISECATEGORY exerciseCategory;

    /** Feeling of the exercise. */
    private final int feeling;

    /**
     * Optional fields.
     */

     /** Comment to be built. */
     private String comment;

     /** Subcategory to be built. */
     private Subcategory exerciseSubcategory;

     /** Distance to be built. */
     private Double distance;

     /** Maximum heart rate to be built. */
     private Integer maxHeartRate;


     

}
