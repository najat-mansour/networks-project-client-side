module com.example.hw_02_part_02_client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires json.simple;

    opens launcher to javafx.fxml;
    exports launcher;

    opens controllers to javafx.fxml;

    opens models to com.fasterxml.jackson.databind, javafx.base;
}