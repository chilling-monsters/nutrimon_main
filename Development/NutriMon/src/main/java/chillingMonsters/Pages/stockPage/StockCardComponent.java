package chillingMonsters.Pages.stockPage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class StockCardComponent extends AnchorPane {
  private long foodID = 0;

  @FXML
  private Label cardName;

  @FXML
  private Label cardAmount;

  @FXML
  private Label cardExpDate;

  public StockCardComponent(long foodID, String name, double amount, long minExpDate) {
    super();
    this.foodID = foodID;

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/stockPage/stockCard.fxml"));

    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);
    this.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        
      }
    });

    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    cardName.setText(name);
    cardAmount.setText(String.format("%.0f g", amount));
    cardExpDate.setText(String.format("%d days", minExpDate));
  }
}
