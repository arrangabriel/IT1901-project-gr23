package core;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class EntryManager implements Iterable<String> {
    
    private HashMap<String, LogEntry> entryMap = new HashMap<String, LogEntry>();

    public EntryManager() {

    }

    public String addEntry(String title, String comment, LocalDate date, Duration duration) {
        String id = String.valueOf(entryMap.size());
        addEntry(id, title, comment, date, duration);
        return id;
    }

    public void addEntry(String id, String title, String comment, LocalDate date, Duration duration) {
        if (entryMap.containsKey(id)) {
            throw new IllegalArgumentException("Entry allready exists");
        }
        entryMap.put(id, new LogEntry(id, title, comment, date, duration));
    }

    public LogEntry getEntry(String id) {
        if (entryMap.containsKey(id)) {
            return entryMap.get(id);
        } else {
            throw new IllegalArgumentException("Entry does not exits");
        }
    }

    public void removeEntry(String id) {
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
    public Iterator<String> iterator() {
        return this.entryMap.keySet().iterator();
    }

    public Set<Entry<String, LogEntry>> getEntryList() {
        return this.entryMap.entrySet();
    }
}
