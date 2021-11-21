package client;

import client.LogClient.SortArgWrapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestClient {

    private WireMockServer mockServer;

    private LogClient logClient;

    @BeforeEach
    public void startWireMockServer() {
        WireMockConfiguration mockConfig =
                WireMockConfiguration.wireMockConfig().port(8080);
        mockServer = new WireMockServer(mockConfig.portNumber());
        mockServer.start();
        WireMock.configureFor("localhost", mockConfig.portNumber());
        logClient = new LogClient("http://localhost", mockServer.port());
    }

    @Test
    public void testLogClient() {

        String body =
                "{\"title\": \"Example title\",\"comment\": \"Example comment\",\"date\": \"2021-10-25\",\"feeling\": \"7\",\"duration\": \"3600\",\"distance\": \"3\",\"maxHeartRate\": \"150\",\"exerciseCategory\": \"STRENGTH\",\"exerciseSubCategory\": \"PULL\"}";
        stubFor(get(urlEqualTo("/api/v1/entries/0"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));
        try {
            HashMap<String, String> entry = logClient.getLogEntry("0");
            assertEquals("Example title", entry.get("title"));
            assertEquals("Example comment", entry.get("comment"));
            assertEquals("2021-10-25", entry.get("date"));
            assertEquals("7", entry.get("feeling"));
            assertEquals("3600", entry.get("duration"));
            assertEquals("3", entry.get("distance"));
            assertEquals("150", entry.get("maxHeartRate"));
            assertEquals("STRENGTH", entry.get("exerciseCategory"));
            assertEquals("PULL", entry.get("exerciseSubCategory"));

        } catch (URISyntaxException | InterruptedException | ExecutionException | ServerResponseException e) {
            e.printStackTrace();
            fail();
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
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));
        try {
            logClient.addLogEntry(entryHash);
        } catch (URISyntaxException | InterruptedException | ExecutionException | ServerResponseException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetExerciseCategory() {
        String body =
                "{\"categories\": {\"strength\": [\"push\",\"pull\"],\"running\": [\"short\",\"highintensity\"],\"cycling\": [\"short\",\"highintensity\"]}}";

        stubFor(get(urlEqualTo("/api/v1/entries/filters"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));
        try {
            logClient.getExerciseCategories();
        } catch (URISyntaxException | InterruptedException | ExecutionException | ServerResponseException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testGetStatistics() {
        String body =
                "{\"empty\": \"False\",\"count\": \"1\",\"totalDuration\": \"2.0\",\"averageDuration\": \"2.0\",\"averageSpeed\": \"7.0\",\"averageFeeling\": \"4.0\",\"maximumHr\": \"180\"}";
        stubFor(get(urlEqualTo("/api/v1/entries/stats?d=" +
                (LocalDate.now().minusYears(1)) + "-" +
                LocalDate.now()))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));

        SortArgWrapper builder = new SortArgWrapper();
        builder.date(LocalDate.now().plusYears(-1) + "-" +
                LocalDate.now());

        try {
            HashMap<String, String> entry = logClient.getStatistics(builder);
            assertEquals("False", entry.get("empty"));
            assertEquals("1", entry.get("count"));
            assertEquals("2.0", entry.get("totalDuration"));
            assertEquals("2.0", entry.get("averageDuration"));
            assertEquals("7.0", entry.get("averageSpeed"));
            assertEquals("4.0", entry.get("averageFeeling"));
            assertEquals("180", entry.get("maximumHr"));

        } catch (URISyntaxException | InterruptedException | ExecutionException | ServerResponseException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetStatisticsChart() {
        String body =
                "{\"swimming\": \"2\",\"running\": \"3\",\"strength\": \"0\",\"cycling\": \"2\"}";
        stubFor(get(urlEqualTo("/api/v1/entries/chart?d=" +
                (LocalDate.now().minusYears(1)) + "-" +
                LocalDate.now()))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));

        SortArgWrapper builder = new SortArgWrapper();
        builder.date(LocalDate.now().plusYears(-1) + "-" +
                LocalDate.now());

        try {
            HashMap<String, String> entry = logClient.getChartData(builder);
            assertEquals("2", entry.get("swimming"));
            assertEquals("3", entry.get("running"));
            assertEquals("0", entry.get("strength"));
            assertEquals("2", entry.get("cycling"));


        } catch (URISyntaxException | InterruptedException |
                ExecutionException | ServerResponseException e) {
            e.printStackTrace();
            fail();

        }

    }


    @Test
    public void testDeleteLogEntry() {
        stubFor(post(urlEqualTo("/api/v1/entries/remove/0"))
                /* .withRequestBody(containing("id=0")) */
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")));
        try {
            logClient.deleteLogEntry("0");
        } catch (URISyntaxException | InterruptedException | ExecutionException | ServerResponseException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testListBuilder() {
        SortArgWrapper sortArgWrapper =
                new SortArgWrapper().reverse().sort("date").category("running")
                        .subCategory("short")
                        .date("2021-01-01-2021-09-09");

        String body =
                "{\"entries\": [{\"id\": \"0\",\"title\": \"Example title\",\"comment\": \"Example comment\",\"date\": \"Example date\",\"feeling\": \"Example feeling\",\"duration\": \"Example duration\",\"distance\": \"Example distance\",\"maxHeartRate\": \"Example heart rate\",\"exerciseCategory\": \"Example category\",\"exerciseSubCategory\": \"Example subcategory\"},{\"id\": \"1\",\"title\": \"Example title\",\"comment\": \"Example comment\",\"date\": \"Example date\",\"feeling\": \"Example feeling\",\"duration\": \"Example duration\",\"distance\": \"Example distance\",\"maxHeartRate\": \"Example heart rate\",\"exerciseCategory\": \"Example category\",\"exerciseSubCategory\": \"Example subcategory\"},{\"id\": \"2\",\"title\": \"Example title\",\"comment\": \"Example comment\",\"date\": \"Example date\",\"feeling\": \"Example feeling\",\"duration\": \"Example duration\",\"distance\": \"Example distance\",\"maxHeartRate\": \"Example heart rate\",\"exerciseCategory\": \"Example category\",\"exerciseSubCategory\": \"Example subcategory\"},]}";

        stubFor(get(urlEqualTo(
                "/api/v1/entries/list?r=true&s=date&c=running&sc=short&d=2021-01-01-2021-09-09"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));

        try {
            logClient.getLogEntryList(sortArgWrapper);
        } catch (URISyntaxException | InterruptedException | ExecutionException | ServerResponseException e) {
            e.printStackTrace();
            fail();
        }
    }

    @AfterEach
    public void stopWireMockServer() {
        mockServer.stop();
    }

}
