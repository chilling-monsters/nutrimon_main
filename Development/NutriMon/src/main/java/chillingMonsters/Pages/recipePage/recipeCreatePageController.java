package chillingMonsters.Pages.recipePage;

import chillingMonsters.AlertHandler;
import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Recipe.RecipeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class recipeCreatePageController implements PageController {
	private long recipeID;
	private PageOption option;

	private StringProperty cachedName = new SimpleStringProperty("");
	private StringProperty cachedCategory = new SimpleStringProperty("");
	private StringProperty cachedFormDetails = new SimpleStringProperty("");
	private StringProperty cachedCookTime = new SimpleStringProperty("");

	private Map<Long, StringProperty> ingrMap = new LinkedHashMap<>();

	@FXML
	private AnchorPane createCard;

	@FXML
	private ScrollPane formList;

	@FXML
	private TextField formName;

	@FXML
	private TextField formCookTime;

	@FXML
	private VBox ingredientList;

	@FXML
	private Button addIngredientButton;

	@FXML
	private ChoiceBox formCategory;

	@FXML
	private TextArea formDetails;

	@FXML
	private Button deleteRecipeButton;

	@FXML
	private Button cancelRecipeButton;

	@FXML
	private Button saveRecipeButton;

	public recipeCreatePageController(Long recipeID, PageOption option) {
		this.recipeID = recipeID;
		this.option = option;
	}

	@FXML
	public void initialize() {
		formCategory.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snack");

		formName.textProperty().bindBidirectional(cachedName);
		formCategory.valueProperty().bindBidirectional(cachedCategory);
		formDetails.textProperty().bindBidirectional(cachedFormDetails);
		formCookTime.textProperty().bindBidirectional(cachedCookTime);

		addIngredientButton.setOnAction(event -> handleAddIngredient());
		saveRecipeButton.setOnAction(event -> handleCreateButton());
		cancelRecipeButton.setOnAction(event -> handleCancelButton());
		deleteRecipeButton.setOnAction(event -> handleDeleteButton());

		formList.addEventFilter(ScrollEvent.SCROLL, event -> handleFormScroll(event));
		createCard.setOnScroll(event -> handleCardScroll(event));
	}

	public void refresh() {
		if (option == PageOption.UPDATE && recipeID != 0) {
			RecipeController controller = ControllerFactory.makeRecipeController();
			Map<String, Object> recipe = controller.getRecipe(recipeID);

			formName.setText(Utility.toCapitalized(recipe.get("recipeName").toString()));
			formCategory.setValue(recipe.get("recipeCategory").toString());
			formDetails.setText(recipe.get("recipeDescription").toString());
			formCookTime.setText(recipe.get("recipeCookTime").toString());

			List<Map<String, Object>> ingrL = (List<Map<String, Object>>) recipe.get("ingredients");
			for (Map<String, Object> i : ingrL) {
				Long foodID = Utility.parseID(i.get("foodID").toString(), 0);
				Float amount = Float.parseFloat(i.get("ingredientQtty").toString());

				addToIngredientList(foodID, amount);
			}
		}

		renderIngredientList();
	}

	//Event handlers
	private void handleFormScroll(ScrollEvent event) {
		if (createCard.getHeight() == createCard.getMaxHeight()) {
			return;
		}

		event.consume();
		ScrollEvent retargettedScrollEvent = new ScrollEvent(createCard, createCard, event.getEventType(),
			event.getX(), event.getY(), event.getScreenX(), event.getScreenY(), event.isShiftDown(),
			event.isControlDown(), event.isAltDown(), event.isMetaDown(), event.isDirect(),
			event.isInertia(), event.getDeltaX(), event.getDeltaY(), event.getTotalDeltaX(),
			event.getTotalDeltaY(), event.getTextDeltaXUnits(), event.getTextDeltaX(),
			event.getTextDeltaYUnits(), event.getTextDeltaY(), event.getTouchCount(),
			event.getPickResult());

		Event.fireEvent(createCard, retargettedScrollEvent);
	}

	private void handleCardScroll(ScrollEvent event) {
		double diffHeight = 0;
		if (event.getDeltaY() > 0) diffHeight = -10;
		else if (event.getDeltaY() < 0) diffHeight = 10;

		double top = AnchorPane.getTopAnchor(createCard) - diffHeight;
		if (top > Utility.MAX_TOP_ANCHOR) top = Utility.MAX_TOP_ANCHOR;
		else if (top < Utility.MIN_TOP_ANCHOR) top = Utility.MIN_TOP_ANCHOR;

		if (createCard.getMinHeight() <= createCard.getHeight() && createCard.getHeight() <= createCard.getMaxHeight())  {
			AnchorPane.setTopAnchor(createCard, top);
			formList.setPrefHeight(formList.getHeight() + diffHeight);
		}
	}

	private void handleAddIngredient() {
		PageFactory.toNextPage(PageFactory.getSearchPage(PageOption.UPDATE));
	}

	private void handleCreateButton() {
		String name = cachedName.getValue().trim();
		String category = cachedCategory.getValue().trim();
		String details = cachedFormDetails.getValue().trim();
		double cookTime = Utility.parseQuantity(cachedCookTime.getValue(), 0);

		if (name.isEmpty()) {
			AlertHandler.showAlert(Alert.AlertType.ERROR, "Oops!", "Please enter a name for your recipe");
			return;
		}

		if (category.isEmpty()) {
			AlertHandler.showAlert(Alert.AlertType.ERROR, "Oops!", "Please enter a category for your recipe");
			return;
		}

		if (details.isEmpty()) {
			AlertHandler.showAlert(Alert.AlertType.ERROR, "Oops!", "Please enter some details for your recipe");
			return;
		}

		if (cookTime == 0) {
			AlertHandler.showAlert(Alert.AlertType.ERROR, "Oops!", "Please enter a time estimate for your recipe");
			return;
		}

		if (ingrMap.size() == 0) {
			AlertHandler.showAlert(Alert.AlertType.ERROR, "Oops!", "Please use some ingredients for your recipe");
			return;
		}

		Map<Long, Float> ingredients = new HashMap<>();
		for (Long k : ingrMap.keySet()) {
			String value = ingrMap.get(k).getValue();

			float fValue = Float.valueOf(value.replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
			if (value.isEmpty() || Math.abs(fValue - 0) < 0.01) {
				AlertHandler.showAlert(Alert.AlertType.ERROR, "Oops!", "Please use some ingredients for your recipe");
				return;
			}

			ingredients.put(k, fValue);
		}

		PageFactory.setFormInProgress(false);
		RecipeController controller = ControllerFactory.makeRecipeController();

		switch (option) {
			case RECIPE:
				recipeID = controller.createRecipe(name, category, details, cookTime, ingredients);
				controller.saveRecipe(recipeID);
				break;
			case UPDATE:
				controller.updateRecipe(recipeID, name, category, details, cookTime, ingredients);
				break;
			case DEFAULT:
				break;
		}

		if (recipeID > 0) PageFactory.toNextPage(PageFactory.getRecipeEntryPage(recipeID));
		else PageFactory.toNextPage(PageFactory.getRecipePage());
	}

	private void handleCancelButton() {
		boolean confirmed = AlertHandler.showConfirmationAlert("Are you sure?", "Unsaved changes will be lost");
		if (confirmed) {
			PageFactory.setFormInProgress(false);
			if (option == PageOption.UPDATE) {
				PageFactory.toNextPage(PageFactory.getRecipeEntryPage(recipeID));
			} else {
				PageFactory.toNextPage(PageFactory.getRecipePage());
			}
		}
	}

	private void handleDeleteButton() {
		boolean confirmed = AlertHandler.showConfirmationAlert("Are you sure?", "This recipe will be deleted");
		if (confirmed) {
			PageFactory.setFormInProgress(false);
			RecipeController controller = ControllerFactory.makeRecipeController();
			controller.deleteRecipe(recipeID);
			PageFactory.toNextPage(PageFactory.getRecipePage());
		}
	}

	//Helper functions
	public void addToIngredientList(long foodID, float amount) {
		ingrMap.put(foodID, new SimpleStringProperty(amount == 0 ? "g" : String.format("%.1fg", amount)));
		renderIngredientList();
	}

	private void renderIngredientList() {
		ingredientList.getChildren().clear();

		for (long foodID : ingrMap.keySet()) {
			IngredientController ingrController = ControllerFactory.makeIngredientController();
			Map<String, Object> result =  ingrController.getIngredient(foodID);
			String name = Utility.parseFoodName(result.get("foodName").toString());
			String category = result.get("fCategory").toString();

			RecipeCreateIngredientCardComponent ingrCard = new RecipeCreateIngredientCardComponent(foodID,0F, name, category);
			ingredientList.getChildren().add(ingrCard);

			ingrCard.setOnMouseClicked(event -> {
					if (AlertHandler.showConfirmationAlert("Are you sure?", "This ingredient will be removed from the recipe")) {
						ingredientList.getChildren().remove(ingrCard);
						ingrMap.remove(foodID);
					}
				}
			);

			ingrCard.amountProperty().bindBidirectional(ingrMap.get(foodID));
		}
	}
}
