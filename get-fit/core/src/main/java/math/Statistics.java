package math;

import core.EntryManager;
import core.LogEntry;
import java.util.Iterator;
import core.EntryManager.SortedIteratorBuilder;

/** Statistics class. */
public final class Statistics {

    /** Hidden constructor. */
    private Statistics() { }

    /**
     * Returns the count of entries in the entryManager.
     * @param entryManager the entry manager to count over.
     * @return the count of entries.
     */
    public static int getCount(final EntryManager entryManager){
        return entryManager.entryCount();
    }

    /**
     * Returns the total duration of all LogEntries in the EntryManager.
     * @param entryManager the entryManager to total.
     * @return the total duration.
     */
    public static double getTotalDuration(final EntryManager entryManager) {
        double sum = 0;
        for (LogEntry logEntry : entryManager) {
            sum += logEntry.getDuration().toSeconds();
        }

        return sum;
    }

    /**
     * Returns the average duration across all LogEntries in the EntryManager.
     * @param entryManager the entryManager to average.
     * @return the average duration.
     */
    public static double getAverageDuration(final EntryManager entryManager) {
        double sum = getTotalDuration(entryManager);
        double average = sum / entryManager.entryCount();

        return average;
    }

    /**
    * Returns the average speed across all LogEntries in the EntryManager that
    * has RUNNING as exercise category.
    * @param entryManager the entryManager to calculate average speed from.
    * @return the average speed of a group of entries.
    * @throws IllegalStateException if distance is not positive.
    */
    public static double getAverageSpeed(final EntryManager entryManager)
        throws IllegalStateException {

        double distance = 0;

        Iterator<LogEntry> entries = new SortedIteratorBuilder(
            LogEntry.SORTCONFIGURATIONS.DATE).filterExerciseCategory(
                LogEntry.EXERCISECATEGORY.RUNNING).iterator(false);

        double time = 0;

        while (entries.hasNext()) {
           LogEntry entry = entries.next();

           if (entry.getDistance().equals(null)) {
            distance += entry.getDistance();
           }
           time += entry.getDuration().toMinutes();
        }

        if (distance == 0) {
            throw new IllegalStateException("The distance must be over 0");
        }
        return time / distance;
    }
}
