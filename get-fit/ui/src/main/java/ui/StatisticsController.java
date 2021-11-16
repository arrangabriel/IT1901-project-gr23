package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import client.LogClient;
import client.ServerResponseException;
import client.LogClient.ListBuilder;

/*
---------------------------------------
This component is for a future release.
---------------------------------------
 */

public class StatisticsController {


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
     * The bar chart.
     */
    @FXML
    private BarChart<String, Number> statisticsChart;


    @FXML
    private CategoryAxis xAxis;

    @FXML
    private DatePicker start, end;
    /**
     * x-axis for the bar chart.
     */


    
    private final LogClient client = new LogClient("http://localhost", 8080);

    private ObservableList<String> exerciseTypeSelecter =
            FXCollections.observableArrayList("ANY", "running", "swimming",
                    "strength", "cycling");

    @FXML
    private void initialize() {
        exerciseType.setItems(exerciseTypeSelecter);

        //Default values for dates are today and one year from now.
        start.setValue(LocalDate.now().plusYears(-1));
        end.setValue(LocalDate.now());

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
        if ((exerciseType.getValue() != null) && (exerciseType.getValue() != "ANY")) {
            listBuilder.category(exerciseType.getValue().toUpperCase());
        }

        listBuilder.date(start.getValue().toString() + "-" + end.getValue().toString());
        HashMap<String, String> dataEntries;

        try {
            dataEntries = this.client.getStatistics(listBuilder);
            System.out.println(dataEntries);

            if (dataEntries.containsKey("empty")) {
                if(dataEntries.get("empty").equals("True")) {
                throw new IllegalStateException(
                    "There are no entries");
                }
            }

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

                    }
                }
            } catch (URISyntaxException | InterruptedException | ExecutionException e) {
                errorLabel.setText("Could not connect to server");
                e.printStackTrace();
            } catch (ServerResponseException e) {
                errorLabel.setText(e.getMessage());
            } catch (IllegalStateException eae) {
                errorLabel.setText("There are no sessions saved");
        }

        this.createBarChart();
    }



    @SuppressWarnings("Unchecked")
    private void createBarChart() {
       
        ListBuilder listBuilder = new ListBuilder();
        listBuilder.date(start.getValue().toString() + "-" + end.getValue().toString());
        HashMap<String, String> dataEntries;

        int swimming = 0;
        int running = 0;
        int strength = 0;
        int cycling = 0;

        try {
            dataEntries = this.client.getChartData(listBuilder);
            System.out.println(dataEntries);

            for (String dataEntry : dataEntries.keySet()) {
                switch (dataEntry) {

                    case "swimming" :
                        swimming = Integer.parseInt(dataEntries.get(dataEntry));
                        break;
                    case "running" :
                        running = Integer.parseInt(dataEntries.get(dataEntry));
                        break;
                    
                    case "strength" :
                        strength = Integer.parseInt(dataEntries.get(dataEntry));
                        break;

                    case "cycling" :
                        cycling = Integer.parseInt(dataEntries.get(dataEntry));
                        break;
                }
            }
        } catch (URISyntaxException | InterruptedException | ExecutionException e) {
            errorLabel.setText("Could not connect to server");
            e.printStackTrace();
        } catch (ServerResponseException e) {
            errorLabel.setText(e.getMessage());
        } catch (IllegalStateException eae) {
            errorLabel.setText("There are no sessions saved");
        }        

        Series<String, Number> chart = new XYChart.Series<>();
		
		chart.getData().add(new XYChart.Data<>("Swimming", swimming));
		chart.getData().add(new XYChart.Data<>("Running", running));
		chart.getData().add(new XYChart.Data<>("Strength", strength));
		chart.getData().add(new XYChart.Data<>("Cycling", cycling));

        statisticsChart.setAnimated(false);
		
		statisticsChart.getData().setAll(chart);
    }   
}