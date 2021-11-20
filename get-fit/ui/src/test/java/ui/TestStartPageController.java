package ui;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.matching.UrlPattern;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.editStub;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

public class TestStartPageController extends ApplicationTest{

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

    @Test
    public void testSwitchPage(){
        Assertions.assertEquals("Get fit", this.stageRef.getTitle());
        click("Add workout");
        Assertions.assertEquals("Add new session", this.stageRef.getTitle());
        click("Return");
        Assertions.assertEquals("Get fit", this.stageRef.getTitle());
        click("Statistics");
        Assertions.assertEquals("Statistics", this.stageRef.getTitle());
        click("Return");
        Assertions.assertEquals("Get fit", this.stageRef.getTitle());
    }

    @Test
    public void testDelete() {

        UrlPattern externalUrl = urlPathMatching("/api/v1/entries/remove/0");
        stubFor(post(externalUrl));


        String body2 = "{\"entries\": [{\"id\": \"1\",\"title\": \"Example title2\",\"comment\": \"Example comment\",\"date\": \"2021-05.04\",\"feeling\": \"8\",\"duration\": \"5400\",\"distance\": \"10\",\"maxHeartRate\": \"150\",\"exerciseCategory\": \"Swimming\",\"exerciseSubCategory\": \"Short\"},{\"id\": \"2\",\"title\": \"Example title3\",\"comment\": \"Example comment\",\"date\": \"2021-06-06\",\"feeling\": \"4\",\"duration\": \"5400\",\"distance\": \"12\",\"maxHeartRate\": \"220\",\"exerciseCategory\": \"Cycling\",\"exerciseSubCategory\": \"Long\"},]}";

        editStub(get(urlEqualTo("/api/v1/entries/list?r=false&s=title&c=any"))
                .withId(id)
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body2)));

        click("Delete");
        mockServer.verify(2, getRequestedFor(urlEqualTo("/api/v1/entries/list?r=false&s=title&c=any"))); 

    }

    @Test
    public void testReverse(){
        String body2 = "{\"entries\": [{\"id\": \"2\",\"title\": \"Example title3\",\"comment\": \"Example comment\",\"date\": \"2021-06-06\",\"feeling\": \"4\",\"duration\": \"5400\",\"distance\": \"12\",\"maxHeartRate\": \"220\",\"exerciseCategory\": \"Cycling\",\"exerciseSubCategory\": \"Long\"},"+
            "{\"id\": \"1\",\"title\": \"Example title2\",\"comment\": \"Example comment\",\"date\": \"2021-05.04\",\"feeling\": \"8\",\"duration\": \"5400\",\"distance\": \"10\",\"maxHeartRate\": \"150\",\"exerciseCategory\": \"Swimming\",\"exerciseSubCategory\": \"Short\"},"+
            "{\"id\": \"0\",\"title\": \"Example title1\",\"comment\": \"Example comment\",\"date\": \"2021-09-01\",\"feeling\": \"6\",\"duration\": \"3600\",\"distance\": \"10\",\"maxHeartRate\": \"120\",\"exerciseCategory\": \"Running\",\"exerciseSubCategory\": \"short\"}]}";

        editStub(get(urlEqualTo("/api/v1/entries/list?r=true&s=title&c=any")).withId(id)
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body2)));
        
        clickOn("#reverseBox");
        mockServer.verify(1, getRequestedFor(urlEqualTo("/api/v1/entries/list?r=true&s=title&c=any"))); 

    }

    @Test
    public void testFiter(){

        String body = "{\"entries\": [{\"id\": \"0\",\"title\": \"Example title1\",\"comment\": \"Example comment\",\"date\": \"2021-09-01\",\"feeling\": \"6\",\"duration\": \"3600\",\"distance\": \"10\",\"maxHeartRate\": \"120\",\"exerciseCategory\": \"Running\",\"exerciseSubCategory\": \"short\"},"+
        "{\"id\": \"2\",\"title\": \"Example title3\",\"comment\": \"Example comment\",\"date\": \"2021-06-06\",\"feeling\": \"4\",\"duration\": \"5400\",\"distance\": \"12\",\"maxHeartRate\": \"220\",\"exerciseCategory\": \"Cycling\",\"exerciseSubCategory\": \"Long\"},"+
        "{\"id\": \"1\",\"title\": \"Example title2\",\"comment\": \"Example comment\",\"date\": \"2021-05.04\",\"feeling\": \"8\",\"duration\": \"5400\",\"distance\": \"10\",\"maxHeartRate\": \"150\",\"exerciseCategory\": \"Swimming\",\"exerciseSubCategory\": \"Short\"}]}";
   
        editStub(get(urlEqualTo("/api/v1/entries/list?r=false&s=date&c=any"))
        .withId(id)
        .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));

        clickOn("#sortConfig");
        click("Date");

        mockServer.verify(1, getRequestedFor(urlEqualTo("/api/v1/entries/list?r=false&s=date&c=any")));

    }

    @Test
    public void testSorting(){
        

        String body = "{\"entries\": [{\"id\": \"1\",\"title\": \"Example title2\",\"comment\": \"Example comment\",\"date\": \"2021-05.04\",\"feeling\": \"8\",\"duration\": \"5400\",\"distance\": \"10\",\"maxHeartRate\": \"150\",\"exerciseCategory\": \"Strength\",\"exerciseSubCategory\": \"Short\"},"+
                "{\"id\": \"2\",\"title\": \"Example title3\",\"comment\": \"Example comment\",\"date\": \"2021-06-06\",\"feeling\": \"4\",\"duration\": \"5400\",\"distance\": \"12\",\"maxHeartRate\": \"220\",\"exerciseCategory\": \"Running\",\"exerciseSubCategory\": \"Long\"},"+
                "{\"id\": \"0\",\"title\": \"Example title1\",\"comment\": \"Example comment\",\"date\": \"2021-09-01\",\"feeling\": \"6\",\"duration\": \"3600\",\"distance\": \"10\",\"maxHeartRate\": \"120\",\"exerciseCategory\": \"Running\",\"exerciseSubCategory\": \"Short\"}]}"; 
        
        editStub(get(urlEqualTo("/api/v1/entries/list?r=false&s=duration&c=any"))
        .withId(id)
        .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));

        clickOn("#sortConfig");
        click("Duration");

        String body2 = "{\"entries\": [{\"id\": \"0\",\"title\": \"Example title1\",\"comment\": \"Example comment\",\"date\": \"2021-09-01\",\"feeling\": \"6\",\"duration\": \"3600\",\"distance\": \"10\",\"maxHeartRate\": \"120\",\"exerciseCategory\": \"Running\",\"exerciseSubCategory\": \"short\"},"+
                "{\"id\": \"2\",\"title\": \"Example title3\",\"comment\": \"Example comment\",\"date\": \"2021-06-06\",\"feeling\": \"4\",\"duration\": \"5400\",\"distance\": \"12\",\"maxHeartRate\": \"220\",\"exerciseCategory\": \"Running\",\"exerciseSubCategory\": \"Long\"}]}";

        editStub(get(urlEqualTo("/api/v1/entries/list?r=false&s=duration&c=running&sc=any"))
        .withId(id)
        .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body2)));

        
        clickOn("#sortCategory");
        click("Running");

        mockServer.verify(1, getRequestedFor(urlEqualTo("/api/v1/entries/list?r=false&s=duration&c=running&sc=any")));

        String body3 = "{\"entries\": [{\"id\": \"0\",\"title\": \"Example title1\",\"comment\": \"Example comment\",\"date\": \"2021-09-01\",\"feeling\": \"6\",\"duration\": \"3600\",\"distance\": \"10\",\"maxHeartRate\": \"120\",\"exerciseCategory\": \"Running\",\"exerciseSubCategory\": \"short\"}]}";
        editStub(get(urlEqualTo("/api/v1/entries/list?r=false&s=duration&c=running&sc=short"))
        .withId(id)
        .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body3)));

        clickOn("#sortSubcategory");
        click("Short");

        mockServer.verify(1, getRequestedFor(urlEqualTo("/api/v1/entries/list?r=false&s=duration&c=running&sc=short")));

        String body4 = "{\"entries\": [{\"id\": \"1\",\"title\": \"Example title2\",\"comment\": \"Example comment\",\"date\": \"2021-05.04\",\"feeling\": \"8\",\"duration\": \"5400\",\"distance\": \"10\",\"maxHeartRate\": \"150\",\"exerciseCategory\": \"Strength\",\"exerciseSubCategory\": \"Short\"}]}";
       

        editStub(get(urlEqualTo("/api/v1/entries/list?r=false&s=duration&c=strength&sc=any"))
        .withId(id)
        .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body4)));
       
        clickOn("#sortCategory");
        click("Strength");

        mockServer.verify(1, getRequestedFor(urlEqualTo("/api/v1/entries/list?r=false&s=duration&c=strength&sc=any")));
    
    }

    @Test
    public void testShow() {
        click("Show");
        clickOn("#hideView");  
   }

   @AfterEach
    public void stopWireMockServer() {
        mockServer.stop();
    }

    
}
