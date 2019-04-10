package chillingMonsters.Pages.stockPage;

import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class StockCardComponent extends AnchorPane {
  private long foodID;

  @FXML
  private Label cardName;

  @FXML
  private Label cardAmount;

  @FXML
  private Label cardExpDate;

  @FXML
  private Label cardCategory;

  public StockCardComponent(long foodID, String name, double amount, long minExpDate, String category) {
    super();
    this.foodID = foodID;

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/stockPage/stockCard.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    this.setOnMouseClicked(event -> handleOnClick());

    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    cardName.setText(name);
    cardAmount.setText(String.format("%.0fg", amount));
    cardCategory.setText(category);

    String expDate = minExpDate != 0
        ? String.format("%d day%s", minExpDate, Math.abs(minExpDate) > 1 ? "s" : "")
        : "Today";
    cardExpDate.setText(expDate);
  }

  private void handleOnClick() {
    PageFactory.toNextPage(PageFactory.getStockEntryPage(foodID, PageOption.DEFAULT));
  }
}
