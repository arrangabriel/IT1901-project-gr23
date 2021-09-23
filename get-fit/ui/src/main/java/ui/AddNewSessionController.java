package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;

import java.time.Duration;

import localpersistence.EntrySaverJson;
import java.time.LocalDate;


public class AddNewSessionController {
    //fxml component attributes
    @FXML private Label header;
    @FXML private Label timeLabel;
    @FXML private Label dateLabel;
    @FXML private Label distanceLabel;
    @FXML private Label commentLabel;

    @FXML private TextField nameOfSessionField;
    @FXML private TextField distanceField;
    @FXML private TextField hour;
    @FXML private TextField min;
    @FXML private TextArea commentField;

    @FXML private DatePicker sessionDatePicker;

    @FXML private Button createSession;

    /**
     * Adds an entry to the app EntryManager and switches the view to StartPage
     * @param event the event data from pushed button.
     * @throws IOException if .FXML file could not be found.
     */
    @FXML
    public void createSessionButtonPushed(ActionEvent event) throws IOException{
        // TODO - handle exception when entry could not be created
        App.entryManager.addEntry(nameOfSessionField.getText(),commentField.getText(),sessionDatePicker.getValue(), Duration.ofSeconds(1));
        EntrySaverJson.save(App.entryManager);
        App.setRoot("StartPage");
    }


    /**
     * Initializes the controller.
     * @throws NumberFormatException if the input is too large
     */
    @FXML
    private void initialize() throws NumberFormatException {
        // this code is quite duplicate, but ObservableValue makes it necessary
        hour.textProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue.isEmpty()) {
                try {
                    // throws exception if newvalue is not numeric
                    int value = Integer.parseInt(newValue);

                    // this 999 value is slightly arbitrary, to fit the input to the textField
                    if(value < 0 || value > 99){
                        throw new NumberFormatException("Input out of allowed range.");
                    }
                    // check if input is multiple zeroes
                    if(value == 0){
                        hour.setText("0");
                    }
                } catch (NumberFormatException e) {
                    // reset to previous value
                    hour.setText(oldValue);
                }
            }
        });
        min.textProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue.isEmpty()) {
                try {
                    // throws exception if newvalue is not numeric
                    int value = Integer.parseInt(newValue);
                    if(value < 0 || value > 59){
                        throw new NumberFormatException("Input out of allowed range.");
                    }
                    // check if input is multiple zeroes
                    if(value == 0){
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
        // TODO - implement this properly
//        sessionDatePicker.setOnKeyPressed(event -> {
//            if(event.getCode() == KeyCode.ENTER) {
//                sessionDatePicker.show();
//            }
//        });
    }

    
}