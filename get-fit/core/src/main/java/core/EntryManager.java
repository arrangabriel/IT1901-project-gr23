package core;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides management for EntryLogs.
 */
public class EntryManager implements Iterable<LogEntry> {

    /** Hashmap of LogEntries. */
    private final HashMap<String, LogEntry> entryMap = new HashMap<>();

    /**
     * An entry manager instance is a wrapper for a list of logEntries.
     * Functions as the API interface for the core-module.
     */
    public EntryManager() { }

    /**
     * Adds a fresh entry to this EntryManager. Generates an id internally.
     *
     * @param title the title field for the new LogEntry as a string.
     * @param comment the comment field for the new LogEntry as a string.
     * @param date the date field for the new LogEntry
     *  as a time.LocalDate instance.
     * @param duration the duration field for the new LogEntry
     *  as a time.Duration instance.
     * @param feeling how the exercise felt on a scale from 1 to 10.
     * @param distance how much distance was covered during the exercise.
     * @param maxHeartRate the max recorded hear rate during the exercise.
     * @param exerciseCategory the category of the exercise.
     * @param exerciseSubCategory the subcategory of the exercise.
     * @throws IllegalArgumentException if any of the input is invalid.
     * @return the generated id for the new LogEntry as a string.
     */
    public String addEntry(
        final String title,
        final String comment,
        final LocalDate date,
        final Duration duration,
        final int feeling,
        final Double distance,
        final Integer maxHeartRate,
        final LogEntry.EXERCISECATEGORY exerciseCategory,
        final LogEntry.Subcategory exerciseSubCategory)
            throws IllegalArgumentException {

        String id = String.valueOf(entryMap.size());

        addEntry(id, title, comment, date, duration, feeling,
                distance, maxHeartRate, exerciseCategory, exerciseSubCategory);

        return id;
    }

    /**
     * Adds an already existing LogEntry (i.e. one that has an id) to this
     * EntryManager
     *
     * @param id       the id for the new LogEntry
     * @param title the title field for the new LogEntry as a string.
     * @param comment the comment field for the new LogEntry as a string.
     * @param date the date field for the new LogEntry
     *  as a time.LocalDate instance.
     * @param duration the duration field for the new LogEntry
     *  as a time.Duration instance.
     * @param feeling how the exercise felt on a scale from 1 to 10.
     * @param distance how much distance was covered during the exercise.
     * @param maxHeartRate the max recorded hear rate during the exercise.
     * @param exerciseCategory the category of the exercise.
     * @param exerciseSubCategory the subcategory of the exercise.
     * @throws IllegalArgumentException if any of the input is invalid.
     */
    public void addEntry(
        final String id,
        final String title,
        final String comment,
        final LocalDate date,
        final Duration duration,
        final int feeling,
        final Double distance,
        final Integer maxHeartRate,
        final LogEntry.EXERCISECATEGORY exerciseCategory,
        final LogEntry.Subcategory exerciseSubCategory)
            throws IllegalArgumentException {

        if (id == null
        || title == null
        || comment == null
        || date == null
        || duration == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        if (entryMap.containsKey(id)) {
            throw new IllegalArgumentException("Entry already exists");
        }
        entryMap.put(id, new LogEntry(id, title, comment, date, duration,
            feeling, distance, maxHeartRate, exerciseCategory,
             exerciseSubCategory));
    }

    /**
     * Gets a LogEntry instance by its id, if such a LogEntry exists.
     *
     * @param id the id to be searched for.
     * @throws IllegalArgumentException if id is null or the entry doesn't exist
     * @return the LogEntry instance with the associated id.
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
     * @throws IllegalArgumentException if id is null
     * @return wether an entry was actually removed
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

    public class SortedBuilder {

        /**Internally modifiable stream of LogEntries.*/
        private Stream<LogEntry> logEntryStream;
        // this might not be neccesary
        //private boolean filteredByCategory = false;

        public SortedBuilder(
                final LogEntry.SORTCONFIGURATIONS sortConfiguration)
                throws IllegalArgumentException {

            if (sortConfiguration == null) {
                throw new IllegalArgumentException("Sort configuration cannot be null.");
            }

            private Comparator<LogEntry> comparator = switch (sortConfiguration) {
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
         * @throws IllegalArgumentException if category is null.
         * @return the modified SortedBuilder.
         */
        public SortedBuilder filterExerciseCategory(
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
            //this.filteredByCategory = true;
        }


        public SortedBuilder filterSubCategory(
                final LogEntry.Subcategory subcategory)
                throws IllegalArgumentException {

            if (subcategory == null) {
                throw new IllegalArgumentException("Subcategory cannot be null.");
            }

            this.logEntryStream = this.logEntryStream
                    .filter((entry) -> entry
                            .getExerciseSubCategory()
                            .equals(subcategory));
            return this;
        }
    }

    /**
     * Returns an iterator sorted by parameters.
     *
     * @param sortConfig          one of the supported sorting configurations.
     * @param reverse             reverses output if set to true.
     * @param exerciseCategory    the main excercise category to sort by.
     * @param exerciseSubCategory the exercise sub-category to sort by.
     * @return an iterator of LogEntry instances,
     * sorted by the parameter criteria.
     * @throws IllegalArgumentException if any arguments are null.
     */
    public Iterator<LogEntry> sortedIterator(
        final LogEntry.SORTCONFIGURATIONS sortConfig,
        final boolean reverse,
        final LogEntry.EXERCISECATEGORY exerciseCategory,
        final LogEntry.Subcategory exerciseSubCategory)
            throws IllegalArgumentException {

        if (sortConfig == null || exerciseCategory == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        Stream<LogEntry> entryStream = entryMap.values().stream();

        if (!exerciseCategory.equals(LogEntry.EXERCISECATEGORY.ANY)) {
            entryStream = entryStream
                                    .filter((entry) -> entry
                                    .getExerciseCategory()
                                    .equals(exerciseCategory));

            // If null, don't perform subcategory filtering.
            // This being a general ANY enum would be nice,
            // but can't be done with current structure.

            if (exerciseSubCategory != null) {
                entryStream = entryStream
                                    .filter((entry) -> entry
                                    .getExerciseSubCategory()
                                    .equals(exerciseSubCategory));
            }
        }

        switch (sortConfig) {
            case DATE:
                entryStream = entryStream
                                    .sorted(Comparator
                                    .comparing(LogEntry::getDate));
            case DURATION:
                entryStream = entryStream
                                    .sorted(Comparator
                                    .comparing(LogEntry::getDuration));
            case TITLE:
                entryStream = entryStream
                                    .sorted(Comparator
                                    .comparing(LogEntry::getTitle));
            default:
                break;
        }

        List<LogEntry> entryList = entryStream.collect(Collectors.toList());
        if (reverse) {
            Collections.reverse(entryList);
        }

        return entryList.iterator();
    }

    /**
     * Returns an iterator sorted by parameters.
     *
     * @param sortConfig one of the supported sorting configurations.
     * @return an iterator of LogEntry instances,
     * sorted by the parameter criteria.
     * @throws IllegalArgumentException if any arguments are null.
     */
    public Iterator<LogEntry> sortedIterator(
        final LogEntry.SORTCONFIGURATIONS sortConfig)
            throws IllegalArgumentException {

        return sortedIterator(sortConfig,
                                false,
                                LogEntry.EXERCISECATEGORY.ANY,
                                null);

    }

    /**
     * Returns an iterator sorted by parameters.
     *
     * @param sortConfig one of the supported sorting configurations.
     * @param reverse    reverses output if set to true.
     * @return an iterator of LogEntry instances,
     * sorted by the parameter criteria.
     * @throws IllegalArgumentException if any arguments are null.
     */
    public Iterator<LogEntry> sortedIterator(
        final LogEntry.SORTCONFIGURATIONS sortConfig,
        final boolean reverse)
            throws IllegalArgumentException {

        return sortedIterator(sortConfig,
                                reverse,
                                LogEntry.EXERCISECATEGORY.ANY,
                                null);

    }

    /**
     * Returns an iterator sorted by parameters.
     *
     * @param sortConfig       one of the supported sorting configurations.
     * @param exerciseCategory one of the supported exercise categories.
     * @return an iterator of LogEntry instances,
     * sorted by the parameter criteria.
     * @throws IllegalArgumentException if any arguments are null.
     */
    public Iterator<LogEntry> sortedIterator(
        final LogEntry.SORTCONFIGURATIONS sortConfig,
        final LogEntry.EXERCISECATEGORY exerciseCategory)
            throws IllegalArgumentException {

        return sortedIterator(sortConfig,
                                false,
                                exerciseCategory,
                                null);
    }

    /**
     * Returns an iterator sorted by parameters.
     *
     * @param sortConfig       one of the supported sorting configurations.
     * @param reverse          reverses output if set to true.
     * @param exerciseCategory one of the supported exercise categories.
     * @return an iterator of LogEntry instances,
     * sorted by the parameter criteria.
     * @throws IllegalArgumentException if any arguments are null.
     */
    public Iterator<LogEntry> sortedIterator(
        final LogEntry.SORTCONFIGURATIONS sortConfig,
        final boolean reverse,
            final LogEntry.EXERCISECATEGORY exerciseCategory)
            throws IllegalArgumentException {
        return sortedIterator(sortConfig, reverse, exerciseCategory, null);
    }

    /**
     * Returns an iterator sorted by parameters.
     *
     * @param sortConfig          one of the supported sorting configurations.
     * @param exerciseCategory    one of the supported exercise categories.
     * @param exerciseSubCategory one of the supported exercise subCategories.
     * @return an iterator of LogEntry instances,
     * sorted by the parameter criteria.
     * @throws IllegalArgumentException if any arguments are null.
     */
    public Iterator<LogEntry> sortedIterator(
        final LogEntry.SORTCONFIGURATIONS sortConfig,
        final LogEntry.EXERCISECATEGORY exerciseCategory,
        final LogEntry.Subcategory exerciseSubCategory)
            throws IllegalArgumentException {

        return sortedIterator(sortConfig,
                                false,
                                exerciseCategory,
                                exerciseSubCategory);

    }
}
