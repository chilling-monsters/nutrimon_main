package chillingMonsters.Pages.stockPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class StockCardComponent extends AnchorPane {
  private final int foodID = 0;
  private final String foodName = "Dumb dumb dumb";
  private final float amount = 0;
  private final int minExpDate = 0;

  @FXML
  private Label cardName;

  @FXML
  private Label cardAmount;

  @FXML
  private Label cardExpDate;

  public StockCardComponent(/*int foodID, String name, float amount, int minExpDate*/) {
    super();

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/stockPage/stockCard.fxml"));

    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    cardName.setText(foodName);
    cardAmount.setText(String.format("%.1f servings", amount));
    cardExpDate.setText(String.format("%d days", minExpDate));
  }
}
