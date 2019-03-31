package chillingMonsters.Pages.stockPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class StockCardComponent extends AnchorPane {

  @FXML
  private AnchorPane stockCardPane;

  public StockCardComponent() {
    super();

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/stockPage/stockCard.fxml"));

    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
