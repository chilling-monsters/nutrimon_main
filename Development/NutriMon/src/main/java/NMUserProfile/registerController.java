package NMUserProfile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;

public class registerController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtF_confirm_password"
    private TextField pswdF_confirm_password; // Value injected by FXMLLoader

    @FXML // fx:id="choiceB_gender"
    private ChoiceBox choiceB_gender; // Value injected by FXMLLoader

    @FXML // fx:id="txtF_email"
    private TextField txtF_email; // Value injected by FXMLLoader

    @FXML // fx:id="btn_cancel"
    private Button btn_cancel; // Value injected by FXMLLoader

    @FXML // fx:id="btn_confirm"
    private Button btn_confirm; // Value injected by FXMLLoader

    @FXML // fx:id="txtF_name"
    private TextField txtF_name; // Value injected by FXMLLoader

    @FXML // fx:id="txtF_new_password"
    private TextField pswdF_new_password; // Value injected by FXMLLoader


    @FXML
    void confirmButtonAction(ActionEvent event) {
        Window alert = btn_confirm.getScene().getWindow();
        userProfileQuery uq = new userProfileQuery();

        // Check all boxes, not null
        if (txtF_email.getText().isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, alert, "Oops!", "Please enter your Email Address");
            return;
        }

        if (txtF_name.getText().isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, alert, "Oops!", "Please enter your Name");
            return;
        }

        if (pswdF_new_password.getText().isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, alert, "Oops!", "Please enter your Password");
            return;
        }

        if (pswdF_confirm_password.getText().isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, alert, "Oops!", "Please re-enter your Password");
            return;
        }

        if (choiceB_gender.getSelectionModel().getSelectedItem() == null) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, alert, "Oops!", "Please choose your Gender");
            return;
        }

        // Check the Email, missing '@', unique
        String email = txtF_email.getText();
        if (email.indexOf('@') == -1) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, alert, "Failed...", "Invalid Email address");
            return;
        }

        if (uq.strExists("userProfile", "userEmail", email)) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, alert, "Failed...", "This Email address has been used");
            return;
        }

        // Check the password, passwords must match, more than 8 digits
        String new_password = pswdF_new_password.getText();
        String confirm_password = pswdF_confirm_password.getText();
        if (!new_password.equals(confirm_password)) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, alert, "Failed...", "Your Passwords does not match");
            return;
        }

        if (new_password.length() < 8) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, alert, "Failed...", "Your Password must contain at least 8 digits");
            return;
        }


        String name = txtF_name.getText();
        String gender = (String)choiceB_gender.getSelectionModel().getSelectedItem();
        uq.insertProfile(email, name, new_password, gender);

        AlertHandler.showAlert(Alert.AlertType.CONFIRMATION, alert, "Success!", "Your profile has been created. Welcome to Nutrimon!");

        // Back to login Page
        registerPage regPage = new registerPage();
        regPage.backToLogin(event);
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {
        // System.out.println("canceled");
        registerPage regPage = new registerPage();

        regPage.backToLogin(event);
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        assert pswdF_confirm_password != null : "fx:id=\"txtF_confirm_password\" was not injected: check your FXML file 'register.fxml'.";
        assert choiceB_gender != null : "fx:id=\"choiceB_gender\" was not injected: check your FXML file 'register.fxml'.";
        assert txtF_email != null : "fx:id=\"txtF_email\" was not injected: check your FXML file 'register.fxml'.";
        assert btn_cancel != null : "fx:id=\"btn_cancel\" was not injected: check your FXML file 'register.fxml'.";
        assert btn_confirm != null : "fx:id=\"btn_confirm\" was not injected: check your FXML file 'register.fxml'.";
        assert txtF_name != null : "fx:id=\"txtF_name\" was not injected: check your FXML file 'register.fxml'.";
        assert pswdF_new_password != null : "fx:id=\"txtF_new_password\" was not injected: check your FXML file 'register.fxml'.";

        choiceB_gender.getItems().addAll("male", "female", "other");
        // pswdF_new_password.setTooltip(new Tooltip("Password must have no less than 8 digits"));
    }
}
