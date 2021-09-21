package localpersistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import core.EntryManager;
import core.LogEntry;

public class TestEntrySaverJson {

    private final String saveFile = "SavedTestData.json";

    private EntryManager genValidManager() {
        EntryManager manager = new EntryManager();
        manager.addEntry("0", "Test0", "comment the first", LocalDate.now().minusDays(2), Duration.ofSeconds(3600));
        manager.addEntry("1", "Test1", "comment the second", LocalDate.now().minusDays(1), Duration.ofSeconds(3600*2));
        manager.addEntry("2", "Test2", "comment the third", LocalDate.now().minusDays(3), Duration.ofSeconds(3600/2));
        manager.addEntry("3", "Test3", "comment the fourth", LocalDate.now().minusDays(7), Duration.ofSeconds(3600*4));
        return manager;
    }

    @BeforeEach
    public void deleteFile() {
        File f = new File(saveFile);
        f.delete();
    }

    @Test
    public void testSaveAndLoad() {
        EntryManager manager = genValidManager();
        EntrySaverJson saver = new EntrySaverJson();
        try {
            saver.save(manager, saveFile);
        } catch (IOException e) {
            Assertions.fail();
        }
        EntryManager newManager = new EntryManager();
        Assertions.assertEquals(0, newManager.length());
        try {
            saver.load(newManager, saveFile);
        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
        assertEquals(manager.length(), newManager.length());
        for (LogEntry entry : manager) {
            assertEquals(entry.getTitle(), entry.getTitle());
            assertEquals(entry.getComment(), entry.getComment());
            assertEquals(entry.getDate(), entry.getDate());
            assertEquals(entry.getDuartion(), entry.getDuartion());

        }
    }

}
