package chillingMonsters.Pages.stockPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.StockController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class stockPageController implements PageController {
  @FXML
  public VBox cardList;

  @FXML
  public void initialize() {
    StockController controller = ControllerFactory.makeStockController();
    List<Map<String, Object>> stockList = controller.show();

    for (Map<String, Object> stock : stockList) {
      long id = (Long) stock.get("foodID");
      String name  = controller.parseFoodName(stock.get("foodName").toString());
      double amount = (Double) stock.get("quantity");
      long exp = (Long) stock.get("next_exp");
      String category = stock.get("fCategory").toString();
      StockCardComponent sCard = new StockCardComponent(id, name, amount, exp, category);

      cardList.getChildren().add(sCard);
    }
  }

  @FXML
  void stockCreateButtonAction(ActionEvent event) {
    PageFactory.getAddStockSearchPage().startPage(event);
  }
}
