package chillingMonsters.Pages.registerPage;

import chillingMonsters.Pages.PageFactory;
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
}
