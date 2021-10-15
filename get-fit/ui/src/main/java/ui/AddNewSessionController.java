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
    private final int maxHours = 99;
    /**
     * Maximum minute limit.
     */
    private final int maxMinutes = 59;
    /**
     * Maximum heart rate limit.
     */
    private final int maxHeartRateLimit = LogEntry.MAXHEARTRATEHUMAN;

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
        // TODO - handle exception when entry could not be created

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
            // checks if duration fields have values in them.
            try {
                duration = Duration.ofHours(Integer.parseInt(hour.getText()))
                        .plusSeconds(Duration.ofMinutes(
                                Integer.parseInt(min.getText())).getSeconds());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Duration must be set.");
            }

            // tries to build.
            LogEntry.EntryBuilder logBuilder = new LogEntry.EntryBuilder(
                    title, date, duration, category, feeling);

            // adds maxHeartRate if value is present.
            try {
                maxHeartRate = Integer.parseInt(heartRate.getText());
                logBuilder = logBuilder.maxHeartRate(maxHeartRate);
            } catch (NumberFormatException event1) {
            }

            // adds comment if value is present.
            if (!comment.isEmpty()) {
                logBuilder = logBuilder.comment(comment);
            }

            // returns null if nothing is selected.
            String subCategoryString = tags.getValue();

            switch (category) {
                case STRENGTH -> {
                    // adds subcategory if value is present.
                    try {
                        subCategory = LogEntry.STRENGTHSUBCATEGORIES.valueOf(
                                subCategoryString);
                        logBuilder =
                                logBuilder.exerciseSubcategory(subCategory);
                    } catch (NullPointerException event1) {
                    }
                }
                case SWIMMING, RUNNING, CYCLING -> {
                    // adds distance if value is present.
                    try {
                        distanceValue =
                                Integer.parseInt(distance.getText());
                        logBuilder =
                                logBuilder.distance((double) distanceValue);
                    } catch (NumberFormatException event1) {
                    }
                    // adds subcategory if value is present.
                    try {
                        subCategory = LogEntry.CARDIOSUBCATEGORIES.valueOf(
                                subCategoryString);
                        logBuilder =
                                logBuilder.exerciseSubcategory(subCategory);
                    } catch (NullPointerException event1) {
                    }
                }
                default -> {
                }
            }
            App.entryManager.addEntry(logBuilder.build());
            EntrySaverJson.save(App.entryManager);

            FXMLLoader Loader = new FXMLLoader();
            Loader.setLocation(getClass().getResource("StartPage.fxml"));

            Parent p = Loader.load();
            Scene  s = new Scene(p);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setTitle("Add new session");
            window.setScene(s);
            window.show();
            
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

    /**
     * This function handles going back too main page when fired.
     *
     * @param ignored an ActionEvent from the observed change.
     * @throws IOException if StartPage could not be found.
     */
    @FXML
    public void returnButtonPushed(ActionEvent event) throws IOException{
        FXMLLoader Loader = new FXMLLoader();
            Loader.setLocation(getClass().getResource("StartPage.fxml"));
            Parent p = Loader.load();
            Scene  s = new Scene(p);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setTitle("Add new session");
            window.setScene(s);
            window.show();
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
        // TODO - add new fields to this
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

    /**
     * Initializes the controller.
     *
     * @throws NumberFormatException if the input is too large
     */
    @FXML
    private void initialize() throws NumberFormatException {
        // TODO - maybe refactor this to a function call
        ObservableList<String> exerciseCategoryNames = exerciseCategories
                .stream()
                .map(
                        Enum::toString)
                .collect(Collectors
                        .toCollection(FXCollections::observableArrayList));

        exerciseType.setItems(exerciseCategoryNames);
        exerciseType.getSelectionModel().selectFirst();
        tags.setItems(getSubcategoryStringObservableList(
                LogEntry.EXERCISECATEGORY.STRENGTH));
        setCardio(false);

        // validation of fields when they are changed
        validateIntegerInput(hour, maxHours);
        validateIntegerInput(min, maxMinutes);
        validateIntegerInput(heartRate,
                LogEntry.MAXHEARTRATEHUMAN);
        // set current date on startup
        sessionDatePicker.setValue(LocalDate.now());
    }

    /**
     * Adds a max (and lower 0) int validation listener to the textField.
     *
     * @param field    the field to be validated.
     * @param maxValue maximum int to validate against.
     */
    private void validateIntegerInput(final TextField field,
                                      final int maxValue) {
        field.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    // throws exception if newvalue is not numeric
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
}
