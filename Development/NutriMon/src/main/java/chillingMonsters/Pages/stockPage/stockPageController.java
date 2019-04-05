package chillingMonsters.Pages.stockPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.StockController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class stockPageController implements PageController {
  @FXML
  public VBox cardList;

  @FXML
  public void initialize() {
    String EXPIRE_KEY = "Expires Soon";
    StockController controller = ControllerFactory.makeStockController();
    List<Map<String, Object>> stockList = controller.show();

    Map<String, List<StockCardComponent>> componentMap = new HashMap<>();

    List<StockCardComponent> expiresSoon = new ArrayList<>();
    componentMap.put(EXPIRE_KEY, expiresSoon);

    for (int i = 0; i < stockList.size(); i++) {
      Map<String, Object> stock = stockList.get(i);

      long id = (Long) stock.get("foodID");
      String name  = Utility.parseFoodName(stock.get("foodName").toString());
      double amount = (Double) stock.get("quantity");
      long exp = (Long) stock.get("next_exp");
      String category = stock.get("fCategory").toString();
      StockCardComponent sCard = new StockCardComponent(id, name, amount, exp, category);

      if (exp <= Utility.SPOILAGE_WARNING_DAYS) {
        expiresSoon.add(new StockCardComponent(id, name, amount, exp, category));
      }

      List<StockCardComponent> categoryGroup = componentMap.get(category);
      if (categoryGroup != null) {
        categoryGroup.add(sCard);
      } else {
        categoryGroup = new ArrayList<>();
        categoryGroup.add(sCard);
        componentMap.put(category, categoryGroup);
      }
    }

    if (!expiresSoon.isEmpty()) {
      addToList(EXPIRE_KEY, expiresSoon);
    }

    componentMap.remove(EXPIRE_KEY);

    for (String group : componentMap.keySet()) {
      addToList(group, componentMap.get(group));
    }
  }

  @FXML
  void stockCreateButtonAction(ActionEvent event) {
    PageFactory.getAddStockSearchPage().startPage(event);
  }

  private void addToList(String label, List<StockCardComponent> group) {
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

    for (StockCardComponent s : group) {
      s.getStyleClass().add("stockCard");
      cardList.getChildren().add(s);
    }
  }
}
