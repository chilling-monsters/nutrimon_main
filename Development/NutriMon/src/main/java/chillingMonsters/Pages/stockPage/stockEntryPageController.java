package chillingMonsters.Pages.stockPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.StockController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageOption;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class stockEntryPageController implements PageController {
	private long foodID;
	private PageOption option;

	@FXML
	public Label entryName;

	@FXML
	public Label entryAvgSpoilage;

	@FXML
	public Label entryCategory;

	@FXML
	public VBox entryList;

	public stockEntryPageController(long foodID, PageOption option) {
		this.foodID = foodID;
		this.option = option;
	}

	@FXML
	public void initialize() {
		StockController controller = ControllerFactory.makeStockController();
		List<Map<String, Object>> resultsList = controller.showStockIngredient(foodID);

		entryName.setText(controller.parseFoodName(resultsList.get(0).get("foodName").toString()));
		entryCategory.setText(resultsList.get(0).get("fCategory").toString());
		entryAvgSpoilage.setText(String.format("%d Days", resultsList.get(0).get("expTime")));

		for (Map<String, Object> result : resultsList) {
//			String timeLeft = result.get("foodExpDate").toString();
//			float amount = (Float) result.get("foodQtty");
//			StockEntryCardComponent sCard = new StockEntryCardComponent(timeLeft, "DAMIT", amount);
//
//			entryList.getChildren().add(sCard);
		}
	}
}
