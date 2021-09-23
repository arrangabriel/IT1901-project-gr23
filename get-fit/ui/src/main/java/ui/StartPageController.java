package ui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import core.LogEntry;
import core.EntryManager;


public class StartPageController {
    @FXML Button addSession;
    @FXML ListView<String> listOfEntries;

    

    @FXML
    /**
     * Switches the view to AddNewSession
     * @param event
     * @throws IOException
     */
    public void addSessionButtonPushed(ActionEvent event) throws IOException{
        App.setRoot("AddNewSession");

    }

    /**
     * Iterates over the EntryManager of the app and adds the titles to listOfEntries
     */
    public void addToList(){

        for (LogEntry entry : App.entryManager){
            listOfEntries.getItems().add(entry.getTitle());
        }
    }


    
    @FXML
    /**
     * Initializes the controller
     */
    private void initialize(){
        this.addToList();
        
    }


}
