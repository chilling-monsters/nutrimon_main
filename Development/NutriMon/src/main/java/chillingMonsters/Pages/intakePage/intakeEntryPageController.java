package chillingMonsters.Pages.intakePage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Intake.IntakeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Pages.searchPage.SearchCardComponent;
import chillingMonsters.Utility;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class intakeEntryPageController implements PageController {
	private long intakeID;
	private PageOption option;

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
	private AnchorPane editForm;

	@FXML
	private TextField amountTxF;

	@FXML
	private DatePicker dateTxF;

	@FXML
	private Button deleteEntryButton;

	@FXML
	private Button cancelEntryButton;

	@FXML
	private Button saveEntryButton;

	public intakeEntryPageController(long intakeID, PageOption option) {
		this.intakeID = intakeID;
		this.option = option;
	}

	@FXML
	public void initialize() {
		cancelEntryButton.setOnAction(event -> handleCancelOnClick());
		deleteEntryButton.setOnAction(event -> handleDeleteOnClick());
		saveEntryButton.setOnAction(event -> handleSaveOnClick());
		amountTxF.textProperty().addListener(event -> handleInputChange());
		dateTxF.valueProperty().addListener(event -> handleDateChange());

		switch (option) {
			case INTAKE_STOCK:
				break;
			case INTAKE_RECIPE:
				break;
			case UPDATE:
				refreshIntake();
				break;
			default:
				break;
		}
	}

	private void refreshIntake() {
		IntakeController controller = ControllerFactory.makeIntakeController();
		Map<String, Object> intake = controller.getIntake(intakeID);

		String type = intake.get("type").toString().toUpperCase();
		if (type.equals("RECIPE")) option = PageOption.INTAKE_RECIPE;
		else if (type.equals("STOCK")) option = PageOption.INTAKE_STOCK;

		entryCategory.setText(String.format("INTAKE TYPE: %S", type));

		Long ID = 0L;
		if (intake.get("foodID") != null) {
			ID = Utility.parseID(intake.get("foodID").toString(), 0);
			amount = Utility.parseQuantity(intake.get("intakeQtty").toString(), 0);

			addIngredientCard(ID);
		} else if (intake.get("recipeID") != null) {
			ID = Utility.parseID(intake.get("recipeID").toString(), 0);
			amount = Utility.parseQuantity(intake.get("serving").toString(), 0);

			addRecipeCard(ID);
		}

		int calories = Integer.parseInt(intake.get("Calories").toString());
		caloriesPerUnit = calories / amount;
		amountTxF.setText(String.format("%.1f", amount));

		String date = intake.get("date").toString();
		dateTxF.setValue(LocalDate.parse(date));
	}

	private void addIngredientCard(long foodID) {
		Map<String, Object> ingr = ControllerFactory.makeIngredientController().getIngredient(foodID);
		String name = Utility.parseFoodName(ingr.get("foodName").toString());
		String category = ingr.get("fCategory").toString();

		entryContent.getChildren().add(new SearchCardComponent(foodID, name, category, PageOption.DEFAULT));
	}

	private void addRecipeCard(long recipeID) {
		Map<String, Object> rcp = ControllerFactory.makeRecipeController().getRecipe(recipeID);
		String name = Utility.toCapitalized(rcp.get("recipeName").toString());
		String category = rcp.get("recipeCategory").toString();

		entryContent.getChildren().add(new SearchCardComponent(recipeID, name, category, PageOption.RECIPE));
	}

	private void handleInputChange() {
		Double value = Utility.parseQuantity(amountTxF.getText(), 0);

		String sFormatter = (option == PageOption.INTAKE_RECIPE) ? "%.1f serving" + ((value - 1 > 0.1) ? "s" : "") : ".1f g";
		entryCurrentAmount.setText(String.format(sFormatter, value));

		Double updateCal = caloriesPerUnit * value;
		entryCalories.setText(String.format("%.0f Cal", updateCal));
	}

	private void handleDateChange() {
		String date = dateTxF.getValue().toString();
		entryDate.setText(Utility.parseProperDate(date));
	}

	private void handleCancelOnClick() {

	}

	private void handleDeleteOnClick() {

	}

	private void handleSaveOnClick() {

	}
}

