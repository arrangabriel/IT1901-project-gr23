package math;

import java.time.Duration;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.EntryManager;
import core.LogEntry;
import core.LogEntry.EntryBuilder;

public class TestStatistics {

    static int minute = 60;
    static int hour = minute * 60;

    private EntryManager genValidEntryManager() {
        return new EntryManager();
    }

    private EntryBuilder genValidEntryBuilder(Duration duration, Double distance, LogEntry.EXERCISECATEGORY exerciseCategory) {
        String title = "Test";
        String comment = "This is a test";
        LocalDate date = LocalDate.now().minusDays(1);
        int feeling = 1;

        EntryBuilder builder = new EntryBuilder(
            title, date, duration, exerciseCategory, feeling)
            .comment(comment)
            .distance(distance);
        return builder;
    }

    @Test
    public void testGetCount() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder = genValidEntryBuilder(Duration.ofSeconds(hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING);
        String id = manager.addEntry(builder.build());
        Assertions.assertEquals(1, Statistics.getCount(manager));
        manager.addEntry(builder.build());
        Assertions.assertEquals(2, Statistics.getCount(manager));
        manager.removeEntry(id);
        Assertions.assertEquals(1, Statistics.getCount(manager));
    }

    @Test
    public void testGetTotalDuration() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 = genValidEntryBuilder(Duration.ofSeconds(hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING);
        EntryBuilder builder2 = genValidEntryBuilder(Duration.ofSeconds(2 * hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING);
        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        //Compares with 3 hours which equals 10800 seconds. 
        Assertions.assertEquals(10800, Statistics.getTotalDuration(manager));
    }

    @Test
    public void testGetAverageDuration() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 = genValidEntryBuilder(Duration.ofSeconds(hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING);
        EntryBuilder builder2 = genValidEntryBuilder(Duration.ofSeconds(2 * hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING);
        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        //Compares with 1.5 hours which is equal to  
        Assertions.assertEquals(5400, Statistics.getAverageDuration(manager));
    }

    @Test
    public void testGetAverageSpeed() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 = genValidEntryBuilder(Duration.ofSeconds(hour), 5.0, LogEntry.EXERCISECATEGORY.RUNNING);
        EntryBuilder builder2 = genValidEntryBuilder(Duration.ofSeconds(2 * hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING);
        EntryBuilder builder3 = genValidEntryBuilder(Duration.ofSeconds(3 * hour), 15.0, LogEntry.EXERCISECATEGORY.STRENGTH);
        EntryBuilder builder4 = genValidEntryBuilder(Duration.ofSeconds(3 * hour), 40.0, LogEntry.EXERCISECATEGORY.CYCLING);
        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        manager.addEntry(builder3.build());
        manager.addEntry(builder4.build());

        Assertions.assertEquals(12, Statistics.getAverageSpeed(manager, LogEntry.EXERCISECATEGORY.RUNNING));
        Assertions.assertEquals(4.5, Statistics.getAverageSpeed(manager, LogEntry.EXERCISECATEGORY.CYCLING));


    }

    @Test
    public void testIllegalGetAverageSpeed() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder = genValidEntryBuilder(Duration.ofSeconds(hour), null, LogEntry.EXERCISECATEGORY.RUNNING);
        manager.addEntry(builder.build());
        Assertions.assertThrows(IllegalStateException.class, () -> Statistics.getAverageSpeed(manager, LogEntry.EXERCISECATEGORY.RUNNING));
    }



}