package chillingMonsters.Pages.searchPage;

import chillingMonsters.Pages.PageFactory;
import javafx.fxml.FXMLLoader;
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

		this.setOnMouseClicked(event -> handleOnClick());
	}

	public void handleOnClick() {
		PageFactory.toNextPage(PageFactory.getRecipeCreatePage());
	}
}
