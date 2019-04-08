package chillingMonsters.Pages.recipePage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Recipe.RecipeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class recipePageController implements PageController {
	@FXML
	private VBox cardList;

	@FXML
	private ImageView menuButton;

	@FXML
	private Button recipeCreateButton;

	@FXML
	public void initialize() {
		RecipeController controller = ControllerFactory.makeRecipeController();
		List<Map<String, Object>> recipeList = controller.showSavedRecipes();

		if (recipeList.isEmpty()) {
			Label emptyLabel = new Label("We ain't got squash.");
			emptyLabel.getStyleClass().add("emptyWarningText");

			cardList.getChildren().add(emptyLabel);
			return;
		}

		for (int i = 0; i < recipeList.size(); i++) {
			Map<String, Object> recipe = recipeList.get(i);

			long id = (Long) recipe.get("recipeID");
			String name  = recipe.get("recipeName").toString();
			String category = recipe.get("recipeCategory").toString();
			float cookTime = (Float) recipe.get("recipeCookTime");
			//TODO: get calories here
//			float calories = (Float) recipe.get("recipeCalories");

			RecipeCardComponent sCard = new RecipeCardComponent(id, name, category, cookTime, 0);

			cardList.getChildren().add(sCard);
		}
	}

	@FXML
	void onMenuClicked(MouseEvent event) {
		ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());

		PageFactory.getSearchPage().startPage(e);
	}

	@FXML
	void recipeCreateButtonAction(ActionEvent event) {
		PageFactory.getSearchPage(PageOption.RECIPE).startPage(event);
	}
}
