package NMUserProfile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class loginPage extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // load the FXML file
            AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));

            Scene scene = new Scene(root,600,400);

            scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);

            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}