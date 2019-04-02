package chillingMonsters.Pages.stockPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.StockController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class stockPageController {
  @FXML
  public VBox cardList;

  @FXML
  private ObservableList<StockCardComponent> cards = FXCollections.observableArrayList();

  @FXML
  void initialize() {
    StockController controller = ControllerFactory.makeStockController();

    //TODO: fetch stock list here
    for (int i = 0; i < 3; i++) {
      StockCardComponent sCard = new StockCardComponent();
      cardList.getChildren().add(sCard);
    }
  }
}
