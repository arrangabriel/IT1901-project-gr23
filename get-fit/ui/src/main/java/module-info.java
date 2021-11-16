module ui {
    requires client;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;

    opens ui to javafx.graphics, javafx.fxml;
}
