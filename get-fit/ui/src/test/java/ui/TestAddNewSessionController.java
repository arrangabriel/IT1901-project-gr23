package ui;

import org.testfx.framework.junit5.ApplicationTest;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;



import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.matcher.control.LabeledMatchers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;


public class TestAddNewSessionController extends ApplicationTest{
    private Parent root;
    private Stage stageRef;

    private WireMockConfiguration mockConfig;
    private UUID id = UUID.randomUUID();
    private WireMockServer mockServer;

  
    public void startWireMockServer() throws URISyntaxException {
        mockConfig = WireMockConfiguration.wireMockConfig().port(8080);
        mockServer = new WireMockServer(mockConfig.portNumber());
        mockServer.start();
        WireMock.configureFor("localhost", mockConfig.portNumber());
        String body2 = "{\"entries\": [{\"id\": \"0\",\"title\": \"Example title1\",\"comment\": \"Example comment\",\"date\": \"2021-09-01\",\"feeling\": \"6\",\"duration\": \"3600\",\"distance\": \"10\",\"maxHeartRate\": \"120\",\"exerciseCategory\": \"Running\",\"exerciseSubCategory\": \"short\"},{\"id\": \"1\",\"title\": \"Example title2\",\"comment\": \"Example comment\",\"date\": \"2021-05.04\",\"feeling\": \"8\",\"duration\": \"5400\",\"distance\": \"10\",\"maxHeartRate\": \"150\",\"exerciseCategory\": \"Swimming\",\"exerciseSubCategory\": \"Short\"},{\"id\": \"2\",\"title\": \"Example title3\",\"comment\": \"Example comment\",\"date\": \"2021-06-06\",\"feeling\": \"4\",\"duration\": \"5400\",\"distance\": \"12\",\"maxHeartRate\": \"220\",\"exerciseCategory\": \"Cycling\",\"exerciseSubCategory\": \"Long\"}]}";

        stubFor(get(urlEqualTo("/api/v1/entries/list?r=false&s=title&c=any")).withId(id)
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body2)));
        String body = "{\"categories\": {\"strength\": [\"Push\",\"pull\",\"fullbody\",\"legs\"],\"running\": [\"short\",\"highintensity\",\"long\",\"lowintensity\"],\"cycling\": [\"short\",\"highintensity\",\"long\",\"lowintensity\"],\"swimming\": [\"short\",\"highintensity\",\"long\",\"lowintensity\"]}}";
        stubFor(get(urlEqualTo("/api/v1/entries/filters"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));
                 
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
    @Test 
    public void startApp() throws IOException{
        updateRoot();
        assertNotNull(root);
        
    }


      private void addEntryClicking(String title, String comment, String hour,
                                  String min, String type, String hr,
                                  String sub, String distance) throws IOException {
        updateRoot();                  
        clickOn("#addSession");
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

        clickOn("#addSession");
        clickOn("#titleField");
        write("New new Session");
        clickOn("#sessionDatePicker");
        clickOn("#hour");
        write("1");
        clickOn("#min");
        write("30");
        clickOn("#exerciseType");
        clickOn("Running");
        clickOn("#heartRate");
        write("1");
        clickOn("#commentField");
        write("New comment");
        click("Create session");
        clickOn("#heartRate");
        write("100");


        String body = "{\"id\":\"0\"}";
        stubFor(post(urlEqualTo("/api/v1/entries/add"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));

        click("Create session");

        mockServer.verify(1, postRequestedFor(urlEqualTo("/api/v1/entries/add")));
    }

    @Test
    public void strengthCreation() throws IOException {

        String body = "{\"id\":\"0\"}";
        stubFor(post(urlEqualTo("/api/v1/entries/add"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));


        addEntryClicking("Gainzz", "I did bench presses as well as some push-ups",
        "1", "45", "Strength", "90", "Push", null);
        
        mockServer.verify(1, postRequestedFor(urlEqualTo("/api/v1/entries/add")));
    }

    @Test
    public void cardioCreation() throws IOException {

        String body = "{\"id\":\"0\"}";
        stubFor(post(urlEqualTo("/api/v1/entries/add"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));

        addEntryClicking("Cardio", "I ran a while",
        "3", "30", "Running", "200", "Long", "6");
        
        mockServer.verify(1, postRequestedFor(urlEqualTo("/api/v1/entries/add")));
    }

    @Test
    public void swimingCreation() throws IOException {

        String body = "{\"id\":\"0\"}";
        stubFor(post(urlEqualTo("/api/v1/entries/add"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));

        addEntryClicking("Laps", "Did a couple of laps",
        "1", "00", "Swimming", "220", "Highintensity", "10");

        mockServer.verify(1, postRequestedFor(urlEqualTo("/api/v1/entries/add")));
    }

    

    @Test
    public void testReturn() {
        click("Add workout");
        Assertions.assertEquals("Add new session", this.stageRef.getTitle());
        clickOn("#titleField");
        write("New Test");
        clickOn("#hour");
        write("0");
        clickOn("#min");
        write("30");
        clickOn("#exerciseType");
        clickOn("Running");
        clickOn("#heartRate");
        write("60");
        clickOn("#distance");
        write("20");

        String body = "{\"id\":\"0\"}";
        stubFor(post(urlEqualTo("/api/v1/entries/add"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));

        click("Create session");
        mockServer.verify(1, postRequestedFor(urlEqualTo("/api/v1/entries/add")));
        
    }

    @Test
    public void testValidation(){
        click("Add workout");
        Assertions.assertEquals("Add new session", this.stageRef.getTitle());
        click("Create session");
        clickOn("#titleField");
        write("New Test");
        click("Create session");
        clickOn("#hour");
        write("0");
        clickOn("#min");
        write("30");
        clickOn("#exerciseType");
        clickOn("Running");
        clickOn("#heartRate");
        write("60");

      
        String body = "{\"id\":\"0\"}";
        stubFor(post(urlEqualTo("/api/v1/entries/add"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));

        click("Create session");
        mockServer.verify(1, postRequestedFor(urlEqualTo("/api/v1/entries/add")));

       

    }

    @AfterEach
    public void stopWireMockServer() {
        mockServer.stop();
    }
    
}
