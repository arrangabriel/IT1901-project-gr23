package core;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class EntryManager implements Iterable<LogEntry> {
    
    private final HashMap<String, LogEntry> entryMap = new HashMap<>();

    public EntryManager() {

    }

    public String addEntry(String title, String comment, LocalDate date, Duration duration) {

        if (title == null || comment == null || date == null || duration == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        String id = String.valueOf(entryMap.size());
        addEntry(id, title, comment, date, duration);
        return id;
    }

    public void addEntry(String id, String title, String comment, LocalDate date, Duration duration) {

        if (id == null || title == null || comment == null || date == null || duration == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        if (entryMap.containsKey(id)) {
            throw new IllegalArgumentException("Entry allready exists");
        }
        entryMap.put(id, new LogEntry(id, title, comment, date, duration));
    }

    public LogEntry getEntry(String id) {

        if (id == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        if (entryMap.containsKey(id)) {
            return entryMap.get(id);
        } else {
            throw new IllegalArgumentException("Entry does not exits");
        }
    }

    public void removeEntry(String id) {

        if (id == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        
        if (entryMap.containsKey(id)) {
            entryMap.remove(id);
        } else {
            throw new IllegalArgumentException("Entry does not exits");
        }
    }

    public int length() {
        return this.entryMap.size();
    }

    @Override
    public Iterator<LogEntry> iterator() {
        return this.entryMap.values().iterator();
    }

    public Set<Entry<String, LogEntry>> getEntryList() {
        return this.entryMap.entrySet();
    }
}
