package ui;

import localpersistence.EntrySaverJson;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import ui.StartPageController;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.IOException;
import org.testfx.matcher.control.LabeledMatchers;
import java.util.List;
import ui.AddNewSessionController;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.Assertions;


public class AppTest extends ApplicationTest {
    private static Scene scene;
    //private StartPageController controller;
    private StartPageController controller;
    private Parent root;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("StartPage.fxml"));
        Parent root = Loader.load();
        Scene  s = new Scene(root);
        stage.setScene(s);
        stage.show();
    }

    private void click(String... labels) {
        for (var label : labels) {
            clickOn(LabeledMatchers.hasText(label));
        }
    }

    public Parent getRootNode() {
        return root;
    }




    @Test
    public void testCreateButton(){

        click("Add session");

        clickOn("#nameOfSessionField");
        write("New Session");
        clickOn("#sessionDatePicker");
        clickOn("#hour");
        write("1");
        clickOn("#min");
        write("30");
        clickOn("#exerciseType");

        clickOn("#maximumHeartRate");
        write("150");
        clickOn("#commentField");
        write("New comment");


        click("Create session");
        sleep(2000);
    }


    private ListView<String> getEntriesView() {
        return (ListView<String>) getRootNode().lookup("#listOfEntries");
    }

    @Test
    public void checkView(){

        List<String> viewEntries = getEntriesView().getItems();
        for(int i = 0; i < viewEntries.size(); i++){
            Assertions.assertEquals(App.entryManager.getEntry(String.valueOf(i)).getTitle(), viewEntries.get(i));
        }


    }


     
}