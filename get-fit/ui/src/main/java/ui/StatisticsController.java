package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;


import core.LogEntry;
import core.EntryManager;

public class StatisticsController {

    //fxml component attributes
    @FXML private Label totalDuration;
    @FXML private Label numberOfSessions;
    @FXML private Label averageFeeling;
    @FXML private Label maximumHr;
    @FXML private Label averageSpeed;
    @FXML private Label speed;
    @FXML private ComboBox<String> timeInterval;
    @FXML private ComboBox<String> exerciseType, tags;

    private ObservableList<String> exerciseTypeSelecter = FXCollections.observableArrayList("Running" , "Cycling", "Strength" ,"Svimming");
    private ObservableList<String> timeIntervalSelector = FXCollections.observableArrayList("Year", "Month", "Week");

    @FXML
    private void initialize() {
        exerciseType.setItems(exerciseTypeSelecter);
        timeInterval.setItems(timeIntervalSelector);   
    }

    
}
