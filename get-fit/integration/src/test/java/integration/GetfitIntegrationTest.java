package integration;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testfx.framework.junit5.ApplicationTest;

import client.LogClient;
import client.ServerResponseException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import restserver.GetfitController;
import restserver.GetfitApplication;
import restserver.GetfitService;

@SpringBootTest(classes = GetfitController.class)
public class GetfitIntegrationTest {

    private Parent root;
    private Stage stageRef;
    

    @Autowired
    public GetfitController controller;

    private LogClient logClient;

    @BeforeEach
    public void startClient() throws InterruptedException {
        this.logClient = new LogClient("http://localhost", 8080);
    }

    @Test
    public void testCompilation() {
        Assertions.assertNotNull(controller);
    }

    @Test
    public void testEntryCreation() {
        HashMap<String, String> entry = new HashMap<>();

        entry.put("title", "Example title");
        entry.put("content", "Example content");
        entry.put("date", "2020-01-01");
        entry.put("feeling", "7");
        entry.put("duration", "3600");
        entry.put("distance", "3");
        entry.put("maxHeartRate", "150");
        entry.put("exerciseCategory", "STRENGTH");
        entry.put("exerciseSubCategory", "PULL");

        try {
            logClient.addLogEntry(entry);
        } catch (Exception e) {
            Assertions.fail("Could not create entry");
        }
    }

    private String createEntry(
            String title, 
            String comment,
            String date,
            String feeling,
            String duration,
            String distance,
            String maxHeartRate,
            String exerciseCategory,
            String exerciseSubCategory
        ) {

        HashMap<String, String> entry = new HashMap<>();

        entry.put("title", title);
        entry.put("content", comment);
        entry.put("date", date);
        entry.put("feeling", feeling);
        entry.put("duration", duration);
        entry.put("distance", distance);
        entry.put("maxHeartRate", maxHeartRate);
        entry.put("exerciseCategory", exerciseCategory);
        entry.put("exerciseSubCategory", exerciseSubCategory);

        try {
            return logClient.addLogEntry(entry);
        } catch (Exception e) {
            Assertions.fail();
            return null;
        }
    }

    @Test
    private void testEntryDeletion() {
        String id = createEntry(
                "Example title", 
                "Example content",
                "2020-01-01",
                "7",
                "3600",
                "3",
                "150",
                "STRENGTH",
                "PULL"
        );
        try {
            this.logClient.getLogEntry(id);
        } catch (Exception e) {
            Assertions.fail();
        }
        try {
            this.logClient.deleteLogEntry(id);
        } catch (Exception e) {
            Assertions.fail();
        }
        Assertions.assertThrows(ServerResponseException.class, () -> {
            this.logClient.getLogEntry(id);
        });
    }

    @Test
    public void testGetLogEtry() {

        HashMap<String, String> originalEntry = new HashMap<>();
        originalEntry.put("title", "Cool title");
        originalEntry.put("comment", "Example content");
        originalEntry.put("date", "2020-01-01");
        originalEntry.put("feeling", "7");
        originalEntry.put("duration", "3600");
        originalEntry.put("distance", "3");
        originalEntry.put("maxHeartRate", "150");
        originalEntry.put("exerciseCategory", "STRENGTH");
        originalEntry.put("exerciseSubCategory", "PULL");

        String id = createEntry(
            originalEntry.get("title"),
            originalEntry.get("comment"),
            originalEntry.get("date"),
            originalEntry.get("feeling"),
            originalEntry.get("duration"),
            originalEntry.get("distance"),
            originalEntry.get("maxHeartRate"),
            originalEntry.get("exerciseCategory"),
            originalEntry.get("exerciseSubCategory")
        );
        try {
            HashMap<String, String> retreivedEntry = this.logClient.getLogEntry(id);
            Assertions.assertEquals(originalEntry, retreivedEntry);
        } catch (Exception e) {
            Assertions.fail();
        }

        Assertions.assertThrows(ServerResponseException.class, () -> {
            this.logClient.getLogEntry("-1");
        });
    }

}