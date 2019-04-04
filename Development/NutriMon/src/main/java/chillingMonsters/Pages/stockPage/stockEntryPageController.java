package chillingMonsters.Pages.stockPage;

import chillingMonsters.AlertHandler;
import chillingMonsters.Controllers.ControllerFactory;
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
	private long foodID;
	private long currentStockItemID;
	private int avgSpoilageDays;
	private PageOption option;
	private double displayAmount = 0;
	private boolean showForm = false;

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
		createForm.setVisible(false);
		refreshStock();

		backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				handleBackOnClick(event);
			}
		});

		addStockButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleAddStock(event);
			}
		});

		amountTxF.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleAddStock(event);
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
				handleCancel(event);
			}
		});
	}

	private void refreshStock() {
		entryList.getChildren().clear();

		StockController controller = ControllerFactory.makeStockController();
		List<Map<String, Object>> resultsList = controller.showStockIngredient(foodID);

		String name = resultsList.get(0).get("foodName").toString();
		String category = resultsList.get(0).get("fCategory").toString();
		avgSpoilageDays = (Integer) resultsList.get(0).get("expTime");

		entryName.setText(Utility.parseFoodName(name));
		entryCategory.setText(category);
		entryAvgSpoilage.setText(String.format("%d Days", avgSpoilageDays));

		for (Map<String, Object> result : resultsList) {
			Long timeLeft = (Long) result.get("time_left");
			String addedDate = Utility.parseDate((Timestamp) result.get("added_date"));
			float amount = (Float) result.get("foodQtty");
			StockEntryCardComponent sCard = new StockEntryCardComponent(timeLeft, addedDate, amount);

			entryList.getChildren().add(sCard);
		}
	}

	private void handleBackOnClick(MouseEvent event) {
		ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());

		PageFactory.getStockPage().startPage(e);
	}

	private void handleAddStock(ActionEvent event) {
		if (!showForm) {
			option = PageOption.ADD_STOCK;
			showForm = true;
			toggleForm();
		}
		else {
			if (displayAmount == 0) {
				addStockButton.setSelected(true);
				AlertHandler.showAlert(Alert.AlertType.ERROR, "Add Stock Failed...", "New Amount Must Be A Positive Number");
				return;
			}
			showForm = false;
			toggleForm();
		}
	}

	private void handleCancel(ActionEvent event) {
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
			handleInputChange();

			if (option == PageOption.ADD_STOCK) {
				entryTimeLeft.setText(String.format("Expires in %d days", avgSpoilageDays));
				entryAddedDate.setText("IF ADDED TODAY");
				dateTxF.setValue(LocalDate.now());
			}
		} else {
			option = PageOption.DEFAULT;
			addStockButton.setText("+");
			addStockButton.setSelected(false);
			amountTxF.setText("");
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
}
