package integration;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SpringBootTest(classes = restserver.GetfitController.class)
public class GetfitIT extends ApplicationTest {
    private Parent root;
    private Stage stageRef;

    @Override
    public void start(Stage stage) throws IOException {

        this.stageRef = stage;
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("StartPage.fxml"));
        Parent root = Loader.load();
        Scene s = new Scene(root);
        stage.setTitle("Get fit");
        stage.setScene(s);
        stage.show();

    }

    private void updateRoot() throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("StartPage.fxml"));
        this.root = Loader.load();
    }

    @BeforeEach
    public void timeOut() {
        // Ensure that the application has started before tests begin.
        sleep(1000);
    }

    @Test
    public void startApp() throws IOException {
        updateRoot();
        Assertions.assertNotNull(root);
    }
}