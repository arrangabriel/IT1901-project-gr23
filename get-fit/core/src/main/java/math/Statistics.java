package math;

import core.EntryManager;
import core.LogEntry;

public class Statistics {
    
    public static int getCount(EntryManager entryManager){
        return entryManager.entryCount();
    }

    /**
     * Returns the average duration across all LogEntries in the EntryManager.
     * @param entryManager the entryManager to average.
     * @return the average.
     */
    public static double getAverageDuration(EntryManager entryManager) {
        double sum = 0;
        for (LogEntry logEntry : entryManager) {
            sum += logEntry.getDuration().toSeconds();
        }
        double average = sum/entryManager.entryCount();

        return average;
    }

}
