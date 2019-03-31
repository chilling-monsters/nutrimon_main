package chillingMonsters.Pages.loginPage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class loginPage extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // load the FXML file
            AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("loginPage/login.fxml"));

            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());

            primaryStage.setScene(scene);

            primaryStage.setTitle("NutriMon - Don't waste, it's time to eat!");
            primaryStage.show();

            System.out.println("Current page: Login page");

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}