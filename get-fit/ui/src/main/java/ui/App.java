package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import core.EntryManager;
import localpersistence.EntrySaverJson;
import java.io.FileNotFoundException;

/**
 * JavaFX App
 */
public class App extends Application {
    protected static EntryManager entryManager = new EntryManager();
    private static Stage stageRef;

    @Override
    public void start(Stage stage) throws IOException {
        // TODO - fix this
        Parent parent = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
        stage.setScene(new Scene(parent));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
     }
}
