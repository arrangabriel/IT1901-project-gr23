package ui;

import core.EntryManager;
import core.LogEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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

    private ObservableList<String> sortConfigs;
    private ObservableList<String> sortCategories;
    private ObservableList<String> sortStrengthSubcategories;
    private ObservableList<String> sortCardioSubcategories;
    private boolean reverse;

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
    public void sort(final Event event) {
        LogEntry.SORTCONFIGURATIONS config =
                LogEntry.SORTCONFIGURATIONS.valueOf(sortConfig.getValue());
        EntryManager.SortedIteratorBuilder iteratorBuilder =
                new EntryManager.SortedIteratorBuilder(App.entryManager,
                        config);
        try {
            // throws illegalargumentexception if value is ANY.
            LogEntry.EXERCISECATEGORY category =
                    LogEntry.EXERCISECATEGORY.valueOf(sortCategory.getValue());
            iteratorBuilder = iteratorBuilder.filterExerciseCategory(category);
            LogEntry.Subcategory subcategory = null;
            switch (category) {
                case STRENGTH:
                    subcategory = LogEntry.STRENGTHSUBCATEGORIES.valueOf(
                            sortSubcategory.getValue());
                    break;
                case SWIMMING, CYCLING, RUNNING:
                    subcategory = LogEntry.CARDIOSUBCATEGORIES.valueOf(
                            sortSubcategory.getValue());
                    break;
                default:
                    break;
            }
            iteratorBuilder = iteratorBuilder.filterSubCategory(subcategory);
        } catch (IllegalArgumentException ignored1) {
            // reaching this part short circuits filtering, order matters.
        }
        addIteratorToView(iteratorBuilder.iterator(reverse));
    }

    public void addIteratorToView(final Iterator<LogEntry> entries) {
        listOfEntries.getItems().clear();
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
        open.setText("Show");

        open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //Do stuff
            }
        });

        Button delete = new Button();
        delete.setText("Delete");

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                App.entryManager.removeEntry(entry.getId());
                try {
                    EntrySaverJson.save(App.entryManager);
                } catch (IOException exc) {
                    errorLabel.setText(exc.getMessage());
                }
                sort(null);
            }
        });

        GridPane.setHalignment(title, HPos.LEFT);
        GridPane.setHalignment(date, HPos.LEFT);
        GridPane.setHalignment(category, HPos.LEFT);
        GridPane.setHalignment(open, HPos.RIGHT);
        GridPane.setHalignment(delete, HPos.LEFT);

        grid.add(title, 0, 0);
        grid.add(date, 1, 0);
        grid.add(category, 2, 0);
        grid.add(open, 3, 0);
        grid.add(delete, 3, 0);

        vBox.getChildren().add(grid);

        return vBox;
    }

    @FXML
    public void replaceSubcategories(Event event) {
        // hide and clear if there should be none
        if (sortCategory.getValue().equals("ANY")) {
            sortSubcategory.setItems(FXCollections.observableArrayList());
            sortSubcategory.setVisible(false);
        } else {
            switch (LogEntry.EXERCISECATEGORY.valueOf(
                    sortCategory.getValue())) {
                case STRENGTH -> {
                    sortSubcategory.setItems(sortStrengthSubcategories);
                    sortSubcategory.getSelectionModel().selectFirst();
                    sortSubcategory.setVisible(true);
                }
                case SWIMMING, CYCLING, RUNNING -> {
                    sortSubcategory.setItems(sortCardioSubcategories);
                    sortSubcategory.getSelectionModel().selectFirst();
                    sortSubcategory.setVisible(true);
                }
                default -> {
                }
            }
        }
        // silly
        sort(event);
    }

    @FXML
    public void reverse(ActionEvent event) {
        reverse = !reverse;
        //if (reverse) {
        //    set symbol here at a later date
        //}
        sort(event);
    }

    /**
     * Initializes the controller.
     */
    @FXML
    private void initialize() {
        //populate sorting selectors
        sortConfigs =
                FXCollections.observableArrayList();
        sortCategories =
                FXCollections.observableArrayList();
        sortCategories.add("ANY");
        sortStrengthSubcategories =
                FXCollections.observableArrayList();
        sortStrengthSubcategories.add("ANY");
        sortCardioSubcategories =
                FXCollections.observableArrayList();
        sortCardioSubcategories.add("ANY");

        for (LogEntry.SORTCONFIGURATIONS sortConfiguration : LogEntry.SORTCONFIGURATIONS.values()) {
            sortConfigs.add(sortConfiguration.name());
        }
        for (LogEntry.EXERCISECATEGORY exercisecategory : LogEntry.EXERCISECATEGORY.values()) {
            sortCategories.add(exercisecategory.name());
        }
        for (LogEntry.STRENGTHSUBCATEGORIES strengthSubcategory : LogEntry.STRENGTHSUBCATEGORIES.values()) {
            sortStrengthSubcategories.add(strengthSubcategory.name());
        }
        for (LogEntry.CARDIOSUBCATEGORIES cardioSubcategory : LogEntry.CARDIOSUBCATEGORIES.values()) {
            sortCardioSubcategories.add(cardioSubcategory.name());
        }

        sortConfig.setItems(sortConfigs);
        sortConfig.getSelectionModel().selectFirst();
        sortCategory.setItems(sortCategories);
        sortCategory.getSelectionModel().selectFirst();
        sortSubcategory.setVisible(false);

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
