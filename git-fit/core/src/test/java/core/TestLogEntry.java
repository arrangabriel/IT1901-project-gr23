package core;

import java.time.LocalDate;
import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TestLogEntry {

    static int minute = 60;
    static int hour = minute*60;

    private LogEntry genValid() {
        String id = "0";
        String title = "Tets";
        String comment = "This is a test";
        LocalDate date = LocalDate.now().minusDays(1);
        Duration duration = Duration.ofSeconds(hour);
        LogEntry entry = new LogEntry(id, title, comment, date, duration);
        return entry;
    }

    
    @Test
    public void testLogEntry() {
        String id = "0";
        String title = "Tets";
        String comment = "This is a test";
        LocalDate date = LocalDate.now().minusDays(1);
        Duration duration = Duration.ofSeconds(hour);
        LogEntry entry = new LogEntry(id, title, comment, date, duration);
        Assertions.assertEquals(id, entry.getId());
        Assertions.assertEquals(title, entry.getTitle());
        Assertions.assertEquals(comment, entry.getComment());
        Assertions.assertEquals(date, entry.getDate());
        Assertions.assertEquals(duration, entry.getDuartion());
    }

    @Test
    public void illegalDate() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LogEntry("0", "Test", "comment", LocalDate.now().plusDays(1), Duration.ofSeconds(hour)));
        LogEntry entry = genValid();
        Assertions.assertThrows(IllegalArgumentException.class, () -> entry.setDate(LocalDate.now().plusDays(1)));
    }

    @Test
    public void illegalDuration() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LogEntry("0", "Test", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(-1*hour)));
        LogEntry entry = genValid();
        Assertions.assertThrows(IllegalArgumentException.class, () -> entry.setDuartion(Duration.ofSeconds(-1*hour)));
    }

}
