package math;

import core.EntryManager;
import core.EntryManager.SortedIteratorBuilder;
import core.LogEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Statistics class.
 */
public final class Statistics {

    /**
     * Hidden constructor.
     */
    private Statistics() {
    }


    /**
     * Returns the count of entries in the entryManager.
     *
     * @param entryManager the entry manager to count over.
     * @return the count of entries.
     */
    public static int getCount(
            final EntryManager entryManager,
            final String date) {

        List<LogEntry> entries = listFilteredByDates(entryManager, date);

        return entries.size();
    }

    /**
     * Returns the total duration of all LogEntries in the EntryManager.
     *
     * @param entryManager the entryManager to total.
     * @return the total duration.
     */
    public static double getTotalDuration(
            final EntryManager entryManager,
            final String date) {

        List<LogEntry> entries = listFilteredByDates(entryManager, date);
        double sum = 0;
        for (LogEntry logEntry : entries) {
            sum += logEntry.getDuration().toSeconds();
        }

        return sum;
    }

    /**
     * Returns the average duration across all LogEntries in the EntryManager.
     *
     * @param entryManager the entryManager to average.
     * @return the average duration.
     */
    public static double getAverageDuration(
            final EntryManager entryManager,
            final String date) {

        List<LogEntry> entries = listFilteredByDates(entryManager, date);

        double sum = getTotalDuration(entryManager, date);
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
     * @throws IllegalStateException if distance is not positive.
     */
    public static double getAverageSpeed(
            final EntryManager entryManager,
            final LogEntry.EXERCISECATEGORY category,
            final String date)
            throws IllegalStateException {


        Iterator<LogEntry> entries = new SortedIteratorBuilder(entryManager,
                LogEntry.SORTCONFIGURATIONS.DATE).
                filterExerciseCategory(
                        category).
                filterTimeInterval(
                        LocalDate.parse(date.substring(0, 10)),
                        LocalDate.parse(date.substring(11))).
                iterator(false);

        double time = 0;
        double distance = 0;

        while (entries.hasNext()) {
            LogEntry entry = entries.next();

            if (entry.getDistance() != (null)) {
                distance += entry.getDistance();
                time += entry.getDuration().toMinutes();
            }
        }

        if (distance == 0) {
            throw new IllegalStateException("The distance must be over 0");
        }
        return time / distance;
    }

    /**
     * Returns the average feeling of all LogEntries in the EntryManager.
     *
     * @param entryManager the entryManager to calculate average feeling from.
     * @return the average feeling.
     */
    public static double getAverageFeeling(final EntryManager entryManager,
                                           final String date) {
        List<LogEntry> entries = listFilteredByDates(entryManager, date);
        double sum = 0;
        for (LogEntry logEntry : entries) {
            sum += logEntry.getFeeling();
        }
        double average = sum / entryManager.entryCount();
        return average;
    }

    /**
     * Returns the maximum heart rate across all LogEntries in the EntryManager.
     *
     * @param entryManager the entryManager to get the maximum heart rate from.
     * @param date         the date to filter by.
     * @return the maximum heart rate.
     */
    public static double getMaximumHr(
            final EntryManager entryManager,
            final String date) {

        List<LogEntry> entries = listFilteredByDates(entryManager, date);

        double maxHr = 0;
        for (LogEntry logEntry : entries) {
            if (logEntry.getMaxHeartRate() > maxHr) {
                maxHr = logEntry.getMaxHeartRate();
            }
        }
        return maxHr;
    }

    private static List<LogEntry> listFilteredByDates(
            final EntryManager entryManager,
            final String date) {

        LogEntry.SORTCONFIGURATIONS sortConfiguration = LogEntry.
                SORTCONFIGURATIONS.DATE;

        EntryManager.SortedIteratorBuilder iteratorBuilder =
                new EntryManager.SortedIteratorBuilder(
                        entryManager, sortConfiguration);

        System.out.println(date);
        Iterator<LogEntry> iterator = iteratorBuilder.filterTimeInterval(
                        LocalDate.parse(date.substring(0, 10)),
                        LocalDate.parse(date.substring(11))).
                iterator(false);

        List<LogEntry> entries = new ArrayList<>();
        iterator.forEachRemaining(entries::add);

        return entries;
    }

}

