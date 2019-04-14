package chillingMonsters.Pages.userProfilePage;

import chillingMonsters.AlertHandler;
import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.UserProfile.UserProfileController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Map;

public class userProfilePageController implements PageController {
    private static final String PASSWORD_STR = "--- --- ---";

    private String username;
    private String email;
    private String password;

    @FXML
    private TextField usernameTxF;

    @FXML
    private TextField emailTxF;

    @FXML
    private PasswordField curPasswordTxF;

    @FXML
    private ToggleButton changePasswordButton;

    @FXML
    private VBox changePswForm;

    @FXML
    private PasswordField newPasswordTxF;

    @FXML
    private PasswordField cfPasswordTxF;

    @FXML
    private Button logoutButton;

    @FXML
    private Button updateButton;

    public void initialize() {
        changePswForm.visibleProperty().bindBidirectional(changePasswordButton.selectedProperty());
        curPasswordTxF.disableProperty().bind(changePasswordButton.selectedProperty().not());

        changePasswordButton.setOnAction(event -> handleChangePasswordForm());

        logoutButton.setOnAction(event -> handleLogout());
        updateButton.setOnAction(event -> handleUpdate());
    }

    public void refresh() {
        UserProfileController controller = ControllerFactory.makeUserProfileController();
        Map<String, Object> result = controller.getProfile();

        changePasswordButton.setSelected(false);

        username = result.get("userName").toString();
        email = result.get("userEmail").toString();
        password = result.get("password").toString();

        usernameTxF.setText(username);
        emailTxF.setText(email);

        curPasswordTxF.setText(PASSWORD_STR);
        newPasswordTxF.setText("");
        cfPasswordTxF.setText("");
    }

    //Event handlers
    private void handleChangePasswordForm() {
        boolean fVisible = changePswForm.isVisible();
        PageFactory.setFormInProgress(fVisible);

        if (fVisible) {
            curPasswordTxF.setText("");
        } else {
            curPasswordTxF.setText(PASSWORD_STR);
        }

    }

    private void handleLogout() {
        if (AlertHandler.showConfirmationAlert("Are you sure?", "You will be logged out")) {
            PageFactory.logout();
            ControllerFactory.makeUserProfileController().logout();
        }
    }

    private void handleUpdate() {
        String newUserName = usernameTxF.getText();
        String newEmail = emailTxF.getText();
        String curPassword = curPasswordTxF.getText();
        String newPassword = newPasswordTxF.getText();
        String cfPassword = cfPasswordTxF.getText();

        if (newUserName.equals(username)
            && newEmail.equals(email)
            && !changePswForm.isVisible()) {
            AlertHandler.showAlert(Alert.AlertType.INFORMATION, "Dodged that bullet!", "Your profile remains unchanged!");
            return;
        }

        // Check the Email, not null missing '@', unique
        if (newEmail.isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Oops!", "Please enter your email address");
            return;
        }

        if (newEmail.indexOf('@') == -1) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "Invalid email address");
            return;
        }

        UserProfileController controller = ControllerFactory.makeUserProfileController();
        if (controller.exists(newEmail)) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "This Email address has been used");
            return;
        }

        // Check name, not null
        if (newUserName.isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Oops!", "User name cannot be empty");
            return;
        }

        if (changePswForm.isVisible()) {
            // Check the password, more than 8 digits
            if (curPassword.isEmpty() || newPassword.isEmpty() || cfPassword.isEmpty()) {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Oops!", "Password cannot be empty");
                return;
            }

            if (newPassword.length() < 8) {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "Your Password must contain at least 8 digits");
                return;
            }

            if (!newPassword.equals(cfPassword)) {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "Your new passwords do not match");
                return;
            }

            if (!curPassword.equals(password)) {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "Your current password is not correct");
                return;
            }

            controller.updateProfile(newUserName, newEmail, newPassword, "");
        } else {
            controller.updateProfile(newUserName, newEmail, password, "");
        }


        AlertHandler.showAlert(Alert.AlertType.INFORMATION, "Success!", "Your profile have been updated!");
        refresh();
    }
}
