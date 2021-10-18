package ui;

import core.EntryManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App.
 */
public class App extends Application {
    protected static EntryManager entryManager = new EntryManager();

    /**
     * Starts the app.
     *
     * @param args app arguments.
     */
    public static void main(final String[] args) {
        launch();
    }

    @Override
    public void start(final Stage stage) throws IOException {
        try {
            Parent parent =
                    FXMLLoader.load(getClass().getResource("StartPage.fxml"));
            stage.setTitle("Get fit");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (NullPointerException e) {
            System.out.println("Missing StartPage.fxml file.");
        }
    }
}
