package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.testfx.matcher.control.LabeledMatchers;
import java.io.IOException;



public class AddNewSessionController_test extends ApplicationTest {

    private AddNewSessionController controller;
    private static Scene scene;

    @Override
    public void start(final Stage stage) throws Exception {
        scene = new Scene(loadFXML("AddNewSession"));
        stage.setScene(scene);
        stage.show();
    }

    void setRoot(String file) throws IOException{
        scene.setRoot(loadFXML(file));
    }
    
    private Parent loadFXML (String file) throws IOException{
        return new FXMLLoader(this.getClass().getResource(file + ".fxml")).load();
    }
  

  private void click(String... labels) {
    for (var label : labels) {
        clickOn(LabeledMatchers.hasText(label));
    }
}



  @Test
  public void testCreateButton(){
      String nameOfSession = "New session";
      String comment = "New comment";
      String hour = "1";
      String min = "30";
      String maximumHeartrate = "150";
      clickOn("nameOfSessionField").write(nameOfSession);
      clickOn("commentField").write(comment);
      clickOn("hour").write(min);

      click("#createNewSessionButtonPushed");
  }

  /*@Test
  public void testReturnButton() {
    List<Window> before = Window.getWindows();
    Parent beforeRoot = null;
    for (Window window : before) {
      beforeRoot = window.getScene().getRoot();
    }
    clickOn("#Return");
    try {
      Thread.sleep(10000);
    } catch (Exception e) {
      fail();
    }
    List<Window> after = Window.getWindows();
    Parent afterRoot = null;
    for (Window window : after) {
      afterRoot = window.getScene().getRoot();
    }
    assertNotEquals(afterRoot, beforeRoot);
  }*/

  @Test
  public void testController_initial() {
    assertNotNull(this.controller);
  }


    
}
