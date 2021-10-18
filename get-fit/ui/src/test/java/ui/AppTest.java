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

import org.junit.AfterClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;


public class AppTest extends ApplicationTest {
    @FXML
    ListView<String> listOfEntries;
    private static Scene scene;
    //private StartPageController controller;
    private StartPageController controller;
    private Parent root;
    private Stage stageRef;

    @Override
    public void start(Stage stage) throws IOException {
        this.stageRef = stage;
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("StartPage.fxml"));
        Parent root = Loader.load();
        Scene  s = new Scene(root);
        stage.setTitle("Get fit");
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

    private static void saveEntryManager() {
        try {
            EntrySaverJson.save(App.entryManager);
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    private static void clearEntryManager() {
        for (Iterator<LogEntry> iterator = App.entryManager.iterator(); iterator.hasNext();) {
            iterator.next();
            iterator.remove();
        }
        saveEntryManager();
    }

    private void addEntryClicking(String title, String comment, String hour, String min, String type, String hr, String sub, String distance) {
        click("Add session");
        clickOn("#titleField");
        write(title);
        clickOn("#hour");
        write(hour);
        clickOn("#min");
        write(min);
        clickOn("#exerciseType");
        clickOn(type);
        clickOn("#tags");
        clickOn(sub);
        clickOn("#heartRate");
        write(hr);
        if (distance != null) {
            clickOn("#distance");
            write(distance);
        }
        clickOn("#commentField");
        write(comment);
        click("Create session");
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

    public void checkView() {
        try {
            updateRoot();
        } catch (IOException e) {
            Assertions.fail();
        }
        List<String> viewEntries = getEntriesView().getItems();
        for(int i = 0; i < viewEntries.size(); i++){
            Assertions.assertEquals(App.entryManager.getEntry(String.valueOf(i)).getTitle(), viewEntries.get(i));
        }
    }

    @Test
    public void varietyCreation() {
        addEntryClicking("Push", "I did bench presses as well as some pushups",
        "1", "45", "STRENGTH", "90","PUSH", null);
        addEntryClicking("Cardio", "I ran a while",
        "3", "30", "RUNNING", "200","LONG", "6");
        addEntryClicking("Swimming", "Did a couple of laps",
        "1", "00", "SWIMMING", "220","HIGHINTENSITY", "10");

        
        checkView();
    }

    @Test
    public void goBack() {
        Assertions.assertEquals("Get fit", this.stageRef.getTitle());
        click("Add workout");
        Assertions.assertEquals("Add new session", this.stageRef.getTitle());
        click("Return");
        Assertions.assertEquals("Get fit", this.stageRef.getTitle());
        click("Add workout");
        Assertions.assertEquals("Add new session", this.stageRef.getTitle());
        clickOn("#titleField");
        write("New Test");
        clickOn("#hour");
        write("0");
        clickOn("#min");
        write("30");
        clickOn("#exerciseType");
        clickOn("#heartRate");
        write("60");
        click("Create session");
        Assertions.assertEquals("Get fit", this.stageRef.getTitle());
    }

    @AfterClass
    public static void teardown() {
        clearEntryManager();
    }


     
}