package localpersistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import core.EntryManager;
import core.LogEntry;
import core.LogEntry.EntryBuilder;

public class TestEntrySaverJson {

    static int minute = 60;
    static int hour = minute * 60;

    private static final String saveFile = "SavedTestData.json";

    private EntryBuilder genValidBuilder(String title, String comment) {
        LocalDate date = LocalDate.now().minusDays(1);
        Duration duration = Duration.ofSeconds(hour);
        int feeling = 1;
        double distance = 1;
        Integer maxHeartRate = 80;

        LogEntry.EXERCISECATEGORY exerciseCategory = LogEntry.EXERCISECATEGORY.STRENGTH;
        LogEntry.Subcategory subcategory = LogEntry.STRENGTHSUBCATEGORIES.PULL;

        EntryBuilder builder = new EntryBuilder(title, date, duration, exerciseCategory, feeling).comment(comment)
                .exerciseSubCategory(subcategory).distance(distance).maxHeartRate(maxHeartRate);
        return builder;
    }

    private EntryManager genValidManager() {
        EntryManager manager = new EntryManager();

        manager.addEntry("0", genValidBuilder("Test1", "comment").build());
        manager.addEntry("1", genValidBuilder("Test2", "comment2").build());
        manager.addEntry("2", genValidBuilder("Test3", "comment3").build());
        manager.addEntry("3", genValidBuilder("Test4", "comment4").build());
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
        } catch (IOException e) {
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
    public void testAddAfterSaveAndLoad() {
        EntryManager manager = genValidManager();
        int len = manager.entryCount();
        try {
            EntrySaverJson.save(manager, saveFile);
        } catch (IOException e) {
            Assertions.fail();
        }
        EntryManager newManager = new EntryManager();
        Assertions.assertEquals(0, newManager.entryCount());
        try {
            EntrySaverJson.load(newManager, saveFile);
        } catch (IOException e) {
            Assertions.fail();
        }
        Assertions.assertEquals(len, newManager.entryCount());
        newManager.addEntry(genValidBuilder("New Title", "New Comment").build());
        Assertions.assertEquals(len + 1, newManager.entryCount());
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

    @Test
    public void testAbsolutePathSaving() {
        EntryManager manager = genValidManager();
        try {
            EntrySaverJson.save(manager);
        } catch (IllegalArgumentException | IOException e) {
            Assertions.fail("Failed to save data");
        }
        try {
            EntrySaverJson.load(new EntryManager());
        } catch (IllegalArgumentException | IOException e) {
            Assertions.fail("Failed to load data");
        }
        File f = new File(EntrySaverJson.SYSTEM_SAVE_LOCATION);
        f.delete();
    }

    @AfterAll
    public static void teardown() {
        File f = new File(saveFile);
        f.delete();
    }

}
