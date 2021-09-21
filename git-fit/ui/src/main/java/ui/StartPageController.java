package ui;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.stage.Stage;


public class StartPageController {
    @FXML Button addSession;
    @FXML ListView listOfSession;

    

    @FXML
    public void addSessionButtonPushed(ActionEvent event) throws IOException{
        App.setRoot("AddNewSession");

      

    }


    
    @FXML
    private void initialize(){

    }


}
