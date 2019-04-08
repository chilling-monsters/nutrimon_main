package chillingMonsters.Pages.searchPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Recipe.RecipeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class searchPageController implements PageController {
  public String searchQuery = "";
  private PageOption option;

  @FXML
  public TextField searchTxF;

  @FXML
  public VBox searchList;

  @FXML
  public ImageView backButton;

  public searchPageController(PageOption option) {
    this.option = option;
  }

  @FXML
  void onSearchEnter(ActionEvent event) {
    if (searchQuery == searchTxF.getText() || searchTxF.getText().isEmpty()) {
      return;
    }

    searchQuery = searchTxF.getText();
    searchList.getChildren().clear();

    List<Map<String, Object>> ingrSearchResult = new ArrayList<>();
    List<Map<String, Object>> recpSearchResult = new ArrayList<>();

    IngredientController ingr = ControllerFactory.makeIngredientController();
    RecipeController recp = ControllerFactory.makeRecipeController();

    switch (option) {
      case STOCK:
        ingrSearchResult = ingr.searchIngredient(searchQuery);
        break;
      case RECIPE:
        recpSearchResult = recp.searchRecipe(searchQuery);
        addCreateYourOwnCard();
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
      Long recipeID = (Long) result.get("recipeID");
      String name = Utility.parseFoodName(result.get("recipeName").toString());
      String category = result.get("recipeCategory").toString().toUpperCase();

      SearchCardComponent sCard = new SearchCardComponent(recipeID, name, category, PageOption.RECIPE);
      if (option == PageOption.DEFAULT) sCard.getStyleClass().add("hightlightCard");

      searchList.getChildren().add(sCard);
    }

    for (Map<String, Object> result : ingrSearchResult) {
      Long foodId = (Long) result.get("foodID");
      String name = Utility.parseFoodName(result.get("foodName").toString());
      String category = result.get("fCategory").toString().toUpperCase();

      SearchCardComponent sCard = new SearchCardComponent(foodId, name, category, option);

      searchList.getChildren().add(sCard);
    }
  }

  @FXML
  public void initialize() {
    searchTxF.setText(searchQuery);

    backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        handleOnClick(event);
      }
    });

    if (option == PageOption.RECIPE) {
      addCreateYourOwnCard();
    }
  }

  private void handleOnClick(MouseEvent event) {
    ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());

    PageFactory.getLastPage().startPage(e);
  }

  private void addCreateYourOwnCard() {
    NewRecipeSearchCard nCard = new NewRecipeSearchCard();
    searchList.getChildren().add(nCard);
  }

  private void addEmptyWarningLabel() {
    Label emptyLabel = new Label("We ain't got squash.");
    emptyLabel.getStyleClass().add("emptyWarningText");

    searchList.getChildren().add(emptyLabel);
  }
}
