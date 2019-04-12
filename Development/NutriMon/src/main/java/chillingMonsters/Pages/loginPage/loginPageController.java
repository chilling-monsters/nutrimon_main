package chillingMonsters.Pages.loginPage;

import chillingMonsters.AlertHandler;
import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.UserProfile.UserProfileController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class loginPageController implements PageController {
    @FXML // fx:id="btn_register"
    private Button btn_register; // Value injected by FXMLLoader

    @FXML // fx:id="pswdF_password"
    private PasswordField pswdF_password; // Value injected by FXMLLoader

    @FXML // fx:id="txtF_user_name"
    private TextField txtF_email; // Value injected by FXMLLoader

    @FXML // fx:id="btn_login"
    private Button btn_login; // Value injected by FXMLLoader

    @FXML
    void loginButtonAction(ActionEvent event) {
        UserProfileController login = ControllerFactory.makeUserProfileController();

        String email = txtF_email.getText();
        if (email.isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Oops!", "Please enter your Email");
            return;
        }

        if (email.indexOf('@') == -1) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "Invalid Email address");
            return;
        }

        String password = pswdF_password.getText();
        if (password.isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Oops!", "Please enter your Password");
            return;
        }

        if (password.length() < 8) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "Your Password must contain at least 8 digits");
            return;
        }

        if (!login.verifyCredentials(email, password)) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Login Failed...", "Your User name or password combination doesn't exist");
            return;
        }

        PageFactory.toNextPage(PageFactory.getLandingRefresh());
    }

    @FXML
    void registerButtonAction(ActionEvent event) {
        PageFactory.toNextPage(PageFactory.getRegisterPage());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    public void initialize() {
         txtF_email.setText("test@email.com");
         pswdF_password.setText("password");
        assert btn_register != null : "fx:id=\"btn_register\" was not injected: check your FXML file 'login.fxml'.";
        assert pswdF_password != null : "fx:id=\"pswdF_password\" was not injected: check your FXML file 'login.fxml'.";
        assert txtF_email != null : "fx:id=\"txtF_user_name\" was not injected: check your FXML file 'login.fxml'.";
        assert btn_login != null : "fx:id=\"btn_login\" was not injected: check your FXML file 'login.fxml'.";
    }
}