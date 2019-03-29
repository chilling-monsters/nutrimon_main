package chillingMonsters.Pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;
import chillingMonsters.AlertHandler;
import chillingMonsters.Controllers.*;


public class loginPageController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btn_register"
    private Button btn_register; // Value injected by FXMLLoader

    @FXML // fx:id="pswdF_password"
    private PasswordField pswdF_password; // Value injected by FXMLLoader

    @FXML // fx:id="ancP_login_main"
    private AnchorPane ancP_login_main; // Value injected by FXMLLoader

    @FXML // fx:id="txtF_user_name"
    private TextField txtF_email; // Value injected by FXMLLoader

    @FXML // fx:id="btn_login"
    private Button btn_login; // Value injected by FXMLLoader

    @FXML
    void loginButtonAction(ActionEvent event) {
        // Window alert = btn_login.getScene().getWindow();
        UserProfileController login = ControllerFactory.makeUserProfileController();

        if (txtF_email.getText().isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Oops!", "Please enter your Email");
            return;
        }

        if (pswdF_password.getText().isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Oops!", "Please enter your Password");
            return;
        }

        if (!login.checkCredentials(txtF_email.getText(), pswdF_password.getText())) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Login Failed...", "Your User name or password combination doesn't exist");
            return;
        }

        AlertHandler.showAlert(Alert.AlertType.CONFIRMATION, "Welcome back!", "Success!");
        // Platform.exit(); // close the window -- for testing purposes
    }

    @FXML
    void registerButtonAction(ActionEvent event) {
        //Window alert = btn_register.getScene().getWindow();
        registerPage regPage = new registerPage();

        regPage.startPage(event);
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btn_register != null : "fx:id=\"btn_register\" was not injected: check your FXML file 'login.fxml'.";
        assert pswdF_password != null : "fx:id=\"pswdF_password\" was not injected: check your FXML file 'login.fxml'.";
        assert ancP_login_main != null : "fx:id=\"ancP_login_main\" was not injected: check your FXML file 'login.fxml'.";
        assert txtF_email != null : "fx:id=\"txtF_user_name\" was not injected: check your FXML file 'login.fxml'.";
        assert btn_login != null : "fx:id=\"btn_login\" was not injected: check your FXML file 'login.fxml'.";

    }
}