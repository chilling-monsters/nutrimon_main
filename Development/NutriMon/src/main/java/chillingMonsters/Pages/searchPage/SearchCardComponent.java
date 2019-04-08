package chillingMonsters.Pages.searchPage;

import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Pages.recipePage.recipeCreatePage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SearchCardComponent extends AnchorPane {
  private PageOption option = PageOption.DEFAULT;
  private long ID;

  @FXML
  private Label cardName;

  @FXML
  private Label cardLabel;

  @FXML
  private ImageView searchImage;

  public SearchCardComponent(long ID, String name, String category, PageOption option) {
    super();

    this.ID = ID;
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
      case STOCK:
        PageFactory.getStockEntryPage(ID, option).startPage(e);
        break;
      case RECIPE:
        PageFactory.getRecipeEntryPage(ID).startPage(e);
        break;
      case UPDATE:
        recipeCreatePage recipeForm = PageFactory.getRecipeCreatePage();
        recipeForm.addToIngredientList(ID);
        recipeForm.startPage(e);
        break;
      case DEFAULT:
        if (option == PageOption.RECIPE) {
          PageFactory.getRecipeEntryPage(ID).startPage(e);
        } else {
          PageFactory.getIngredientPage(ID).startPage(e);
        }
        break;
    }
  }
}