package chillingMonsters.Pages.recipePage;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class RecipeCreateIngredientCardComponent extends AnchorPane {
	@FXML
	private Label cardCategory;

	@FXML
	private Label cardName;

	@FXML
	private TextField amountTxF;

	public RecipeCreateIngredientCardComponent(Long foodID, Float amount, String name, String category) {
		super();

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/recipePage/recipeIngredientCard.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		amountTxF.setText(String.format("%.1fg", amount));
		cardName.setText(name);
		cardCategory.setText(category);
	}

	public StringProperty amountProperty() {
		return amountTxF.textProperty();
	}

	public void setReadyOnly() {
		amountTxF.setDisable(true);
		amountTxF.getStyleClass().add("disabledText");
	}

	public void setHighlight(boolean hightlight) {
		if (hightlight) {
			amountTxF.getStyleClass().add("secondaryHighlightText");
			cardName.getStyleClass().add("secondaryHighlightText");
		}
	}
}