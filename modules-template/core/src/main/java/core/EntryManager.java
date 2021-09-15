package core;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

public class EntryManager {
    
    private ArrayList<Entry> entryList = new ArrayList<Entry>();

    public EntryManager() {

    }

    public void addEntry(String title, String comment, LocalDate date, Duration duration) {
        entryList.add(new Entry(title, comment, date, duration));
    }

    public Entry getEntry(int index) {
        if (index + 1 > this.entryList.size()) {
            throw new IllegalArgumentException("Index out of bound");
        }

        return this.entryList.get(index);
    }

    public Entry getEntriey() {
        return getEntry(0);
    }

    public void removeEntry(int index) {
        if (index + 1 > this.entryList.size()) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        this.entryList.remove(index);
    }

    public void removeEntry() {
        removeEntry(0);
    }
}
