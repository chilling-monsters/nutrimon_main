package chillingMonsters.Pages.searchPage;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class searchPage {

  public void startPage(ActionEvent event) {
    try {
      Parent stockRoot = FXMLLoader.load(getClass().getClassLoader().getResource("searchPage/search.fxml"));

      Scene searchScene = new Scene(stockRoot);
      searchScene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());

      Stage searchStage = (Stage)((Node)(event.getSource())).getScene().getWindow();
      searchStage.setScene(searchScene);
      searchStage.setTitle("Search");

      searchStage.show();

      System.out.println("Current page: Search Page");

    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}
