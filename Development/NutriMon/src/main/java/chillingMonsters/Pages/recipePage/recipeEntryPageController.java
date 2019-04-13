package chillingMonsters.Pages.recipePage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Recipe.RecipeController;
import chillingMonsters.Controllers.Stock.StockController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class recipeEntryPageController implements PageController {
	private long recipeID;

	@FXML
	private ImageView moreButton;

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
	private Label ingrCalories;

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
		addRecipeButton.setOnAction(event -> handleAddRecipe());
		recipeName.setOnMouseClicked(event -> handleNameClick());
		moreButton.setOnMouseClicked(event -> handleMoreClick());

		scrollRecipeDetailPane.addEventFilter(ScrollEvent.SCROLL, event -> handleListScroll(event));
		recipeCard.setOnScroll(event -> handleCardScroll(event));
	}

	public void refresh() {
		recipeIngredientsList.getChildren().clear();
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
		float calories = 0;
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
			Long foodID = Utility.parseID(ingr.get("foodID").toString(), 0);
			Float amount = Float.parseFloat(ingr.get("ingredientQtty").toString());
			Float stockAmount = stockController.getStockQuantity(foodID);

			Map<String, Object> ingrDetails = ingrController.getIngredient(foodID);
			String ingrName = Utility.parseFoodName(ingrDetails.get("foodName").toString());

			Float portion = amount / 100;
			calories += Float.parseFloat(ingrDetails.get("fCalories").toString()) * portion;
			protein += Float.parseFloat(ingrDetails.get("fProtein").toString()) * portion;
			totalFat += Float.parseFloat(ingrDetails.get("fTotalFat").toString()) * portion;
			satFat += Float.parseFloat(ingrDetails.get("fSaturatedFat").toString()) * portion;
			cholesterol += Float.parseFloat(ingrDetails.get("fCholestero").toString()) * portion;
			carb += Float.parseFloat(ingrDetails.get("fCarbohydrate").toString()) * portion;
			sugar += Float.parseFloat(ingrDetails.get("fSugar").toString()) * portion;
			sodium += Float.parseFloat(ingrDetails.get("fSodium").toString()) * portion;
			calcium += Float.parseFloat(ingrDetails.get("fCalcium").toString()) * portion;
			iron += Float.parseFloat(ingrDetails.get("fIron").toString()) * portion;
			potassium += Float.parseFloat(ingrDetails.get("fPotassium").toString()) * portion;
			c += Float.parseFloat(ingrDetails.get("fVC").toString()) * portion;
			e += Float.parseFloat(ingrDetails.get("fVE").toString()) * portion;
			d += Float.parseFloat(ingrDetails.get("fVD").toString()) * portion;

			String labelCategory = ingrDetails.get("fCategory").toString();
			RecipeCreateIngredientCardComponent ingrLabel = new RecipeCreateIngredientCardComponent(foodID, amount, ingrName, labelCategory);
			ingrLabel.setReadyOnly();

			if (amount > stockAmount) {
				ready = false;
				ingrLabel.setHighlight(true);
			}

			recipeIngredientsList.getChildren().add(ingrLabel);

			ingrLabel.setOnMouseClicked(event -> handleOnIngridentCardClick(foodID));
		}

		String readyTxt = "";
		if (ready) {
			readyTxt = "Ready";
		} else {
			readyTxt = "Not Ready";
			recipeReady.getStyleClass().add("secondaryHighlightText");
		}

		recipeName.setText(name);
		recipeCategory.setText(category);
		recipeAddedDate.setText(date);
		recipeCookTime.setText(time);
		recipeDetail.setText(detail);
		recipeReady.setText(readyTxt);

		ingrCalories.setText(String.format("%.1f Cal", calories));
		ingrProtein.setText(String.format("%.1f g Protein", protein));
		ingrTotalFat.setText(String.format("%.1f g Fat", totalFat));
		ingrSatFat.setText(String.format("%.1f g Sat. Fat", satFat));
		ingrCholesterol.setText(String.format("%.1f g Cholesterol", cholesterol));
		ingrCarb.setText(String.format("%.1f g CarbonHydrate", carb));
		ingrSugar.setText(String.format("%.1f g Sugar", sugar));

		ingrSodium.setText(String.format("%.1f g Sodium", sodium));
		ingrCalcium.setText(String.format("%.1f g Calcium", calcium));
		ingrIron.setText(String.format("%.1f g Iron", iron));
		ingrPotassium.setText(String.format("%.1f g Potassium", potassium));
		ingreC.setText(String.format("%.1f g Vitamin C", c));
		ingreE.setText(String.format("%.1f g Vitamin E", e));
		ingreD.setText(String.format("%.1f g Vitamin D", d));

		scrollRecipeDetailPane.setMinHeight(scrollRecipeDetailPane.getMinHeight() - recipeName.getHeight() + recipeName.getMinHeight());
		if (controller.isSaved(recipeID)) {
			addRecipeButton.setText("Saved");
			addRecipeButton.setSelected(true);
		}
	}

	//event handlers
	private void handleOnIngridentCardClick(long foodID) {
		PageFactory.toNextPage(PageFactory.getStockEntryPage(foodID, PageOption.DEFAULT));
	}

	private void handleAddRecipe() {
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

	private void handleMoreClick() {
		PageFactory.setFormInProgress(true);
		PageFactory.toNextPage(PageFactory.getRecipeEditPage(recipeID, PageOption.UPDATE));
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

		double top = AnchorPane.getTopAnchor(recipeCard) - diffHeight;
		if (top > Utility.MAX_TOP_ANCHOR) top = Utility.MAX_TOP_ANCHOR;
		else if (top < Utility.MIN_TOP_ANCHOR) top = Utility.MIN_TOP_ANCHOR;

		if (recipeCard.getMinHeight() <= recipeCard.getHeight() && recipeCard.getHeight() <= recipeCard.getMaxHeight())  {
			AnchorPane.setTopAnchor(recipeCard, top);
			scrollRecipeDetailPane.setPrefHeight(scrollRecipeDetailPane.getHeight() + diffHeight);
		}
	}
}
