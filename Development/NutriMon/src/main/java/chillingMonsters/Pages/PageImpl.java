package chillingMonsters.Pages;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public abstract class PageImpl implements Page {
	protected AnchorPane pagePane;

	protected String fxmlLocation;
	protected String title;
	protected String debugString;
	protected PageController controller;

	public PageImpl(String fxmlLocation, String title, String debugString, PageController controller) {
		this.fxmlLocation = fxmlLocation;
		this.title = title;
		this.debugString = debugString;

		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlLocation));

		if (controller != null) {
			loader.setController(controller);
			this.controller = controller;
		}

		try {
			pagePane = loader.load();
			if (controller == null) this.controller = loader.getController();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public PageImpl(String fxmlLocation, String title, String debugString) {
		this(fxmlLocation, title, debugString, null);
	}

	public AnchorPane getPagePane() { return pagePane; }
}
