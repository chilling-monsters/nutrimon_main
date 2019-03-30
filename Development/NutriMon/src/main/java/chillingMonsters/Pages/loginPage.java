package chillingMonsters.Pages;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

import java.io.IOException;

public class loginPage extends Application {
    /*
    private double xOffset = 0;
    private double yOffset = 0;
    */
    @Override
    public void start(Stage primaryStage) {
        try {
            // load the FXML file
            AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));

            Scene scene = new Scene(root,382,828);

            scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());

            /*
            primaryStage.initStyle(StageStyle.UNDECORATED);
            //grab your root here
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            //move around here
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });
            */

            primaryStage.setScene(scene);
            primaryStage.setMinWidth(382);
            primaryStage.setMinHeight(828);
            primaryStage.setMaxWidth(382);
            primaryStage.setMaxHeight(828);
            //primaryStage.setFullScreen(true);
            primaryStage.setTitle("NutriMon - Don't waste, it's time to eat!");
            primaryStage.show();

            System.out.println("Current page: Login page");

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}