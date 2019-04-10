package chillingMonsters;

import chillingMonsters.Pages.PageFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
	@Override
	public void start(Stage primaryStage) {
		StackPane appRoot = new StackPane();
		appRoot.setPrefSize(360, 740);

		Scene scene = new Scene(appRoot);
		scene.setFill(Color.TRANSPARENT);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());

		primaryStage.setScene(scene);
//		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setTitle("NutriMon - Don't waste, it's time to eat!");
		primaryStage.getIcons().add(new Image("/img/NutriMon logo.png"));
		primaryStage.setResizable(false);
		primaryStage.show();

		PageFactory.setApp(appRoot);
		System.out.println("Current page: Login page");
	}
}
