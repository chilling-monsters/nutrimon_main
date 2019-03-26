package NMUserProfile;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import java.io.IOException;

public class registerPage {

    public void startPage(ActionEvent event) {
        try {
            Parent regRoot = FXMLLoader.load(getClass().getClassLoader().getResource("register.fxml"));

            Scene regScene = new Scene(regRoot);

            Stage regStage = (Stage)((Node)(event.getSource())).getScene().getWindow();
            regStage.setScene(regScene);
            regStage.setTitle("Register");

            regStage.show();

            System.out.println("INFO: Register page invoked");

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToLogin(ActionEvent event) {
        try {
            Parent logRoot = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));

            Scene logScene = new Scene(logRoot);

            Stage logStage = (Stage)((Node)(event.getSource())).getScene().getWindow();
            logStage.setScene(logScene);
            logStage.setTitle("Login");

            logStage.show();

            System.out.println("INFO: Login page retrieved");

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
