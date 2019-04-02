package chillingMonsters.Pages.stockPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.StockController;
import chillingMonsters.Pages.searchPage.searchPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class stockPageController {
  @FXML
  public VBox cardList;

  @FXML
  void initialize() {
    StockController controller = ControllerFactory.makeStockController();
//    List<Map<String, Object>> stockList = controller.showStock();

    //TODO: fetch stock list here
    for (int i = 0; i < 3; i++) {
      StockCardComponent sCard = new StockCardComponent();
      cardList.getChildren().add(sCard);
    }
  }

  @FXML
  void stockCreateButtonAction(ActionEvent event) {
    searchPage search = new searchPage();
    search.startPage(event);
  }
}
