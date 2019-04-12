package chillingMonsters.Pages.userProfilePage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import chillingMonsters.AlertHandler;
import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.UserProfile.UserProfileController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import javafx.event.ActionEvent;

public class userProfilePageController implements PageController {

    @FXML // fx:id="edit_pswd"
    private Button edit_pswd; // Value injected by FXMLLoader

    @FXML // fx:id="lbl_usrid"
    private Label lbl_usrid; // Value injected by FXMLLoader

    @FXML // fx:id="choiceB_gender"
    private ChoiceBox<?> choiceB_gender; // Value injected by FXMLLoader

    @FXML // fx:id="edit_name"
    private Button edit_name; // Value injected by FXMLLoader

    @FXML // fx:id="txtF_email"
    private TextField txtF_email; // Value injected by FXMLLoader

    @FXML // fx:id="pswdF_pswd"
    private PasswordField pswdF_pswd; // Value injected by FXMLLoader
    private boolean passChanged = false;    // default false

    @FXML // fx:id="edit_email"
    private Button edit_email; // Value injected by FXMLLoader

    @FXML // fx:id="edit_gdr"
    private Button edit_gdr; // Value injected by FXMLLoader

    @FXML // fx:id="btn_confirm"
    private Button btn_confirm; // Value injected by FXMLLoader

    @FXML // fx:id="btn_delete"
    private Button btn_delete; // Value injected by FXMLLoader

    @FXML // fx:id="txtF_name"
    private TextField txtF_name; // Value injected by FXMLLoader

    @FXML // fx:id="backButton"
    private ImageView backButton; // Value injected by FXMLLoader

    @FXML // fx:id="btn_logout"
    private Button btn_logout; // Value injected by FXMLLoader

    private String userName = null;
    private String userEmail = null;
    private String password = null;
    private String gender = null;


    @FXML
    void editNameButtonAction(ActionEvent event) {
        txtF_name.setDisable(false);
        btn_confirm.setDisable(false);
    }

    @FXML
    void editEmailButtonAction(ActionEvent event) {
        txtF_email.setDisable(false);
        btn_confirm.setDisable(false);
    }

    @FXML
    void editPswdButtonAction(ActionEvent event) {
        pswdF_pswd.clear();
        pswdF_pswd.setDisable(false);

        passChanged = true;
        btn_confirm.setDisable(false);
    }

    @FXML
    void editGdrButtonAction(ActionEvent event) {
        choiceB_gender.setDisable(false);
        btn_confirm.setDisable(false);
    }

    @FXML
    void deleteButtonAction(ActionEvent event) {
        UserProfileController profileCtrl = ControllerFactory.makeUserProfileController();

        if (AlertHandler.showCriticalAlert(Alert.AlertType.WARNING, "Delete your profile",
                "You will lose all your records on NutriMon. Are you sure?")){
            profileCtrl.deleteProfile();
//            PageFactory.getLoginPage().startPage(event);
        }
    }

    @FXML
    void logoutButtonAction(ActionEvent event) {
        if (btn_confirm.isDisabled()) {
//            PageFactory.getLoginPage().startPage(event);
            AlertHandler.showAlert(Alert.AlertType.CONFIRMATION, "See you soon!", "Logged out!");
        }
        else
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Oops!", "Please confirm your changes before logout!");
    }


    @FXML
    void confirmButtonAction(ActionEvent event) {
        UserProfileController profileCtrl = ControllerFactory.makeUserProfileController();
        String email;

        userName = txtF_name.getText();
        email = userEmail;
        userEmail = txtF_email.getText();
        if (passChanged)
            password = pswdF_pswd.getText();
        gender = choiceB_gender.getValue().toString();

        // Check the Email, not null missing '@', unique

        if (userEmail.isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Oops!", "Please enter your Email Address");
            return;
        }

        if (userEmail.indexOf('@') == -1) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "Invalid Email address");
            return;
        }
        if (!userEmail.equals(email))
            if (profileCtrl.exists(userEmail)) {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "This Email address has been used");
                return;
            }

        // Check name, not null
        if (userName.isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Oops!", "User name cannot be empty");
            return;
        }

        // Check the password, more than 8 digits
        if (password.isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Oops!", "Password cannot be empty");
            return;
        }

        if (password.length() < 8) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Failed...", "Your Password must contain at least 8 digits");
            return;
        }

        if (gender.equals(" -- "))
            gender = "";


        profileCtrl.updateProfile(userName, userEmail, password, gender);
        AlertHandler.showAlert(Alert.AlertType.CONFIRMATION, "Success!", "Your profile have been updated!");

        btn_confirm.setDisable(true);
        pswdF_pswd.setDisable(true);
        txtF_name.setDisable(true);
        txtF_email.setDisable(true);
        choiceB_gender.setDisable(true);
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    public void initialize() {

        UserProfileController profileCtrl = ControllerFactory.makeUserProfileController();
        Map<String, Object> profileList = profileCtrl.getProfile();
        userName = profileList.get("userName").toString();
        userEmail = profileList.get("userEmail").toString();
        password = profileList.get("password").toString();
        gender = profileList.get("gender").toString();

        // Preset and Disable fields
        lbl_usrid.setText(Long.toString(profileCtrl.getUserID()));

        txtF_name.setText(userName);
        txtF_name.setDisable(true);

        txtF_email.setText(userEmail);
        txtF_email.setDisable(true);

        pswdF_pswd.setText("-----------------------------");    // not showing the real password
        pswdF_pswd.setDisable(true);

        if (gender.equals(""))
            choiceB_gender.getSelectionModel().selectFirst();
        else if (gender.equals("male"))
            choiceB_gender.getSelectionModel().select(1);
        else if (gender.equals("female"))
            choiceB_gender.getSelectionModel().select(2);
        else
            choiceB_gender.getSelectionModel().select(3);
        choiceB_gender.setDisable(true);

        btn_confirm.setDisable(true);

    }
}
