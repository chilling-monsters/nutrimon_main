package chillingMonsters.Pages.stockPage;

import chillingMonsters.AlertHandler;
import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Stock.StockController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
	private boolean showForm = false;

	private long foodID;
	private double totalAmount = 0;
	private int avgSpoilageDays;

	private long currentStockItemID;
	private long displaySpoilageDays = 0;
	private Timestamp displayAddedDate = new Timestamp(System.currentTimeMillis());
	private double displayAmount = 0;

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
	public ImageView moreButton;

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
		refreshStock();

		if (option == PageOption.STOCK) {
			handleAddStock();
		} else {
			toggleForm(false);
		}

		moreButton.setOnMouseClicked(event -> handleMoreOnClick());
		addStockButton.setOnAction(event -> handleAddStock());
		deleteEntryButton.setOnAction(event -> handleDeleteStock());
		cancelEntryButton.setOnAction(event -> handleCancel());
		scrollList.addEventFilter(ScrollEvent.SCROLL, event -> handleListScroll(event));
		adjustSizePane.setOnScroll(event -> handleCardScroll(event));

		amountTxF.setOnAction(event -> handleAddStock());
		amountTxF.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > Utility.TEXTFIELD_MAX_LENGTH) return;

				handleInputChange(oldValue, newValue);
			}
		});

		dateTxF.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				handleDateChange(newValue);
			}
		});
	}

	private void refreshStock() {
		entryList.getChildren().clear();
		scrollList.setPrefHeight(scrollList.getMinHeight());
		scrollList.setVvalue(0);

		IngredientController ingrController = ControllerFactory.makeIngredientController();
		Map<String, Object> ingredient = ingrController.getIngredient(foodID);

		String name = ingredient.get("foodName").toString();
		String category = ingredient.get("fCategory").toString();
		avgSpoilageDays = Integer.parseInt(ingredient.get("expTime").toString());

		entryName.setText(Utility.parseFoodName(name));
		entryCategory.setText(category);
		entryAvgSpoilage.setText(String.format("%d Day%s", avgSpoilageDays, avgSpoilageDays > 1 ? "s" : ""));

		StockController controller = ControllerFactory.makeStockController();
		List<Map<String, Object>> resultsList = controller.showStockEntry(foodID);

		totalAmount = 0;
		if (resultsList.isEmpty()) {
			Label emptyLabel = new Label("We ain't got squash.");
			emptyLabel.getStyleClass().add("emptyWarningText");

			entryList.getChildren().add(emptyLabel);
		} else {
			for (Map<String, Object> result : resultsList) {
				Long stockItemID = Long.parseLong(result.get("stockItemID").toString());
				Long timeLeft = Long.parseLong(result.get("time_left").toString());
				Timestamp addedDate = (Timestamp) result.get("added_date");
				float amount = Float.parseFloat(result.get("foodQtty").toString());
				totalAmount += amount;

				StockEntryCardComponent sCard = new StockEntryCardComponent(stockItemID, timeLeft, addedDate, amount);

				if (timeLeft <= Utility.SPOILAGE_WARNING_DAYS) sCard.getStyleClass().add("hightlightCard");

				sCard.setOnMouseClicked(event -> handleCardClick(event));

				entryList.getChildren().add(sCard);
			}
		}

		entryTotalAmount.setText(String.format("%.0f g", totalAmount));
	}

	private void handleMoreOnClick() {
		handleCancel();
		PageFactory.toNextPage(PageFactory.getIngredientPage(foodID));
	}

	private void handleAddStock() {
		if (!showForm) {
			option = PageOption.STOCK;

			displaySpoilageDays = avgSpoilageDays;
			displayAddedDate = Utility.today();
			displayAmount = 0;

			toggleForm(true);
		} else {
			if (displayAmount <= 0) {
				addStockButton.setSelected(true);
				AlertHandler.showAlert(Alert.AlertType.ERROR, "Add Stock Failed...", "New Amount Must Be A Positive Number");
				return;
			}

			StockController controller = ControllerFactory.makeStockController();
			LocalDate expDate = displayAddedDate.toLocalDateTime().toLocalDate().plusDays(avgSpoilageDays);
			switch (option) {
				case STOCK:
					controller.createStock(foodID, displayAmount, expDate.toString());
					break;
				case UPDATE:
					controller.updateStock(currentStockItemID, displayAmount, expDate.toString());
					break;
				case DEFAULT:
					break;
			}

			toggleForm(false);
		}
	}

	private void handleCancel() {
		if (showForm) {
			boolean confirmed = AlertHandler.showConfirmationAlert("Are you sure?", "Unsaved changes will be lost");
			if (confirmed) {
				toggleForm(false);
			}
		}
	}

	private void handleDeleteStock() {
		StockController controller = ControllerFactory.makeStockController();

		if (option == PageOption.UPDATE) {
			boolean confirmed = AlertHandler.showConfirmationAlert("Delete Stock", "Are you sure you want to delete this stock?");
			if (confirmed) {
				controller.deleteStock(currentStockItemID);
				toggleForm(false);
			}
		} else {
			handleCancel();
		}
	}

	private void toggleForm(boolean show) {
		showForm = show;
		if (show) {
			addStockButton.setText("Save");
			addStockButton.setSelected(true);
			adjustSizePane.setPrefHeight(adjustSizePane.getMinHeight());

			dateTxF.setValue(displayAddedDate.toLocalDateTime().toLocalDate());
			amountTxF.setText(String.format("%.0f", displayAmount));
			entryCurrentAmount.setText(String.format("%.0f grams", displayAmount));

			setExpiryString();

			deleteEntryButton.setVisible(option == PageOption.UPDATE);
		} else {
			option = PageOption.DEFAULT;
			addStockButton.setText("+");
			addStockButton.setSelected(false);
			amountTxF.setText("0");
			entryCurrentAmount.setText("0 grams");
			refreshStock();
		}

		entryList.setVisible(!show);
		createForm.setVisible(show);
	}

	private void handleInputChange(String oldValue, String newValue) {
		displayAmount = Utility.parseQuantity(newValue, 0);

		if (oldValue.isEmpty()) {
			totalAmount += displayAmount;
		} else {
			double oldAmount = Utility.parseQuantity(oldValue, displayAmount);
			totalAmount += (displayAmount - oldAmount);
		}


		entryCurrentAmount.setText(String.format("%.0f grams", displayAmount));
		entryTotalAmount.setText(String.format("%.0f g", totalAmount));
	}

	private void handleCardClick(MouseEvent event) {
		if (!showForm) {
			option = PageOption.UPDATE;

			StockEntryCardComponent clickedCard = (StockEntryCardComponent) event.getSource();
			currentStockItemID = clickedCard.getStockItemID();
			displaySpoilageDays = clickedCard.getTimeLeft();
			displayAddedDate = clickedCard.getAddedDate();
			displayAmount = clickedCard.getAmount();
			totalAmount -= displayAmount;

			toggleForm(true);
		}
	}

	private void handleDateChange(LocalDate newValue) {
		displayAddedDate = Timestamp.valueOf(newValue.atStartOfDay());
		entryAddedDate.setText(String.format("FROM %S", Utility.parseDate(displayAddedDate)));

		displaySpoilageDays = avgSpoilageDays - DAYS.between(newValue, LocalDate.now());
		setExpiryString();
	}

	private void setExpiryString() {
		String plural = Math.abs(displaySpoilageDays) > 1 ? "s" : "";
		if (displaySpoilageDays > 0) {
			entryTimeLeft.setText(String.format("Expires in %d day%s", displaySpoilageDays, plural));
		} else if (displaySpoilageDays < 0) {
			entryTimeLeft.setText(String.format("Expired %d day%s ago", -displaySpoilageDays, plural));
		} else {
			entryTimeLeft.setText("Expires today");
		}
	}

	private void handleCardScroll(ScrollEvent event) {
		if (showForm) return;

		double diffHeight = 0;
		if (event.getDeltaY() > 0) diffHeight = -10;
		else if (event.getDeltaY() < 0) diffHeight = 10;

		adjustSizePane.setPrefHeight(adjustSizePane.getHeight() + diffHeight);
		scrollList.setPrefHeight(scrollList.getHeight() + diffHeight);
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
