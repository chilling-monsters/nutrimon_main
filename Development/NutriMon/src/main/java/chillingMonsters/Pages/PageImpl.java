package chillingMonsters.Pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class PageImpl implements Page {
	private String fxmlLocation;
	private String title;
	private String debugString;
	private PageController controller;

	public PageImpl(String fxmlLocation, String title, String debugString) {
		this.fxmlLocation = fxmlLocation;
		this.title = title;
		this.debugString = debugString;
	}

	public PageImpl(String fxmlLocation, String title, String debugString, PageController controller) {
		this(fxmlLocation, title, debugString);
		this.controller = controller;
	}

	public void startPage(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlLocation));

			if (controller != null) {
				loader.setController(controller);
			}

			Scene scene = new Scene(loader.load());

			scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());
			Stage stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
			stage.setScene(scene);
			stage.setTitle(title);

			stage.show();

			System.out.println(debugString);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
