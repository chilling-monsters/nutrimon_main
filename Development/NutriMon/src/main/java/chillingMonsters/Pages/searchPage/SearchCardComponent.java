package chillingMonsters.Pages.searchPage;

import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Pages.recipePage.recipeCreatePage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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

    this.setOnMouseClicked(event -> handleOnClick());

    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    cardName.setText(name);
    cardLabel.setText(category.toUpperCase());
  }

  private void handleOnClick() {
    switch (option) {
      case STOCK:
        PageFactory.toNextPage(PageFactory.getStockEntryPage(ID, option));
        break;
      case RECIPE:
        PageFactory.toNextPage(PageFactory.getRecipeEntryPage(ID));
        break;
      case UPDATE:
        recipeCreatePage recipeForm = (recipeCreatePage) PageFactory.getRecipeForm();
        recipeForm.addToIngredientList(ID);
        PageFactory.toNextPage(recipeForm);
        break;
      case DEFAULT:
        PageFactory.toNextPage(PageFactory.getIngredientPage(ID));
        break;
      case INTAKE_RECIPE:
      case INTAKE_STOCK:
        PageFactory.toNextPage(PageFactory.getIntakeEntry(ID, option));
        break;
    }
  }
}