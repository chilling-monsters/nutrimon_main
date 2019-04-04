package chillingMonsters.Pages.stockPage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.StockController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.sql.Timestamp;
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

	@FXML
	public ImageView backButton;

	public stockEntryPageController(long foodID, PageOption option) {
		this.foodID = foodID;
		this.option = option;
	}

	@FXML
	public void initialize() {
		StockController controller = ControllerFactory.makeStockController();
		List<Map<String, Object>> resultsList = controller.showStockIngredient(foodID);

		entryName.setText(Utility.parseFoodName(resultsList.get(0).get("foodName").toString()));
		entryCategory.setText(resultsList.get(0).get("fCategory").toString());
		entryAvgSpoilage.setText(String.format("%d Days", resultsList.get(0).get("expTime")));

		for (Map<String, Object> result : resultsList) {
			Long timeLeft = (Long) result.get("time_left");
			String addedDate = Utility.parseDate((Timestamp) result.get("added_date"));
			float amount = (Float) result.get("foodQtty");
			StockEntryCardComponent sCard = new StockEntryCardComponent(timeLeft, addedDate, amount);

			entryList.getChildren().add(sCard);
		}

		backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ActionEvent e = new ActionEvent(event.getSource(), event.getTarget());

				PageFactory.getStockPage().startPage(e);
			}
		});
	}
}
