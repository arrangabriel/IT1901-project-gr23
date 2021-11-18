package math;

import core.EntryManager;
import core.LogEntry;
import core.ExerciseCategory;
import core.SortConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Statistics class.
 */
public final class Statistics {

    //Update javadoc

    /**
     * Hidden constructor.
     */
    private Statistics() {
    }


    /**
     * Returns the count of entries in the entryManager
     * filtered through date and category.
     * @param entryManager The entry manager to count over.
     * @param category The category to filter by.
     * @param date The date to filter by.
     * @return the count of entries.
     */
    public static int getCount(
            final EntryManager entryManager,
            final String category,
            final String date) {

        List<LogEntry> entries = listFilteredByDates(
            entryManager, category, date);

        return entries.size();
    }

    /**
     * Returns the total duration of all LogEntries in the EntryManager
     * filtered by date and category.
     * @param entryManager The entryManager to total.
     * @param category The category to filter by.
     * @param date The date to filter by.
     * @return the total duration.
     */
    public static double getTotalDuration(
            final EntryManager entryManager,
            final String category,
            final String date) {

        List<LogEntry> entries = listFilteredByDates(
            entryManager, category, date);

        double sum = 0;
        for (LogEntry logEntry : entries) {
            sum += logEntry.getDuration().toSeconds();
        }

        return sum;
    }

    /**
     * Returns the average duration across all LogEntries in the EntryManager
     * filtered by date and category.
     * @param entryManager The entryManager to get from average.
     * @param category The category to filter by.
     * @param date The date to filter by.
     * @return the average duration.
     */
    public static double getAverageDuration(
            final EntryManager entryManager,
            final String category,
            final String date) {

        List<LogEntry> entries = listFilteredByDates(
            entryManager, category, date);

        double sum = getTotalDuration(entryManager, category, date);
        double average = sum / entries.size();

        return average;
    }

    /**
     * Returns the average speed across all LogEntries in the EntryManager.
     *
     * @param entryManager the entryManager to calculate average speed from.
     * @param category     the exercise category to sort average speed from.
     * @param date         the date to filter by
     * @return the average speed of the sessions in min/km.
     */
    public static double getAverageSpeed(
            final EntryManager entryManager,
            final String category,
            final String date) {

        if (category == null) {
            return 0.0;
        }

        List<LogEntry> entries = listFilteredByDates(
        entryManager,
        category,
        date);

        double time = 0.0;
        double distance = 0.0;

        for (LogEntry logEntry : entries) {
            if (logEntry.getDistance() != null) {
                distance += logEntry.getDistance();
                time += logEntry.getDuration().toMinutes();
            }
        }

        if (distance == 0.0) {
            return 0.0;
        }

        return time / distance;
    }

    /**
     * Returns the average feeling of all LogEntries in the EntryManager
     * filtered by dates and category.
     * @param entryManager The entryManager to calculate average feeling from.
     * @param category The category to filter by.
     * @param date The date to filter by.
     * @return The average feeling.
     */
    public static double getAverageFeeling(final EntryManager entryManager,
                                            final String category,
                                            final String date) {

        List<LogEntry> entries = listFilteredByDates(
            entryManager, category, date);

        double sum = 0;

        for (LogEntry logEntry : entries) {
            sum += logEntry.getFeeling();
        }
        double average = sum / entryManager.entryCount();
        return average;
    }

    /**
     * Returns the maximum heart rate across all LogEntries in the EntryManager
     * filtered by date and category.
     * @param entryManager The entryManager to get the maximum heart rate from.
     * @param date The date to filter by.
     * @param category the category to filter by.
     * @return the maximum heart rate.
     */
    public static double getMaximumHr(
            final EntryManager entryManager,
            final String category,
            final String date) {

        List<LogEntry> entries = listFilteredByDates(
            entryManager, category, date);

        double maxHr = 0;
        for (LogEntry logEntry : entries) {
            if (logEntry.getMaxHeartRate() != null) {
                if (logEntry.getMaxHeartRate() > maxHr) {
                    maxHr = logEntry.getMaxHeartRate();
                }
            }
        }
        return maxHr;
    }

    /**
     * Filters the entries of the given
     * entryManger by category and date.
     * @param entryManager The manager that contains the entries.
     * @param category The category to filter by.
     * @param date The date to filter by.
     * @return The filtered entries in a list.
     */
    private static List<LogEntry> listFilteredByDates(
            final EntryManager entryManager,
            final String category,
            final String date) {

        SortConfiguration sortConfiguration =
                SortConfiguration.DATE;


        EntryManager.SortedIteratorBuilder iteratorBuilder =
                new EntryManager.SortedIteratorBuilder(
                        entryManager, sortConfiguration);
        
        
        if (category != null) {
            Iterator<LogEntry> iterator = iteratorBuilder.filterTimeInterval(
            LocalDate.parse(date.substring(0, 10)),
            LocalDate.parse(date.substring(11))).
            filterExerciseCategory(
            ExerciseCategory.valueOf(category)).
            iterator(false);

            List<LogEntry> entries = new ArrayList<>();
            iterator.forEachRemaining(entries::add);

            return entries;
        }

        Iterator<LogEntry> iterator = iteratorBuilder.filterTimeInterval(
        LocalDate.parse(date.substring(0, 10)),
        LocalDate.parse(date.substring(11))).
        iterator(false);


        List<LogEntry> entries = new ArrayList<>();
        iterator.forEachRemaining(entries::add);

        return entries;
    }

}

