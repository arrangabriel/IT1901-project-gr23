package localpersistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.IllegalSelectorException;
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
        manager.addEntry("0", "Test0", "comment the first", LocalDate.now().minusDays(2), Duration.ofSeconds(3600), 1, null, null, LogEntry.EXERCISE_CATEGORIES.STRENGTH, LogEntry.STRENGTH_SUBCATEGORIES.PUSH);
        manager.addEntry("1", "Test1", "comment the second", LocalDate.now().minusDays(1), Duration.ofSeconds(3600*2), 7, Double.valueOf(3000), 170, LogEntry.EXERCISE_CATEGORIES.RUNNING, LogEntry.CARDIO_SUBCATEGORIES.LONG);
        manager.addEntry("2", "Test2", "comment the third", LocalDate.now().minusDays(3), Duration.ofSeconds(3600/2), 5, null, 180, LogEntry.EXERCISE_CATEGORIES.STRENGTH, LogEntry.STRENGTH_SUBCATEGORIES.LEGS);
        manager.addEntry("3", "Test3", "comment the fourth", LocalDate.now().minusDays(7), Duration.ofSeconds(3600*4), 9, Double.valueOf(6000), 190, LogEntry.EXERCISE_CATEGORIES.CYCLING, LogEntry.CARDIO_SUBCATEGORIES.HIGH_INTENSITY);
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
        try {
            EntrySaverJson.save(manager, saveFile);
        } catch (IOException e) {
            Assertions.fail();
        }
        EntryManager newManager = new EntryManager();
        Assertions.assertEquals(0, newManager.entryCount());
        try {
            EntrySaverJson.load(newManager, saveFile);
        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
        Assertions.assertEquals(manager.entryCount(), newManager.entryCount());
        for (LogEntry entry : manager) {
            Assertions.assertEquals(entry.getTitle(), entry.getTitle());
            Assertions.assertEquals(entry.getComment(), entry.getComment());
            Assertions.assertEquals(entry.getDate(), entry.getDate());
            Assertions.assertEquals(entry.getDuration(), entry.getDuration());

        }
    }

    @Test
    public void testBadFile() {
        EntryManager manager = genValidManager();
        File file = new File(saveFile);
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write("Nonsense");
            writer.flush();
            writer.close();
        } catch (IOException e) {

        }
        Assertions.assertThrows(IllegalStateException.class, () -> EntrySaverJson.load(manager, saveFile));
    }

    @Test
    public void testBadArgs() {
        EntryManager manager = genValidManager();
        Assertions.assertThrows(IllegalArgumentException.class, () -> EntrySaverJson.load(manager, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> EntrySaverJson.load(null, saveFile));
        Assertions.assertThrows(IllegalArgumentException.class, () -> EntrySaverJson.save(manager, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> EntrySaverJson.save(null, saveFile));
    }



}
