package chillingMonsters.Pages.stockPage;

import chillingMonsters.AlertHandler;
import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Stock.StockController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

public class stockEntryPageController implements PageController {
	private PageOption option;
	private long foodID;
	private long currentStockID;

	private double baseTotal;
	private int baseTimeLeft;
	private DoubleProperty total = new SimpleDoubleProperty(0);
	private DoubleProperty current = new SimpleDoubleProperty(0);
	private IntegerProperty timeLeft = new SimpleIntegerProperty(0);

	@FXML
	public Label entryName;

	@FXML
	public Label entryAvgSpoilage;

	@FXML
	public Label entryTotalAmount;

	@FXML
	public Label entryCategory;

	@FXML
	public VBox entryList;

	@FXML
	public AnchorPane createForm;

	@FXML
	public Label moreLabel;

	@FXML
	public Button intakeButton;

	@FXML
	public ToggleButton addStockButton;

	@FXML
	public Button deleteEntryButton;

	@FXML
	public Button cancelEntryButton;

	@FXML
	public Label entryCurrentAmount;

	@FXML
	public Label entryTimeLeft;

	@FXML
	public Label entryAddedDate;

	@FXML
	public TextField amountTxF;

	@FXML
	public DatePicker dateTxF;

	@FXML
	public AnchorPane adjustSizePane;

	@FXML
	public ScrollPane scrollList;

	public stockEntryPageController(long foodID, PageOption option) {
		this.foodID = foodID;
		this.option = option;
	}

	@FXML
	public void initialize() {
		createForm.visibleProperty().bindBidirectional(addStockButton.selectedProperty());
		entryList.visibleProperty().bind(addStockButton.selectedProperty().not());
		moreLabel.visibleProperty().bind(addStockButton.selectedProperty().not());
		addStockButton.selectedProperty().addListener(event -> {
			boolean selected = addStockButton.isSelected();
			PageFactory.setFormInProgress(selected);
			addStockButton.setText(selected ? "Save" : "Add");
		});

		intakeButton.setOnAction(event -> handleAddToIntake());
		moreLabel.setOnMouseClicked(event -> handleMoreOnClick());
		cancelEntryButton.setOnMouseClicked(event -> handleCancelOnClick());
		deleteEntryButton.setOnMouseClicked(event -> handleDeleteOnClick());
		addStockButton.setOnAction(event -> handleSaveAddOnClick());

		entryTotalAmount.textProperty().bind(Bindings.format("%.1fg", total));
		entryCurrentAmount.textProperty().bind(Bindings.format("%.1fg", current));

		amountTxF.textProperty().addListener(event -> handleInputOnType());
		dateTxF.valueProperty().addListener(event -> handleDateOnType());

		scrollList.addEventFilter(ScrollEvent.SCROLL, event -> handleListScroll(event));
		adjustSizePane.setOnScroll(event -> handleCardScroll(event));
	}

	public void refresh() {
		entryList.getChildren().clear();
		createForm.setVisible(false);

		IngredientController ingr = ControllerFactory.makeIngredientController();
		Map<String, Object> result = ingr.getIngredient(foodID);

		entryName.setText(Utility.parseFoodName(result.get("foodName").toString()));
		entryCategory.setText(result.get("fCategory").toString());

		baseTimeLeft = Integer.parseInt(result.get("expTime").toString());
		entryAvgSpoilage.setText(String.format("%d Day%s", baseTimeLeft, baseTimeLeft > 1 ? "s" : ""));

		total.setValue(0);
		StockController stck = ControllerFactory.makeStockController();
		List<Map<String, Object>> resultLists = stck.showStockEntry(foodID);

		if (resultLists.isEmpty()) {
			Label emptyLabel = new Label("We ain't got squash.");
			emptyLabel.getStyleClass().add("emptyWarningText");

			entryList.getChildren().add(emptyLabel);
		}

		for (Map<String, Object> r : resultLists) {
			Long stockItemID = Utility.parseID(r.get("stockItemID").toString(), 0);
			Long left = Utility.parseID(r.get("time_left").toString(), 0);
			Timestamp addedDate = (Timestamp) r.get("added_date");
			float amount = Float.parseFloat(r.get("foodQtty").toString());
			total.setValue(total.getValue() + amount);

			StockEntryCardComponent sCard = new StockEntryCardComponent(stockItemID, left, addedDate, amount);
			if (left <= Utility.SPOILAGE_WARNING_DAYS) sCard.getStyleClass().add("hightlightCard");
			sCard.setOnMouseClicked(event -> {
				option = PageOption.UPDATE;

				addStockButton.setSelected(true);
				currentStockID = stockItemID;

				baseTotal = total.getValue() - amount;

				amountTxF.setText(String.format("%.1f", amount));
				dateTxF.setValue(addedDate.toLocalDateTime().toLocalDate());
			});

			entryList.getChildren().add(sCard);
		}

		if (option == PageOption.STOCK) {
			createForm.setVisible(true);
			handleSaveAddOnClick();
		}
	}

	//Event handlers
	private void handleAddToIntake() {
		if (createForm.isVisible()) {
			if (!AlertHandler.showConfirmationAlert("Are you sure?", "All unsaved changes will be lost")) {
				return;
			}
		}

		refresh();
		PageFactory.toNextPage(PageFactory.getIntakeEntry(foodID, PageOption.INTAKE_STOCK));
	}

	private void handleMoreOnClick() {
		if (createForm.isVisible()) {
			if (!AlertHandler.showConfirmationAlert("Are you sure?", "All unsaved changes will be lost")) {
				return;
			}
		}

		option = PageOption.DEFAULT;
		refresh();
		PageFactory.toNextPage(PageFactory.getIngredientPage(foodID));
	}

	private void handleCancelOnClick() {
		if (createForm.isVisible()) {
			if (AlertHandler.showConfirmationAlert("Are you sure?", "All unsaved changes will be lost")) {
				option = PageOption.DEFAULT;
				refresh();
			}
		}
	}

	private void handleDeleteOnClick() {
		if (createForm.isVisible()) {
			if (AlertHandler.showConfirmationAlert("Are you sure?", "This stock entry will be deleted")) {
				StockController controller = ControllerFactory.makeStockController();
				controller.deleteStock(currentStockID);

				option = PageOption.DEFAULT;
				refresh();
			}
		}
	}

	private void handleSaveAddOnClick() {
		if (createForm.isVisible()) {
			option = PageOption.STOCK;
			baseTotal = total.getValue();

			amountTxF.setText("0");
			dateTxF.setValue(LocalDate.now());

			deleteEntryButton.setVisible(false);
		} else {
			if (current.getValue() <= 0) {
				AlertHandler.showAlert(Alert.AlertType.ERROR, "Add Stock Failed...", "New Amount Must Be A Positive Number");
				return;
			}

			StockController controller = ControllerFactory.makeStockController();
			LocalDate expDate = dateTxF.getValue().plusDays(baseTimeLeft);

			switch (option) {
				case STOCK:
					controller.createStock(foodID, current.getValue(), expDate.toString());
					break;
				case UPDATE:
					controller.updateStock(currentStockID, current.getValue(), expDate.toString());
					break;
			}

			option = PageOption.DEFAULT;
			refresh();
		}
	}

	private void handleInputOnType() {
		current.setValue(Utility.parseQuantity(amountTxF.getText(), 0));
		total.setValue(baseTotal + current.getValue());
	}

	private void handleDateOnType() {
		LocalDate addedDate = dateTxF.getValue();
		timeLeft.setValue(baseTimeLeft + DAYS.between(LocalDate.now(), addedDate));
		entryAddedDate.setText(String.format("ADDED %S", Utility.parseProperDate(addedDate.toString())));

		String plural = timeLeft.getValue() > 1 ? "s" : "";
		if (timeLeft.getValue() > 0) {
			entryTimeLeft.setText(String.format("Expires in %d day%s", timeLeft.getValue(), plural));
		} else if (timeLeft.getValue() < 0) {
			entryTimeLeft.setText(String.format("Expired %d day%s ago", -timeLeft.getValue(), plural));
		} else {
			entryTimeLeft.setText("Expires today");
		}
	}

	private void handleCardScroll(ScrollEvent event) {
		double diffHeight = 0;
		if (event.getDeltaY() > 0) diffHeight = -10;
		else if (event.getDeltaY() < 0) diffHeight = 10;

		double top = AnchorPane.getTopAnchor(adjustSizePane) - diffHeight;
		if (top > Utility.MAX_TOP_ANCHOR) top = Utility.MAX_TOP_ANCHOR;
		else if (top < Utility.MIN_TOP_ANCHOR) top = Utility.MIN_TOP_ANCHOR;

		if (adjustSizePane.getMinHeight() <= adjustSizePane.getHeight() && adjustSizePane.getHeight() <= adjustSizePane.getMaxHeight())  {
			AnchorPane.setTopAnchor(adjustSizePane, top);
			adjustSizePane.setPrefHeight(adjustSizePane.getHeight() + diffHeight);
		}
	}

	private void handleListScroll(ScrollEvent event) {
		if (scrollList.getHeight() == scrollList.getMaxHeight()) {
			return;
		}

		event.consume();
		ScrollEvent retargettedScrollEvent = new ScrollEvent(adjustSizePane, adjustSizePane, event.getEventType(),
			event.getX(), event.getY(), event.getScreenX(), event.getScreenY(), event.isShiftDown(),
			event.isControlDown(), event.isAltDown(), event.isMetaDown(), event.isDirect(),
			event.isInertia(), event.getDeltaX(), event.getDeltaY(), event.getTotalDeltaX(),
			event.getTotalDeltaY(), event.getTextDeltaXUnits(), event.getTextDeltaX(),
			event.getTextDeltaYUnits(), event.getTextDeltaY(), event.getTouchCount(),
			event.getPickResult());

		Event.fireEvent(adjustSizePane, retargettedScrollEvent);
	}
}