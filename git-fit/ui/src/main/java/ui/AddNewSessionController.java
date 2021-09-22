package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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

    /*private void timeValueValidation() implements ChangeListener<String> {
        @Override
        public void changed(ObservebleValue<? extends String>) observeble, String OldValue, String newValue){
            try {
                 Integer.parseInt(observeble.GetValue()))
            }
            catch (NumberFormatException e){

            }

            }
            
        }

    }*/



    @FXML 
    public void createSessionButtonPushed(ActionEvent event) throws IOException{
        App.entryManager.addEntry(nameOfSessionField.getText(),commentField.getText(),sessionDatePicker.getValue(), Duration.ofSeconds(1));
        App.setRoot("StartPage");

    }

  


    @FXML
    private void initialize(){

    }

    
}
