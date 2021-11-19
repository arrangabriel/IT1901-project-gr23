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
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import client.LogClient;
import client.ServerResponseException;
import client.LogClient.ListBuilder;


public class StatisticsController {


    /**
     * Labels for different statistics.
     */
    @FXML
    private Label totalDuration, speedLabel, numberOfSessions,
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
    private DatePicker start, end;
    /**
     * x-axis for the bar chart.
     */


    
    private final LogClient client = new LogClient("http://localhost", 8080);

    private ObservableList<String> exerciseTypeSelecter =
            FXCollections.observableArrayList("Any", "Running", "Swimming",
                    "Strength", "Cycling");

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
        this.getData();
        this.createBarChart();
    }


    private void getData() {

        speedLabel.setVisible(true);
        averageSpeed.setVisible(true);

        if (exerciseType.getValue() != null 
        && exerciseType.getValue().toString().equals("Strength")
        && !(exerciseType.getValue().toString().equals("Any"))) {
            
            speedLabel.setVisible(false);
            averageSpeed.setVisible(false);
        }

        ListBuilder listBuilder = new ListBuilder();
        if ((exerciseType.getValue() != null) 
        && !(exerciseType.getValue().toString().equals("Any"))) {

            listBuilder.category(exerciseType.getValue().toUpperCase());
        }

        listBuilder.date(start.getValue().toString() + "-" + end.getValue().toString());
        HashMap<String, String> dataEntries;

        try {
            dataEntries = this.client.getStatistics(listBuilder);

            if (dataEntries.containsKey("empty")) {
                if(dataEntries.get("empty").equals("True")) {
                throw new IllegalStateException(
                    "There are no entries");
                }
            }

            for (Entry<String,String> dataEntry : dataEntries.entrySet()) {
                switch (dataEntry.getKey()) {

                    case "count" :
                        numberOfSessions.setText(
                            dataEntry.getValue());
                        break;
                    case "totalDuration" :
                        totalDuration.setText(
                            dataEntry.getValue());
                        break;
                    
                    case "averageDuration" :
                        averageDuration.setText(
                            dataEntry.getValue());
                        break;

                    case "averageSpeed" :
                        averageSpeed.setText(
                            dataEntry.getValue()
                            .substring(0, 3)
                            + "min/km");
                        break;
                    
                    case "averageFeeling" :
                        averageFeeling.setText(
                            dataEntry.getValue()
                            .substring(0, 3));
                        break;
                    
                    case "maximumHr" :
                        maximumHr.setText(
                            dataEntry.getValue());
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

            for (Entry<String,String> dataEntry : dataEntries.entrySet()) {
                switch (dataEntry.getKey()) {

                    case "swimming" :
                        swimming = Integer.parseInt(dataEntry.getValue());
                        break;
                    case "running" :
                        running = Integer.parseInt(dataEntry.getValue());
                        break;
                    
                    case "strength" :
                        strength = Integer.parseInt(dataEntry.getValue());
                        break;

                    case "cycling" :
                        cycling = Integer.parseInt(dataEntry.getValue());
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