package chillingMonsters.Pages.searchPage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class searchPageController {
  @FXML
  public TextField searchTxF;

  @FXML
  public VBox searchList;

  @FXML
  void onSearchEnter(ActionEvent event) {
    System.out.println(searchTxF.getText());

  }
}
