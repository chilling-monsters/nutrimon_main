package chillingMonsters.Pages.registerPage;

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
            Parent regRoot = FXMLLoader.load(getClass().getClassLoader().getResource("registerPage/register.fxml"));

            Scene regScene = new Scene(regRoot);

            regScene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());

            Stage regStage = (Stage)((Node)(event.getSource())).getScene().getWindow();
            regStage.setScene(regScene);
            regStage.setTitle("Register");

            regStage.show();

            System.out.println("Current page: Register Page");

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToLogin(ActionEvent event) {
        try {
            Parent logRoot = FXMLLoader.load(getClass().getClassLoader().getResource("loginPage/login.fxml"));

            Scene logScene = new Scene(logRoot);

            Stage logStage = (Stage)((Node)(event.getSource())).getScene().getWindow();
            logStage.setScene(logScene);
            logStage.setTitle("Login");

            logStage.show();

            System.out.println("Current page: Login page (from register)");

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
