package chillingMonsters.Pages.registerPage;

import chillingMonsters.AlertHandler;
import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.UserProfile.UserProfileController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class registerPageController implements PageController {
    @FXML // fx:id="pswdF_confirm_password"
    private TextField pswdF_confirm_password; // Value injected by FXMLLoader

    @FXML // fx:id="txtF_email"
    private TextField txtF_email; // Value injected by FXMLLoader

    @FXML // fx:id="btn_cancel"
    private Button btn_cancel; // Value injected by FXMLLoader

    @FXML // fx:id="btn_confirm"
    private Button btn_confirm; // Value injected by FXMLLoader

    @FXML // fx:id="txtF_name"
    private TextField txtF_name; // Value injected by FXMLLoader

    @FXML // fx:id="pswdF_new_password"
    private TextField pswdF_new_password; // Value injected by FXMLLoader

    @FXML
    void confirmButtonAction(ActionEvent event) {
        UserProfileController register = ControllerFactory.makeUserProfileController();

        // Check the Email, not null missing '@', unique
        String email = txtF_email.getText();
        if (email.isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Oops!", "Please enter your Email Address");
            return;
        }

        if (email.indexOf('@') == -1) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "Invalid Email address");
            return;
        }

        if (register.exists(email)) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "This Email address has been used");
            return;
        }

        // Check name, not null
        String name = txtF_name.getText();
        if (name.isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Oops!", "Please enter your Name");
            return;
        }

        // Check the password, not null, passwords must match, more than 8 digits
        String new_password = pswdF_new_password.getText();
        String confirm_password = pswdF_confirm_password.getText();
        if (new_password.isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Oops!", "Please enter your Password");
            return;
        }

        if (confirm_password.isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Oops!", "Please re-enter your Password");
            return;
        }

        if (!new_password.equals(confirm_password)) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "Your Passwords does not match");
            return;
        }

        if (new_password.length() < 8) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "Your Password must contain at least 8 digits");
            return;
        }

        // Update database
        register.createProfile(name, email, new_password);
        PageFactory.toNextPage(PageFactory.getLandingPage());
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {
        PageFactory.toNextPage(PageFactory.getLoginPage());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    public void initialize() {
        assert pswdF_confirm_password != null : "fx:id=\"txtF_confirm_password\" was not injected: check your FXML file 'register.fxml'.";
        assert txtF_email != null : "fx:id=\"txtF_email\" was not injected: check your FXML file 'register.fxml'.";
        assert btn_cancel != null : "fx:id=\"btn_cancel\" was not injected: check your FXML file 'register.fxml'.";
        assert btn_confirm != null : "fx:id=\"btn_confirm\" was not injected: check your FXML file 'register.fxml'.";
        assert txtF_name != null : "fx:id=\"txtF_name\" was not injected: check your FXML file 'register.fxml'.";
        assert pswdF_new_password != null : "fx:id=\"txtF_new_password\" was not injected: check your FXML file 'register.fxml'.";
    }
}
