package chillingMonsters.Pages.recipePage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Recipe.RecipeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.HashMap;
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

		Map<String, List<RecipeCardComponent>> componentMap = new HashMap<>();

		for (int i = 0; i < recipeList.size(); i++) {
			Map<String, Object> recipe = recipeList.get(i);

			long id = (Long) recipe.get("recipeID");
			String name  = recipe.get("recipeName").toString();
			String category = recipe.get("recipeCategory").toString();
			int cookTime = (Integer) recipe.get("recipeCookTime");
			//TODO: get calories here
//			float calories = (Float) recipe.get("recipeCalories");

			RecipeCardComponent sCard = new RecipeCardComponent(id, name, category, cookTime, 0);

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

	@FXML
	void onMenuClicked(MouseEvent event) {
		ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());

		PageFactory.getSearchPage().startPage(e);
	}

	@FXML
	void recipeCreateButtonAction(ActionEvent event) {
		PageFactory.getSearchPage(PageOption.RECIPE).startPage(event);
	}

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
