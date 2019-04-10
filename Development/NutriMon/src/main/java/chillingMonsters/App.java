package chillingMonsters;

import chillingMonsters.Pages.PageFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
	@Override
	public void start(Stage primaryStage) {
		StackPane appRoot = new StackPane();

		Scene scene = new Scene(appRoot);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.setHeight(740);
		primaryStage.setWidth(360);
		primaryStage.setTitle("NutriMon - Don't waste, it's time to eat!");
		primaryStage.getIcons().add(new Image("img/NutriMon logo.png"));
		primaryStage.setResizable(false);
		primaryStage.show();

		PageFactory.setApp(appRoot);
		System.out.println("Current page: Login page");
	}
}
