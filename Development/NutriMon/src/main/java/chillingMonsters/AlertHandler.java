package chillingMonsters;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertHandler {
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType); // initialize

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.show();
    }

    public static boolean showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional confirmedOption = alert.showAndWait();

        ButtonType confirmedButton = (ButtonType) confirmedOption.get();
        return confirmedButton.getButtonData() == ButtonBar.ButtonData.OK_DONE;
    }

    public static boolean showCriticalAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType); // initialize

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType confirm = new ButtonType("Confirm");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirm, cancel);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == confirm)
            return true;
        else
            return false;
    }
}
