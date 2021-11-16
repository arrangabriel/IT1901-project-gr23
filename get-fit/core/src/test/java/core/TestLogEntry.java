package core;

import java.time.LocalDate;
import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.LogEntry.EntryBuilder;

public class TestLogEntry {

    static int minute = 60;
    static int hour = minute * 60;

    private EntryBuilder genValid() {
        String title = "Tets";
        String comment = "This is a test";
        LocalDate date = LocalDate.now().minusDays(1);
        Duration duration = Duration.ofSeconds(hour);
        int feeling = 1;
        double distance = 1;
        Integer maxHeartRate = 80;

        ExerciseCategory exerciseCategory = ExerciseCategory.STRENGTH;
        Subcategory subcategory = StrengthSubCategory.PULL;

        EntryBuilder builder = new EntryBuilder(title, date, duration, exerciseCategory, feeling).comment(comment)
                .exerciseSubCategory(subcategory).distance(distance).maxHeartRate(maxHeartRate);
        return builder;
    }

    @Test // Above test proves it should work, considering it is nearly identical with the
          // test in LogEntry (line 271)
    public void testLogEntry() {
        String title = "Tets";
        String comment = "This is a test";
        LocalDate date = LocalDate.now().minusDays(1);
        Duration duration = Duration.ofSeconds(hour);

        int feeling = 3;
        double distance = 7.0;
        Integer maxHeartRate = 183;

        ExerciseCategory exerciseCategory = ExerciseCategory.RUNNING;
        Subcategory subcategory = CardioSubCategory.HIGHINTENSITY;

        EntryBuilder builder = new EntryBuilder(title, date, duration, exerciseCategory, feeling).comment(comment)
                .exerciseSubCategory(subcategory).distance(distance).maxHeartRate(maxHeartRate);
        LogEntry entry = new LogEntry(builder);

        Assertions.assertEquals(title, entry.getTitle());
        Assertions.assertEquals(comment, entry.getComment());
        Assertions.assertEquals(date, entry.getDate());
        Assertions.assertEquals(duration, entry.getDuration());
        Assertions.assertEquals(feeling, entry.getFeeling());
        Assertions.assertEquals(distance, entry.getDistance());
        Assertions.assertEquals(maxHeartRate, entry.getMaxHeartRate());
    }

    @Test
    public void illegalId() {
        LogEntry entry = new EntryBuilder("Test", LocalDate.now().minusDays(1), Duration.ofSeconds(hour),
                ExerciseCategory.STRENGTH, 1).build();
        entry.setId("0");
        Assertions.assertThrows(IllegalStateException.class, () -> entry.setId("0"));
    }

    @Test
    public void illegalTitle() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntryBuilder("", LocalDate.now().minusDays(1),
                Duration.ofSeconds(hour), ExerciseCategory.STRENGTH, 1).build());
    }

    @Test
    public void illegalDate() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntryBuilder("Test",
                LocalDate.now().plusDays(1), Duration.ofSeconds(hour), ExerciseCategory.STRENGTH, 1).build());
    }

    @Test
    public void illegalDuration() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntryBuilder("Test",
                LocalDate.now().minusDays(1), Duration.ofSeconds(-hour), ExerciseCategory.STRENGTH, 1).build());
    }

    @Test
    public void illegalFeeling() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntryBuilder("Test",
                LocalDate.now().minusDays(1), Duration.ofSeconds(hour), ExerciseCategory.STRENGTH, 11).build());
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntryBuilder("Test",
                LocalDate.now().minusDays(1), Duration.ofSeconds(hour), ExerciseCategory.STRENGTH, 0).build());
    }

    @Test
    public void illegalDistance() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> genValid().distance(-10.0).build());
        Assertions.assertThrows(IllegalArgumentException.class, () -> genValid().distance(0.0).build());
    }

    @Test
    public void illegalMaxHeartRate() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> genValid().maxHeartRate(10).build());
        Assertions.assertThrows(IllegalArgumentException.class, () -> genValid().maxHeartRate(700).build());
    }
}
