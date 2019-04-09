package chillingMonsters.Pages.recipePage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Recipe.RecipeController;
import chillingMonsters.Controllers.Stock.StockController;
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

	@FXML
	private Label ingrProtein;

	@FXML
	private Label ingrTotalFat;

	@FXML
	private Label ingrSatFat;

	@FXML
	private Label ingrCholesterol;

	@FXML
	private Label ingrCarb;

	@FXML
	private Label ingrSugar;

	@FXML
	private Label ingrSodium;

	@FXML
	private Label ingrCalcium;

	@FXML
	private Label ingrIron;

	@FXML
	private Label ingrPotassium;

	@FXML
	private Label ingreC;

	@FXML
	private Label ingreE;

	@FXML
	private Label ingreD;

	public recipeEntryPageController(long recipeID) {
		this.recipeID = recipeID;
	}

	@FXML
	public void initialize() {
		RecipeController controller = ControllerFactory.makeRecipeController();
		Map<String, Object> result = controller.getRecipe(recipeID);

		String name = Utility.toCapitalized(result.get("recipeName").toString());
		String category = result.get("recipeCategory").toString().toUpperCase();
		String date = String.format("CREATED %s", Utility.parseDate((Timestamp) result.get("dateCreated")).toUpperCase());
		String time = String.format("%d mins", result.get("recipeCookTime"));
		String detail = result.get("recipeDescription").toString();

		IngredientController ingrController = ControllerFactory.makeIngredientController();
		StockController stockController = ControllerFactory.makeStockController();
		List<Map<String, Object>> ingredientList = (List<Map<String, Object>>) result.get("ingredients");

		boolean ready = true;
		float protein = 0;
		float totalFat = 0;
		float satFat = 0;
		float cholesterol = 0;
		float carb = 0;
		float sugar = 0;
		float sodium = 0;
		float calcium = 0;
		float iron = 0;
		float potassium = 0;
		float c = 0;
		float e = 0;
		float d = 0;

		for (Map<String, Object> ingr : ingredientList) {
			Long foodID = Long.parseLong(ingr.get("foodID").toString());
			Float amount = Float.parseFloat(ingr.get("ingredientQtty").toString());
			Float stockAmount = stockController.getStockQuantity(foodID);

			Map<String, Object> ingrDetails = ingrController.getIngredient(foodID);
			String ingrName = Utility.parseFoodName(ingrDetails.get("foodName").toString());

			protein += Float.parseFloat(ingrDetails.get("fProtein").toString());
			totalFat += Float.parseFloat(ingrDetails.get("fTotalFat").toString());
			satFat += Float.parseFloat(ingrDetails.get("fSaturatedFat").toString());
			cholesterol += Float.parseFloat(ingrDetails.get("fCholestero").toString());
			carb += Float.parseFloat(ingrDetails.get("fCarbohydrate").toString());
			sugar += Float.parseFloat(ingrDetails.get("fSugar").toString());
			sodium += Float.parseFloat(ingrDetails.get("fSodium").toString());
			calcium += Float.parseFloat(ingrDetails.get("fCalcium").toString());
			iron += Float.parseFloat(ingrDetails.get("fIron").toString());
			potassium += Float.parseFloat(ingrDetails.get("fPotassium").toString());
			c += Float.parseFloat(ingrDetails.get("fVC").toString());
			e += Float.parseFloat(ingrDetails.get("fVE").toString());
			d += Float.parseFloat(ingrDetails.get("fVD").toString());

			String labelTxt = String.format("%.0fg of %s", amount, ingrName);
			Label ingrLabel = new Label(labelTxt);
			ingrLabel.setWrapText(true);
			ingrLabel.getStyleClass().add("recipeIngredientText");
			ingrLabel.getStyleClass().add("detailText");
			ingrLabel.getStyleClass().add("myButton");

			if (amount > stockAmount) {
				ready = false;
				ingrLabel.getStyleClass().add("secondaryHighlightTextt");
			}

			ingrLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());
					PageFactory.getIngredientPage(foodID).startPage(e);
				}
			});

			recipeIngredientsList.getChildren().add(ingrLabel);
		}

		String readyTxt = "";
		if (ready) {
			readyTxt = "Ready";
		} else {
			readyTxt = "Not Ready";
			recipeReady.getStyleClass().add("secondaryHighlightTextt");
		}

		recipeName.setText(name);
		recipeCategory.setText(category);
		recipeAddedDate.setText(date);
		recipeCookTime.setText(time);
		recipeDetail.setText(detail);
		recipeReady.setText(readyTxt);

		ingrProtein.setText(String.format("%.1f g", protein));
		ingrTotalFat.setText(String.format("%.1f g", totalFat));
		ingrSatFat.setText(String.format("%.1f g", satFat));
		ingrCholesterol.setText(String.format("%.1f g", cholesterol));
		ingrCarb.setText(String.format("%.1f g", carb));
		ingrSugar.setText(String.format("%.1f g", sugar));

		ingrSodium.setText(String.format("%.1f g", sodium));
		ingrCalcium.setText(String.format("%.1f g", calcium));
		ingrIron.setText(String.format("%.1f g", iron));
		ingrPotassium.setText(String.format("%.1f g", potassium));
		ingreC.setText(String.format("%.1f g", c));
		ingreE.setText(String.format("%.1f g", e));
		ingreD.setText(String.format("%.1f g", d));

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
