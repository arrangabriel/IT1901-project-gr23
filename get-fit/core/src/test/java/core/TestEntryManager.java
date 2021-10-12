package core;

import java.time.LocalDate;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TestEntryManager {

    static int minute = 60;
    static int hour = minute*60;

    private EntryManager genValidManager() {
        return new EntryManager();
    }

    @Test
    public void testEntryManager() {
        EntryManager manager = new EntryManager();
    }

    @Test
    public void testAddEntry() {
        EntryManager manager = genValidManager();
        Assertions.assertEquals(0, manager.entryCount());
        manager.addEntry("0", "title", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISE_CATEGORIES.STRENGTH, LogEntry.STRENGTH_SUBCATEGORIES.PULL);
        Assertions.assertEquals(1, manager.entryCount());
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntry("0", "title", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISE_CATEGORIES.STRENGTH, LogEntry.STRENGTH_SUBCATEGORIES.PULL));
    }

    @Test
    public void testRemoveEntry() {
        EntryManager manager = genValidManager();
        manager.addEntry("0", "title", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISE_CATEGORIES.STRENGTH, LogEntry.STRENGTH_SUBCATEGORIES.PULL);
        Assertions.assertEquals(1, manager.entryCount());
        manager.removeEntry("0");
        Assertions.assertEquals(0, manager.entryCount());
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.removeEntry("0"));
    }

    @Test
    public void testGetEntry() {
        EntryManager manager = genValidManager();
        manager.addEntry("0", "title", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISE_CATEGORIES.STRENGTH, LogEntry.STRENGTH_SUBCATEGORIES.PULL);
        Assertions.assertEquals("0", manager.getEntry("0").getId());
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.getEntry("1"));
    }

}
