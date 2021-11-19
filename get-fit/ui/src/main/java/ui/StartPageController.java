package ui;

import client.LogClient;
import client.LogClient.ListBuilder;
import client.ServerResponseException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class StartPageController {

    /**
     * Session log client.
     */
    private final LogClient client = new LogClient("http://localhost", 8080);

    // region FXML-elements
    /***/
    @FXML
    private CheckBox reverseBox;
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
    private ListView<VBox> listOfEntries;
    /***/
    @FXML
    private Label errorLabel;
    // endregion

    /**
     * String-names of strength-subcategories.
     */
    private ObservableList<String> sortStrengthSubcategories;
    /**
     * String-names of cardio-subcategories.
     */
    private ObservableList<String> sortCardioSubcategories;

    private void retry(final String func, final String... args) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Connection error");
        alert.setHeaderText("Could not connect to server");
        alert.setContentText("""
                Could not establish a connection to the server.
                Press OK to retry.
                Press Cancel to quit""");
        errorLabel.setText("Could not connect to server");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            switch (func) {
                case "initialize" -> this.initialize();
                case "updateList" -> this.updateList();
                case "createListEntry" -> {
                    HashMap<String, String> entry = new HashMap<>();
                    entry.put(args[0], args[1]);
                    this.createListEntry(entry);
                }
                default -> {
                }
            }
            errorLabel.setText("");
        } else {
            System.exit(0);
        }
    }

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

    /**
     * Switches the view to Statics.
     *
     * @param event event data from pushed button.
     * @throws IOException if .FXML file could not be found.
     */
    @FXML
    public void onStatisticsPage(final ActionEvent event)
            throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Statistics.fxml"));
        Parent p = loader.load();
        Scene s = new Scene(p);
        Stage window =
                (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Statistics");
        window.setScene(s);
        window.show();
    }

    /**
     * Hides entry view.
     */
    @FXML
    public void closeView() {
        entryView.setVisible(false);
    }

    /**
     * FXML wrapper method.
     */
    @FXML
    public void sort() {
        updateList();
    }

    /**
     * Updates the log entry list by querying the server
     * using selected params from the dropdown menus.
     */
    public void updateList() {
        errorLabel.setText("");

        listOfEntries.getItems().clear();
        // Gather query information
        String sort = sortConfig.getValue().toLowerCase();
        String categoryFilter = sortCategory.getValue().toLowerCase();
        String subFilter = sortSubcategory.getValue();
        if (subFilter != null) {
            subFilter = subFilter.toLowerCase();
        }

        ListBuilder builder =
                new ListBuilder().sort(sort).category(categoryFilter)
                        .subCategory(subFilter);

        if (reverseBox.isSelected()) {
            builder.reverse();
        }

        List<HashMap<String, String>> entries;
        try {
            entries = this.client.getLogEntryList(builder);
            for (HashMap<String, String> entry : entries) {
                this.listOfEntries.getItems().add(createListEntry(entry));
            }
        } catch (URISyntaxException
                | InterruptedException
                | ExecutionException e) {
            retry("updateList");
            errorLabel.setText("Could not connect to server");
            e.printStackTrace();
        } catch (ServerResponseException e) {
            errorLabel.setText(e.getMessage());
        }

    }


    @SuppressWarnings("checkstyle:MagicNumber")
    private VBox createListEntry(final HashMap<String, String> entry) {

        errorLabel.setText("");

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

        open.setOnAction(event -> {
            titleView.setText(entry.get("title"));
            dateView.setText(entry.get("date"));
            categoryView.setText(
                    capitalize(entry.get("exerciseCategory")));

            String subCategory = entry.get("exerciseSubCategory");
            if (!subCategory.equals("null")) {
                subCategory = capitalize(subCategory);
            }
            setOptionalField(subCategory,
                    subcategoryView,
                    subcategoryLabel);

            durationView.setText(durationToHours(
                    Duration.ofSeconds(Long.parseLong(entry.get("duration")))));
            feelingView.setText(String.valueOf(entry.get("feeling")));

            String distance = entry.get("distance");
            if (!distance.equals("null")) {
                distance = distance.concat("km");
            }
            setOptionalField(distance, distanceView,
                    distanceLabel);

            setOptionalField(entry.get("maxHeartRate"), heartRateView,
                    heartRateLabel);

            String comment = entry.get("comment");
            if (!comment.equals("null")) {
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
            } catch (URISyntaxException
                    | InterruptedException
                    | ExecutionException e1) {
                errorLabel.setText("Could not connect to server");
                e1.printStackTrace();
            } catch (ServerResponseException e1) {
                errorLabel.setText("");
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
        vBox.setId(entry.get("id"));

        return vBox;
    }

    private void setOptionalField(final String data, final Text textField,
                                  final Text textLabel) {
        if (!data.equals("null")) {
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
     * Updates ui when main category is selected. Also updates the current sort.
     */
    @FXML
    public void replaceSubcategories() {
        // hide and clear subcategories when there should be none.
        if (sortCategory.getValue().equals("Any")) {
            sortSubcategory.setItems(FXCollections.observableArrayList());
            sortSubcategory.setVisible(false);
        } else {
            switch (sortCategory.getValue()) {
                case "Strength" -> {
                    sortSubcategory.setItems(sortStrengthSubcategories);
                    sortSubcategory.getSelectionModel().selectFirst();
                    sortSubcategory.setVisible(true);
                }
                case "Swimming", "Cycling", "Running" -> {
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

    @SuppressWarnings("checkstyle:MagicNumber")
    private String durationToHours(final Duration duration) {
        double sec = (double) duration.toSeconds();
        double h = (double) Math.round((sec / 3600) * 10);
        return h / 10 + "h";
    }

    private String capitalize(final String string) {
        if (string.length() > 0) {
            return string.substring(0, 1).toUpperCase()
                    + string.substring(1).toLowerCase();
        } else {
            return "";
        }
    }

    private void bindVisibility(final Node node) {
        node.managedProperty().bind(node.visibleProperty());
    }

    /**
     * Initializes the controller.
     *
     * @throws SecurityException if security is violated.
     */
    @FXML
    private void initialize() {
        entryView.setVisible(false);
        // populate sorting selectors
        ObservableList<String> sortConfigs =
                FXCollections.observableArrayList();

        ObservableList<String> sortCategories =
                FXCollections.observableArrayList();
        sortCategories.add("Any");
        sortStrengthSubcategories = FXCollections.observableArrayList();
        sortStrengthSubcategories.add("Any");
        sortCardioSubcategories = FXCollections.observableArrayList();
        sortCardioSubcategories.add("Any");

        sortConfigs.add("Title");
        sortConfigs.add("Date");
        sortConfigs.add("Duration");

        HashMap<String, List<String>> filters;
        try {
            filters = this.client.getExerciseCategories();
            sortCategories.addAll(
                    filters.keySet().stream().map(this::capitalize)
                            .collect(Collectors.toList()));
            sortStrengthSubcategories.addAll(
                    filters.get("strength").stream().map(this::capitalize)
                            .collect(Collectors.toList()));
            sortCardioSubcategories.addAll(
                    filters.get("running").stream().map(this::capitalize)
                            .collect(Collectors.toList()));

            sortConfig.setItems(sortConfigs);
            sortConfig.getSelectionModel().selectFirst();
            sortCategory.setItems(sortCategories);
            sortCategory.getSelectionModel().selectFirst();
            sortSubcategory.setVisible(false);

        } catch (ExecutionException e) {
            this.retry("initialize");

        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        } catch (ServerResponseException e) {
            this.errorLabel.setText(e.getMessage());
        }

        bindVisibility(heartRateLabel);
        bindVisibility(heartRateView);
        bindVisibility(distanceLabel);
        bindVisibility(distanceView);
        bindVisibility(subcategoryLabel);
        bindVisibility(subcategoryView);

        this.updateList();
    }
}
