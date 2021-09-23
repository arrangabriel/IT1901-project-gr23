package ui;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import core.LogEntry;
import core.EntryManager;

import javafx.stage.Stage;


public class StartPageController {
    @FXML Button addSession;
    @FXML ListView<String> listOfEntries;

    

    @FXML
    public void addSessionButtonPushed(ActionEvent event) throws IOException{
        App.setRoot("AddNewSession");

    }

    public void addToList(){

        for (LogEntry entry : App.entryManager){
            listOfEntries.getItems().add(entry.getTitle());

        }
    }


    
    @FXML
    private void initialize(){
        this.addToList();
        load(App.entryManager);
        
    }


}
