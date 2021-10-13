package core;

import java.time.LocalDate;
import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.LogEntry.CARDIOSUBCATEGORIES;
import core.LogEntry.EXERCISECATEGORY;
import core.LogEntry.STRENGTHSUBCATEGORIES;
import core.LogEntry.Subcategory;


public class TestLogEntry {

    static int minute = 60;
    static int hour = minute*60;

    private LogEntry genValid() {
        String id = "0";
        String title = "Tets";
        String comment = "This is a test";
        LocalDate date = LocalDate.now().minusDays(1);
        Duration duration = Duration.ofSeconds(hour);
        int feeling = 1;
        double distance = 1;
        Integer maxHeartRate = 20;

        EXERCISECATEGORY exerciseCategory = EXERCISECATEGORY.STRENGTH;
        Subcategory subcategory = STRENGTHSUBCATEGORIES.PULL;
        LogEntry entry = new LogEntry(id, title, comment, date, duration, feeling, distance, maxHeartRate, exerciseCategory, subcategory);
        return entry;
    }

    
    @Test
    public void testLogEntry() {
        String id = "0";
        String title = "Tets";
        String comment = "This is a test";
        LocalDate date = LocalDate.now().minusDays(1);
        Duration duration = Duration.ofSeconds(hour);

        int feeling = 3;
        double distance = 7.0;
        Integer maxHeartRate = 183;

        EXERCISECATEGORY exerciseCategory = EXERCISECATEGORY.RUNNING;
        Subcategory subcategory = CARDIOSUBCATEGORIES.HIGHINTENSITY;

        LogEntry entry = new LogEntry(id, title, comment, date, duration, feeling, distance, maxHeartRate, exerciseCategory, subcategory);
        Assertions.assertEquals(id, entry.getId());
        Assertions.assertEquals(title, entry.getTitle());
        Assertions.assertEquals(comment, entry.getComment());
        Assertions.assertEquals(date, entry.getDate());
        Assertions.assertEquals(duration, entry.getDuration());
        Assertions.assertEquals(feeling, entry.getFeeling());
        Assertions.assertEquals(distance, entry.getDistance());
        Assertions.assertEquals(maxHeartRate, entry.getMaxHeartRate());
    }

    @Test
    public void illegalTitle() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LogEntry("0", "", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, 1.0, 20, EXERCISECATEGORY.STRENGTH, STRENGTHSUBCATEGORIES.PULL));
        LogEntry entry = genValid();
        Assertions.assertThrows(IllegalArgumentException.class, () -> entry.setTitle(""));
    }

    @Test
    public void illegalDate() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LogEntry("0", "Test", "comment", LocalDate.now().plusDays(1), Duration.ofSeconds(hour), 1, 1.0, 20, EXERCISECATEGORY.STRENGTH, STRENGTHSUBCATEGORIES.PULL));
        LogEntry entry = genValid();
        Assertions.assertThrows(IllegalArgumentException.class, () -> entry.setDate(LocalDate.now().plusDays(1)));
    }

    @Test
    public void illegalDuration() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LogEntry("0", "Test", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(-1*hour), 1, 1.0, 20, EXERCISECATEGORY.STRENGTH, STRENGTHSUBCATEGORIES.PULL));
        LogEntry entry = genValid();
        Assertions.assertThrows(IllegalArgumentException.class, () -> entry.setDuration(Duration.ofSeconds(-1*hour)));
    }

    @Test
    public void illegalFeeling() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LogEntry("0", "Test", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 11, 1.0, 20, EXERCISECATEGORY.STRENGTH, STRENGTHSUBCATEGORIES.PULL));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LogEntry("0", "Test", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 0, 1.0, 20, EXERCISECATEGORY.STRENGTH, STRENGTHSUBCATEGORIES.PULL));
        LogEntry entry = genValid();
        Assertions.assertThrows(IllegalArgumentException.class, () -> entry.setFeeling(11));
        Assertions.assertThrows(IllegalArgumentException.class, () -> entry.setFeeling(0));
    }

    @Test
    public void illegalDistance() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LogEntry("0", "Test", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 10, -2.0, 20, EXERCISECATEGORY.STRENGTH, STRENGTHSUBCATEGORIES.PULL));
        LogEntry entry = genValid();
        Assertions.assertThrows(IllegalArgumentException.class, () -> entry.setDistance(-1.0));
    }

    @Test
    public void illegalMaxHeartRate() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LogEntry("0", "Test", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 10, 1.0, 19, EXERCISECATEGORY.STRENGTH, STRENGTHSUBCATEGORIES.PULL));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LogEntry("0", "Test", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 10, 1.0, 251, EXERCISECATEGORY.STRENGTH, STRENGTHSUBCATEGORIES.PULL));
        LogEntry entry = genValid();
        Assertions.assertThrows(IllegalArgumentException.class, () -> entry.setMaxHeartRate(19));
        Assertions.assertThrows(IllegalArgumentException.class, () -> entry.setMaxHeartRate(251));
    }

}
