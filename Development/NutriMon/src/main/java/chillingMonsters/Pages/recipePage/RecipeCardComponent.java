package chillingMonsters.Pages.recipePage;

import chillingMonsters.Pages.PageFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class RecipeCardComponent extends AnchorPane {
	private long recipeID;

	@FXML
	private Label cardName;

	@FXML
	private Label cardCategory;

	@FXML
	private Label cardCookTime;

	@FXML
	private Label cardCalories;

	public RecipeCardComponent(long recipeID, String name, String category, int time, int calories) {
		super();
		this.recipeID = recipeID;

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/recipePage/recipeCard.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.setOnMouseClicked(event -> handleOnClick());

		cardName.setText(name);
		cardCategory.setText(category.toUpperCase());
		cardCookTime.setText(String.format("%d mins", time));
		cardCalories.setText(String.format("%d Cal", calories));
	}

	public void handleOnClick() {
		PageFactory.toNextPage(PageFactory.getRecipeEntryPage(recipeID));
	}
}
