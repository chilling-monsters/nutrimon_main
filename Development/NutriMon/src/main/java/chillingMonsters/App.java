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
		AnchorPane login = PageFactory.getLoginPage().getPagePane();
		StackPane appRoot = new StackPane(login);

		Scene scene = new Scene(appRoot);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.setTitle("NutriMon - Don't waste, it's time to eat!");
		primaryStage.getIcons().add(new Image("img/NutriMon logo.png"));
		primaryStage.setResizable(true);
		primaryStage.show();

		PageFactory.setApp(appRoot);
		System.out.println("Current page: Login page");
	}
}
