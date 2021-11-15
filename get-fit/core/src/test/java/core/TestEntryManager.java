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

        private EntryBuilder genValidBuilder(ExerciseCategory category, Subcategory subCategory) {
                String title = "Tets";
                String comment = "This is a test";
                LocalDate date = LocalDate.now().minusDays(1);
                Duration duration = Duration.ofSeconds(hour);
                int feeling = 1;
                double distance = 1;
                Integer maxHeartRate = 80;

                return new EntryBuilder(title, date, duration, category, feeling).comment(comment)
                                .exerciseSubCategory(subCategory).distance(distance).maxHeartRate(maxHeartRate);
        }

        @Test
        public void testEntryManager() {
                new EntryManager();
        }

        @Test
        public void testAddEntry() {
                EntryManager manager = genValidManager();
                Assertions.assertEquals(0, manager.entryCount());
                EntryBuilder builder = genValidBuilder(
                        ExerciseCategory.STRENGTH,
                                StrengthSubCategory.PUSH);
                String id = manager.addEntry(builder.build());
                Assertions.assertEquals(1, manager.entryCount());
                Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntry(id, builder.build()));
        }

        @Test
        public void testRemoveEntry() {
                EntryManager manager = genValidManager();
                EntryBuilder builder = genValidBuilder(
                        ExerciseCategory.STRENGTH,
                                StrengthSubCategory.PUSH);
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
                EntryBuilder builder = genValidBuilder(
                        ExerciseCategory.STRENGTH,
                                StrengthSubCategory.PUSH);
                manager.addEntry("0", builder.build());
                Assertions.assertThrows(IllegalArgumentException.class, () -> manager.getEntry("1"));
                Assertions.assertThrows(IllegalArgumentException.class, () -> manager.getEntry(null));
        }

        @Test
        public void testEntryCount() {
                EntryManager manager = genValidManager();
                EntryBuilder builder = genValidBuilder(
                        ExerciseCategory.STRENGTH,
                                StrengthSubCategory.PUSH);
                Assertions.assertEquals(0, manager.entryCount());
                manager.addEntry(builder.build());
                Assertions.assertEquals(1, manager.entryCount());
        }

        @Test
        public void testIterator() {
                EntryManager manager = genValidManager();
                int entryAmount = manager.entryCount();
                int counter = 0;
                for (LogEntry entry : manager) {
                        Assertions.assertNotEquals(null, entry);
                        counter++;
                }
                Assertions.assertEquals(entryAmount, counter);
        }

        @Test
        public void testSortedIteratorBuilder() {
                EntryManager manager = genValidManager();
                EntryBuilder builder1 = genValidBuilder(
                        ExerciseCategory.STRENGTH,
                                StrengthSubCategory.PUSH);
                EntryBuilder builder2 = genValidBuilder(
                        ExerciseCategory.STRENGTH,
                                StrengthSubCategory.PUSH);
                EntryBuilder builder3 = genValidBuilder(
                        ExerciseCategory.CYCLING,
                                CardioSubCategory.HIGHINTENSITY);
                EntryBuilder builder4 = new EntryBuilder("Test", LocalDate.now().minusDays(3), Duration.ofSeconds(hour),
                                ExerciseCategory.STRENGTH, 4);

                manager.addEntry(builder1.build());
                manager.addEntry(builder2.build());
                manager.addEntry(builder3.build());
                manager.addEntry(builder4.build());

                int entryAmount = manager.entryCount();

                EntryManager.SortedIteratorBuilder itrbld1 = new EntryManager.SortedIteratorBuilder(manager,
                                SortConfiguration.DATE);
                EntryManager.SortedIteratorBuilder itrbld2 = new EntryManager.SortedIteratorBuilder(manager,
                                SortConfiguration.DURATION);
                EntryManager.SortedIteratorBuilder itrbld3 = new EntryManager.SortedIteratorBuilder(manager,
                                SortConfiguration.TITLE);

                itrbld2.filterExerciseCategory(ExerciseCategory.STRENGTH);
                int c1 = 0;
                int c2 = 0;
                int c3 = 0;
                for (Iterator<LogEntry> itr = itrbld1.iterator(false); itr.hasNext();) {
                        itr.next();
                        c1++;
                }

                Assertions.assertEquals(entryAmount, c1);

                for (Iterator<LogEntry> itr = itrbld2.iterator(false); itr.hasNext();) {
                        itr.next();
                        c2++;
                }
                Assertions.assertThrows(IllegalArgumentException.class, () -> itrbld3.filterSubCategory(null));
                Assertions.assertThrows(IllegalArgumentException.class, () -> itrbld3.filterTimeInterval(null, null));

                itrbld3.filterTimeInterval(LocalDate.now().minusDays(2), LocalDate.now());

                Assertions.assertTrue(c1 > c2);

                for (Iterator<LogEntry> itr3 = itrbld3.iterator(false); itr3.hasNext();) {
                        itr3.next();
                        c3++;
                }

                Assertions.assertTrue(c1 > c3);
        }
}
