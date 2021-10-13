package core;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import core.LogEntry.EntryBuilder;
import core.LogEntry.Validity;

/**
 * Provides management for EntryLogs.
 */
public class EntryManager implements Iterable<LogEntry> {

    /**
     * Hashmap of LogEntries.
     */
    private final HashMap<String, LogEntry> entryMap = new HashMap<>();

    /**
     * An entry manager instance is a wrapper for a list of logEntries.
     * Functions as the API interface for the core-module.
     */
    public EntryManager() {
    }

    /**
     * Adds a fresh entry to this EntryManager. Generates an id internally.
     *
     * @param title               the title field for the new LogEntry as a string.
     * @param comment             the comment field for the new LogEntry as a string.
     * @param date                the date field for the new LogEntry
     *                            as a time.LocalDate instance.
     * @param duration            the duration field for the new LogEntry
     *                            as a time.Duration instance.
     * @param feeling             how the exercise felt on a scale from 1 to 10.
     * @param distance            how much distance was covered during the exercise.
     * @param maxHeartRate        the max recorded hear rate during the exercise.
     * @param exerciseCategory    the category of the exercise.
     * @param exerciseSubCategory the subcategory of the exercise.
     * @return the generated id for the new LogEntry as a string.
     * @throws IllegalArgumentException if any of the input is invalid.
     * @see #validate
     */
    public String addEntry(final EntryBuilder builder)
            throws IllegalArgumentException {

        String id = String.valueOf(entryMap.size());

        addEntry(id, builder);

        return id;
    }

    /**
     * Adds an already existing LogEntry (i.e. one that has an id) to this
     * EntryManager
     *
     * @param id                  the id for the new LogEntry
     * @param title               the title field for the new LogEntry as a string.
     * @param comment             the comment field for the new LogEntry as a string.
     * @param date                the date field for the new LogEntry
     *                            as a time.LocalDate instance.
     * @param duration            the duration field for the new LogEntry
     *                            as a time.Duration instance.
     * @param feeling             how the exercise felt on a scale from 1 to 10.
     * @param distance            how much distance was covered during the exercise.
     * @param maxHeartRate        the max recorded hear rate during the exercise.
     * @param exerciseCategory    the category of the exercise.
     * @param exerciseSubCategory the subcategory of the exercise.
     * @throws IllegalArgumentException if any of the input is invalid or the id is allready in use.
     * @see #validate
     */
    public void addEntry(
        final String id,
        final EntryBuilder builder)
            throws IllegalArgumentException {
        

        if (entryMap.containsKey(id)) {
            throw new IllegalArgumentException("Entry already exists");
        }

        Validity validity = LogEntry.validate(builder);
        if (!validity.valid()) {
            throw new IllegalArgumentException(validity.reason());
        }

        entryMap.put(id, new LogEntry(builder));
    }

    /**
     * Gets a LogEntry instance by its id, if such a LogEntry exists.
     *
     * @param id the id to be searched for.
     * @return the LogEntry instance with the associated id.
     * @throws IllegalArgumentException if id is null or the entry doesn't exist
     */
    public LogEntry getEntry(final String id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        if (entryMap.containsKey(id)) {
            return entryMap.get(id);
        } else {
            throw new IllegalArgumentException("Entry does not exits");
        }
    }

    /**
     * Removes a LogEntry by its id, if such a LogEntry exists.
     *
     * @param id the id to be removed.
     * @return wether an entry was actually removed
     * @throws IllegalArgumentException if id is null
     */
    public boolean removeEntry(final String id)
            throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        return entryMap.remove(id) != null;
    }

    /**
     * @return the number of LogEntries in this EntryManager
     */
    public int entryCount() {
        return this.entryMap.size();
    }

    /**
     * Gives an iterator for the LogEntries in this EntryManager in an arbitrary
     * order.
     *
     * @return an iterator of LogEntry instances.
     */
    @Override
    public Iterator<LogEntry> iterator() {
        return this.entryMap.values().iterator();
    }

    public class SortedIteratorBuilder {

        /**
         * Internally modifiable stream of LogEntries.
         */
        private Stream<LogEntry> logEntryStream;

        /**
         * Builder for a sorted iterator of this EntryManager's LogEntries.
         *
         * @param sortConfiguration a LogEntry.SORTCONFIGURATIONS to sort by.
         * @throws IllegalArgumentException if sortConfigurations is null.
         */
        public SortedIteratorBuilder(
                final LogEntry.SORTCONFIGURATIONS sortConfiguration)
                throws IllegalArgumentException {

            if (sortConfiguration == null) {
                throw new IllegalArgumentException(
                        "Sort configuration cannot be null.");
            }

            Comparator<LogEntry> comparator = switch (sortConfiguration) {
                case DATE -> Comparator.comparing(LogEntry::getDate);
                case DURATION -> Comparator.comparing(LogEntry::getDuration);
                case TITLE -> Comparator.comparing(LogEntry::getTitle);
            };

            this.logEntryStream = entryMap
                    .values()
                    .stream()
                    .sorted(comparator);
        }

        /**
         * Filters the entries to a single exercise category.
         *
         * @param category LogEntry.EXERCISECATEGORY to be filtered by.
         * @return the modified SortedIteratorBuilder.
         * @throws IllegalArgumentException if category is null.
         */
        public SortedIteratorBuilder filterExerciseCategory(
                final LogEntry.EXERCISECATEGORY category)
                throws IllegalArgumentException {

            if (category == null) {
                throw new IllegalArgumentException("Category cannot be null.");
            }

            this.logEntryStream = this.logEntryStream
                    .filter((entry) -> entry
                            .getExerciseCategory()
                            .equals(category));
            return this;
        }

        /**
         * Filters the entries to a single exercise category.
         *
         * @param subcategory LogEntry.Subcategory to be filtered by.
         * @return the modified SortedIteratorBuilder.
         * @throws IllegalArgumentException if Subcategory is null.
         */
        public SortedIteratorBuilder filterSubCategory(
                final LogEntry.Subcategory subcategory)
                throws IllegalArgumentException {

            if (subcategory == null) {
                throw new IllegalArgumentException(
                        "Subcategory cannot be null.");
            }

            this.logEntryStream = this.logEntryStream
                    .filter((entry) -> entry
                            .getExerciseSubCategory()
                            .equals(subcategory));
            return this;
        }

        /**
         * Filters the entries to a time interval.
         *
         * @param firstDate the beginning date (inclusive) of the interval.
         * @param lastDate  the ending date (inclusive) of the interval.
         * @return the modified SortedIteratorBuilder.
         * @throws IllegalArgumentException if any of the dates are null.
         */
        public SortedIteratorBuilder filterTimeInterval(
                final LocalDate firstDate, final LocalDate lastDate)
                throws IllegalArgumentException {

            if (firstDate == null || lastDate == null) {
                throw new IllegalArgumentException("Dates cannot be null.");
            }

            this.logEntryStream = this.logEntryStream
                    .filter((entry) -> {
                        LocalDate entryDate = entry.getDate();
                        // Check that date is in the interval (inclusive)
                        return (entryDate.isAfter(firstDate)
                                || entryDate.isEqual(firstDate))
                                && (entryDate.isBefore(lastDate)
                                || entryDate.isEqual(lastDate));
                    });

            return this;
        }

        /**
         * Builds the sorted iterator.
         *
         * @param reverse reverses order if set to true.
         * @return an iterator of LogEntries.
         */
        public Iterator<LogEntry> iterator(final boolean reverse) {

            List<LogEntry> entryList = this.logEntryStream
                    .collect(Collectors.toList());
            if (reverse) {
                Collections.reverse(entryList);
            }

            return entryList.iterator();
        }
    }
}
