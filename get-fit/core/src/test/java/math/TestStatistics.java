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
    static String date = LocalDate.now().minusYears(1).toString()
     + "-" + LocalDate.now().toString();

    private EntryManager genValidEntryManager() {
        return new EntryManager();
    }

    private EntryBuilder genValidEntryBuilder(Duration duration, Double distance, LogEntry.EXERCISECATEGORY exerciseCategory, Integer feeling) {
        String title = "Test";
        String comment = "This is a test";
        LocalDate date = LocalDate.now().minusDays(1);

        EntryBuilder builder = new EntryBuilder(
            title, date, duration, exerciseCategory, feeling)
            .comment(comment)
            .distance(distance);
        return builder;
    }

    //Todo test illegal cases:

    @Test
    public void testGetCount() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder = genValidEntryBuilder(Duration.ofSeconds(hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING, 1);
        String id = manager.addEntry(builder.build());
        Assertions.assertEquals(1, Statistics.getCount(manager, date));
        manager.addEntry(builder.build());
        Assertions.assertEquals(2, Statistics.getCount(manager, date));
        manager.removeEntry(id);
        Assertions.assertEquals(1, Statistics.getCount(manager, date));
    }

    @Test
    public void testGetTotalDuration() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 = genValidEntryBuilder(Duration.ofSeconds(hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING, 1);
        EntryBuilder builder2 = genValidEntryBuilder(Duration.ofSeconds(2 * hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING, 1);
        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        //Compares with 3 hours which equals 10800 seconds. 
        Assertions.assertEquals(10800, Statistics.getTotalDuration(manager, date));
    }

    @Test
    public void testGetAverageDuration() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 = genValidEntryBuilder(Duration.ofSeconds(hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING, 1);
        EntryBuilder builder2 = genValidEntryBuilder(Duration.ofSeconds(2 * hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING, 1);
        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        //Compares with 1.5 hours which is equal to  
        Assertions.assertEquals(5400, Statistics.getAverageDuration(manager, date));
    }

    @Test
    public void testGetAverageSpeed() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 = genValidEntryBuilder(Duration.ofSeconds(hour), 5.0, LogEntry.EXERCISECATEGORY.RUNNING, 1);
        EntryBuilder builder2 = genValidEntryBuilder(Duration.ofSeconds(2 * hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING, 1);
        EntryBuilder builder3 = genValidEntryBuilder(Duration.ofSeconds(3 * hour), 15.0, LogEntry.EXERCISECATEGORY.STRENGTH, 1);
        EntryBuilder builder4 = genValidEntryBuilder(Duration.ofSeconds(3 * hour), 40.0, LogEntry.EXERCISECATEGORY.CYCLING, 1);
        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        manager.addEntry(builder3.build());
        manager.addEntry(builder4.build());

        Assertions.assertEquals(12, Statistics.getAverageSpeed(manager, LogEntry.EXERCISECATEGORY.RUNNING, date));
        Assertions.assertEquals(4.5, Statistics.getAverageSpeed(manager, LogEntry.EXERCISECATEGORY.CYCLING, date));


    }

    @Test
    public void testIllegalGetAverageSpeed() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder = genValidEntryBuilder(Duration.ofSeconds(hour), null, LogEntry.EXERCISECATEGORY.RUNNING, 1);
        manager.addEntry(builder.build());
        Assertions.assertThrows(IllegalStateException.class, () -> Statistics.getAverageSpeed(manager, LogEntry.EXERCISECATEGORY.RUNNING, date));
    }

    @Test
    public void testGetAverageFeeling() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 = genValidEntryBuilder(Duration.ofSeconds(hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING, 1);
        EntryBuilder builder2 = genValidEntryBuilder(Duration.ofSeconds(hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING, 10);
        EntryBuilder builder3 = genValidEntryBuilder(Duration.ofSeconds(hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING, 7);

        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        manager.addEntry(builder3.build());

        Assertions.assertEquals(6, Statistics.getAverageFeeling(manager, date));
    }

    @Test
    public void testGetMaximumHr() {
        EntryManager manager = genValidEntryManager();
        EntryBuilder builder1 = genValidEntryBuilder(Duration.ofSeconds(hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING, 1);
        EntryBuilder builder2 = genValidEntryBuilder(Duration.ofSeconds(hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING, 10);
        EntryBuilder builder3 = genValidEntryBuilder(Duration.ofSeconds(hour), 10.0, LogEntry.EXERCISECATEGORY.RUNNING, 7);

        builder1.maxHeartRate(130);
        builder2.maxHeartRate(200);
        builder3.maxHeartRate(145);

        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        manager.addEntry(builder3.build());

        Assertions.assertEquals(200, Statistics.getMaximumHr(manager, date));
    }
}
