package ui;

import client.LogClient;
import client.LogClient.SortArgWrapper;
import client.ServerResponseException;
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

public class StatisticsController {

    /**
     * x-axis for the bar chart.
     */
    private final LogClient client = new LogClient("http://localhost", 8080);

    /**
     * Selector values.
     */
    private final ObservableList<String> exerciseTypeSelector =
            FXCollections.observableArrayList("Any", "Running", "Swimming",
                    "Strength", "Cycling");
    /**
     * Duration field.
     */
    @FXML
    private Label totalDuration;

    /**
     * Speed field.
     */
    @FXML
    private Label speedLabel;

    /**
     * Number of sessions field.
     */
    @FXML
    private Label numberOfSessions;

    /**
     * Average feeling field.
     */
    @FXML
    private Label averageFeeling;

    /**
     * Average speed field.
     */
    @FXML
    private Label averageSpeed;

    /**
     * Maximum heart rate field.
     */
    @FXML
    private Label maximumHr;

    /**
     * Average duration field.
     */
    @FXML
    private Label averageDuration;

    /**
     * Error message field.
     */
    @FXML
    private Label errorLabel;

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

    /**
     * Start date picker.
     */
    @FXML
    private DatePicker start;

    /**
     * End date picker.
     */
    @FXML
    private DatePicker end;

    @FXML
    private void initialize() {
        exerciseType.setItems(exerciseTypeSelector);

        //Default values for dates are today and one year from now.
        start.setValue(LocalDate.now().plusYears(-1));
        end.setValue(LocalDate.now());
        speedLabel.setVisible(false);
        averageSpeed.setVisible(false);
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
        window.setTitle("Get fit");
        window.setScene(s);
        window.show();
    }

    /**
     * Fires when data-button is pressed.
     */
    @FXML
    public void onHandleData() {
        this.getData();
        this.createBarChart();
    }

    private void getData() {
        speedLabel.setVisible(false);
        averageSpeed.setVisible(false);

        if (exerciseType.getValue() != null) {
            if ((exerciseType.getValue().equals("Swimming")
                || (exerciseType.getValue().equals("Running"))
                || (exerciseType.getValue().equals("Cycling")))) {
                    speedLabel.setVisible(true);
                    averageSpeed.setVisible(true);
            }
        }

        SortArgWrapper sortArgWrapper = new SortArgWrapper();
        if ((exerciseType.getValue() != null)
                && !(exerciseType.getValue().equals("Any"))) {
            sortArgWrapper.category(exerciseType.getValue().toUpperCase());
        }

        sortArgWrapper.date(
                start.getValue().toString() + "-" + end.getValue().toString());
        HashMap<String, String> dataEntries;

        try {
            dataEntries = this.client.getStatistics(sortArgWrapper);

            if (dataEntries.containsKey("empty")) {
                if (dataEntries.get("empty").equals("True")) {
                    throw new IllegalStateException("There are no entries");
                }
            }

            dataEntries.forEach((key, value) -> {
                final int rounding = 3;
                switch (key) {
                    case "count" -> numberOfSessions.setText(value);
                    case "totalDuration" -> totalDuration.setText(value);
                    case "averageDuration" -> averageDuration.setText(value);
                    case "averageSpeed" -> averageSpeed.setText(
                            value.substring(0, rounding)
                            + "min/km");
                    case "averageFeeling" -> averageFeeling.setText(
                            value.substring(0, rounding));
                    case "maximumHr" -> maximumHr.setText(value);
                    default -> {
                    }
                }
            });
        } catch (URISyntaxException
                | InterruptedException
                | ExecutionException e) {
            errorLabel.setText("Could not connect to server");
            e.printStackTrace();
        } catch (ServerResponseException e) {
            errorLabel.setText("Uh oh, there was an issue with your request.");
        } catch (IllegalStateException eae) {
            errorLabel.setText("There are no sessions saved");
        }

        this.createBarChart();
    }


    //@SuppressWarnings("Unchecked")
    private void createBarChart() {
        SortArgWrapper sortArgWrapper = new SortArgWrapper();
        HashMap<String, String> dataEntries;
        int swimming = 0;
        int running = 0;
        int strength = 0;
        int cycling = 0;

        sortArgWrapper.date(
                start.getValue().toString() + "-" + end.getValue().toString());

        try {
            dataEntries = this.client.getChartData(sortArgWrapper);
            for (Entry<String, String> dataEntry : dataEntries.entrySet()) {
                switch (dataEntry.getKey()) {
                    case "swimming" -> {
                        swimming =
                                Integer.parseInt(dataEntry.getValue());
                    }
                    case "running" -> {
                        running =
                                Integer.parseInt(dataEntry.getValue());
                    }
                    case "strength" -> {
                        strength =
                                Integer.parseInt(dataEntry.getValue());
                    }
                    case "cycling" -> {
                        cycling =
                                Integer.parseInt(dataEntry.getValue());
                    }
                    default -> {
                    }
                }
            }
        } catch (URISyntaxException
                | InterruptedException
                | ExecutionException e) {
            errorLabel.setText("Could not connect to server");
            e.printStackTrace();
        } catch (ServerResponseException e) {
            errorLabel.setText("Uh oh, there was an issue with your request.");
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
