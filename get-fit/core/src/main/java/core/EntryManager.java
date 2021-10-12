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

public class EntryManager implements Iterable<LogEntry> {

    private final HashMap<String, LogEntry> entryMap = new HashMap<>();

    /**
     * An entry manager instance is a wrapper for a list of logEntries.
     * Functions as the API interface for the core-module.
     */
    public EntryManager() {}

    /**
     * Adds a fresh entry to this EntryManager. Generates an id internally.
     * @param title the title field for the new LogEntry as a string.
     * @param comment the comment field for the new LogEntry as a string.
     * @param date the date field for the new LogEntry as a time.LocalDate instance.
     * @param duration the duration field for the new LogEntry as a time.Duration instance.
     * @throws IllegalArgumentException if any of the arguments are null.
     * @return the generated id for the new LogEntry as a string.
     */
    public String addEntry(String title, String comment, LocalDate date, Duration duration, int feeling, Double distance, Integer maxHeartRate, LogEntry.EXERCISE_CATEGORIES exerciseCategory, LogEntry.Subcategories exerciseSubCategory) throws IllegalArgumentException {

        if (title == null || comment == null || date == null || duration == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        String id = String.valueOf(entryMap.size());
        addEntry(id, title, comment, date, duration, feeling, distance, maxHeartRate, exerciseCategory, exerciseSubCategory);
        return id;
    }

    /**
     * Adds an already existing LogEntry (i.e. one that has an id) to this EntryManager
     * @param id the id for the new LogEntry
     * @param title the title field for the new LogEntry as a string.
     * @param comment the comment field for the new LogEntry as a string.
     * @param date the date field for the new LogEntry as a time.LocalDate instance.
     * @param duration the duration field for the new LogEntry as a time.Duration instance.
     * @throws IllegalArgumentException if any of the arguments are null or the entry already exists.
     */
    public void addEntry(String id, String title, String comment, LocalDate date, Duration duration, int feeling, Double distance, Integer maxHeartRate, LogEntry.EXERCISE_CATEGORIES exerciseCategory, LogEntry.Subcategories exerciseSubCategory) throws IllegalArgumentException {

        if (id == null || title == null || comment == null || date == null || duration == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        if (entryMap.containsKey(id)) {
            throw new IllegalArgumentException("Entry already exists");
        }
        entryMap.put(id, new LogEntry(id, title, comment, date, duration, feeling, distance, maxHeartRate, exerciseCategory, exerciseSubCategory));
    }

    /**
     * Gets a LogEntry instance by its id, if such a LogEntry exists.
     * @param id the id to be searched for.
     * @throws IllegalArgumentException if id is null or the entry doesn't exist
     * @return the LogEntry instance with the associated id.
     */
    public LogEntry getEntry(String id) throws IllegalArgumentException {

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
     * @param id the id to be removed.
     * @throws IllegalArgumentException if id is null or the entry doesn't exist
     */
    public void removeEntry(String id) throws IllegalArgumentException {

        if (id == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        
        if (entryMap.containsKey(id)) {
            entryMap.remove(id);
        } else {
            throw new IllegalArgumentException("Entry does not exits");
        }
    }

    /**
     * @return the number of LogEntries in this EntryManager
     */
    public int entryCount() {
        return this.entryMap.size();
    }

    /**
     * Gives an iterator for the LogEntries in this EntryManager in an arbitrary order.
     * @return an iterator of LogEntry instances.
     */
    @Override
    public Iterator<LogEntry> iterator() {
        return this.entryMap.values().iterator();
    }

    /**
     * Returns an iterator sorted by parameters.
     * @param sortConfig one of the supported sorting configurations.
     * @param reverse reverses output if set to true.
     * @param exerciseCategory the main excercise category to sort by.
     * @param exerciseSubCategory the exercise sub-category to sort by.
     * @return an iterator of LogEntry instances, sorted by the parameter criteria.
     * @throws IllegalArgumentException if any arguments are null.
     */
    public Iterator<LogEntry> sortedIterator(LogEntry.SORT_CONFIGURATIONS sortConfig, boolean reverse, LogEntry.EXCERCISE_CATEGORIES exerciseCategory,
                                             LogEntry.Subcategories exerciseSubCategory) throws IllegalArgumentException{
        if (sortConfig == null || exerciseCategory == null){
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        Stream<LogEntry> entryStream = entryMap.values().stream();

        if(!exerciseCategory.equals(LogEntry.EXCERCISE_CATEGORIES.ANY)) {
            entryStream = entryStream.filter((entry) -> entry.getExcerciseCategory().equals(exerciseCategory));
            // If null, don't perform subcategory filtering.
            // This being a general ANY enum would be nice, but can't be done with current structure.
            if(exerciseSubCategory != null) {
                entryStream = entryStream.filter((entry) -> entry.getExcerciseSubCategory().equals(exerciseSubCategory));
            }
        }

        switch (sortConfig){
            case DATE -> entryStream = entryStream.sorted(Comparator.comparing(LogEntry::getDate));
            case DURATION -> entryStream = entryStream.sorted(Comparator.comparing(LogEntry::getDuration));
            case TITLE -> entryStream = entryStream.sorted(Comparator.comparing(LogEntry::getTitle));
        }

        List<LogEntry> entryList = entryStream.collect(Collectors.toList());
        if (reverse){ Collections.reverse(entryList); }

        return entryList.iterator();
    }

    /**
     * Returns an iterator sorted by parameters.
     * @param sortConfig one of the supported sorting configurations.
     * @return an iterator of LogEntry instances, sorted by the parameter criteria.
     */
    public Iterator<LogEntry> sortedIterator(LogEntry.SORT_CONFIGURATIONS sortConfig){
        return sortedIterator(sortConfig, false, LogEntry.EXCERCISE_CATEGORIES.ANY, null);
    }

    /**
     * Returns an iterator sorted by parameters.
     * @param sortConfig one of the supported sorting configurations.
     * @param reverse reverses output if set to true.
     * @return an iterator of LogEntry instances, sorted by the parameter criteria.
     */
    public Iterator<LogEntry> sortedIterator(LogEntry.SORT_CONFIGURATIONS sortConfig, boolean reverse){
        return sortedIterator(sortConfig, reverse, LogEntry.EXCERCISE_CATEGORIES.ANY, null);
    }

    /**
     * Returns an iterator sorted by parameters.
     * @param sortConfig one of the supported sorting configurations.
     * @param exerciseCategory one of the supported exercise categories.
     * @return an iterator of LogEntry instances, sorted by the parameter criteria.
     */
    public Iterator<LogEntry> sortedIterator(LogEntry.SORT_CONFIGURATIONS sortConfig,
                                             LogEntry.EXCERCISE_CATEGORIES exerciseCategory){
        return sortedIterator(sortConfig, false, exerciseCategory, null);
    }

    /**
     * Returns an iterator sorted by parameters.
     * @param sortConfig one of the supported sorting configurations.
     * @param reverse reverses output if set to true.
     * @param exerciseCategory one of the supported exercise categories.
     * @return an iterator of LogEntry instances, sorted by the parameter criteria.
     */
    public Iterator<LogEntry> sortedIterator(LogEntry.SORT_CONFIGURATIONS sortConfig, boolean reverse,
                                             LogEntry.EXCERCISE_CATEGORIES exerciseCategory){
        return sortedIterator(sortConfig, reverse, exerciseCategory, null);
    }

    /**
     * Returns an iterator sorted by parameters.
     * @param sortConfig one of the supported sorting configurations.
     * @param exerciseCategory one of the supported exercise categories.
     * @param exerciseSubCategory one of the supported exercise subCategories.
     * @return an iterator of LogEntry instances, sorted by the parameter criteria.
     */
    public Iterator<LogEntry> sortedIterator(LogEntry.SORT_CONFIGURATIONS sortConfig,
                                             LogEntry.EXCERCISE_CATEGORIES exerciseCategory,
                                             LogEntry.Subcategories exerciseSubCategory){
        return sortedIterator(sortConfig, false, exerciseCategory, exerciseSubCategory);
    }
}
