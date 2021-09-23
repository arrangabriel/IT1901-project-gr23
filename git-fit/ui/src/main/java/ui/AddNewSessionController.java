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
import core.EntryManager;
import java.time.Duration;



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

    @FXML 
    public void createSessionButtonPushed(ActionEvent event) throws IOException{
        App.entryManager.addEntry(nameOfSessionField.getText(),commentField.getText(),sessionDatePicker.getValue(), Duration.ofSeconds(1));
        App.setRoot("StartPage");

    }


    @FXML
    private void initialize(){

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
    }

    
}
