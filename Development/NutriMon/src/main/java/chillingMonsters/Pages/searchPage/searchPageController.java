package chillingMonsters.Pages.searchPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Recipe.RecipeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class searchPageController implements PageController {
	private PageOption option;
	public String searchQuery = "";

	private List<Map<String, Object>> rcpCache = new ArrayList<>();
	private List<Map<String, Object>> ingrCache = new ArrayList<>();

	@FXML
	public TextField searchTxF;

	@FXML
	public VBox searchList;

	@FXML
	public Label moreLabel;

	public searchPageController(PageOption option) {
		this.option = option;
	}

	@FXML
	public void initialize() {
		searchTxF.setText(searchQuery);
		rcpCache.clear();
		ingrCache.clear();

		moreLabel = new Label("More ...");
		moreLabel.setAlignment(Pos.CENTER);
		moreLabel.getStyleClass().addAll("headerText", "hightlightText", "card", "searchCardPane", "cardPane");
		moreLabel.setOnMouseClicked(event -> handleMoreOnClick());
	}

	public void refresh() {
		initialize();
		onSearchEnter();
	}

	//Event handlers
	@FXML
	public void onSearchEnter() {
		addCreateYourOwnCard();

		String currentSearch = searchTxF.getText().trim();
		if (currentSearch.isEmpty()) return;

		searchQuery = currentSearch;
		searchList.getChildren().clear();
		addCreateYourOwnCard();

		IngredientController ingr = ControllerFactory.makeIngredientController();
		RecipeController recp = ControllerFactory.makeRecipeController();

		List<Map<String, Object>> ingrSearchResult = new ArrayList<>();
		List<Map<String, Object>> recpSearchResult = new ArrayList<>();

		switch (option) {
			case STOCK:
				ingrSearchResult = ingr.searchIngredient(searchQuery);
				break;
			case RECIPE:
				recpSearchResult = recp.searchRecipe(searchQuery);
				break;
			case UPDATE:
				ingrSearchResult = ingr.searchIngredient(searchQuery);
				break;
			case DEFAULT:
			case INTAKE_RECIPE:
			case INTAKE_STOCK:
				ingrSearchResult = ingr.searchIngredient(searchQuery);
				recpSearchResult = recp.searchRecipe(searchQuery);
				break;
		}

		if (ingrSearchResult.isEmpty() && recpSearchResult.isEmpty()) {
			addEmptyWarningLabel();
			return;
		}

		for (Map<String, Object> result : recpSearchResult) {
			if (searchList.getChildren().size() >= Utility.CACHE_MAX_SIZE) {
				rcpCache.add(result);
			} else {
				renderRecipeCard(result);
			}
		}

		for (Map<String, Object> result : ingrSearchResult) {
			if (searchList.getChildren().size() >= Utility.CACHE_MAX_SIZE) {
				ingrCache.add(result);
			} else {
				renderIngrCard(result);
			}
		}

		addMoreButton();
	}

	//Event handlers
	private void handleMoreOnClick() {
		searchList.getChildren().remove(moreLabel);

		int i = 0;
		while (i < Utility.CACHE_MAX_SIZE) {
			if (rcpCache.isEmpty()) break;

			renderRecipeCard(rcpCache.get(0));
			rcpCache.remove(0);
			i++;
		}

		while (i < Utility.CACHE_MAX_SIZE) {
			if (ingrCache.isEmpty()) break;

			renderIngrCard(ingrCache.get(0));
			ingrCache.remove(0);
			i++;
		}

		addMoreButton();
	}

	//Helper functions
	private void renderRecipeCard(Map<String, Object> r) {
		Long recipeID = Utility.parseID(r.get("recipeID").toString(), 0);
		String name = Utility.toCapitalized(r.get("recipeName").toString());
		String category = r.get("recipeCategory").toString().toUpperCase();

		PageOption cardOption = PageOption.RECIPE;
		if (option == PageOption.INTAKE_STOCK) cardOption = PageOption.INTAKE_RECIPE;

		SearchCardComponent sCard = new SearchCardComponent(recipeID, name, category, cardOption);
		sCard.getStyleClass().add("hightlightCard");

		searchList.getChildren().add(sCard);
	}

	private void renderIngrCard(Map<String, Object> i) {
		Long foodId = Utility.parseID(i.get("foodID").toString(), 0);
		String name = Utility.parseFoodName(i.get("foodName").toString());
		String category = i.get("fCategory").toString().toUpperCase();

		SearchCardComponent sCard = new SearchCardComponent(foodId, name, category, option);

		searchList.getChildren().add(sCard);
	}

	private void addCreateYourOwnCard() {
		if (option != PageOption.RECIPE) return;
		if (searchList.getChildren().isEmpty()
			|| !(searchList.getChildren().get(0) instanceof NewRecipeSearchCard)) {
			NewRecipeSearchCard nCard = new NewRecipeSearchCard();
			searchList.getChildren().add(0, nCard);
		}
	}

	private void addEmptyWarningLabel() {
		Label emptyLabel = new Label("We ain't got squash.");
		emptyLabel.getStyleClass().add("emptyWarningText");

		searchList.getChildren().add(emptyLabel);
	}

	private void addMoreButton() {
		if (rcpCache.size() > 0 || ingrCache.size() > 0) {
			searchList.getChildren().add(moreLabel);
		}
	}
}
