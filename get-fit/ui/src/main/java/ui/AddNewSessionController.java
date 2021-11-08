package ui;

import client.LogClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class AddNewSessionController {

    /**
     * Slightly arbitrary maximum hour limit for duration.
     */
    private static final int MAX_HOURS = 99;
    /**
     * Maximum minute limit.
     */
    private static final int MAX_MINUTES = 59;
    /**
     * Maximum distance limit.
     */
    private static final int MAX_DISTANCE = 200;
    /**
     * Maximum heart rate limit.
     */
    private static final int MAX_HEARTRATE = 300;
    /**
     * Minimum heart rate limit.
     */
    private static final int MIN_HEARTRATE = 20;

    /**
     * Session log client.
     */
    private final LogClient client = new LogClient("http://localhost", 8080);

    /**
     * Exercise categories and subcategories.
     */
    private HashMap<String, List<String>> categories;

    /**
     * Label for duration input.
     */
    @FXML
    private Label durationLabel;
    /**
     * Label for exercise type selector.
     */
    @FXML
    private Label exerciseTypeLabel;
    /**
     * Back-button.
     */
    @FXML
    private Button back;
    /**
     * Label for feeling slider.
     */
    @FXML
    private Label feelingLabel;
    /**
     * Label for heart rate input.
     */
    @FXML
    private Label maxHeartRateLabel;
    /**
     * Heart rate input field.
     */
    @FXML
    private TextField heartRate;
    /**
     * Main header.
     */
    @FXML
    private Label titleLabel;
    /**
     * Label for time fields.
     */
    @FXML
    private Label timeLabel;
    /**
     * Label for date field.
     */
    @FXML
    private Label dateLabel;
    /**
     * Label for distance field.
     */
    @FXML
    private Label distanceLabel;
    /**
     * Label for comment field.
     */
    @FXML
    private Label commentLabel;
    /**
     * Label for error message.
     */
    @FXML
    private Label errorLabel;
    /**
     * Label for tags-selector.
     */
    @FXML
    private Label tagsLabel;
    /**
     * Title input-field.
     */
    @FXML
    private TextField titleField;
    /**
     * Distance input-field.
     */
    @FXML
    private TextField distance;
    /**
     * Hour input-field.
     */
    @FXML
    private TextField hour;
    /**
     * Minute input-field.
     */
    @FXML
    private TextField min;
    /**
     * Comment input-field.
     */
    @FXML
    private TextArea commentField;
    /**
     * Date-picker.
     */
    @FXML
    private DatePicker sessionDatePicker;
    /**
     * Feeling input-slider.
     */
    @FXML
    private Slider feelingSlider;
    /**
     * Exercise type-selector.
     */
    @FXML
    private ComboBox<String> exerciseType;
    /**
     * Exercise subcategory-selector.
     */
    @FXML
    private ComboBox<String> tags;
    /**
     * Create session button.
     */
    @FXML
    private Button createSession;

    /**
     * Adds an entry to the app EntryManager and switches the view to StartPage.
     *
     * @param event an ActionEvent from the observed change.
     * @throws IOException if .FXML file could not be found.
     */
    @FXML
    public void createSessionButtonPushed(final ActionEvent event)
            throws IOException {

        String title = titleField.getText();
        String date = sessionDatePicker.getValue().toString();
        String duration = "null";
        String category = exerciseType.getValue();
        String feeling = String.valueOf(feelingSlider.getValue());
        String maxHeartRate = "null";
        String subCategory = "null";
        String distanceValue = "null";
        String comment = commentField.getText();

        try {
            // checks if duration fields have values.
            try {
                duration = String.valueOf(
                        Duration.ofHours(Integer.parseInt(hour.getText()))
                                .plusSeconds(Duration.ofMinutes(
                                                Integer.parseInt(min.getText()))
                                        .getSeconds()));

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Duration must be set.");
            }

            // tries to build with required values.
            // LogEntry.EntryBuilder logBuilder = new LogEntry.EntryBuilder(
            //         title, date, duration, category, feeling);

            // adds maxHeartRate if value is present.
            try {
                maxHeartRate = (heartRate.getText());
                int intMaxHeartRate = Integer.parseInt(maxHeartRate);
                if (intMaxHeartRate < MIN_HEARTRATE) {
                    throw new IllegalArgumentException(
                            "Heart rate must be more than "
                                    + MIN_HEARTRATE);
                } else if (intMaxHeartRate > MAX_HEARTRATE) {
                    throw new IllegalArgumentException(
                            "Heart rate must be less than "
                                    + MAX_HEARTRATE);
                }
            } catch (NumberFormatException ignored) {
            }

            // adds comment if value is present.
            if (comment.isEmpty()) {
                comment = "null";
            }

            // returns null if nothing is selected.
            String subCategoryString = tags.getValue();
            if (subCategoryString == null) {
                subCategory = "null";
            } else {
                subCategory = subCategoryString;
            }

            String distanceString = distance.getText();
            if (distanceString == "") {
                distanceValue = "null";
            }

            HashMap<String, String> entryMap = new HashMap<>();

            entryMap.put("title", title);
            entryMap.put("date", date);
            entryMap.put("duration", duration);
            entryMap.put("category", category);
            entryMap.put("feeling", feeling);
            entryMap.put("maxHeartRate", maxHeartRate);
            entryMap.put("subCategory", subCategory);
            entryMap.put("distance", distanceValue);
            entryMap.put("comment", comment);


            // add and save newly created LogEntry.
            try {
                this.client.addLogEntry(entryMap);
                goToStartPage(event);
            } catch (URISyntaxException | InterruptedException | ExecutionException e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Connection error");
                alert.setHeaderText("Could not connect to server");
                alert.setContentText(
                        "Could not establish a connection to the server.\nPress OK to retry.\nPress Cancel to quit");
                errorLabel.setText("Could not connect to server");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    this.createSessionButtonPushed(null);
                } else {
                    System.exit(0);
                }
            }


        } catch (IllegalArgumentException e) {
            if (title.isEmpty()) {
                // case 1, title is empty.
                errorLabel.setText("Title is needed.");
            } else {
                // case 2, durations are empty.
                errorLabel.setText(e.getMessage());
            }
        }
    }

    private void goToStartPage(final ActionEvent event) throws IOException {
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
     * This function handles going back too main page when fired.
     *
     * @param event an ActionEvent from the observed change.
     * @throws IOException if StartPage could not be found.
     */
    @FXML
    public void backButtonPushed(final ActionEvent event) throws IOException {
        goToStartPage(event);
    }

    /**
     * Changes ui according to the selected exercise category.
     *
     * @param event an ActionEvent from the observed change.
     */
    @FXML
    public void handleTagsSelector(final ActionEvent event) {

        String mainCategory =
                exerciseType.getSelectionModel().getSelectedItem();
        tags.setItems(this.getSubcategoryStringObservableList(mainCategory));
    }

    private void setCardio(final boolean isCardio) {
        distance.setVisible(isCardio);
        distanceLabel.setVisible(isCardio);
    }

    private String capitalize(final String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private ObservableList<String> getSubcategoryStringObservableList(
            final String mainCategory) {
        return categories.get(mainCategory).stream()
                .map(this::capitalize)
                .collect(Collectors
                        .toCollection(FXCollections::observableArrayList));
    }

    private void validateIntegerInput(final TextField field,
                                      final int maxValue) {
        field.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    // throws exception if newValue is not numeric
                    int value = Integer.parseInt(newValue);
                    if (value < 0 || value > maxValue) {
                        throw new NumberFormatException(
                                "Input out of allowed range.");
                    }
                    // check if input is multiple zeroes
                    if (value == 0) {
                        field.setText("0");
                    }
                } catch (NumberFormatException e) {
                    // reset to previous value
                    field.setText(oldValue);
                }
            }
        });
    }


    /**
     * Initializes the controller.
     *
     * @throws NumberFormatException if the input is too large
     */
    @FXML
    private void initialize() throws NumberFormatException {
        try {
            this.categories = client.getExerciseCategories();
        } catch (ExecutionException e) {
            errorLabel.setText("Could not connect to server.");
            return;
        } catch (URISyntaxException | InterruptedException e) {
            // Can't really happen
            e.printStackTrace();
        }

        Set<String> exerciseCategories = this.categories.keySet();

        // generate an ObservableList of exercise category names.
        ObservableList<String> exerciseCategoryNames = exerciseCategories
                .stream()
                .map(this::capitalize)
                .collect(Collectors
                        .toCollection(FXCollections::observableArrayList));

        // set initial values.
        exerciseType.setItems(exerciseCategoryNames);
        exerciseType.getSelectionModel().selectFirst();
        tags.setItems(exerciseCategoryNames);
        setCardio(false);
        sessionDatePicker.setValue(LocalDate.now());

        // validation of fields when they are changed.
        validateIntegerInput(hour, MAX_HOURS);
        validateIntegerInput(min, MAX_MINUTES);
        validateIntegerInput(heartRate, MAX_HEARTRATE);
        validateIntegerInput(distance, MAX_DISTANCE);
    }
}
