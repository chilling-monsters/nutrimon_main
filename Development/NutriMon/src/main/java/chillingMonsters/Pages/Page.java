package chillingMonsters.Pages;

import javafx.scene.layout.AnchorPane;

public interface Page {
	AnchorPane getPagePane();
	void refresh();
}