package chillingMonsters.Pages.landingPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Recipe.RecipeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.recipePage.RecipeCardComponent;
import chillingMonsters.Utility;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class landingPageController implements PageController {
	@FXML
	private VBox cardList;

	@FXML
	public void initialize() {
		cardList.getChildren().clear();
	}

	public void refresh() {
		initialize();
		RecipeController controller = ControllerFactory.makeRecipeController();
		List<Map<String, Object>> results = controller.showAvailableRecipes();

		if (results.isEmpty()) {
			Label emptyLabel = new Label("We ain't got squash.");
			emptyLabel.getStyleClass().add("emptyWarningText");

			cardList.getChildren().add(emptyLabel);
			return;
		}

		Map<String, List<RecipeCardComponent>> componentMap = new TreeMap<>(Utility.parseRecipeComparator());

		for (Map<String, Object> recipe : results) {
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
		}

		for (String label : componentMap.keySet()) {
			addToList(label, componentMap.get(label));
		}

	}

	//helper functions
	private void addToList(String label, List<RecipeCardComponent> group) {
		Label groupLabel = new Label(Utility.toCapitalized(label));

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
