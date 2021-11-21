package math;

import core.EntryManager;
import core.ExerciseCategory;
import core.LogEntry;
import core.SortConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Statistics class.
 */
public final class Statistics {
    /**
     * Date format length.
     */
    private static final int DATE_LENGTH = 10;

    /**
     * Hidden constructor to simulate static class.
     */
    private Statistics() {}

    /**
     * Returns the count of entries in the entryManager.
     *
     * @param entryManager the entry manager to count over.
     * @param category     the category to filter by.
     * @param date         the date interval to filter by.
     * @return the count of entries.
     */
    public static int getCount(
            final EntryManager entryManager,
            final String category,
            final String date) {
        List<LogEntry> entries =
                listFilteredByDates(entryManager, category, date);

        return entries.size();
    }

    /**
     * Returns the total duration of all LogEntries in the EntryManager.
     *
     * @param entryManager the entryManager to total.
     * @param category     the category to filter by.
     * @param date         the date interval to filter by.
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
     * Returns the average duration across all LogEntries in the EntryManager.
     *
     * @param entryManager the entryManager to average.
     * @param category     the category to filter by.
     * @param date         the date interval to filter by.
     * @return the average duration.
     */
    public static double getAverageDuration(
            final EntryManager entryManager,
            final String category,
            final String date) {
        List<LogEntry> entries = listFilteredByDates(
                entryManager, category, date);

        double sum = getTotalDuration(entryManager, category, date);

        return sum / entries.size();
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
     * Returns the average feeling of all LogEntries in the EntryManager.
     *
     * @param entryManager the entryManager to calculate average feeling from.
     * @param category     the exercise category to sort average speed from.
     * @param date         the date to filter by
     * @return the average feeling.
     */
    public static double getAverageFeeling(
            final EntryManager entryManager,
            final String category,
            final String date) {
        List<LogEntry> entries = listFilteredByDates(
                entryManager, category, date);

        double sum = 0;

        for (LogEntry logEntry : entries) {
            sum += logEntry.getFeeling();
        }

        return sum / entryManager.entryCount();
    }

    /**
     * Returns the maximum heart rate across all LogEntries in the EntryManager.
     *
     * @param entryManager the entryManager to get the maximum heart rate from.
     * @param category     the exercise category to sort average speed from.
     * @param date         the date to filter by
     * @return the maximum heart rate.
     */
    public static double getMaximumHr(
            final EntryManager entryManager,
            final String category,
            final String date) {
        List<LogEntry> entries = listFilteredByDates(
                entryManager, category, date);

        return entries.stream().map(LogEntry::getMaxHeartRate).max(Integer::compare).orElse(0);
    }

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
            Iterator<LogEntry> iterator = iteratorBuilder
                .filterTimeInterval(
                    LocalDate.parse(date.substring(0, DATE_LENGTH)),
                    LocalDate.parse(date.substring(DATE_LENGTH + 1)))
                .filterExerciseCategory(
                    ExerciseCategory.valueOf(category))
                .iterator(false);

            List<LogEntry> entries = new ArrayList<>();
            iterator.forEachRemaining(entries::add);

            return entries;
        }

        Iterator<LogEntry> iterator = iteratorBuilder
            .filterTimeInterval(
                LocalDate.parse(date.substring(0, DATE_LENGTH)),
                LocalDate.parse(date.substring(DATE_LENGTH + 1)))
            .iterator(false);

        List<LogEntry> entries = new ArrayList<>();
        iterator.forEachRemaining(entries::add);

        return entries;
    }
}
