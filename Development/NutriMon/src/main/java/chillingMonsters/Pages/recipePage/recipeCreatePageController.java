package chillingMonsters.Pages.recipePage;

import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;

public class recipeCreatePageController implements PageController {

	@FXML
	private ImageView backButton;

	@FXML
	private AnchorPane createCard;

	@FXML
	private ScrollPane formList;

	@FXML
	private TextArea formName;

	@FXML
	private ChoiceBox<?> formCategory;

	@FXML
	private Label formCalories;

	@FXML
	private Button addIngredientButton;

	@FXML
	private TextArea formDetails;

	@FXML
	private Button deleteRecipeButton;

	@FXML
	private Button cancelRecipeButton;

	@FXML
	private Button saveRecipeButton;

	@FXML
	public void initialize() {
		backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());
				PageFactory.getLastPage().startPage(e);
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
}
