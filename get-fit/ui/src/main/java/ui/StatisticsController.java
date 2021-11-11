package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

import client.LogClient;
import client.LogClient.ListBuilder;

/*
---------------------------------------
This component is for a future release.
---------------------------------------
 */

public class StatisticsController {

    private final ToggleGroup toggleGroup = new ToggleGroup();

    /**
     * Labels for different statistics.
     */
    @FXML
    private Label totalDuration, speed, numberOfSessions,
    averageFeeling, averageSpeed, maximumHr, averageDuration, errorLabel;
   
    /**
     * Button to press for returning to start page.
     */
    @FXML
    private Button showData;

    /**
     * Choosing which exercise type to display.
     */
    @FXML
    private ComboBox<String> exerciseType;

    /**
     * The duration for each pole in the diagram.
     */
    @FXML
    private RadioButton weeks, months, years;
    
    /**
     * The bar chart.
     */
    @FXML
    private StackedBarChart<String, Number> statisticsChart;

    @FXML
    private DatePicker start, end;
    /**
     * x-axis for the bar chart.
     */
    @FXML
    private CategoryAxis xAxis;

    /**
     * y-axis for the bar chart.
     */
    @FXML
    private NumberAxis yAxis;


    
    private final LogClient client = new LogClient("http://localhost", 8080);

    private ObservableList<String> exerciseTypeSelecter =
            FXCollections.observableArrayList("Cardio", "Endurance",
                    "Not selected");

    @FXML
    private void initialize() {
        exerciseType.setItems(exerciseTypeSelecter);
        weeks.setToggleGroup(toggleGroup);
        months.setToggleGroup(toggleGroup);
        years.setToggleGroup(toggleGroup);

        //Default values for dates are today and one year from now.
        start.setValue(LocalDate.now());
        end.setValue(LocalDate.now().plusYears(-1));

    }

    /**
     * Switches the view to start page.
     *
     * @param event event data from pushed button.
     * @throws IOException if .FXML file could not be found.
     */
    @FXML
    public void onReturn(final ActionEvent event)
            throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StartPage.fxml"));
        Parent p = loader.load();
        Scene s = new Scene(p);
        Stage window =
                (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Start page");
        window.setScene(s);
        window.show();
    }

    @FXML
    public void onHandleData() {
        ListBuilder listBuilder = new ListBuilder();
        if (exerciseType.getValue() != null) {
            listBuilder.category(exerciseType.getValue());
        }
        listBuilder.date(start.getValue().toString()+'-'+end.getValue());
        HashMap<String, String> dataEntries;

        try {
            dataEntries = this.client.getStatistics(listBuilder);
            for (String dataEntry : dataEntries.keySet()) {
                switch (dataEntry) {

                    case "count" :
                        numberOfSessions.setText(
                            dataEntries.get(dataEntry));
                        break;
                    case "totalDuration" :
                        totalDuration.setText(
                            dataEntries.get(dataEntry));
                        break;
                    
                    case "averageDuration" :
                        averageDuration.setText(
                            dataEntries.get(dataEntry));
                        break;

                    case "averageSpeed" :
                        averageSpeed.setText(
                            dataEntries.get(dataEntry));
                        break;
                    
                    case "averageFeeling" :
                        averageFeeling.setText(
                            dataEntries.get(dataEntry));
                        break;
                    
                    case "maximumHr" :
                        maximumHr.setText(
                            dataEntries.get(dataEntry));
                        break;
                    
                    default:
                        throw new IllegalArgumentException("Not according to Schema");

                    }
                }
            } catch (URISyntaxException | InterruptedException | ExecutionException e) {
                //Should catch URISyntaxException | InterruptedException | ExecutionException but
                //it would not work?
                errorLabel.setText(e.getMessage());
            }
        }
    }
        



    //Unfinished function:
    /*
    private void createStackedBarChart(final String xLabel,
                                       final Collection<String> category) {

        xAxis.setCategories(FXCollections.<String>observableArrayList(
                (Arrays.asList("test1", "test2"))));
        xAxis.setLabel(xLabel);

        yAxis.setLabel("Hours");

        //Create chart when core is finished
        statisticsChart = new StackedBarChart<>(xAxis, yAxis);
        //for (LogEntry entry : App.entryManager) {
        //final XYChart.Series<String, Number> series = new XYChart.Series<>();
    }*/

