package chillingMonsters.Pages.stockPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Stock.StockController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.*;

public class stockPageController implements PageController {
  private static String EXPIRE_KEY = "Expires Soon";
  @FXML
  public VBox cardList;

  @FXML
  public ImageView menuButton;

  @FXML
  public void initialize() {
    StockController controller = ControllerFactory.makeStockController();
    List<Map<String, Object>> stockList = controller.showStocks();

    Map<String, List<StockCardComponent>> componentMap = new TreeMap<>();

    List<StockCardComponent> expiresSoon = new ArrayList<>();
    componentMap.put(EXPIRE_KEY, expiresSoon);

    if (stockList.isEmpty()) {
      Label emptyLabel = new Label("We ain't got squash.");
      emptyLabel.getStyleClass().add("emptyWarningText");

      cardList.getChildren().add(emptyLabel);
      return;
    }

    for (int i = 0; i < stockList.size(); i++) {
      Map<String, Object> stock = stockList.get(i);

      long id = Long.parseLong(stock.get("foodID").toString());
      String name  = Utility.parseFoodName(stock.get("foodName").toString());
      double amount = Double.parseDouble(stock.get("quantity").toString());
      long exp = Long.parseLong(stock.get("next_exp").toString());
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
    PageFactory.getSearchPage(PageOption.STOCK).startPage(event);
  }

  @FXML
  void onMenuClicked(MouseEvent event) {
    ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());

    PageFactory.getSearchPage().startPage(e);
  }

  private void addToList(String label, List<StockCardComponent> group) {
    Label groupLabel = new Label(Utility.toCapitalized(label));
    groupLabel.getStyleClass().add("labelText");

    if (label == EXPIRE_KEY) groupLabel.getStyleClass().add("hightlightText");

    Line underline = new Line();
    underline.setStartX(0.0f);
    underline.setStartY(100.0f);
    underline.setEndX(300.0f);
    underline.setEndY(100.0f);
    underline.getStyleClass().add("line");

    cardList.getChildren().add(groupLabel);
    cardList.getChildren().add(underline);

    for (StockCardComponent s : group) {
      s.getStyleClass().add("listCard");
      if (label == EXPIRE_KEY) s.getStyleClass().add("hightlightCard");
      cardList.getChildren().add(s);
    }
  }
}
