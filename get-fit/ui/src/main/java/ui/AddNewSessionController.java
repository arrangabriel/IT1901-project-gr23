package ui;

import core.LogEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import localpersistence.EntrySaverJson;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AddNewSessionController {

    /**
     * Slightly arbitrary maximum hour limit for duration.
     */
    private static final int maxHours = 99;
    /**
     * Maximum minute limit.
     */
    private static final int maxMinutes = 59;
    /**
     * Maximum distance limit.
     */
    private static final int maxDistance = 200;

    /**
     * All possible exercise category values.
     */
    private final ObservableList<LogEntry.EXERCISECATEGORY> exerciseCategories =
            FXCollections.observableArrayList(
                    LogEntry.EXERCISECATEGORY.values());

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
        LocalDate date = sessionDatePicker.getValue();
        Duration duration;
        LogEntry.EXERCISECATEGORY category =
                LogEntry.EXERCISECATEGORY.valueOf(exerciseType.getValue());
        int feeling = (int) feelingSlider.getValue();
        int maxHeartRate;
        LogEntry.Subcategory subCategory;
        int distanceValue;
        String comment = commentField.getText();

        try {
            // checks if duration fields have values.
            try {
                duration = Duration.ofHours(Integer.parseInt(hour.getText()))
                        .plusSeconds(Duration.ofMinutes(
                                Integer.parseInt(min.getText())).getSeconds());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Duration must be set.");
            }

            // tries to build with required values.
            LogEntry.EntryBuilder logBuilder = new LogEntry.EntryBuilder(
                    title, date, duration, category, feeling);

            // adds maxHeartRate if value is present.
            try {
                maxHeartRate = Integer.parseInt(heartRate.getText());
                if (maxHeartRate < LogEntry.MINHEARTRATEHUMAN) {
                    throw new IllegalArgumentException(
                            "Heart rate must be more than "
                                    + LogEntry.MINHEARTRATEHUMAN);
                }
                logBuilder = logBuilder.maxHeartRate(maxHeartRate);
            } catch (NumberFormatException ignored) {
            }

            // adds comment if value is present.
            if (!comment.isEmpty()) {
                logBuilder = logBuilder.comment(comment);
            }

            // returns null if nothing is selected.
            String subCategoryString = tags.getValue();

            switch (category) {
                // special cases
                case STRENGTH -> {
                    // adds subcategory if value is present.
                    try {
                        subCategory = LogEntry.STRENGTHSUBCATEGORIES.valueOf(
                                subCategoryString);
                        logBuilder =
                                logBuilder.exerciseSubcategory(subCategory);
                    } catch (NullPointerException ignored) {
                    }
                }
                case SWIMMING, RUNNING, CYCLING -> {
                    // adds distance if value is present.
                    try {
                        distanceValue =
                                Integer.parseInt(distance.getText());
                        logBuilder =
                                logBuilder.distance((double) distanceValue);
                    } catch (NumberFormatException ignored) {
                    }
                    // adds subcategory if value is present.
                    try {
                        subCategory = LogEntry.CARDIOSUBCATEGORIES.valueOf(
                                subCategoryString);
                        logBuilder =
                                logBuilder.exerciseSubcategory(subCategory);
                    } catch (NullPointerException ignored) {
                    }
                }
                default -> {
                }
            }

            // add and save newly created LogEntry.
            App.entryManager.addEntry(logBuilder.build());
            EntrySaverJson.save(App.entryManager);

            goToStartPage(event);

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

        LogEntry.EXERCISECATEGORY mainCategory = LogEntry.EXERCISECATEGORY
                .valueOf(exerciseType.getSelectionModel().getSelectedItem());
        switch (mainCategory) {
            case STRENGTH -> {
                tags.setItems(getSubcategoryStringObservableList(mainCategory));
                setCardio(false);
            }
            case CYCLING, RUNNING, SWIMMING -> {
                tags.setItems(getSubcategoryStringObservableList(mainCategory));
                setCardio(true);
            }
            default -> {
            }
        }
    }

    private void setCardio(final boolean isCardio) {
        distance.setVisible(isCardio);
        distanceLabel.setVisible(isCardio);
    }

    private ObservableList<String> getSubcategoryStringObservableList(
            final LogEntry.EXERCISECATEGORY mainCategory) {
        return Arrays.stream(mainCategory.getSubcategories())
                .map(
                        LogEntry.Subcategory::toString)
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

        // generate an ObservableList of exercise category names.
        ObservableList<String> exerciseCategoryNames = exerciseCategories
                .stream()
                .map(
                        Enum::toString)
                .collect(Collectors
                        .toCollection(FXCollections::observableArrayList));

        // set initial values.
        exerciseType.setItems(exerciseCategoryNames);
        exerciseType.getSelectionModel().selectFirst();
        tags.setItems(getSubcategoryStringObservableList(
                LogEntry.EXERCISECATEGORY.STRENGTH));
        setCardio(false);
        sessionDatePicker.setValue(LocalDate.now());

        // validation of fields when they are changed.
        validateIntegerInput(hour, maxHours);
        validateIntegerInput(min, maxMinutes);
        validateIntegerInput(heartRate,
                LogEntry.MAXHEARTRATEHUMAN);
        validateIntegerInput(distance, maxDistance);
    }
}
