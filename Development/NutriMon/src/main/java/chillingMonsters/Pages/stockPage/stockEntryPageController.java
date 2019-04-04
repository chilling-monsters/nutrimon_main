package chillingMonsters.Pages.stockPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.IngredientController;
import chillingMonsters.Pages.PageController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Map;

public class stockEntryPageController implements PageController {
	@FXML
	public Label entryName;

	@FXML
	public Label entryAvgSpoilage;

	@FXML
	public Label entryCategory;

	public stockEntryPageController(long foodID) {
		IngredientController controller = ControllerFactory.makeIngredientController();
//		Map<String, Object> ingredient = controller.getFood(foodID);

//		entryName.setText(ingredient.get("foodName").toString());
//		entryAvgSpoilage.setText(ingredient.get("f"));
	}

	@FXML
	public void initialize() {

	}
}
