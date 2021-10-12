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
import java.util.Arrays;
import java.util.Collection;

import javafx.scene.control.ToggleGroup;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.StackedBarChart;



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
    @FXML private StackedBarChart<String, Number> statisticsChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;


    private ObservableList<String> exerciseTypeSelecter = FXCollections.observableArrayList("Cardio", "Endurance", "Not selected");
    private final ToggleGroup toggleGroup = new ToggleGroup();

    
     

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
        App.setRoot("StartPage");
    }

    //Unfinished function:
    private void createStackedBarChart(String xLabel, Collection<String> category) {

        xAxis.setCategories(FXCollections.<String>observableArrayList((Arrays.asList("test1", "test2"))));
        xAxis.setLabel(xLabel);

        yAxis.setLabel("Hours");

        //Create chart when core is finished
        statisticsChart = new StackedBarChart<>(xAxis, yAxis);
        //for (LogEntry entry : App.entryManager) {
        //final XYChart.Series<String, Number> series = new XYChart.Series<>();
            
        } 



    }
    
}
