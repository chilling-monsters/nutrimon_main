package chillingMonsters.Pages.intakePage;

import chillingMonsters.AlertHandler;
import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Intake.IntakeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Pages.searchPage.SearchCardComponent;
import chillingMonsters.Utility;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class intakeEntryPageController implements PageController {
	private long ID;
	private PageOption option;

	private StringProperty category = new SimpleStringProperty("");
	private double amount;
	private double caloriesPerUnit;

	@FXML
	private Label entryDate;

	@FXML
	private Label entryCategory;

	@FXML
	private VBox entryContent;

	@FXML
	private Label entryCurrentAmount;

	@FXML
	private Label entryCalories;

	@FXML
	private VBox editForm;

	@FXML
	private TextField amountTxF;

	@FXML
	private DatePicker dateTxF;

	@FXML
	private Button cancelEntryButton;

	@FXML
	private ToggleButton saveEntryButton;

	public intakeEntryPageController(long ID, PageOption option) {
		this.ID = ID;
		this.option = option;
	}

	@FXML
	public void initialize() {
		editForm.visibleProperty().bindBidirectional(saveEntryButton.selectedProperty());

		cancelEntryButton.setOnAction(event -> handleCancelOnClick());
		saveEntryButton.selectedProperty().addListener(event -> handleSaveOnClick());
		amountTxF.textProperty().addListener(event -> handleInputChange());
		dateTxF.valueProperty().addListener(event -> handleDateChange());
		entryCategory.textProperty().bind(Bindings.format("INTAKE TYPE: %S", category));
	}

	public void refresh() {
		switch (option) {
			case INTAKE_STOCK:
				category.setValue("STOCK");
				amountTxF.setText("0");
				dateTxF.setValue(LocalDate.now());
				saveEntryButton.setSelected(true);
				addIngredientCard(ID);
				break;
			case INTAKE_RECIPE:
				category.setValue("RECIPE");
				amountTxF.setText("0");
				dateTxF.setValue(LocalDate.now());
				saveEntryButton.setSelected(true);
				addRecipeCard(ID);
				break;
			case UPDATE:
				amountTxF.setDisable(true);
				refreshIntake();
				break;
			default:
				break;
		}
	}

	//event listeners
	private void handleInputChange() {
		amount = Utility.parseQuantity(amountTxF.getText(), 0);

		String sFormatter = (category.getValue().equals("RECIPE")) ? "%.1f serving" + ((amount - 1 > 0.1) ? "s" : "") : "%.1f g";
		entryCurrentAmount.setText(String.format(sFormatter, amount));

		Double updateCal = caloriesPerUnit * amount;
		entryCalories.setText(String.format("%.0f Cal", updateCal));
	}

	private void handleDateChange() {
		String date = dateTxF.getValue().toString();
		entryDate.setText(Utility.parseProperDate(date));
	}

	private boolean handleCancelOnClick() {
		if (editForm.isVisible()) {
			boolean confirmed = AlertHandler.showConfirmationAlert("Are you sure?", "Unsaved changes will be lost");
			if (confirmed) {
				editForm.setVisible(false);
			}

			return confirmed;
		}

		return true;
	}

	private void handleSaveOnClick() {
		boolean selected = saveEntryButton.isSelected();
		PageFactory.setFormInProgress(selected);
		saveEntryButton.setText(selected ? "Save" : "Edit");

		if (!saveEntryButton.isSelected()) {
			IntakeController controller = ControllerFactory.makeIntakeController();

			switch (option) {
				case INTAKE_STOCK:
					controller.intakeStock(ID, (float) amount);
					PageFactory.toNextPage(PageFactory.getIntakePage());
					break;
				case INTAKE_RECIPE:
					break;
				case UPDATE:
					controller.updateIntakeDate(ID, Timestamp.valueOf(dateTxF.getValue().atStartOfDay()).toString());
					break;
			}
		}
	}

	//helper functions
	private void refreshIntake() {
		IntakeController controller = ControllerFactory.makeIntakeController();
		Map<String, Object> intake = controller.getIntake(ID);

		category.setValue(intake.get("type").toString().toUpperCase());

		Long contentID = 0L;
		if (intake.get("foodID") != null) {
			contentID = Utility.parseID(intake.get("foodID").toString(), 0);
			amount = Utility.parseQuantity(intake.get("intakeQtty").toString(), 0);

			addIngredientCard(contentID);
		} else if (intake.get("recipeID") != null) {
			contentID = Utility.parseID(intake.get("recipeID").toString(), 0);
			amount = Utility.parseQuantity(intake.get("serving").toString(), 0);

			addRecipeCard(contentID);
		}

		amountTxF.setText(String.format("%.1f", amount));

		String date = intake.get("date").toString();
		dateTxF.setValue(LocalDate.parse(date));
	}

	private void addIngredientCard(long foodID) {
		Map<String, Object> ingr = ControllerFactory.makeIngredientController().getIngredient(foodID);
		String name = Utility.parseFoodName(ingr.get("foodName").toString());
		String category = ingr.get("fCategory").toString();
		caloriesPerUnit = Double.parseDouble(ingr.get("fCalories").toString()) / 100;

		SearchCardComponent inCard = new SearchCardComponent(foodID, name, category, PageOption.DEFAULT);
		EventHandler<? super MouseEvent> onMouseClicked = inCard.getOnMouseClicked();
		inCard.setOnMouseClicked(event -> {
			if (handleCancelOnClick()) onMouseClicked.handle(event);
		});
		entryContent.getChildren().add(inCard);
	}

	private void addRecipeCard(long recipeID) {
		Map<String, Object> rcp = ControllerFactory.makeRecipeController().getRecipe(recipeID);
		String name = Utility.toCapitalized(rcp.get("recipeName").toString());
		String category = rcp.get("recipeCategory").toString();
		caloriesPerUnit = Double.parseDouble(rcp.get("caloriesPerServing").toString());

		SearchCardComponent rpCard = new SearchCardComponent(recipeID, name, category, PageOption.RECIPE);
		EventHandler<? super MouseEvent> onMouseClicked = rpCard.getOnMouseClicked();
		rpCard.setOnMouseClicked(event -> {
			if (handleCancelOnClick()) onMouseClicked.handle(event);
		});
		entryContent.getChildren().add(rpCard);
	}
}

