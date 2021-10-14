package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import localpersistence.EntrySaverJson;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

import org.testfx.matcher.control.LabeledMatchers;

import core.LogEntry;
import core.EntryManager;
import core.LogEntry.EntryBuilder;

import java.util.Iterator;
import java.util.List;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;


public class AppTest extends ApplicationTest {
    @FXML
    ListView<String> listOfEntries;
    private static Scene scene;
    //private StartPageController controller;
    private StartPageController controller;
    private Parent root;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("StartPage.fxml"));
        Parent root = Loader.load();
        Scene  s = new Scene(root);
        stage.setScene(s);
        stage.show();
    }

    private void click(String... labels) {
        for (var label : labels) {
            clickOn(LabeledMatchers.hasText(label));
        }
    }

    private void updateRoot() throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("StartPage.fxml"));
        this.root = Loader.load();
    }

    private void saveEntryManager() {
        try {
            EntrySaverJson.save(App.entryManager);
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    private void clearEntryManager() {
        for (Iterator<LogEntry> iterator = App.entryManager.iterator(); iterator.hasNext();) {
            iterator.next();
            iterator.remove();
        }
        saveEntryManager();
    }

    @BeforeEach
    private void setUp() {
        clearEntryManager();
        LogEntry entry = new EntryBuilder("Test", LocalDate.now().minusDays(1), Duration.ofSeconds(3600), LogEntry.EXERCISECATEGORY.STRENGTH, 5)
            .comment("Generated test")
            .build();

        LogEntry entry2 = new EntryBuilder("Second Test", LocalDate.now().minusDays(1), Duration.ofSeconds(3600), LogEntry.EXERCISECATEGORY.STRENGTH, 5)
            .comment("Another generated test")
            .build();

        App.entryManager.addEntry(entry);
        App.entryManager.addEntry(entry2);
        saveEntryManager();
    }

    @Test
    public void testCreateButton(){
        int oldCount = App.entryManager.entryCount();

        click("Add session");
        clickOn("#titleField");
        write("New new Session");
        clickOn("#sessionDatePicker");
        clickOn("#hour");
        write("1");
        clickOn("#min");
        write("30");
        clickOn("#exerciseType");

        clickOn("#heartRate");
        write("150");
        clickOn("#commentField");
        write("New comment");


        click("Create session");

        Assertions.assertEquals(oldCount+1, App.entryManager.entryCount());

    }


    private ListView<String> getEntriesView() {
        return (ListView<String>) root.lookup("#listOfEntries");
    }

    @Test
    public void checkView() throws IOException {
        updateRoot();
        List<String> viewEntries = getEntriesView().getItems();
        for(int i = 0; i < viewEntries.size(); i++){
            Assertions.assertEquals(App.entryManager.getEntry(String.valueOf(i)).getTitle(), viewEntries.get(i));
        }


    }


     
}