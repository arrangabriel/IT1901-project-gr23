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
    public LogClient logClient;

    @BeforeEach
    public void startClient() {
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

        String id = logClient.addLogEntry(entry);
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

        return logClient.addLogEntry(entry);
    }

}