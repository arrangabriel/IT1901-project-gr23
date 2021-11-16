package ui;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;


import core.LogEntry;
import core.LogEntry.EntryBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import localpersistence.EntrySaverJson;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class AppTest extends ApplicationTest {
    private Parent root;
    private Stage stageRef;

    private WireMockConfiguration mockConfig;
    private WireMockServer mockServer;

  
    public void startWireMockServer() throws URISyntaxException {
        mockConfig = WireMockConfiguration.wireMockConfig().port(8080);
        mockServer = new WireMockServer(mockConfig.portNumber());
        mockServer.start();
        WireMock.configureFor("localhost", mockConfig.portNumber());
        String body2 = "{\"entries\": [{\"id\": \"0\",\"title\": \"Example title\",\"comment\": \"Example comment\",\"date\": \"Example date\",\"feeling\": \"Example feeling\",\"duration\": \"Example duration\",\"distance\": \"Example distance\",\"maxHeartRate\": \"Example heart rate\",\"exerciseCategory\": \"Example category\",\"exerciseSubCategory\": \"Example subcategory\"},{\"id\": \"1\",\"title\": \"Example title\",\"comment\": \"Example comment\",\"date\": \"Example date\",\"feeling\": \"Example feeling\",\"duration\": \"Example duration\",\"distance\": \"Example distance\",\"maxHeartRate\": \"Example heart rate\",\"exerciseCategory\": \"Example category\",\"exerciseSubCategory\": \"Example subcategory\"},{\"id\": \"2\",\"title\": \"Example title\",\"comment\": \"Example comment\",\"date\": \"Example date\",\"feeling\": \"Example feeling\",\"duration\": \"Example duration\",\"distance\": \"Example distance\",\"maxHeartRate\": \"Example heart rate\",\"exerciseCategory\": \"Example category\",\"exerciseSubCategory\": \"Example subcategory\"},]}";

        stubFor(get(urlEqualTo("/api/v1/entries/list?r=true&s=date&c=running&sc=short&d=2021-01-01-2021-09-09"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body2)));
        String body = "{\"categories\": {\"strength\": [\"push\",\"pull\"],\"running\": [\"short\",\"highintensity\"],\"cycling\": [\"short\",\"highintensity\"]}}";
        stubFor(get(urlEqualTo("/api/v1/entries/filters"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));
       
                
    }

    /*private static void saveEntryManager() {
        try {
            EntrySaverJson.save(App.entryManager);
        } catch (IOException e) {
            Assertions.fail();
        }
    }

    private static void clearEntryManager() {
        for (Iterator<LogEntry> iterator = App.entryManager.iterator();
             iterator.hasNext(); ) {
            LogEntry entry = iterator.next();
            iterator.remove();
            App.entryManager.removeEntry(entry.getId());
        }
        saveEntryManager();
    }

    @AfterAll
    public static void teardown() {
        clearEntryManager();
    }*/

    // Due to some weirdness related to doing work in the background for testing,
    // the ui might not be up-to-date when starting a new test
    public void refresh() {
        click("Add workout");
        click("Return");
    }


    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        startWireMockServer();
        this.stageRef = stage;
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("StartPage.fxml"));
        Parent root = Loader.load();
        Scene s = new Scene(root);
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

    private void addEntryClicking(String title, String comment, String hour,
                                  String min, String type, String hr,
                                  String sub, String distance) {
        click("Add workout");
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

    /*@BeforeEach
    private void setUp() {
        clearEntryManager();
        LogEntry entry = new EntryBuilder("Test", LocalDate.now().minusDays(1),
                Duration.ofSeconds(3600), LogEntry.EXERCISECATEGORY.STRENGTH, 5)
                .comment("Generated test")
                .build();

        LogEntry entry2 =
                new EntryBuilder("Second Test", LocalDate.now().minusDays(1),
                        Duration.ofSeconds(3600),
                        LogEntry.EXERCISECATEGORY.STRENGTH, 5)
                        .comment("Another generated test")
                        .build();

        App.entryManager.addEntry(entry);
        App.entryManager.addEntry(entry2);
        saveEntryManager();
        refresh();
    }*/

    /*@Test
    public void testCreateButton() {
        int oldCount = App.entryManager.entryCount();

        click("Add workout");
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

        Assertions.assertEquals(oldCount + 1, App.entryManager.entryCount());

    }*/

    private ListView<VBox> getEntriesView() {
        return (ListView<VBox>) root.lookup("#listOfEntries");
    }

    /*public void checkView() {
        try {
            updateRoot();
        } catch (IOException e) {
            Assertions.fail();
        }
        List<VBox> viewEntries = getEntriesView().getItems();
        for (VBox box : viewEntries) {
            Assertions.assertEquals(
                    App.entryManager.getEntry(box.getId()).getTitle(),
                    ((Text) ((Pane) box.getChildren().get(0)).getChildren()
                            .get(0)).getText());
        }
        Assertions.assertEquals(viewEntries.size(),
                App.entryManager.entryCount());
    }*/

    @Test
    public void varietyCreation() {
        addEntryClicking("Push", "I did bench presses as well as some push-ups",
                "1", "45", "STRENGTH", "90", "PUSH", null);
        addEntryClicking("Cardio", "I ran a while",
                "3", "30", "RUNNING", "200", "LONG", "6");
        addEntryClicking("Swimming", "Did a couple of laps",
                "1", "00", "SWIMMING", "220", "HIGHINTENSITY", "10");


        //checkView();
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

    /*@Test
    public void testDelete() {
        int numberOfEntries = App.entryManager.entryCount();
        click("Delete");
        Assertions.assertEquals(numberOfEntries - 1,
                App.entryManager.entryCount());
        checkView();
    }*/

    @Test
    public void testShow() {
        try{
            updateRoot();
        }
        catch (IOException e) {
                Assertions.fail();
        }

        AnchorPane entryView = (AnchorPane) root.lookup("#entryView");
        Assertions.assertFalse(entryView.isVisible());
        click("Show");
        entryView = (AnchorPane) root.lookup("#entryView");
        Assertions.assertTrue(entryView.isVisible());
   }
 
}