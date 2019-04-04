package chillingMonsters.Pages.searchPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.IngredientController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class searchPageController implements PageController {
  private PageOption type;

  @FXML
  public TextField searchTxF;

  @FXML
  public VBox searchList;

  public searchPageController(PageOption type) {
    this.type = type;
  }

  @FXML
  void onSearchEnter(ActionEvent event) {
    searchList.getChildren().clear();

    IngredientController controller = ControllerFactory.makeIngredientController();
    List<Map<String, Object>> searchResult = controller.search(searchTxF.getText());

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

  }
}
