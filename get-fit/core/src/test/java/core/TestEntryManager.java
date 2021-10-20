package core;

import core.LogEntry.EntryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Iterator;


public class TestEntryManager {

    static int minute = 60;
    static int hour = minute * 60;

    private EntryManager genValidManager() {
        return new EntryManager();
    }

    private EntryBuilder genValidBuilder(LogEntry.EXERCISECATEGORY category,
                                         LogEntry.Subcategory subCategory) {
        String id = "0";
        String title = "Tets";
        String comment = "This is a test";
        LocalDate date = LocalDate.now().minusDays(1);
        Duration duration = Duration.ofSeconds(hour);
        int feeling = 1;
        double distance = 1;
        Integer maxHeartRate = 80;

        return new EntryBuilder(
                title, date, duration, category, feeling)
                .comment(comment)
                .exerciseSubcategory(subCategory)
                .distance(distance)
                .maxHeartRate(maxHeartRate);
    }

    @Test
    public void testEntryManager() {
        EntryManager manager = new EntryManager();
    }

    @Test
    public void testAddEntry() {
        EntryManager manager = genValidManager();
        Assertions.assertEquals(0, manager.entryCount());
        EntryBuilder builder =
                genValidBuilder(LogEntry.EXERCISECATEGORY.STRENGTH,
                        LogEntry.STRENGTHSUBCATEGORIES.PUSH);
        String id = manager.addEntry(builder.build());
        Assertions.assertEquals(1, manager.entryCount());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> manager.addEntry(id, builder.build()));
    }

    @Test
    public void testRemoveEntry() {
        EntryManager manager = genValidManager();
        EntryBuilder builder =
                genValidBuilder(LogEntry.EXERCISECATEGORY.STRENGTH,
                        LogEntry.STRENGTHSUBCATEGORIES.PUSH);
        String id = manager.addEntry(builder.build());
        Assertions.assertEquals(1, manager.entryCount());
        boolean result1 = manager.removeEntry(id);
        Assertions.assertEquals(0, manager.entryCount());
        boolean result2 = manager.removeEntry(id);
        Assertions.assertTrue(result1);
        Assertions.assertFalse(result2);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> manager.removeEntry(null));
    }

    @Test
    public void testGetEntry() {
        EntryManager manager = genValidManager();
        EntryBuilder builder =
                genValidBuilder(LogEntry.EXERCISECATEGORY.STRENGTH,
                        LogEntry.STRENGTHSUBCATEGORIES.PUSH);
        manager.addEntry("0", builder.build());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> manager.getEntry("1"));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> manager.getEntry(null));
    }

    @Test
    public void testEntryCount() {
        EntryManager manager = genValidManager();
        EntryBuilder builder =
                genValidBuilder(LogEntry.EXERCISECATEGORY.STRENGTH,
                        LogEntry.STRENGTHSUBCATEGORIES.PUSH);
        Assertions.assertEquals(0, manager.entryCount());
        manager.addEntry(builder.build());
        Assertions.assertEquals(1, manager.entryCount());
    }

    @Test
    public void testIterator() {
        EntryManager manager = genValidManager();
        int entryAmount = manager.entryCount();
        int counter = 0;
        for (LogEntry ignored : manager) {
            counter++;
        }
        Assertions.assertEquals(entryAmount, counter);
    }


    @Test
    public void testSortedIteratorBuilder() {
        EntryManager manager = genValidManager();
        EntryBuilder builder1 =
                genValidBuilder(LogEntry.EXERCISECATEGORY.STRENGTH,
                        LogEntry.STRENGTHSUBCATEGORIES.PUSH);
        EntryBuilder builder2 =
                genValidBuilder(LogEntry.EXERCISECATEGORY.STRENGTH,
                        LogEntry.STRENGTHSUBCATEGORIES.PUSH);
        EntryBuilder builder3 =
                genValidBuilder(LogEntry.EXERCISECATEGORY.CYCLING,
                        LogEntry.CARDIOSUBCATEGORIES.HIGHINTENSITY);

        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        manager.addEntry(builder3.build());

        int entryAmount = manager.entryCount();
        int counter = 0;

        EntryManager.SortedIteratorBuilder itrbld1 =
                new EntryManager.SortedIteratorBuilder(manager,
                        LogEntry.SORTCONFIGURATIONS.DATE);
        EntryManager.SortedIteratorBuilder itrbld2 =
                new EntryManager.SortedIteratorBuilder(manager,
                        LogEntry.SORTCONFIGURATIONS.DURATION);
        EntryManager.SortedIteratorBuilder itrbld3 =
                new EntryManager.SortedIteratorBuilder(manager,
                        LogEntry.SORTCONFIGURATIONS.TITLE);

        itrbld2.filterExerciseCategory(LogEntry.EXERCISECATEGORY.STRENGTH);
        int c1 = 0;
        int c2 = 0;
        for (Iterator<LogEntry> itr = itrbld1.iterator(false);
             itr.hasNext(); ) {
            itr.next();
            c1++;
        }
        for (Iterator<LogEntry> itr = itrbld2.iterator(false);
             itr.hasNext(); ) {
            itr.next();
            c2++;
        }
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> itrbld3.filterSubCategory(null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> itrbld3.filterTimeInterval(null, null));

        itrbld3.filterTimeInterval(LocalDate.now().minusDays(2),
                LocalDate.now());

        Assertions.assertTrue(c1 > c2);
        Iterator<LogEntry> itr3 = itrbld3.iterator(false);
    }
}
