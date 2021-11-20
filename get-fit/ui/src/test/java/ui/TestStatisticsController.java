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
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.UUID;



public class TestStatisticsController extends ApplicationTest{


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

    @Test 
    public void testStatisticsData(){
        click("Statistics");

        String body = "{\"empty\": \"False\",\"count\": \"5\",\"totalDuration\": \"20h:10min\",\"averageDuration\":\"2h:10min\",\"averageSpeed\": \"7.0\",\"averageFeeling\": \"3.3\", \"maximumHr\": \"180\" }";
        stubFor(get(urlEqualTo("/api/v1/entries/stats?d="+(LocalDate.now().minusYears(1)).toString()+"-"+LocalDate.now().toString()+"&c=RUNNING"))
        .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));


        String body2 = "{\"swimming\": \"2\",\"running\": \"3\",\"strength\": \"0\",\"cycling\": \"2\"}";
        stubFor(get(urlEqualTo("/api/v1/entries/chart?d="+(LocalDate.now().minusYears(1)).toString()+"-"+LocalDate.now().toString()))
        .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body2)));

        clickOn("#exerciseType");
        clickOn("Running");
        clickOn("#start");
        clickOn("#end");

        click("Enter");

        mockServer.verify(1, getRequestedFor((urlEqualTo("/api/v1/entries/stats?d="+(LocalDate.now().minusYears(1)).toString()+"-"+LocalDate.now().toString()+"&c=RUNNING")))); 
        mockServer.verify(2, getRequestedFor((urlEqualTo("/api/v1/entries/chart?d="+(LocalDate.now().minusYears(1)).toString()+"-"+LocalDate.now().toString())))); 
    }

    @Test
    public void testEmptyStatistics(){
        click("Statistics");

        String body3 = "{\"empty\": \"True\",\"count\": \"0\",\"totalDuration\": \"0h:0min\",\"averageDuration\":\"0h:0min\",\"averageSpeed\": \"0.0\",\"averageFeeling\": \"0.0\", \"maximumHr\": \"0.0\" }";
        stubFor(get(urlEqualTo("/api/v1/entries/stats?d="+(LocalDate.now().minusYears(1)).toString()+"-"+LocalDate.now().toString()))
        .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body3)));

        click("Enter");
        mockServer.verify(1, getRequestedFor((urlEqualTo("/api/v1/entries/stats?d="+(LocalDate.now().minusYears(1)).toString()+"-"+LocalDate.now().toString())))); 
        
        click("Return");

    }
    @AfterEach
    public void stopWireMockServer() {
        mockServer.stop();
    }
    
}
