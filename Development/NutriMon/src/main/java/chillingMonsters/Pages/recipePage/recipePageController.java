package chillingMonsters.Pages.recipePage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Recipe.RecipeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class recipePageController implements PageController {
	private String CREATED_BY_YOU_KEY = "Created by you";
	@FXML
	private VBox cardList;

	@FXML
	private Button recipeCreateButton;

	@FXML
	public void initialize() {
		cardList.getChildren().clear();
	}

	public void refresh() {
		initialize();
		RecipeController controller = ControllerFactory.makeRecipeController();
		List<Map<String, Object>> recipeList = controller.showSavedRecipes();

		if (recipeList.isEmpty()) {
			Label emptyLabel = new Label("We don't have any squash recipes yet");
			emptyLabel.getStyleClass().add("emptyWarningText");

			cardList.getChildren().add(emptyLabel);
			return;
		}


		Map<String, List<RecipeCardComponent>> componentMap = new TreeMap<>(Utility.parseRecipeComparator());
		List<RecipeCardComponent> byYou = new ArrayList<>();
		componentMap.put(CREATED_BY_YOU_KEY, byYou);

		for (Map<String, Object> recipe : recipeList) {
			long id = Utility.parseID(recipe.get("recipeID").toString(), 0);
			String name  = Utility.toCapitalized(recipe.get("recipeName").toString());
			String category = recipe.get("recipeCategory").toString();
			int cookTime = Integer.parseInt(recipe.get("recipeCookTime").toString());
			int calories = 0;
			if (recipe.get("caloriesPerServing") != null) {
				calories = Integer.parseInt(recipe.get("caloriesPerServing").toString());
			}

			RecipeCardComponent sCard = new RecipeCardComponent(id, name, category, cookTime, calories);

			List<RecipeCardComponent> group = componentMap.get(category);
			if (group == null) {
				group = new ArrayList<>();
				componentMap.put(category, group);
			}

			group.add(sCard);

			if (Utility.parseID(recipe.get("userID").toString(), 0) == ControllerFactory.makeUserProfileController().getUserID()) {
				RecipeCardComponent yourCard = new RecipeCardComponent(id, name, category, cookTime, calories);
				yourCard.getStyleClass().add("hightlightCard");
				byYou.add(yourCard);
			}
		}

		if (!byYou.isEmpty()) {
			addToList(CREATED_BY_YOU_KEY, byYou);
		}

		componentMap.remove(CREATED_BY_YOU_KEY);

		for (String label : componentMap.keySet()) {
			addToList(label, componentMap.get(label));
		}
	}

	//button event handler
	@FXML
	void recipeCreateButtonAction() {
		PageFactory.toNextPage(PageFactory.getSearchPage(PageOption.RECIPE));
	}

	//helper functions
	private void addToList(String label, List<RecipeCardComponent> group) {
		Label groupLabel = new Label(Utility.toCapitalized(label));
		if (label == CREATED_BY_YOU_KEY) groupLabel.getStyleClass().add("hightlightText");

		groupLabel.getStyleClass().add("labelText");

		Line underline = new Line();
		underline.setStartX(0.0f);
		underline.setStartY(100.0f);
		underline.setEndX(300.0f);
		underline.setEndY(100.0f);
		underline.getStyleClass().add("line");

		cardList.getChildren().add(groupLabel);
		cardList.getChildren().add(underline);

		for (RecipeCardComponent s : group) {
			s.getStyleClass().add("listCard");
			cardList.getChildren().add(s);
		}
	}

}
