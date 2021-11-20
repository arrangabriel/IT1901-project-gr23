package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * JavaFX App.
 */
public class App extends Application {

    /**
     * Starts the app.
     *
     * @param args app arguments.
     */
    public static void main(final String[] args) {
        launch();
    }

    /**
     * Starts the app.
     *
     * @param stage main stage.
     */
    @Override
    public void start(final Stage stage) {
        try {
            Parent parent =
                    FXMLLoader.load(Objects.requireNonNull(
                            getClass().getResource("StartPage.fxml")));
            stage.setTitle("Get fit");
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Missing StartPage.fxml file.");
        }
    }
}
