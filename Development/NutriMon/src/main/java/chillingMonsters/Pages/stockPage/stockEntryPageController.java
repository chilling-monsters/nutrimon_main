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
	public ImageView backButton;

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

		if (option == PageOption.ADD_STOCK) {
			handleAddStock();
		} else {
			showForm = false;
			toggleForm();
		}

		backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				handleBackOnClick(event);
			}
		});

		moreButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				handleMoreOnClick(event);
			}
		});

		addStockButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleAddStock();
			}
		});

		amountTxF.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleAddStock();
			}
		});

		amountTxF.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > Utility.TEXTFIELD_MAX_LENGTH) return;

				handleInputChange(oldValue, newValue);
			}
		});

		deleteEntryButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleDeleteStock();
			}
		});

		cancelEntryButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleCancel();
			}
		});

		dateTxF.valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				handleDateChange(newValue);
			}
		});

		scrollList.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				handleListScroll(event);
			}
		});

		adjustSizePane.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				handleCardScroll(event);
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
		avgSpoilageDays = (Integer) ingredient.get("expTime");

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
				Long stockItemID = (Long) result.get("stockItemID");
				Long timeLeft = (Long) result.get("time_left");
				Timestamp addedDate = (Timestamp) result.get("added_date");
				float amount = (Float) result.get("foodQtty");
				totalAmount += amount;

				StockEntryCardComponent sCard = new StockEntryCardComponent(stockItemID, timeLeft, addedDate, amount);

				if (timeLeft <= Utility.SPOILAGE_WARNING_DAYS) sCard.getStyleClass().add("expireWarningCard");

				sCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						handleCardClick(event);
					}
				});

				entryList.getChildren().add(sCard);
			}
		}

		entryTotalAmount.setText(String.format("%.0f g", totalAmount));
	}

	private void handleBackOnClick(MouseEvent event) {
		if (showForm) {
			handleCancel();
		} else {
			ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());
			PageFactory.getLastPage().startPage(e);
		}

	}

	private void handleMoreOnClick(MouseEvent event) {
		handleCancel();

		ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());
		PageFactory.getIngredientPage(foodID).startPage(e);
	}

	private void handleAddStock() {
		if (!showForm) {
			option = PageOption.ADD_STOCK;

			displaySpoilageDays = avgSpoilageDays;
			displayAddedDate = Utility.today();
			displayAmount = 0;

			showForm = true;
			toggleForm();
		} else {
			if (displayAmount <= 0) {
				addStockButton.setSelected(true);
				AlertHandler.showAlert(Alert.AlertType.ERROR, "Add Stock Failed...", "New Amount Must Be A Positive Number");
				return;
			}

			StockController controller = ControllerFactory.makeStockController();
			LocalDate expDate = displayAddedDate.toLocalDateTime().toLocalDate().plusDays(avgSpoilageDays);
			switch (option) {
				case ADD_STOCK:
					controller.createStock(foodID, displayAmount, expDate.toString());
					break;
				case UPDATE:
					controller.updateStock(currentStockItemID, displayAmount, expDate.toString());
					break;
				case DEFAULT:
					break;
			}

			showForm = false;
			toggleForm();
		}
	}

	private void handleCancel() {
		showForm = false;
		option = PageOption.DEFAULT;
		toggleForm();
	}

	private void handleDeleteStock() {
		StockController controller = ControllerFactory.makeStockController();

		if (option == PageOption.UPDATE) {
			AlertHandler.showAlert(Alert.AlertType.CONFIRMATION, "Delete Stock", "Are you sure you want to delete this stock?");
			controller.deleteStock(currentStockItemID);
		}

		showForm = false;
		toggleForm();
	}

	private void toggleForm() {
		if (showForm) {
			addStockButton.setText("Save");
			addStockButton.setSelected(true);
			adjustSizePane.setPrefHeight(adjustSizePane.getMinHeight());

			dateTxF.setValue(displayAddedDate.toLocalDateTime().toLocalDate());
			amountTxF.setText(String.format("%.0f", displayAmount));
			entryCurrentAmount.setText(String.format("%.0f grams", displayAmount));

			setExpiryString();
		} else {
			addStockButton.setText("+");
			addStockButton.setSelected(false);
			amountTxF.setText("0");
			entryCurrentAmount.setText("0 grams");
			refreshStock();
		}

		entryList.setVisible(!showForm);
		createForm.setVisible(showForm);
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
			showForm = true;

			StockEntryCardComponent clickedCard = (StockEntryCardComponent) event.getSource();
			currentStockItemID = clickedCard.getStockItemID();
			displaySpoilageDays = clickedCard.getTimeLeft();
			displayAddedDate = clickedCard.getAddedDate();
			displayAmount = clickedCard.getAmount();
			totalAmount -= displayAmount;

			toggleForm();
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
