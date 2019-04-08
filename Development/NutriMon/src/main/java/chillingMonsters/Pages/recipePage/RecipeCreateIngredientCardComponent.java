package chillingMonsters.Pages.recipePage;

import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class RecipeCreateIngredientCardComponent extends AnchorPane {
	@FXML
	private Label cardCategory;

	@FXML
	private Label cardName;

	@FXML
	private TextField amountTxF;

	public RecipeCreateIngredientCardComponent(String name, String category) {
		super();

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/recipePage/recipeIngredientCard.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		cardName.setText(name);
		cardCategory.setText(category);

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
			}
		});
	}

	public StringProperty amountProperty() {
		return amountTxF.textProperty();
	}
}