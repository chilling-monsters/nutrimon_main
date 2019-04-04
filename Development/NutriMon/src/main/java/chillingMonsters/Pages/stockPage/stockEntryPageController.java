package chillingMonsters.Pages.stockPage;

import chillingMonsters.AlertHandler;
import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.IngredientController;
import chillingMonsters.Controllers.StockController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class stockEntryPageController implements PageController {
	private PageOption option;
	private boolean showForm = false;

	private long foodID;
	private int avgSpoilageDays;

	private long currentStockItemID;
	private long displaySpoilageDays;
	private Timestamp displayAddedDate;
	private double displayAmount = 0;


	@FXML
	public Label entryName;

	@FXML
	public Label entryAvgSpoilage;

	@FXML
	public Label entryCategory;

	@FXML
	public VBox entryList;

	@FXML
	public VBox createForm;

	@FXML
	public ImageView backButton;

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

	public stockEntryPageController(long foodID, PageOption option) {
		this.foodID = foodID;
		this.option = option;
	}

	@FXML
	public void initialize() {
		refreshStock();

		if (option == PageOption.ADD_STOCK) {
			showForm = true;
			toggleForm();
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
				handleInputChange();
			}
		});

		deleteEntryButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleDeleteStock(event);
			}
		});

		cancelEntryButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleCancel();
			}
		});
	}

	private void refreshStock() {
		entryList.getChildren().clear();

		IngredientController ingrController = ControllerFactory.makeIngredientController();
		Map<String, Object> ingredient = ingrController.getIngredient(foodID);

		String name = ingredient.get("foodName").toString();
		String category = ingredient.get("fCategory").toString();
		avgSpoilageDays = (Integer) ingredient.get("expTime");

		entryName.setText(Utility.parseFoodName(name));
		entryCategory.setText(category);
		entryAvgSpoilage.setText(String.format("%d Days", avgSpoilageDays));

		StockController controller = ControllerFactory.makeStockController();
		List<Map<String, Object>> resultsList = controller.showStockIngredient(foodID);

		for (Map<String, Object> result : resultsList) {
			Long stockItemID = (Long) result.get("stockItemID");
			Long timeLeft = (Long) result.get("time_left");
			Timestamp addedDate = (Timestamp) result.get("added_date");
			float amount = (Float) result.get("foodQtty");
			StockEntryCardComponent sCard = new StockEntryCardComponent(stockItemID, timeLeft, addedDate, amount);

			sCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					handleCardClick(event);
				}
			});

			entryList.getChildren().add(sCard);
		}
	}

	private void handleBackOnClick(MouseEvent event) {
		ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());

		PageFactory.getStockPage().startPage(e);
	}

	private void handleAddStock() {
		if (!showForm) {
			option = PageOption.ADD_STOCK;
			showForm = true;
			toggleForm();
		} else {
			if (displayAmount <= 0) {
				addStockButton.setSelected(true);
				AlertHandler.showAlert(Alert.AlertType.ERROR, "Add Stock Failed...", "New Amount Must Be A Positive Number");
				return;
			}
			StockController controller = ControllerFactory.makeStockController();
			controller.createStock(foodID, displayAmount);

			showForm = false;
			toggleForm();
		}
	}

	private void handleCancel() {
		showForm = false;
		toggleForm();
	}

	private void handleDeleteStock(ActionEvent event) {
		showForm = false;
	}

	private void toggleForm() {
		if (showForm) {
			addStockButton.setText("Save");
			addStockButton.setSelected(true);

			if (option == PageOption.ADD_STOCK) {
				displaySpoilageDays = avgSpoilageDays;
				displayAddedDate = Utility.today();
				handleInputChange();
			}

			entryCurrentAmount.setText(String.format("%.0f grams", displayAmount));
			entryTimeLeft.setText(String.format("Expires in %d days", displaySpoilageDays));
			entryAddedDate.setText(String.format("FROM %S", Utility.parseDate(displayAddedDate)));
			dateTxF.setValue(displayAddedDate.toLocalDateTime().toLocalDate());
		} else {
			option = PageOption.DEFAULT;
			addStockButton.setText("+");
			addStockButton.setSelected(false);
			amountTxF.setText("");
			refreshStock();
		}

		entryList.setVisible(!showForm);
		createForm.setVisible(showForm);
	}

	private void handleInputChange() {
		String amountTxt = amountTxF.getText();

		try {
			displayAmount = Double.parseDouble(amountTxt);
			if (displayAmount <= 0) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			displayAmount = 0;
		}
		entryCurrentAmount.setText(String.format("%.0f grams", displayAmount));
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

			toggleForm();
		}
	}
}
