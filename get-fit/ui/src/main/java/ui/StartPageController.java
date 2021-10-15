package ui;

import core.EntryManager;
import core.LogEntry;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import localpersistence.EntrySaverJson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class StartPageController {

    /***/
    @FXML
    private ComboBox<String> sortConfig;
    /***/
    @FXML
    private ComboBox<String> sortCategory;
    /***/
    @FXML
    private ComboBox<String> sortSubcategory;
    /***/
    @FXML
    private Button sortReverse;
    /***/
    @FXML
    private Button delete;
    /***/
    @FXML
    private Button goToStatistics;
    /***/
    @FXML
    private Button addSession;
    /***/
    @FXML
    private ListView<VBox> listOfEntries;
    /***/
    @FXML
    private Label errorLabel;

    /**
     * Switches the view to AddNewSession.
     *
     * @param event event data from pushed button.
     * @throws IOException if .FXML file could not be found.
     */
    @FXML
    public void addSessionButtonPushed(final ActionEvent event)
            throws IOException {
        App.setRoot("AddNewSession");
    }

    @FXML
    public void handleGoToStatistics(final ActionEvent event)
            throws IOException {
        // TODO - change this to new version @Emilie
        App.setRoot("Statistics");
    }

    @FXML
    public void deleteButtonPushed(ActionEvent event) throws IOException {
        //App.entryManager.removeEntry(listOfEntries.getSelectionModel().getSelectedItem().getId());
    }

    @FXML
    public void sort(Event event) {
        System.out.println("Whoopty");
    }

    public void addIteratorToView(final Iterator<LogEntry> entries) {
        while (entries.hasNext()) {
            LogEntry entry = entries.next();
            listOfEntries.getItems().add(createListEntry(entry));
        }
    }

    private VBox createListEntry(final LogEntry entry) {
        VBox vBox = new VBox();
        GridPane grid = new GridPane();

        ColumnConstraints colConstraint = new ColumnConstraints();
        colConstraint.setPercentWidth(25);
        grid.getColumnConstraints()
                .addAll(colConstraint, colConstraint, colConstraint,
                        colConstraint);

        Text title = new Text(entry.getTitle());
        Text date = new Text(entry.getDate().toString());
        Text category = new Text(entry.getExerciseCategory().toString());

        // this is a placeholder.
        Button open = new Button();
        open.setText("show");

        GridPane.setHalignment(title, HPos.LEFT);
        GridPane.setHalignment(date, HPos.LEFT);
        GridPane.setHalignment(category, HPos.LEFT);
        GridPane.setHalignment(open, HPos.RIGHT);

        grid.add(title, 0, 0);
        grid.add(date, 1, 0);
        grid.add(category, 2, 0);
        grid.add(open, 3, 0);

        vBox.getChildren().add(grid);

        return vBox;
    }

    /**
     * Initializes the controller.
     */
    @FXML
    private void initialize() {
        //populate sorting selectors


        if (App.entryManager.entryCount() == 0) {
            try {
                EntrySaverJson.load(App.entryManager);
            } catch (FileNotFoundException e) {
                System.out.println("SaveData.json could not be found.");
                errorLabel.setText("The file was not found.");
            }
        }
        Iterator<LogEntry> entries =
                new EntryManager.SortedIteratorBuilder(App.entryManager,
                        LogEntry.SORTCONFIGURATIONS.DATE).iterator(false);
        this.addIteratorToView(entries);
    }
}
