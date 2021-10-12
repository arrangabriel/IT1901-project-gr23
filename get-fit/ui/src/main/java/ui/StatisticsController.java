package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.scene.control.ToggleGroup;


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
    @FXML private Button returnToStartPage;
    @FXML private ComboBox<String> exerciseType;
    @FXML private RadioButton weeks;
    @FXML private RadioButton months;
    @FXML private RadioButton years;


    private ObservableList<String> exerciseTypeSelecter = FXCollections.observableArrayList("Cardio", "Endurance", "Not selected");
    private final ToggleGroup toggleGroup = new ToggleGroup();
    
    //Stacked bar chart


    @FXML
    private void initialize() {
        exerciseType.setItems(exerciseTypeSelecter); 
        weeks.setToggleGroup(toggleGroup);  
        months.setToggleGroup(toggleGroup);
        years.setToggleGroup(toggleGroup);
    }

    
    /**
     * Switches the view to AddNewSession.
     * @param event event data from pushed button.
     * @throws IOException if .FXML file could not be found.
     */
   @FXML
    public void returnToStartPagePushed(ActionEvent event) throws IOException{
        System.out.println("hei");
        App.setRoot("StartPage");
    }

    
    
}
