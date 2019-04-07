package chillingMonsters.Pages.recipePage;

import chillingMonsters.Controllers.ControllerFactory;
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

import java.sql.Timestamp;
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
		//TODO: use recipe controller here
		//RecipeController controller = ControllerFactory.makeRecipeController();
		//List<Map<String, Object>> recipeList = controller.show();
		Map<String, Object> card1 = new HashMap<String, Object>() {{
			put("recipeID", 1L);
			put("recipeName", "Honey Roasted Turkey");
			put("recipeCategory", "Dinner");
			put("recipeCookTime", 125L);
			put("recipeCalories", 1345F);
		}};
		Map<String, Object> card2 = new HashMap<String, Object>() {{
			put("recipeID", 2L);
			put("recipeName", "Shrimp Fried Rice");
			put("recipeCategory", "Lunch");
			put("recipeCookTime", 35L);
			put("recipeCalories", 450.0F);
		}};

		List<Map<String, Object>> recipeList = new ArrayList<Map<String, Object>>() {{
			add(card1);
			add(card2);
			add(card1);
			add(card2);
			add(card1);
			add(card2);
			add(card1);
			add(card2);
			add(card1);
			add(card2);
			add(card1);
			add(card2);
		}};

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
			long cookTime = (Long) recipe.get("recipeCookTime");
			float calories = (Float) recipe.get("recipeCalories");

			RecipeCardComponent sCard = new RecipeCardComponent(id, name, category, cookTime, calories);

			cardList.getChildren().add(sCard);
		}
	}

	@FXML
	void onMenuClicked(MouseEvent event) {
		ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());

		//TODO: replace with legit menu action
		PageFactory.getStockPage().startPage(e);
	}

	@FXML
	void recipeCreateButtonAction(ActionEvent event) {
		PageFactory.getSearchPage(PageOption.ADD_RECIPE).startPage(event);
	}
}
