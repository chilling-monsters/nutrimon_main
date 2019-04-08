package chillingMonsters.Pages.ingredientPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.Map;

public class ingredientPageController implements PageController {
	private long ingredientID;

	@FXML
	private ImageView backButton;

	@FXML
	private Label ingrName;

	@FXML
	private Label ingrCategory;

	@FXML
	private Label ingrAvgSpoilage;

	@FXML
	private Label ingrCalories;

	@FXML
	private Label ingrProtein;

	@FXML
	private Label ingrTotalFat;

	@FXML
	private Label ingrSatFat;

	@FXML
	private Label ingrCholesterol;

	@FXML
	private Label ingrCarb;

	@FXML
	private Label ingrSugar;

	@FXML
	private Label ingrSodium;

	@FXML
	private Label ingrCalcium;

	@FXML
	private Label ingrIron;

	@FXML
	private Label ingrPotassium;

	@FXML
	private Label ingreC;

	@FXML
	private Label ingreE;

	@FXML
	private Label ingreD;

	@FXML
	private ToggleButton addToStockButton;

	public ingredientPageController(long ingredientID) {
		this.ingredientID = ingredientID;
	}

	@FXML
	public void initialize() {
		IngredientController controller = ControllerFactory.makeIngredientController();
		Map<String, Object> result = controller.getIngredient(ingredientID);

		ingrName.setText(Utility.parseFoodName(result.get("foodName").toString()));
		ingrCategory.setText(result.get("fCategory").toString());
		ingrAvgSpoilage.setText(String.format("%s Days", result.get("expTime").toString()));
		ingrCalories.setText(String.format("%s kCal", result.get("fCalories").toString()));

		ingrProtein.setText(result.get("fProtein").toString() + "g");
		ingrTotalFat.setText(result.get("fTotalFat").toString() + "g");
		ingrSatFat.setText(result.get("fSaturatedFat").toString() + "g");
		ingrCholesterol.setText(result.get("fCholestero").toString() + "g");
		ingrCarb.setText(result.get("fCarbohydrate").toString() + "g");
		ingrSugar.setText(result.get("fSugar").toString() + "g");

		ingrSodium.setText(result.get("fSodium").toString() + "g");
		ingrCalcium.setText(result.get("fCalcium").toString() + "g");
		ingrIron.setText(result.get("fIron").toString() + "g");
		ingrPotassium.setText(result.get("fPotassium").toString() + "g");
		ingreC.setText(result.get("fVC").toString() + "g");
		ingreE.setText(result.get("fVE").toString() + "g");
		ingreD.setText(result.get("fVD").toString() + "g");

		addToStockButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				PageFactory.getStockEntryPage(ingredientID, PageOption.ADD_STOCK).startPage(event);
			}
		});

		backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());
				PageFactory.getLastPage().startPage(e);
			}
		});
	}
}
