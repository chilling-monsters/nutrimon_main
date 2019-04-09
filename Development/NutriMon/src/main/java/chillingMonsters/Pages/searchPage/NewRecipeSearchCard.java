package chillingMonsters.Pages.searchPage;

import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class NewRecipeSearchCard extends AnchorPane {
	public NewRecipeSearchCard() {
		super();

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/searchPage/newRecipeSearchCard.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				handleOnClick(event);
			}
		});
	}

	public void handleOnClick(MouseEvent event) {
		ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());
		PageFactory.getRecipeCreatePage().startPage(e);
	}
}
