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

    /**Back-button.*/
    @FXML
    private Button Return;
    /***/
    @FXML
    private Label header11;
    /***/
    @FXML
    private Label header1;
    /***/
    @FXML
    private TextField hour1;
    /**Main header.*/
    @FXML
    private Label header;
    /**Label for time fields.*/
    @FXML
    private Label timeLabel;
    /**Label for date field.*/
    @FXML
    private Label dateLabel;
    /**Label for distance field.*/
    @FXML
    private Label distanceLabel;
    /**Label for comment field*/
    @FXML
    private Label commentLabel;
    /**Label for error message.*/
    @FXML
    private Label errorLabel;
    /**Title input-field.*/
    @FXML
    private TextField nameOfSessionField;
    /**Distance input-field.*/
    @FXML
    private TextField distance;
    /**Hour input-field.*/
    @FXML
    private TextField hour;
    /**Minute input-field*/
    @FXML
    private TextField min;
    /**Comment input-field.*/
    @FXML
    private TextArea commentField;
    /**Date-picker.*/
    @FXML
    private DatePicker sessionDatePicker;
    /**Feeling input-slider.*/
    @FXML
    private Slider feelingSlider;
    /**Exercise type-selector.*/
    @FXML
    private ComboBox<String> exerciseType;
    /**Exercise subcategory-selector.*/
    @FXML
    private ComboBox<String> tags;
    /**Create session button.*/
    @FXML
    private Button createSession;

    /**All possible exercise category values.*/
    private final ObservableList<LogEntry.EXERCISECATEGORY> exerciseCategories =
            FXCollections.observableArrayList(
                    LogEntry.EXERCISECATEGORY.values());

    /**
     * Adds an entry to the app EntryManager and switches the view to StartPage.
     *
     * @param event the event data from pushed button.
     * @throws IOException if .FXML file could not be found.
     */
    @FXML
    public void createSessionButtonPushed(final ActionEvent event)
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

    @FXML
    public void returnButtonPushed(final ActionEvent event) throws IOException {
        App.setRoot("StartPage");
    }

    @FXML
    public void handleTagsSelecter(final ActionEvent event) throws IOException {

        LogEntry.EXERCISECATEGORY mainCategory = LogEntry.EXERCISECATEGORY.valueOf(exerciseType.getSelectionModel().getSelectedItem());
        switch (mainCategory) {
            case ANY:
                // generate empty selector, validate in createSessionButtonPushed
                tags.setItems(FXCollections.observableArrayList(""));
                // this might be iffy
                setCardio(true);
                break;
            case STRENGTH:
                tags.setItems(getSubcategoryStringObservableList(mainCategory));
                // set a placeholder value?
                setCardio(false);
                break;
            case CYCLING:
            case RUNNING:
            case SWIMMING:
                tags.setItems(getSubcategoryStringObservableList(mainCategory));
                setCardio(true);
                break;
        }

    }

    private void setCardio(final boolean isCardio) {
        // TODO - add new fields to this
        distance.setVisible(isCardio);
        distanceLabel.setVisible(isCardio);
    }

    private ObservableList<String> getSubcategoryStringObservableList(
            LogEntry.EXERCISECATEGORY mainCategory) {
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
        distance.setVisible(false);
        distanceLabel.setVisible(false);

        // validation of fields when they are changed
        hour.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    // throws exception if newvalue is not numeric
                    int value = Integer.parseInt(newValue);

                    // this 999 value is slightly arbitrary, to fit the input to the textField
                    if (value < 0 || value > 99) {
                        throw new NumberFormatException(
                                "Input out of allowed range.");
                    }
                    // check if input is multiple zeroes
                    if (value == 0) {
                        hour.setText("0");
                    }
                } catch (NumberFormatException e) {
                    // reset to previous value
                    hour.setText(oldValue);
                }
            }
        });
        min.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    // throws exception if newvalue is not numeric
                    int value = Integer.parseInt(newValue);
                    if (value < 0 || value > 59) {
                        throw new NumberFormatException(
                                "Input out of allowed range.");
                    }
                    // check if input is multiple zeroes
                    if (value == 0) {
                        min.setText("0");
                    }
                } catch (NumberFormatException e) {
                    // reset to previous value
                    min.setText(oldValue);
                }
            }
        });
        // set current date on startup
        sessionDatePicker.setValue(LocalDate.now());
    }


}
