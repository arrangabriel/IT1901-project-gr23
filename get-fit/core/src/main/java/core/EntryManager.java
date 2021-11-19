package core;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides management for EntryLogs.
 */
public final class EntryManager implements Iterable<LogEntry> {

    /**
     * Hashmap of LogEntries.
     */
    private final HashMap<String, LogEntry> entryMap;

    /**
     * Current id hash position.
     */
    private int idHashPosition = 0;

    /**
     * An entry manager instance is a wrapper for a list of logEntries.
     * Functions as the API interface for the core-module.
     */
    public EntryManager() {
        this.entryMap = new HashMap<String, LogEntry>();
    }

    public EntryManager(EntryManager entryManager) {
        this.entryMap = entryManager.getEntryMap();
        this.idHashPosition = entryManager.getIdHashPosition();
    }

    private HashMap<String, LogEntry> getEntryMap() {
        return new HashMap<String, LogEntry>(this.entryMap);
    }
    private int getIdHashPosition() {
        return this.idHashPosition;
    }

    /**
     * Updates a provided EntryManager with the LogEntries represented
     * in the HashMap.
     *
     * @param map          The map representing the LogEntries to add.
     * @param entryManager The EntryManager to update.
     */
    public static void fromHash(
            final HashMap<String, HashMap<String, String>> map,
            final EntryManager entryManager) {

        for (Entry<String, HashMap<String, String>> entryEntry: map.entrySet()) {
            entryManager.updateHashPosition(
                    Integer.parseInt(entryEntry.getKey()));
            LogEntry entry = LogEntry.fromHash(entryEntry.getValue());
            entryManager.addEntry(entryEntry.getKey(), entry);
        }

    }

    /**
     * Adds a fresh entry to this EntryManager. Generates an id internally.
     *
     * @param entry the builder for the new LogEntry.
     * @return the generated id for the new LogEntry as a string.
     * @throws IllegalArgumentException if any of the input is invalid.
     */
    public String addEntry(final LogEntry entry)
            throws IllegalArgumentException {

        this.idHashPosition++;
        String id = String.valueOf(this.idHashPosition);

        addEntry(id, entry);

        return id;
    }

    /**
     * Adds an already existing LogEntry (i.e. one that has an id) to this
     * EntryManager
     *
     * @param id    the id for the new LogEntry.
     * @param entry the builder for the new LogEntry.
     * @throws IllegalArgumentException if any of the input is invalid
     *                                  or the id is already in use.
     * @throws IllegalStateException    if the entry already has a set id.
     */
    public void addEntry(
            final String id,
            final LogEntry entry)
            throws IllegalArgumentException, IllegalStateException {

        if (entryMap.containsKey(id)) {
            throw new IllegalArgumentException("Entry already exists");
        }

        entry.setId(id);
        this.entryMap.put(id, entry);
        updateHashPosition(Integer.parseInt(id));
    }

    /**
     * Updates the position for id hashing.
     *
     * @param pos the new position.
     */
    public void updateHashPosition(final int pos) {
        if (pos > this.idHashPosition) {
            this.idHashPosition = pos;
            this.idHashPosition++;
        }
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
     * @return whether an entry was actually removed
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
     * Represents this EntryManager as a HashMap with LogEntries,
     * also represented as HashMaps.
     *
     * @return The HashMap representing this EntryManager.
     * @see LogEntry#toHash()
     */
    public HashMap<String, HashMap<String, String>> toHashMap() {

        HashMap<String, HashMap<String, String>> map = new HashMap<>();

        this.forEach((entry) -> map.put(entry.getId(), entry.toHash()));

        return map;
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

    /**
     * Builder for LogEntry iterator.
     */
    public static class SortedIteratorBuilder {

        /**
         * Internally modifiable stream of LogEntries.
         */
        private Stream<LogEntry> logEntryStream;

        /**
         * Builder for a sorted iterator of this EntryManager's LogEntries.
         *
         * @param entryManager      the entry manager to get entries from.
         * @param sortConfiguration a LogEntry.SORTCONFIGURATIONS to sort by.
         * @throws IllegalArgumentException if sortConfigurations is null.
         */
        public SortedIteratorBuilder(
                final EntryManager entryManager,
                final SortConfiguration sortConfiguration)
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

            this.logEntryStream = entryManager.entryMap
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
                final ExerciseCategory category)
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
                final Subcategory subcategory)
                throws IllegalArgumentException {

            if (subcategory == null) {
                throw new IllegalArgumentException(
                        "Subcategory cannot be null.");
            }

            this.logEntryStream = this.logEntryStream
                    .filter((entry) -> {
                        Subcategory entrySubcategory =
                                entry.getExerciseSubCategory();
                        if (entrySubcategory != null) {
                            return entrySubcategory.equals(subcategory);
                        } else {
                            return false;
                        }
                    });
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
