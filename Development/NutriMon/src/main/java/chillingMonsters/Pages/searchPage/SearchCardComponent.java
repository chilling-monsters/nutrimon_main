package chillingMonsters.Pages.searchPage;

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

public class SearchCardComponent extends AnchorPane {
  private PageOption option = PageOption.DEFAULT;
  private long foodID;


  @FXML
  private Label cardName;

  @FXML
  private Label cardLabel;

  public SearchCardComponent(long foodID, String name, String category, PageOption option) {
    super();

    this.foodID = foodID;
    this.option = option;

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/searchPage/searchCard.fxml"));
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
    cardLabel.setText(category);
  }

  private void handleOnClick(MouseEvent event) {
    ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());

    switch (option) {
      case ADD_STOCK:
        PageFactory.getStockEntryPage(foodID, option).startPage(e);
        break;
      case ADD_RECIPE:
        System.out.println("Add Recipe");
        break;
      case DEFAULT:
        PageFactory.getIngredientPage(foodID).startPage(e);
        break;
    }
  }
}