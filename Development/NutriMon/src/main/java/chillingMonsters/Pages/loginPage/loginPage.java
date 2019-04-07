package chillingMonsters.Pages.loginPage;

import chillingMonsters.Pages.Page;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class loginPage extends Application implements Page {
    @Override
    public void start(Stage primaryStage) {
        try {
            // load the FXML file
            AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("loginPage/login.fxml"));

            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());

            primaryStage.setScene(scene);

            primaryStage.setTitle("NutriMon - Don't waste, it's time to eat!");
            primaryStage.getIcons().add(new Image("img/NutriMon logo.png"));
            primaryStage.setResizable(false);
            primaryStage.show();

            System.out.println("Current page: Login page");

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void startPage(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getClassLoader().getResource("loginPage/login.fxml"));

            Scene loginScene = new Scene(loginRoot);

            loginScene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());

            Stage loginStage = (Stage)((Node)(event.getSource())).getScene().getWindow();
            loginStage.setScene(loginScene);
            loginStage.setTitle("NutriMon - Don't waste, it's time to eat!");

            loginStage.show();

            System.out.println("Current page: Login Page");

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}