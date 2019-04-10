package chillingMonsters.Pages.searchPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Recipe.RecipeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class searchPageController implements PageController {
  public String searchQuery = "";
  private PageOption option;
  private LinkedHashMap<String, List<SearchCardComponent>> cachedResults;

  @FXML
  public TextField searchTxF;

  @FXML
  public VBox searchList;

  public searchPageController(PageOption option) {
    this.option = option;
    this.cachedResults = new LinkedHashMap<String, List<SearchCardComponent>>() {
      @Override
      protected boolean removeEldestEntry(Map.Entry<String, List<SearchCardComponent>> eldest) {
        return size() > Utility.CACHE_MAX_SIZE;
      }
    };
  }

  @FXML
  void onSearchEnter() {
    addCreateYourOwnCard();

    String currentSearch = searchTxF.getText().trim();
    if (currentSearch.isEmpty()) return;

    List<SearchCardComponent> cache = cachedResults.get(currentSearch);
    if (cache != null) {
      if (!searchList.getChildren().contains(cache)) {
        searchList.getChildren().clear();
        addCreateYourOwnCard();
        searchList.getChildren().addAll(cache);
      }
    } else {
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
          ingrSearchResult = ingr.searchIngredient(searchQuery);
          recpSearchResult = recp.searchRecipe(searchQuery);
          break;
      }

      if (ingrSearchResult.isEmpty() && recpSearchResult.isEmpty()) {
        addEmptyWarningLabel();
        return;
      }

      for (Map<String, Object> result : recpSearchResult) {
        Long recipeID = Long.parseLong(result.get("recipeID").toString());
        String name = Utility.parseFoodName(result.get("recipeName").toString());
        String category = result.get("recipeCategory").toString().toUpperCase();

        SearchCardComponent sCard = new SearchCardComponent(recipeID, name, category, PageOption.RECIPE);
        if (option == PageOption.DEFAULT) sCard.getStyleClass().add("hightlightCard");

        showAndSaveCache(sCard);
      }

      for (Map<String, Object> result : ingrSearchResult) {
        Long foodId = Long.parseLong(result.get("foodID").toString());
        String name = Utility.parseFoodName(result.get("foodName").toString());
        String category = result.get("fCategory").toString().toUpperCase();

        SearchCardComponent sCard = new SearchCardComponent(foodId, name, category, option);

        showAndSaveCache(sCard);
      }
    }
  }

  @FXML
  public void initialize() {
    searchTxF.setText(searchQuery);
    onSearchEnter();
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

  private void showAndSaveCache(SearchCardComponent sCard) {
    searchList.getChildren().add(sCard);

    List<SearchCardComponent> cache = cachedResults.get(searchQuery);
    if (cache == null) {
      cache = new ArrayList<>();
      cache.add(sCard);
      cachedResults.put(searchQuery, cache);
    } else {
      cache.add(sCard);
    }
  }
}
