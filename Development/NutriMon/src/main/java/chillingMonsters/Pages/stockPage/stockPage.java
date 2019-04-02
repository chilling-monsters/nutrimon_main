package chillingMonsters.Pages.stockPage;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class stockPage {

  public void startPage(ActionEvent event) {
    try {
      Parent stockRoot = FXMLLoader.load(getClass().getClassLoader().getResource("stockPage/stock.fxml"));

      Scene stockScene = new Scene(stockRoot);

      Stage stockStage = (Stage)((Node)(event.getSource())).getScene().getWindow();
      stockStage.setScene(stockScene);
      stockStage.setTitle("Your Stock");

      stockStage.show();

      System.out.println("Current page: Stock Page");

    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}
