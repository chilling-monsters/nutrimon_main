package chillingMonsters.Pages.searchPage;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SearchCardComponent extends AnchorPane {
  private long foodID = 0;

  @FXML
  private Label cardName;

  @FXML
  private Label cardLabel;

  public SearchCardComponent(long foodID, String name, String category) {
    super();
    this.foodID = foodID;

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/searchPage/searchCard.fxml"));

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
    cardLabel.setText(category);
  }
}
