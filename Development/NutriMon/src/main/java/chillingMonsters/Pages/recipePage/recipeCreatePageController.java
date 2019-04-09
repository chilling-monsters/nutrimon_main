package chillingMonsters.Pages.recipePage;

import chillingMonsters.AlertHandler;
import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Recipe.RecipeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Pages.searchPage.SearchCardComponent;
import chillingMonsters.Utility;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class recipeCreatePageController implements PageController {
	private long recipeID = 0;
	private PageOption option;

	private StringProperty cachedName = new SimpleStringProperty("");
	private StringProperty cachedCategory = new SimpleStringProperty("");
	private StringProperty cachedFormDetails = new SimpleStringProperty("");
	private StringProperty cachedCookTime = new SimpleStringProperty("");

	private Map<Long, StringProperty> ingrMap = new LinkedHashMap<Long, StringProperty>();

	@FXML
	private ImageView backButton;

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
	private TextField formCategory;

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
		formName.textProperty().bindBidirectional(cachedName);
		formCategory.textProperty().bindBidirectional(cachedCategory);
		formDetails.textProperty().bindBidirectional(cachedFormDetails);
		formCookTime.textProperty().bindBidirectional(cachedCookTime);

		if (option == PageOption.UPDATE && recipeID != 0) {
			RecipeController controller = ControllerFactory.makeRecipeController();
			Map<String, Object> recipe = controller.getRecipe(recipeID);

			formName.setText(recipe.get("recipeName").toString());
			formCategory.setText(recipe.get("recipeCategory").toString());
			formDetails.setText(recipe.get("recipeDescription").toString());
			formCookTime.setText(recipe.get("recipeCookTime").toString());

			List<Map<String, Object>> ingrL = (List<Map<String, Object>>) recipe.get("ingredients");
			for (Map<String, Object> i : ingrL) {
				addToIngredientList(Long.parseLong(i.get("foodID").toString()), Float.parseFloat(i.get("ingredientQtty").toString()));
			}
		}

		for (long foodID : ingrMap.keySet()) {
			IngredientController ingrController = ControllerFactory.makeIngredientController();
			Map<String, Object> result =  ingrController.getIngredient(foodID);
			String name = Utility.parseFoodName(result.get("foodName").toString());
			String category = result.get("fCategory").toString();

			RecipeCreateIngredientCardComponent ingrCard = new RecipeCreateIngredientCardComponent(foodID,0F, name, category);
			ingredientList.getChildren().add(ingrCard);

			ingrCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (AlertHandler.showConfirmationAlert("Are you sure?", "This ingredient will be removed from the recipe")) {
						ingredientList.getChildren().remove(ingrCard);
					}
				}
			});

			ingrCard.amountProperty().bindBidirectional(ingrMap.get(foodID));
		}

		backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());
				handleCancelButton(e);
			}
		});

		formList.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				handleFormScroll(event);
			}
		});

		createCard.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				handleCardScroll(event);
			}
		});

		addIngredientButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleAddIngredient(event);
			}
		});

		saveRecipeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleCreateButton(event);
			}
		});

		cancelRecipeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleCancelButton(event);
			}
		});

		deleteRecipeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleDeleteButton(event);
			}
		});
	}

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

		createCard.setPrefHeight(createCard.getHeight() + diffHeight);

		if (createCard.getHeight() > createCard.getMinHeight()) {
			formList.setMaxHeight(formList.getMaxHeight() + diffHeight);
		}
	}

	private void handleAddIngredient(ActionEvent event) {
		PageFactory.getSearchPage(PageOption.UPDATE).startPage(event);
	}

	public void addToIngredientList(long foodID, float amount) {
		if (ingrMap.containsKey(foodID)) return;

		ingrMap.put(foodID, new SimpleStringProperty(amount == 0 ? "g" : String.format("%.0fg", amount)));
	}

	private void handleCreateButton(ActionEvent event) {
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

			value = value.replaceAll("[^0-9]", "");
			if (value.isEmpty()) {
				AlertHandler.showAlert(Alert.AlertType.ERROR, "Oops!", "Please use some ingredients for your recipe");
				return;
			}

			ingredients.put(k, Float.parseFloat(value));
		}


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
		PageFactory.getRecipeEntryPage(recipeID).startPage(event);
	}

	private void handleCancelButton(ActionEvent event) {
		boolean confirmed = AlertHandler.showConfirmationAlert("Are you sure?", "Unsaved changes will be lost");
		if (confirmed) {
			PageFactory.getLastPage().startPage(event);
		}
	}

	private void handleDeleteButton(ActionEvent event) {
		boolean confirmed = AlertHandler.showConfirmationAlert("Are you sure?", "This recipe will be deleted");
		if (confirmed) {
			RecipeController controller = ControllerFactory.makeRecipeController();
			controller.deleteRecipe(recipeID);
			PageFactory.getRecipePage().startPage(event);
		}
	}
}
