package core;

import java.time.LocalDate;

import java.time.Duration;
import java.util.Iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.LogEntry.EntryBuilder;


public class TestEntryManager {

    static int minute = 60;
    static int hour = minute*60;

    private EntryManager genValidManager() {
        return new EntryManager();
    }

    private EntryBuilder genValidBuilder() {
        String id = "0";
        String title = "Tets";
        String comment = "This is a test";
        LocalDate date = LocalDate.now().minusDays(1);
        Duration duration = Duration.ofSeconds(hour);
        int feeling = 1;
        double distance = 1;
        Integer maxHeartRate = 80;

        LogEntry.EXERCISECATEGORY exerciseCategory = LogEntry.EXERCISECATEGORY.STRENGTH;
        LogEntry.Subcategory subcategory = LogEntry.STRENGTHSUBCATEGORIES.PULL;

        EntryBuilder builder = new EntryBuilder(
            title, date, duration, exerciseCategory, feeling)
            .comment(comment)
            .exerciseSubcategory(subcategory)
            .distance(distance)
            .maxHeartRate(maxHeartRate);
        return builder;
    }
    
    @Test
    public void testEntryManager() {
        EntryManager manager = new EntryManager();
    }

    @Test
    public void testAddEntry() {
        EntryManager manager = genValidManager();
        Assertions.assertEquals(0, manager.entryCount());
        EntryBuilder builder = genValidBuilder();
        String id = manager.addEntry(builder.build());
        Assertions.assertEquals(1, manager.entryCount());
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntry(id, builder.build()));
    }

    @Test
    public void testRemoveEntry() {
        EntryManager manager = genValidManager();
        EntryBuilder builder = genValidBuilder();
        String id = manager.addEntry(builder.build());
        Assertions.assertEquals(1, manager.entryCount());
        boolean result1 = manager.removeEntry(id);
        Assertions.assertEquals(0, manager.entryCount());
        boolean result2 = manager.removeEntry(id);
        Assertions.assertTrue(result1);
        Assertions.assertFalse(result2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.removeEntry(null));
    }

    @Test
    public void testGetEntry() {
        EntryManager manager = genValidManager();
        EntryBuilder builder = genValidBuilder();
        manager.addEntry("0", builder.build());
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.getEntry("1"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.getEntry(null));
    }

    @Test
    public void testEntryCount(){
        EntryManager manager = genValidManager();
        EntryBuilder builder = genValidBuilder();
        Assertions.assertEquals(0, manager.entryCount());
        manager.addEntry(builder.build());
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


    @Test
    public void testSortedIteratorBuilder() {
        EntryManager manager = genValidManager();
        EntryBuilder builder1 = genValidBuilder(EXERCISECATEGORY.STRENGTH, STRENGTHSUBCATEGORIES.PUSH);
        EntryBuilder builder2 = genValidBuilder(EXERCISECATEGORY.STRENGTH, STRENGTHSUBCATEGORIES.PUSH);
        EntryBuilder builder3 = genValidBuilder(EXERCISECATEGORY.CYCLING, CARDIOSUBCATEGORIES.HIGHINTENSITY);
        
        manager.addEntry(builder1.build());
        manager.addEntry(builder2.build());
        manager.addEntry(builder3.build());
        
        int entryAmmount = manager.entryCount();
        int counter = 0;

        EntryManager.SortedIteratorBuilder itrbld1 = new EntryManager.SortedIteratorBuilder(manager, LogEntry.SORTCONFIGURATIONS.DATE);
        EntryManager.SortedIteratorBuilder itrbld2 = new EntryManager.SortedIteratorBuilder(manager, LogEntry.SORTCONFIGURATIONS.DURATION);
        EntryManager.SortedIteratorBuilder itrbld3 = new EntryManager.SortedIteratorBuilder(manager, LogEntry.SORTCONFIGURATIONS.TITLE);
        //Iterator<LogEntry> itr1 = itrbld1;
        //Iterator<LogEntry> itr2 = itrbld2.filterExerciseCategory(LogEntry.EXERCISECATEGORY.STRENGTH);
        itrbld2.filterExerciseCategory(EXERCISECATEGORY.STRENGTH);
        int c1 = 0;
        int c2 = 0;
        for (Iterator<LogEntry> itr = itrbld1.iterator(false); itr.hasNext();) {
            itr.next();
            c1++;
        }for (Iterator<LogEntry> itr = itrbld2.iterator(false); itr.hasNext();) {
            itr.next();
            c2++;
        }
        Assertions.assertThrows(IllegalArgumentException.class, () -> itrbld3.filterSubCategory(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> itrbld3.filterTimeInterval(null, null));
        
        itrbld3.filterTimeInterval(LocalDate.now().minusDays(2), LocalDate.now());
        
        Assertions.assertTrue(c1 > c2);
        Iterator<LogEntry> itr3 = itrbld3.iterator(false);

    }
}
