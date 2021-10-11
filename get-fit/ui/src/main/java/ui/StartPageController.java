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

    /**
     * Switches the view to AddNewSession.
     * @param event event data from pushed button.
     * @throws IOException if .FXML file could not be found.
     */
    @FXML
    public void addSessionButtonPushed(ActionEvent event) throws IOException{
        App.setRoot("AddNewSession");
    }

    public void handleViewStatisticsButton(ActionEvent event) throws IOException{
        App.setRoot("Statistics");
    } 

    /**
     * Iterates over the EntryManager of the app and adds the titles to listOfEntries.
     */
    public void addToList(){
        for (LogEntry entry : App.entryManager){
            listOfEntries.getItems().add(entry.getTitle());
            
        }

    }

    /**
     * Initializes the controller
     */
    @FXML
    private void initialize(){
        this.addToList();
    }
}
