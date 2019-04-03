package chillingMonsters.Pages.searchPage;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SearchCardComponent extends AnchorPane {

  public SearchCardComponent() {
    super();

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/searchPage/searchCard.fxml"));

    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
