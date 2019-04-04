package chillingMonsters.Pages.stockPage;

import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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

    this.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        handleOnClick(event);
      }
    });

    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    cardName.setText(name);
    cardAmount.setText(String.format("%.0fg", amount));
    cardExpDate.setText(String.format("%d days", minExpDate));
    cardCategory.setText(category);
  }

  private void handleOnClick(MouseEvent event) {
    ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());

    PageFactory.getStockEntryPage(foodID).startPage(e);
  }
}
