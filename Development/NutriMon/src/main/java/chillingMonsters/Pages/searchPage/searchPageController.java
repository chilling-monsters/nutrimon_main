package chillingMonsters.Pages.searchPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
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
import java.util.HashMap;
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
    searchQuery = searchTxF.getText();
    searchList.getChildren().clear();

    List<Map<String, Object>> ingrSearchResult = new ArrayList<>();
    List<Map<String, Object>> recpSearchResult = new ArrayList<>();

    IngredientController ingr = ControllerFactory.makeIngredientController();
    //TODO: use recipe controller here
//        RecipeController recp = ControllerFactory.makeIngredientController();
    switch (option) {
      case ADD_STOCK:
        ingrSearchResult = ingr.search(searchQuery);
        break;
      case ADD_RECIPE:
//        recpSearchResult = recp.search(searchQuery);
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
        recpSearchResult.add(card1);
        recpSearchResult.add(card2);
        break;
      case DEFAULT:
        ingrSearchResult = ingr.search(searchQuery);
//        recpSearchResult = recp.search(searchQuery);
        break;
    }

    if (ingrSearchResult.isEmpty() && recpSearchResult.isEmpty() && option != PageOption.ADD_RECIPE) {
      Label emptyLabel = new Label("We ain't got squash.");
      emptyLabel.getStyleClass().add("emptyWarningText");

      searchList.getChildren().add(emptyLabel);
      return;
    }

    for (Map<String, Object> result : ingrSearchResult) {
      Long foodId = (Long) result.get("foodID");
      String name = Utility.parseFoodName(result.get("foodName").toString());
      String category = result.get("fCategory").toString().toUpperCase();

      SearchCardComponent sCard = new SearchCardComponent(foodId, name, category, option);

      searchList.getChildren().add(sCard);
    }

    for (Map<String, Object> result : recpSearchResult) {
      Long recipeID = (Long) result.get("recipeID");
      String name = Utility.parseFoodName(result.get("recipeName").toString());
      String category = result.get("recipeCategory").toString().toUpperCase();

      SearchCardComponent sCard = new SearchCardComponent(recipeID, name, category, option);

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

    if (option == PageOption.ADD_RECIPE) {
      NewRecipeSearchCard nCard = new NewRecipeSearchCard();
      searchList.getChildren().add(nCard);
    }
  }

  private void handleOnClick(MouseEvent event) {
    ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());

    PageFactory.getLastPage().startPage(e);
  }
}
