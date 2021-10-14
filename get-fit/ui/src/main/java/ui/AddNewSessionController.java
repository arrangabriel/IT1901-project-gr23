package ui;

import core.LogEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    /**Maximum heart rate limit.*/
    private final int maxHeartRateLimit = 230;
    /**
     * All possible exercise category values.
     */
    private final ObservableList<LogEntry.EXERCISECATEGORY> exerciseCategories =
            FXCollections.observableArrayList(
                    LogEntry.EXERCISECATEGORY.values());
    /**Label for exercise type selector.*/
    @FXML
    private Label exerciseTypeLabel;
    /**
     * Back-button.
     */
    @FXML
    private Button back;
    /***/
    @FXML
    private Label feelingLabel;
    /**Label for heart rate input.*/
    @FXML
    private Label maxHeartRateLabel;
    /**Heart rate input field.*/
    @FXML
    private TextField heartRate;
    /**
     * Main header.
     */
    @FXML
    private Label header;
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
    private TextField nameOfSessionField;
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
     * @param ignored an ActionEvent from the observed change.
     * @throws IOException if .FXML file could not be found.
     */
    @FXML
    public void createSessionButtonPushed(final ActionEvent ignored)
            throws IOException {
        // TODO - handle exception when entry could not be created
        Duration duration = Duration.ofHours(Integer.parseInt(hour.getText()))
                .plusSeconds(Duration.ofMinutes(
                        Integer.parseInt(min.getText())).getSeconds());
        try {
            // TODO - create and add entry to manager with build pattern
            //App.entryManager.addEntry(
            //        nameOfSessionField.getText(),
            //        commentField.getText(),
            //        sessionDatePicker.getValue(),
            //        duration);
            EntrySaverJson.save(App.entryManager);
            App.setRoot("StartPage");
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }

    }

    /**
     * This function handles going back too main page when fired.
     *
     * @param ignored an ActionEvent from the observed change.
     * @throws IOException if StartPage could not be found.
     */
    @FXML
    public void backButtonPushed(final ActionEvent ignored)
            throws IOException {
        App.setRoot("StartPage");
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
            case ANY -> {
                // validate in createSessionButtonPushed
                // generate empty selector,
                // tags.setItems(FXCollections.observableArrayList(""));
                // this might be iffy
                showTags(false);
                setCardio(true);
            }
            case STRENGTH -> {
                tags.setItems(getSubcategoryStringObservableList(mainCategory));
                showTags(true);
                setCardio(false);
            }
            case CYCLING, RUNNING, SWIMMING -> {
                tags.setItems(getSubcategoryStringObservableList(mainCategory));
                showTags(true);
                setCardio(true);
            }
            default -> {
            }
        }

    }

    private void showTags(final boolean show) {
        tagsLabel.setVisible(show);
        tags.setVisible(show);
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
        showTags(false);
        setCardio(true);

        // validation of fields when they are changed
        validateIntegerInput(hour, maxHours);
        validateIntegerInput(min, maxMinutes);
        validateIntegerInput(heartRate, maxHeartRateLimit);
        // set current date on startup
        sessionDatePicker.setValue(LocalDate.now());
    }

    /**
     * Adds a max (and lower 0) int validation listener to the textField.
     *
     * @param field    the field to be validated.
     * @param maxValue maximum int to validate against.
     */
    private void validateIntegerInput(final TextField field, final int maxValue) {
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
