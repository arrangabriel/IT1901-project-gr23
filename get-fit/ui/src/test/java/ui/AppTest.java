package ui;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.matching.UrlPattern;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
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
import org.junit.jupiter.api.AfterEach;
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

        stubFor(get(urlEqualTo("/api/v1/entries/list?r=false&s=title&c=any"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body2)));
        String body = "{\"categories\": {\"strength\": [\"Push\",\"pull\",\"fullbody\",\"legs\"],\"running\": [\"short\",\"highintensity\",\"long\",\"lowintensity\"],\"cycling\": [\"short\",\"highintensity\",\"long\",\"lowintensity\"],\"swimming\": [\"short\",\"highintensity\",\"long\",\"lowintensity\"]}}";
        stubFor(get(urlEqualTo("/api/v1/entries/filters"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));
                 
    }


    // Due to some weirdness related to doing work in the background for testing,
    // the ui might not be up-to-date when starting a new test
    /*public void refresh() {
        click("Add workout");
        click("Return");
    }*/


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

    @BeforeEach
    public void timeOut(){
        sleep(1000);
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

    @Test 
    public void startApp() throws IOException{
        updateRoot();
        assertNotNull(root);
        
    }

    private void addEntryClicking(String title, String comment, String hour,
                                  String min, String type, String hr,
                                  String sub, String distance) throws IOException {
        updateRoot();                              
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


    @Test
    public void testCreateButton() throws IOException {
        updateRoot();

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
        String body3 = "{\"id\": \"0\"}";

        UrlPattern externalUrl = urlPathMatching("/api/v1/entries/add");
        stubFor(post(externalUrl));

        click("Create session");

        mockServer.verify(1, postRequestedFor(externalUrl)
        );
  
 

    }

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
    public void varietyCreation() throws IOException {

        String body3 = "{\"id\": \"0\"}";
        UrlPattern externalUrl = urlPathMatching("/api/v1/entries/add");
        stubFor(post(externalUrl).willReturn(aResponse().withBody(body3).withStatus(200)));


        addEntryClicking("Gainzz", "I did bench presses as well as some push-ups",
        "1", "45", "Strength", "90", "Push", null);
        addEntryClicking("Cardio", "I ran a while",
        "3", "30", "Running", "200", "Long", "6");
        addEntryClicking("Laps", "Did a couple of laps",
        "1", "00", "Swimming", "220", "Highintensity", "10");
        
        mockServer.verify(3, postRequestedFor(externalUrl));

        //checkView();
    }

    @Test
    public void goBack() {
        Assertions.assertEquals("Get fit", this.stageRef.getTitle());
        click("Add workout");
        Assertions.assertEquals("Add new session", this.stageRef.getTitle());
        click("Return");
        Assertions.assertEquals("Get fit", this.stageRef.getTitle());
        click("Statistics");
        Assertions.assertEquals("Statistics", this.stageRef.getTitle());
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

        String body = "{\"id\": \"0\"}";
        UrlPattern externalUrl = urlPathMatching("/api/v1/entries/add");
        stubFor(post(externalUrl).willReturn(aResponse().withBody(body).withStatus(200)));

        click("Create session");
        mockServer.verify(1, postRequestedFor(externalUrl));
        
    }

    @Test
    public void testDelete() {

        UrlPattern externalUrl = urlPathMatching("/api/v1/entries/remove/0");
        stubFor(post(externalUrl));

        click("Delete");
        mockServer.verify(1, postRequestedFor(externalUrl)); 
    }

    /*@Test
    public void testFiters(){
        UrlPattern externalUrl;
        externalUrl = urlPathMatching("/api/v1/entries/list?r=false&s=date&c=any");
        stubFor(post(externalUrl));
        clickOn("#sortConfig");
        click("date");
        mockServer.verify(1, postRequestedFor(externalUrl));

        externalUrl = urlPathMatching("/api/v1/entries/list?r=false&s=title&c=any");
        stubFor(post(externalUrl));
        clickOn("#sortConfig");
        click("title");
        mockServer.verify(1, postRequestedFor(externalUrl));

        externalUrl = urlPathMatching("/api/v1/entries/list?r=false&s=duration&c=any");
        stubFor(post(externalUrl));
        clickOn("#sortConfig");
        click("duration");
        mockServer.verify(1, postRequestedFor(externalUrl));


    }*/

    /*@Test
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
   }*/
   

   @AfterEach
    public void stopWireMockServer() {
        mockServer.stop();
    }
 
}