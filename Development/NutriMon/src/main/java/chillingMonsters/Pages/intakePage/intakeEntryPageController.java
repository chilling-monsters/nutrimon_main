package chillingMonsters.Pages.intakePage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Intake.IntakeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Pages.searchPage.SearchCardComponent;
import chillingMonsters.Utility;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class intakeEntryPageController implements PageController {
	private long intakeID;
	private double currentAmount = 0;
	private PageOption option;

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

		if (option == PageOption.INTAKE) {

		} else {

		}
	}

	private void refreshIntake() {
		IntakeController controller = ControllerFactory.makeIntakeController();
		Map<String, Object> intake = controller.getIntake(intakeID);

		String type = intake.get("type").toString();
		entryCategory.setText(type.toUpperCase());

		Long ID = 0L;
		String name = "", category = "";
		if (intake.get("foodID") != null) {
			ID = Utility.parseID(intake.get("foodID").toString(), 0);
			currentAmount = Utility.parseQuantity(intake.get("intakeQtty").toString(), 0);

			addIngredientCard(ID);
		} else if (intake.get("recipeID") != null) {
			ID = Utility.parseID(intake.get("recipeID").toString(), 0);
			currentAmount = Utility.parseQuantity(intake.get("serving").toString(), 0);

			addRecipeCard(ID);
		}

		String amountStrFormatter = type.toUpperCase() == "STOCK" ? "%.1f g" : ("%.1f serving" + (Math.abs(currentAmount - 1) > 0.1 ? "s" : ""));
		entryCurrentAmount.setText(String.format(amountStrFormatter, currentAmount));
		amountTxF.setText(String.format("%.1f", currentAmount));

		int calories = Integer.parseInt(intake.get("Calories").toString());
		entryCalories.setText(String.format("%d Cal", calories));

		Date date = (Date) intake.get("date");
		//TODO: set local date
//		dateTxF.setValue(date);
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

	private void handleCancelOnClick() {

	}

	private void handleDeleteOnClick() {

	}

	private void handleSaveOnClick() {

	}
}

