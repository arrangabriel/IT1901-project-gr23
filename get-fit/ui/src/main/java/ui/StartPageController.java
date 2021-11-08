package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import client.LogClient;
import client.LogClient.ListBuilder;

public class StartPageController {

    /***/
    @FXML
    private Text distanceLabel;
    /***/
    @FXML
    private Text heartRateLabel;
    /***/
    @FXML
    private Text subcategoryLabel;
    /***/
    @FXML
    private AnchorPane entryView;
    /***/
    @FXML
    private Button hideView;
    /***/
    @FXML
    private Text titleView;
    /***/
    @FXML
    private Text dateView;
    /***/
    @FXML
    private TextArea commentView;
    /***/
    @FXML
    private Text subcategoryView;
    /***/
    @FXML
    private Text heartRateView;
    /***/
    @FXML
    private Text feelingView;
    /***/
    @FXML
    private Text distanceView;
    /***/
    @FXML
    private Text categoryView;
    /***/
    @FXML
    private Text durationView;
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
    // TODO: Add this box to fxml
    /***/
    private CheckBox reverseBox;
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
     * Session log client.
     */
    private final LogClient client = new LogClient("localhost", 8080);

    /**
     * String-names of sort configurations.
     */
    private ObservableList<String> sortConfigs;
    /**
     * String-names of exercise categories.
     */
    private ObservableList<String> sortCategories;
    /**
     * String-names of strength-subcategories.
     */
    private ObservableList<String> sortStrengthSubcategories;
    /**
     * String-names of cardio-subcategories.
     */
    private ObservableList<String> sortCardioSubcategories;
    /**
     * Sort order.
     */
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddNewSession.fxml"));
        Parent p = loader.load();
        Scene s = new Scene(p);
        Stage window =
                (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Add new session");
        window.setScene(s);
        window.show();
    }

    /* Easter egg for future release
    public void handleViewStatisticsButton(
    ActionEvent event) throws IOException{
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("Statistics.fxml"));
        Parent p = Loader.load();
        Scene  s = new Scene(p);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(s);
        window.show();
    }
    */

    /**
     * Hides entry view.
     *
     * @param event ignored.
     */
    @FXML
    public void closeView(final ActionEvent event) {
        entryView.setVisible(false);
    }

    /**
     * Updates the log entry list by querying the server using selected params from the dropdown menues
     * 
     */
    public void updateList() {

        // Gather query information
        String sort = sortConfig.getValue();
        String categoryFilter = sortCategory.getValue();
        String subFilter = sortSubcategory.getValue();
        boolean reverse = reverseBox.isSelected();

        ListBuilder builder = new ListBuilder()
            .sort(sort)
            .category(categoryFilter)
            .subCategory(subFilter);

        if (reverse) {
            builder.reverse();
        }

        List<HashMap<String, String>> entries;
        try {
            entries = this.client.getLogEntryList(builder);
            for (HashMap<String, String> entry: entries) {
                this.listOfEntries.getItems().add(createListEntry(entry));        
        }
        } catch (URISyntaxException | InterruptedException | ExecutionException e) {
            errorLabel.setText("Could not connect to server");
            e.printStackTrace();
        }

        

    }

    // /**
    //  * Fill list with entries according to sort parameters.
    //  *
    //  * @param event a JavaFX event.
    //  */
    // @FXML
    // public void sort(final Event event) {
    //     List<HashMap<String, String>> entries = this.client.getLogEntryList();

    //     addIteratorToView(entries.iterator());
    // }

    // /**
    //  * Places an iterator of entries in the view.
    //  *
    //  * @param entries an iterator of entries.
    //  */
    // public void addIteratorToView(final Iterator<HashMap<String, String>> entries) {
    //     listOfEntries.getItems().clear();
    //     while (entries.hasNext()) {
    //         HashMap<String, String> entry = entries.next();
    //         listOfEntries.getItems().add(createListEntry(entry));
    //     }
    // }

    private VBox createListEntry(final HashMap<String, String> entryId) {

        HashMap<String, String> response;
        try {
            response = this.client.getLogEntry(entryId.get("id"));
        } catch (URISyntaxException | InterruptedException | ExecutionException e2) {
            errorLabel.setText("Could not connect to server");
            e2.printStackTrace();
            response = null;
        }
        final HashMap<String, String> entry = response;

        VBox vBox = new VBox();
        GridPane grid = new GridPane();

        ColumnConstraints colConstraint = new ColumnConstraints();
        colConstraint.setPercentWidth(25);
        grid.getColumnConstraints()
                .addAll(colConstraint, colConstraint, colConstraint,
                        colConstraint);

        Text title = new Text(entry.get("title"));
        Text date = new Text(entry.get("date"));
        Text category = new Text(entry.get("ExerciseCategory"));

        Button open = new Button();
        open.setText("Show");

        open.setOnAction(e -> {
            // title
            titleView.setText(entry.get("title"));
            // date
            dateView.setText(entry.get("date"));
            // category
            categoryView.setText(entry.get("exerciseCategory"));
            // feeling
            feelingView.setText(String.valueOf(entry.get("feeling")));
            // duration
            durationView.setText(durationToHours(Duration.ofSeconds(Long.parseLong(entry.get("duration")))));
            // subcategory optional
            String subcategory = entry.get("exerciseSubcategory");
            setOptionalField(subcategory, subcategoryView, subcategoryLabel);
            // maxHeartRate optional
            String maxHeartRate = entry.get("maxHeartRate");
            setOptionalField(maxHeartRate, heartRateView, heartRateLabel);
            // distance
            String distance = entry.get("distance");
            setOptionalField(distance, distanceView, distanceLabel);
            // comment
            String comment = entry.get("comment");
            if (comment != null) {
                commentView.setText(comment);
            } else {
                commentView.clear();
            }
            entryView.setVisible(true);
        });

        Button delete = new Button();
        delete.setText("Delete");

        delete.setOnAction(e -> {
            try {
                this.client.deleteLogEntry(entry.get("id"));
            } catch (URISyntaxException | InterruptedException | ExecutionException e1) {
                errorLabel.setText("Could not connect to server");
                e1.printStackTrace();
            }
            this.updateList();
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
        vBox.setId(entryId.get("id"));

        return vBox;
    }

    private void setOptionalField(final String data, final Text textField,
                                  final Text textLabel) {
        if (data != null) {
            textField.setText(data);
            textField.setVisible(true);
            textLabel.setVisible(true);
        } else {
            textField.setText("");
            textField.setVisible(false);
            textLabel.setVisible(false);
        }
    }

    /**
     * Updates ui when main category is selected.
     * Also updates the current sort.
     *
     * @param event a JavaFX event.
     */
    @FXML
    public void replaceSubcategories(final Event event) {
        // hide and clear subcategories when there should be none.
        if (sortCategory.getValue().equals("ANY")) {
            sortSubcategory.setItems(FXCollections.observableArrayList());
            sortSubcategory.setVisible(false);
        } else {
            switch (sortCategory.getValue()) {
                case "STRENGTH" -> {
                    sortSubcategory.setItems(sortStrengthSubcategories);
                    sortSubcategory.getSelectionModel().selectFirst();
                    sortSubcategory.setVisible(true);
                }
                case "SWIMMING", "CYCLING", "RUNNING" -> {
                    sortSubcategory.setItems(sortCardioSubcategories);
                    sortSubcategory.getSelectionModel().selectFirst();
                    sortSubcategory.setVisible(true);
                }
                default -> {
                }
            }
        }
        updateList();
    }

    private String durationToHours(final Duration duration) {
        double hours = (double) duration.toHours();
        double minutes = (double) duration.toMinutes() / 60;
        return (double) Math.round((hours + minutes) * 10) / 10 + "h";
    }

    // /**
    //  * Updates ui sort with reversal.
    //  *
    //  * @param event a JavaFX event.
    //  */
    // @FXML
    // public void reverse(final ActionEvent event) {
    //     reverse = !reverse;
    //     //if (reverse) {
    //     //    set symbol here at a later date
    //     //}
    //     sort(event);
    // }

    /**
     * Initializes the controller.
     */
    @FXML
    private void initialize() {
        entryView.setVisible(false);
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

        // TODO: make dynamic
        sortConfigs.add("title");
        sortConfigs.add("date");
        sortConfigs.add("duration");

        HashMap<String, List<String>> filters;
        try {
            filters = this.client.getExerciseCategories();
            // TODO: Lacks flexibility
            sortCategories.addAll(filters.keySet());
            sortStrengthSubcategories.addAll(filters.get("strength"));
            sortCardioSubcategories.addAll(filters.get("running"));

            sortConfig.setItems(sortConfigs);
            sortConfig.getSelectionModel().selectFirst();
            sortCategory.setItems(sortCategories);
            sortCategory.getSelectionModel().selectFirst();
            sortSubcategory.setVisible(false);

        } catch (URISyntaxException | InterruptedException | ExecutionException e) {
            errorLabel.setText("Could not connect to server");
            e.printStackTrace();
        }

        this.updateList();
    }
}
