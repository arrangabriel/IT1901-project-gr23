package math;

import core.EntryManager;
import core.LogEntry;

public class Statistics {
    
    public static int getCount(EntryManager entryManager){
        return entryManager.entryCount();
    }

    /**
     * Returns the total duration of all LogEntries in the EntryManager.
     * @param entryManager the entryManager to total.
     * @return the total.
     */
    public static double getTotalDuration(EntryManager entryManager) {
        double sum = 0;
        for (LogEntry logEntry : entryManager) {
            sum += logEntry.getDuration().toSeconds();
        }

        return sum;
    }

    /**
     * Returns the average duration across all LogEntries in the EntryManager.
     * @param entryManager the entryManager to average.
     * @return the average.
     */
    public static double getAverageDuration(EntryManager entryManager) {
        double sum = getTotalDuration(entryManager);
        double average = sum/entryManager.entryCount();

        return average;
    }
}
