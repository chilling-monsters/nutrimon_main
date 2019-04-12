package chillingMonsters.Pages.intakePage;

import chillingMonsters.Controllers.ControllerFactory;
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

import java.util.HashMap;
import java.util.Map;

public class intakeEntryPageController implements PageController {
	private long intakeID;

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
	private Button addRecipeButton;

	public intakeEntryPageController(long intakeID) {
		this.intakeID = intakeID;
	}

	@FXML
	public void initialize() {
		IntakeController controller = ControllerFactory.makeIntakeController();
		Map<String, Object> intake = new HashMap<String, Object>() {{
			put("type", "STOCK");
			put("foodID", 200L);
			put("intakeQtty", 12.5);
			put("foodName", "Bullshit");
		}}; //controller.getIntake(intakeID);

		String type = intake.get("type").toString();
		entryCategory.setText(type);

		Long ID = 0L;
		double amount = 0D;
		String name = "", category = "";
		if (intake.get("foodID") != null) {
			ID = Utility.parseID(intake.get("foodID").toString(), 0);
			amount = Utility.parseQuantity(intake.get("intakeQtty").toString(), 0);

			Map<String, Object> ingr = ControllerFactory.makeIngredientController().getIngredient(ID);
			name = ingr.get("foodName").toString();
			category = ingr.get("fCategory").toString();
		} else if (intake.get("recipeID") != null) {
			ID = Utility.parseID(intake.get("recipeID").toString(), 0);
			amount = Utility.parseQuantity(intake.get("serving").toString(), 0);

			Map<String, Object> rcp = ControllerFactory.makeRecipeController().getRecipe(ID);
			name = rcp.get("recipeName").toString();
			category = rcp.get("recipeCategory").toString();
		}
		entryContent.getChildren().add(new SearchCardComponent(ID, name, category, PageOption.DEFAULT));

		String amountStrFormatter = type.toUpperCase() == "STOCK" ? "%.1f g" : ("%.0f servings" + (amount > 1 ? "s" : ""));
		entryCurrentAmount.setText(String.format(amountStrFormatter, amount));
	}
}

