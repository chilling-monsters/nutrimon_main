package NMUserProfile;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;


public class loginController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btn_register"
    private Button btn_register; // Value injected by FXMLLoader

    @FXML // fx:id="pswdF_password"
    private PasswordField pswdF_password; // Value injected by FXMLLoader

    @FXML // fx:id="ancP_login_register"
    private AnchorPane ancP_login_register; // Value injected by FXMLLoader

    @FXML // fx:id="chkB_remem_me"
    private CheckBox chkB_remem_me; // Value injected by FXMLLoader

    @FXML // fx:id="lbl_user_name"
    private Label lbl_user_name; // Value injected by FXMLLoader

    @FXML // fx:id="lbl_user_login"
    private Label lbl_user_login; // Value injected by FXMLLoader

    @FXML // fx:id="ancP_login_main"
    private AnchorPane ancP_login_main; // Value injected by FXMLLoader

    @FXML // fx:id="txtF_user_name"
    private TextField txtF_user_name; // Value injected by FXMLLoader

    @FXML // fx:id="lbl_new_user"
    private Label lbl_new_user; // Value injected by FXMLLoader

    @FXML // fx:id="lbl_password"
    private Label lbl_password; // Value injected by FXMLLoader

    @FXML // fx:id="btn_login"
    private Button btn_login; // Value injected by FXMLLoader

    @FXML
    void loginButtonAction(ActionEvent event) {
        Window alert = btn_login.getScene().getWindow();
        userProfileQuery uq = new userProfileQuery();

        if (txtF_user_name.getText().isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, alert, "Oops!", "Please enter your User name");
            return;
        }

        if (pswdF_password.getText().isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, alert, "Oops!", "Please enter your Password");
            return;
        }

        if (!uq.checkCredentials(txtF_user_name.getText(), pswdF_password.getText())) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, alert, "Login Failed...", "Your User name or password combination doesn't exist");
            return;
        }

        Platform.exit(); // close the window -- for testing purposes
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btn_register != null : "fx:id=\"btn_register\" was not injected: check your FXML file 'login.fxml'.";
        assert pswdF_password != null : "fx:id=\"pswdF_password\" was not injected: check your FXML file 'login.fxml'.";
        assert ancP_login_register != null : "fx:id=\"ancP_login_register\" was not injected: check your FXML file 'login.fxml'.";
        assert chkB_remem_me != null : "fx:id=\"chkB_remem_me\" was not injected: check your FXML file 'login.fxml'.";
        assert lbl_user_name != null : "fx:id=\"lbl_user_name\" was not injected: check your FXML file 'login.fxml'.";
        assert lbl_user_login != null : "fx:id=\"lbl_user_login\" was not injected: check your FXML file 'login.fxml'.";
        assert ancP_login_main != null : "fx:id=\"ancP_login_main\" was not injected: check your FXML file 'login.fxml'.";
        assert txtF_user_name != null : "fx:id=\"txtF_user_name\" was not injected: check your FXML file 'login.fxml'.";
        assert lbl_new_user != null : "fx:id=\"lbl_new_user\" was not injected: check your FXML file 'login.fxml'.";
        assert lbl_password != null : "fx:id=\"lbl_password\" was not injected: check your FXML file 'login.fxml'.";
        assert btn_login != null : "fx:id=\"btn_login\" was not injected: check your FXML file 'login.fxml'.";

    }
}