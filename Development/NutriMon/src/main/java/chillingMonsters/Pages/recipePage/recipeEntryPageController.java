package chillingMonsters.Pages.recipePage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Recipe.RecipeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Utility;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class recipeEntryPageController implements PageController {
	private long recipeID;

	@FXML
	private ImageView backButton;

	@FXML
	private AnchorPane recipeCard;

	@FXML
	private Label recipeName;

	@FXML
	private Label recipeCategory;

	@FXML
	private Label recipeAddedDate;

	@FXML
	private Label recipeCookTime;

	@FXML
	private Label recipeReady;

	@FXML
	private ScrollPane scrollRecipeDetailPane;

	@FXML
	private VBox recipeIngredientsList;

	@FXML
	private Label recipeDetail;

	@FXML
	private ToggleButton addRecipeButton;

	public recipeEntryPageController(long recipeID) {
		this.recipeID = recipeID;
	}

	@FXML
	public void initialize() {
		RecipeController controller = ControllerFactory.makeRecipeController();
		Map<String, Object> result = controller.getRecipe(recipeID);

		String name = result.get("recipeName").toString();
		String category = result.get("recipeCategory").toString().toUpperCase();
		String date = String.format("CREATED %s", Utility.parseDate((Timestamp) result.get("dateCreated")).toUpperCase());
		String time = String.format("%d mins", result.get("recipeCookTime"));
		String detail = result.get("recipeDescription").toString();
		String ready = "";

		if (controller.canbeMade(recipeID)) {
			ready = "Ready";
		} else {
			ready = "Not Ready";
			recipeReady.getStyleClass().add("secondaryHighlightTextt");
		}

		recipeName.setText(name);
		recipeCategory.setText(category);
		recipeAddedDate.setText(date);
		recipeCookTime.setText(time);
		recipeDetail.setText(detail);
		recipeReady.setText(ready);

		IngredientController ingrController = ControllerFactory.makeIngredientController();
		List<Map<String, Object>> ingredientList = (List<Map<String, Object>>) result.get("ingredients");

		for (Map<String, Object> ingr : ingredientList) {
			Map<String, Object> ingrDetails = ingrController.getIngredient((Long) ingr.get("foodID"));
			Float amount = (Float) ingr.get("ingredientQtty");
			String ingrName = Utility.parseFoodName(ingrDetails.get("foodName").toString());

			String labelTxt = String.format("%.0fg of %s", amount, ingrName);
			Label ingrLabel = new Label(labelTxt);
			ingrLabel.setWrapText(true);
			ingrLabel.getStyleClass().add("recipeIngredientText");
			ingrLabel.getStyleClass().add("detailText");

			recipeIngredientsList.getChildren().add(ingrLabel);
		}

		backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				PageFactory.getLastPage().startPage(new ActionEvent(event.getSource(), event.getTarget()));
			}
		});

		scrollRecipeDetailPane.setMinHeight(scrollRecipeDetailPane.getMinHeight() - recipeName.getHeight() + recipeName.getMinHeight());
		scrollRecipeDetailPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				handleListScroll(event);
			}
		});

		recipeCard.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				handleCardScroll(event);
			}
		});

		if (controller.isSaved(recipeID)) {
			addRecipeButton.setText("Saved");
			addRecipeButton.setSelected(true);
		}

		addRecipeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleAddRecipe(event);
			}
		});

		recipeName.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				handleNameClick();
			}
		});
	}

	private void handleListScroll(ScrollEvent event) {
		if (recipeCard.getHeight() == recipeCard.getMaxHeight()) {
			return;
		}

		event.consume();
		ScrollEvent retargettedScrollEvent = new ScrollEvent(recipeCard, recipeCard, event.getEventType(),
			event.getX(), event.getY(), event.getScreenX(), event.getScreenY(), event.isShiftDown(),
			event.isControlDown(), event.isAltDown(), event.isMetaDown(), event.isDirect(),
			event.isInertia(), event.getDeltaX(), event.getDeltaY(), event.getTotalDeltaX(),
			event.getTotalDeltaY(), event.getTextDeltaXUnits(), event.getTextDeltaX(),
			event.getTextDeltaYUnits(), event.getTextDeltaY(), event.getTouchCount(),
			event.getPickResult());

		Event.fireEvent(recipeCard, retargettedScrollEvent);
	}

	private void handleCardScroll(ScrollEvent event) {
		double diffHeight = 0;
		if (event.getDeltaY() > 0) diffHeight = -10;
		else if (event.getDeltaY() < 0) diffHeight = 10;

		recipeCard.setPrefHeight(recipeCard.getHeight() + diffHeight);

		if (recipeCard.getHeight() > recipeCard.getMinHeight()) {
			scrollRecipeDetailPane.setMaxHeight(scrollRecipeDetailPane.getMaxHeight() + diffHeight);
		}
	}

	private void handleAddRecipe(ActionEvent event) {
		RecipeController controller = ControllerFactory.makeRecipeController();

		if (addRecipeButton.isSelected()) {
			addRecipeButton.setText("Saved");
			controller.saveRecipe(recipeID);
		} else {
			addRecipeButton.setText("+");
			controller.unsaveRecipe(recipeID);
		}
	}

	private void handleNameClick() {
		if (recipeName.getMaxHeight() == 30) {
			recipeName.setMaxHeight(Double.POSITIVE_INFINITY);
		} else {
			recipeName.setMaxHeight(30);
		}
	}
}
