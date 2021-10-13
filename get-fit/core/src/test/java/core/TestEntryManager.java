package core;

import java.time.LocalDate;

import java.time.Duration;
import java.util.Iterator;

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
    public void testAddEntry1() {
        EntryManager manager = genValidManager();
        Assertions.assertEquals(0, manager.entryCount());
        manager.addEntry("0", "title", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL);
        Assertions.assertEquals(1, manager.entryCount());
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntry("0", "title", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntry(null, "title", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntry("0", null, "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntry("0", "title", null, LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntry("0", "title", "comment", null, Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntry("0", "title", "comment", LocalDate.now().minusDays(1), null, 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL));
    }

    @Test
    public void testAddEntry2() {
        EntryManager manager = genValidManager();
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntry(null, "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntry("title", null, LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntry("title", "comment", null, Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntry("title", "comment", LocalDate.now().minusDays(1), null, 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL));
        Assertions.assertEquals("0", manager.addEntry( "title", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL));
    }

    @Test
    public void testRemoveEntry() {
        EntryManager manager = genValidManager();
        manager.addEntry("0", "title", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL);
        Assertions.assertEquals(1, manager.entryCount());
        boolean result1 = manager.removeEntry("0");
        Assertions.assertEquals(0, manager.entryCount());
        boolean result2 = manager.removeEntry("0");
        Assertions.assertTrue(result1);
        Assertions.assertFalse(result2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.removeEntry(null));
    }

    @Test
    public void testGetEntry() {
        EntryManager manager = genValidManager();
        manager.addEntry("0", "title", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL);
        Assertions.assertEquals("0", manager.getEntry("0").getId());
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.getEntry("1"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.getEntry(null));
    }

    @Test
    public void testEntryCount(){
        EntryManager manager = genValidManager();
        Assertions.assertEquals(0, manager.entryCount());
        manager.addEntry("0", "title", "comment", LocalDate.now().minusDays(1), Duration.ofSeconds(hour), 1, null, null, LogEntry.EXERCISECATEGORY.STRENGTH, LogEntry.STRENGTHSUBCATEGORIES.PULL);
        Assertions.assertEquals(1, manager.entryCount());
    }

    @Test
    public void testIterator(){
        EntryManager manager = genValidManager();
        int entryAmmount = manager.entryCount();
        int counter = 0;
        for (Iterator<LogEntry> it = manager.iterator(); it.hasNext(); ) {
            LogEntry entry = it.next();
            counter++;
        }
        Assertions.assertEquals(entryAmmount, counter);
    }
}
