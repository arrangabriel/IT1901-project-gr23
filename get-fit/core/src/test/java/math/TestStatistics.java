package math;

import java.time.Duration;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.EntryManager;
import core.LogEntry;
import core.LogEntry.CARDIOSUBCATEGORIES;
import core.LogEntry.EXERCISECATEGORY;
import core.LogEntry.STRENGTHSUBCATEGORIES;
import core.LogEntry.Subcategory;
import core.LogEntry.EntryBuilder;
import math.Statistics;

public class TestStatistics {

    static int minute = 60;
    static int hour = minute * 60;

    private EntryManager genValidEntryManager() {
        return new EntryManager();
    }

    private EntryBuilder genValidEntryBuilder(String id, Duration duration, double distance, LogEntry.EXERCISECATEGORY exerciseCategory) {
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
        manager.addEntry(genValidEntryBuilder("1", Duration.ofSeconds(hour), 10, LogEntry.EXERCISECATEGORY.RUNNING));
        manager.addEntry(genValidEntryBuilder("2", Duration.ofSeconds(hour), 10, LogEntry.EXERCISECATEGORY.RUNNING));
        Assertions.assertEquals(2, Statistics.getCount(manager));
    }

    @Test
    public void testGetTotalDuration() {
    }



}