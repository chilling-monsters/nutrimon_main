package chillingMonsters.Pages.ingredientPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
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

import java.util.Map;

public class ingredientPageController implements PageController {
	private long ingredientID;

	@FXML
	private ImageView backButton;

	@FXML
	private ImageView moreButton;

	@FXML
	private Label ingrName;

	@FXML
	private Label ingrCategory;

	@FXML
	private Label ingrAvgSpoilage;

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

	@FXML
	private ToggleButton addToStockButton;

	@FXML
	private AnchorPane adjustSizeCard;

	@FXML
	private ScrollPane cardScrollPane;

	public ingredientPageController(long ingredientID) {
		this.ingredientID = ingredientID;
	}

	@FXML
	public void initialize() {
		IngredientController controller = ControllerFactory.makeIngredientController();
		Map<String, Object> result = controller.getIngredient(ingredientID);

		ingrName.setText(Utility.parseFoodName(result.get("foodName").toString()));
		ingrCategory.setText(result.get("fCategory").toString());
		ingrAvgSpoilage.setText(String.format("%s Days", result.get("expTime").toString()));
		ingrCalories.setText(String.format("%s Calories", result.get("fCalories").toString()));

		ingrProtein.setText(result.get("fProtein").toString() + "g");
		ingrTotalFat.setText(result.get("fTotalFat").toString() + "g");
		ingrSatFat.setText(result.get("fSaturatedFat").toString() + "g");
		ingrCholesterol.setText(result.get("fCholestero").toString() + "g");
		ingrCarb.setText(result.get("fCarbohydrate").toString() + "g");
		ingrSugar.setText(result.get("fSugar").toString() + "g");

		ingrSodium.setText(result.get("fSodium").toString() + "g");
		ingrCalcium.setText(result.get("fCalcium").toString() + "g");
		ingrIron.setText(result.get("fIron").toString() + "g");
		ingrPotassium.setText(result.get("fPotassium").toString() + "g");
		ingreC.setText(result.get("fVC").toString() + "g");
		ingreE.setText(result.get("fVE").toString() + "g");
		ingreD.setText(result.get("fVD").toString() + "g");

		addToStockButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				PageFactory.getStockEntryPage(ingredientID, PageOption.STOCK).startPage(event);
			}
		});

		backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());
				PageFactory.getLastPage().startPage(e);
			}
		});

		moreButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());
				PageFactory.getStockEntryPage(ingredientID).startPage(e);
			}
		});

		cardScrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				handleListScroll(event);
			}
		});

		adjustSizeCard.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				handleCardScroll(event);
			}
		});
	}

	private void handleCardScroll(ScrollEvent event) {
		double diffHeight = 0;
		if (event.getDeltaY() > 0) diffHeight = -10;
		else if (event.getDeltaY() < 0) diffHeight = 10;

		adjustSizeCard.setPrefHeight(adjustSizeCard.getHeight() + diffHeight);
		cardScrollPane.setPrefHeight(cardScrollPane.getHeight() + diffHeight);
	}

	private void handleListScroll(ScrollEvent event) {
		if (cardScrollPane.getHeight() == cardScrollPane.getMaxHeight()) {
			return;
		}

		event.consume();
		ScrollEvent retargettedScrollEvent = new ScrollEvent(adjustSizeCard, adjustSizeCard, event.getEventType(),
			event.getX(), event.getY(), event.getScreenX(), event.getScreenY(), event.isShiftDown(),
			event.isControlDown(), event.isAltDown(), event.isMetaDown(), event.isDirect(),
			event.isInertia(), event.getDeltaX(), event.getDeltaY(), event.getTotalDeltaX(),
			event.getTotalDeltaY(), event.getTextDeltaXUnits(), event.getTextDeltaX(),
			event.getTextDeltaYUnits(), event.getTextDeltaY(), event.getTouchCount(),
			event.getPickResult());

		Event.fireEvent(adjustSizeCard, retargettedScrollEvent);
	}

}
