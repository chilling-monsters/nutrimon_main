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

import java.util.List;
import java.util.Map;

public class searchPageController implements PageController {
  public String searchQuery = "";
  private PageOption type;

  @FXML
  public TextField searchTxF;

  @FXML
  public VBox searchList;

  @FXML
  public ImageView backButton;

  public searchPageController(PageOption type) {
    this.type = type;
  }

  @FXML
  void onSearchEnter(ActionEvent event) {
    searchQuery = searchTxF.getText();
    searchList.getChildren().clear();

    IngredientController controller = ControllerFactory.makeIngredientController();
    List<Map<String, Object>> searchResult = controller.searchIngredient(searchTxF.getText());

    if (searchResult.isEmpty()) {

      Label emptyLabel = new Label("We ain't got squash.");
      emptyLabel.getStyleClass().add("emptyWarningText");

      searchList.getChildren().add(emptyLabel);
      return;
    }

    for (Map<String, Object> result : searchResult) {
      Long foodId = (Long) result.get("foodID");
      String name = Utility.parseFoodName(result.get("foodName").toString());
      String category = result.get("fCategory").toString();

      SearchCardComponent sCard = new SearchCardComponent(foodId, name, category, type);

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
  }

  private void handleOnClick(MouseEvent event) {
    ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());

    PageFactory.getLastPage().startPage(e);
  }
}
