package chillingMonsters.Pages.stockPage;

import chillingMonsters.Pages.Page;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class stockPage extends Page {
  public stockPage() {
    super("stockPage/stock.fxml", "Your Stock", "Current page: Stock Page");
  }
}
