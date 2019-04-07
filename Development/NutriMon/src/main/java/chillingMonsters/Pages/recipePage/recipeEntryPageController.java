package chillingMonsters.Pages.recipePage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.IngredientController;
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
import java.util.HashMap;
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
		//TODO: use recipe controller here
		//RecipeController controller = ControllerFactory.makeRecipeController();
		//Map<String, Object> result = controller.getRecipe(recipeID);
		Map<String, Object> result = new HashMap<String, Object>() {{
			put("recipeName", "Roasted Honey Glazed Turkey");
			put("dateCreated", Timestamp.valueOf("2019-04-03 00:00:00"));
			put("recipeCookTime", 15);
			put("recipeDescription",
				"1. Preheat the oven to 350 degrees F.\n" +
					"2. Melt the butter in a small saucepan. Add the zest and juice of the lemon and 1 teaspoon of thyme leaves to the butter mixture. Set aside.\n" +
					"3. Take the giblets out of the turkey and wash the turkey inside and out. Remove any excess fat and leftover pinfeathers and pat the outside dry. Place the turkey in a large roasting pan. Liberally salt and pepper the inside of the turkey cavity. Stuff the cavity with the bunch of thyme, halved lemon, quartered onion, and the garlic. Brush the outside of the turkey with the butter mixture and sprinkle with salt and pepper. Tie the legs together with string and tuck the wing tips under the body of the turkey.\n" +
					"4. Roast the turkey about 2 1/2 hours, or until the juices run clear when you cut between the leg and the thigh. Remove the turkey to a cutting board and cover with aluminum foil; let rest for 20 minutes.\n" +
					"5. Slice the turkey and serve.\n" +
					"6. Preheat the oven to 350 degrees F.\n" +
					"7. Melt the butter in a small saucepan. Add the zest and juice of the lemon and 1 teaspoon of thyme leaves to the butter mixture. Set aside.\n" +
					"8. Take the giblets out of the turkey and wash the turkey inside and out. Remove any excess fat and leftover pinfeathers and pat the outside dry. Place the turkey in a large roasting pan. Liberally salt and pepper the inside of the turkey cavity. Stuff the cavity with the bunch of thyme, halved lemon, quartered onion, and the garlic. Brush the outside of the turkey with the butter mixture and sprinkle with salt and pepper. Tie the legs together with string and tuck the wing tips under the body of the turkey.\n" +
					"9. Roast the turkey about 2 1/2 hours, or until the juices run clear when you cut between the leg and the thigh. Remove the turkey to a cutting board and cover with aluminum foil; let rest for 20 minutes.\n" +
					"10. Slice the turkey and serve.\n" +
					"11. Preheat the oven to 350 degrees F.\n" +
					"12. Melt the butter in a small saucepan. Add the zest and juice of the lemon and 1 teaspoon of thyme leaves to the butter mixture. Set aside.\n" +
					"13. Take the giblets out of the turkey and wash the turkey inside and out. Remove any excess fat and leftover pinfeathers and pat the outside dry. Place the turkey in a large roasting pan. Liberally salt and pepper the inside of the turkey cavity. Stuff the cavity with the bunch of thyme, halved lemon, quartered onion, and the garlic. Brush the outside of the turkey with the butter mixture and sprinkle with salt and pepper. Tie the legs together with string and tuck the wing tips under the body of the turkey.\n" +
					"14. Roast the turkey about 2 1/2 hours, or until the juices run clear when you cut between the leg and the thigh. Remove the turkey to a cutting board and cover with aluminum foil; let rest for 20 minutes.\n" +
					"15. Slice the turkey and serve.\n");
			put("ingredients", new HashMap<Long, Float>() {{
				put(1001L, 20.0F);
				put(1002L, 300.0F);
				put(1003L, 50.0F);
				put(1004L, 80.0F);
			}});
			put("recipeCategory", "Dinner");
		}};

		String name = result.get("recipeName").toString();
		String category = result.get("recipeCategory").toString().toUpperCase();
		String date = String.format("CREATED %s", Utility.parseDate((Timestamp) result.get("dateCreated")).toUpperCase());
		String time = String.format("%s mins", result.get("recipeCookTime").toString());
		String detail = result.get("recipeDescription").toString();

		recipeName.setText(name);
		recipeCategory.setText(category);
		recipeAddedDate.setText(date);
		recipeCookTime.setText(time);
		recipeDetail.setText(detail);

		IngredientController ingrController = ControllerFactory.makeIngredientController();
		Map<Long, Float> ingredientList = (Map<Long, Float>) result.get("ingredients");

		for (Long k : ingredientList.keySet()) {
			Map<String, Object> ingrDetails = ingrController.getIngredient(k);
			Float amount = ingredientList.get(k);
			String ingrName = Utility.parseFoodName(ingrDetails.get("foodName").toString()).toUpperCase();

			String labelTxt = String.format("%.0fg of %s", amount, ingrName);
			Label ingrLabel = new Label(labelTxt);
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
		//TODO: use recipe controller here
		//RecipeController controller = ControllerFactory.makeRecipeController();

		if (addRecipeButton.isSelected()) {
			addRecipeButton.setText("Saved");
			//controller.add(recipeID);
		} else {
			addRecipeButton.setText("+");
			//controller.remove(recipeID);
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
