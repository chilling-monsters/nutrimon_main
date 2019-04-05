package chillingMonsters;

import javafx.scene.control.Alert;

public class AlertHandler {

    public static void showAlert(Alert.AlertType alertType, String title, String message) {

        Alert alert = new Alert(alertType); // initialize

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        if (alertType == Alert.AlertType.CONFIRMATION) {
            alert.showAndWait();
        } else {
            alert.show();
        }
    }
}
