package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddNewSessionController {
    
    //fxml component attributes
    @FXML private Label header;
    @FXML private Label timeLabel;
    @FXML private Label dateLabel;
    @FXML private Label distanceLabel;
    @FXML private Label commentLabel;

    @FXML private TextField nameOfSessionField;
    @FXML private TextField timeField;
    @FXML private TextField distanceField;
    @FXML private TextField commentField;

    @FXML private DatePicker sessionDatePicker;

    @FXML private Button createSession;

    @FXML
    private void initialize(){

    }

    
}
