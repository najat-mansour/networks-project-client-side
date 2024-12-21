package helpers;

import javafx.scene.control.Alert;

public class DialogMaker {
    private DialogMaker() {

    }

    public static void showDialog(String title, String content, String headerTitle, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerTitle);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
