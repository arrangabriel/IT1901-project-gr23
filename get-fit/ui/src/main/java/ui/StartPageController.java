package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.FileNotFoundException;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import localpersistence.EntrySaverJson;
import javafx.scene.Node;

import core.LogEntry;
import core.EntryManager;

public class StartPageController {
    @FXML Button addSession, viewStatistics;
    @FXML ListView<String> listOfEntries;
    @FXML Label errorLabel;

    /**
     * Switches the view to AddNewSession.
     * @param event event data from pushed button.
     * @throws IOException if .FXML file could not be found.
     */
    @FXML
    public void addSessionButtonPushed(ActionEvent event) throws IOException{
        FXMLLoader Loader = new FXMLLoader();
		Loader.setLocation(getClass().getResource("AddNewSession.fxml"));
		Parent p = Loader.load();
		Scene  s = new Scene(p);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(s);
		window.show();
      
    }

    public void handleViewStatisticsButton(ActionEvent event) throws IOException{
        FXMLLoader Loader = new FXMLLoader();
		Loader.setLocation(getClass().getResource("Statistics.fxml"));
		Parent p = Loader.load();
		Scene  s = new Scene(p);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(s);
		window.show();
        
    } 
    @FXML
    public void deleteButtonPushed(ActionEvent event) throws IOException{
        //App.entryManager.removeEntry(listOfEntries.getSelectionModel().getSelectedItem().getId());
        //App.entryManager.removeEntry(listOfEntries.getSelectionModel().getSelectedItem().getId());
      
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
        if(App.entryManager.entryCount()== 0){
            try{
                EntrySaverJson.load(App.entryManager);
            }
            catch (FileNotFoundException e){
                System.out.println("SaveData.json could not be found.");
                errorLabel.setText("The file was not found.");
            }
        }
        this.addToList();
    }
}
