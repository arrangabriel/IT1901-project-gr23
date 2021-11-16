package client;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.LogClient.ListBuilder;

public class TestClient {

    private WireMockConfiguration mockConfig;
    private WireMockServer mockServer;

    private LogClient logClient;

    @BeforeEach
    public void startWireMockServer() throws URISyntaxException {
        mockConfig = WireMockConfiguration.wireMockConfig().port(8080);
        mockServer = new WireMockServer(mockConfig.portNumber());
        mockServer.start();
        WireMock.configureFor("localhost", mockConfig.portNumber());
        logClient = new LogClient("http://localhost", mockServer.port());
    }

    @Test
    public void testLogClient() {

        String body = "{\"title\": \"Example title\",\"comment\": \"Example comment\",\"date\": \"2021-10-25\",\"feeling\": \"7\",\"duration\": \"3600\",\"distance\": \"3\",\"maxHeartRate\": \"150\",\"exerciseCategory\": \"STRENGTH\",\"exerciseSubCategory\": \"PULL\"}";
        stubFor(get(urlEqualTo("/api/v1/entries/0"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));
        try {
            HashMap<String, String> entry = logClient.getLogEntry("0");
            assertEquals("Example title", entry.get("title"));
        } catch (URISyntaxException | InterruptedException | ExecutionException | ServerResponseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testAddLogEntry() {

        HashMap<String, String> entryHash = new HashMap<>();

        entryHash.put("title", "Example title");
        entryHash.put("comment", "Example comment");
        entryHash.put("date", "2021-10-25");
        entryHash.put("feeling", "7");
        entryHash.put("duration", "3600");
        entryHash.put("distance", "3");
        entryHash.put("maxHeartRate", "150");
        entryHash.put("exerciseCategory", "STRENGTH");
        entryHash.put("exerciseSubCategory", "PULL");

        String body = "{\"id\": \"0\"}";
        stubFor(post(urlEqualTo("/api/v1/entries/add"))
                /*
                 * .withRequestBody(containing("title=Example title"))
                 * .withRequestBody(containing("comment=Example comment"))
                 * .withRequestBody(containing("date=2021-10-25"))
                 * .withRequestBody(containing("feeling=7"))
                 * .withRequestBody(containing("duration=3600"))
                 * .withRequestBody(containing("distance=3"))
                 * .withRequestBody(containing("maxHeartRate=150"))
                 * .withRequestBody(containing("exerciseCategory=STRENGTH"))
                 * .withRequestBody(containing("exerciseSubCategory=PULL"))
                 */
                // \"title\": \"Example title\",\"comment\": \"Example comment\",\"date\":
                // \"2021-10-25\",\"feeling\": \"7\",\"duration\": \"3600\",\"distance\":
                // \"3\",\"maxHeartRate\": \"150\",\"exerciseCategory\":
                // \"STRENGTH\",\"exerciseSubCategory\": \"PULL\"}
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));
        try {
            logClient.addLogEntry(entryHash);
        } catch (URISyntaxException | InterruptedException | ExecutionException | ServerResponseException e) {
            e.printStackTrace();
        }

    }

    // @Test
    // public void testUpdateLogEntry() {

    // }

    @Test
    public void testGetExerciseCategory() {
        String body = "{\"categories\": {\"strength\": [\"push\",\"pull\"],\"running\": [\"short\",\"highintensity\"],\"cycling\": [\"short\",\"highintensity\"]}}";

        stubFor(get(urlEqualTo("/api/v1/entries/filters"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));
        try {
            logClient.getExerciseCategories();
        } catch (URISyntaxException | InterruptedException | ExecutionException | ServerResponseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDeleteLogEntry() {
        stubFor(post(urlEqualTo("/api/v1/entries/remove/0"))
                /* .withRequestBody(containing("id=0")) */
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json")));
        try {
            logClient.deleteLogEntry("0");
        } catch (URISyntaxException | InterruptedException | ExecutionException | ServerResponseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testListBuilder() {
        ListBuilder listBuilder = new ListBuilder().reverse().sort("date").category("running").subCategory("short")
                .date("2021-01-01-2021-09-09");

        String body = "{\"entries\": [{\"id\": \"0\",\"title\": \"Example title\",\"comment\": \"Example comment\",\"date\": \"Example date\",\"feeling\": \"Example feeling\",\"duration\": \"Example duration\",\"distance\": \"Example distance\",\"maxHeartRate\": \"Example heart rate\",\"exerciseCategory\": \"Example category\",\"exerciseSubCategory\": \"Example subcategory\"},{\"id\": \"1\",\"title\": \"Example title\",\"comment\": \"Example comment\",\"date\": \"Example date\",\"feeling\": \"Example feeling\",\"duration\": \"Example duration\",\"distance\": \"Example distance\",\"maxHeartRate\": \"Example heart rate\",\"exerciseCategory\": \"Example category\",\"exerciseSubCategory\": \"Example subcategory\"},{\"id\": \"2\",\"title\": \"Example title\",\"comment\": \"Example comment\",\"date\": \"Example date\",\"feeling\": \"Example feeling\",\"duration\": \"Example duration\",\"distance\": \"Example distance\",\"maxHeartRate\": \"Example heart rate\",\"exerciseCategory\": \"Example category\",\"exerciseSubCategory\": \"Example subcategory\"},]}";

        stubFor(get(urlEqualTo("/api/v1/entries/list?r=true&s=date&c=running&sc=short&d=2021-01-01-2021-09-09"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "aplication/json").withBody(body)));

        try {
            logClient.getLogEntryList(listBuilder);
        } catch (URISyntaxException | InterruptedException | ExecutionException | ServerResponseException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void stopWireMockServer() {
        mockServer.stop();
    }

}
