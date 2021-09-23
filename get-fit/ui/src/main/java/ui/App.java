package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import core.EntryManager;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    protected static EntryManager entryManager = new EntryManager();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("StartPage"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String file) throws IOException{
        scene.setRoot(loadFXML(file));
    }
    
    private static Parent loadFXML (String file) throws IOException{
        return new FXMLLoader(App.class.getResource(file + ".fxml")).load();
    }



    public static void main(String[] args) {
        launch();
     }
}
