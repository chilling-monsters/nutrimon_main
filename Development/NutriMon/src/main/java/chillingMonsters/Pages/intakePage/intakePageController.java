package chillingMonsters.Pages.intakePage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Intake.IntakeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class intakePageController implements PageController {
	private static final String TODAY = "Today";

	@FXML
	private VBox cardList;

	@FXML
	private Button createButton;

	@FXML
	public void initialize() {
		cardList.getChildren().clear();
	}

	public void refresh() {
		initialize();
		IntakeController controller = ControllerFactory.makeIntakeController();
		Map<String, List<Map<String, Object>>> results = controller.showIntakesByDate();

		if (results == null || results.isEmpty()) {
			Label emptyLabel = new Label("We haven't eaten any squash");
			emptyLabel.getStyleClass().add("emptyWarningText");

			cardList.getChildren().add(emptyLabel);
			return;
		}

		Map<String, List<IntakeCardComponent>> componentMap = new TreeMap<>();
		Map<String, Integer> caloriesMap = new TreeMap<>();

		for (String d : results.keySet()) {
			if (componentMap.get(d) == null) {
				componentMap.put(d, new ArrayList<>());
			}
			List<IntakeCardComponent> dateGroup = componentMap.get(d);

			if (caloriesMap.get(d) == null) {
				caloriesMap.put(d, 0);
			}
			Integer dayTotal = caloriesMap.get(d);

			List<Map<String, Object>> byDate = results.get(d);
			for (Map<String, Object> intake : byDate) {
				Long ID = 0L;
				String name = "", category = "", amountStr = "";
				if (intake.get("foodID") != null) {
					ID = Utility.parseID(intake.get("foodID").toString(), 0);
					double amount = Utility.parseQuantity(intake.get("intakeQtty").toString(), 0);
					amountStr = String.format("%.1fg", amount);

					Map<String, Object> ingr = ControllerFactory.makeIngredientController().getIngredient(ID);
					name = Utility.parseFoodName(ingr.get("foodName").toString());
					category = ingr.get("fCategory").toString();
				} else if (intake.get("recipeID") != null) {
					ID = Utility.parseID(intake.get("recipeID").toString(), 0);
					double amount = Utility.parseQuantity(intake.get("serving").toString(), 0);
					amountStr = String.format("%.1f serving" + ((Math.abs(amount - 1) > 0.1) ? "s" : ""), amount);

					Map<String, Object> rcp = ControllerFactory.makeRecipeController().getRecipe(ID);
					name = Utility.toCapitalized(rcp.get("recipeName").toString());
					category = rcp.get("recipeCategory").toString();
				}

				Long intakeID = Utility.parseID(intake.get("intakeID").toString(), 0);
				int calories = Integer.parseInt(intake.get("Calories").toString());
				String type = intake.get("type").toString();

				IntakeCardComponent inCard = new IntakeCardComponent(intakeID, name, String.format("%s: %s", type, category), calories, amountStr);

				dateGroup.add(inCard);
				dayTotal += calories;
			}

			caloriesMap.put(d, dayTotal);
		}

		for (String k : componentMap.keySet()) {
			List<IntakeCardComponent> group = componentMap.get(k);
			Integer dayTotal = caloriesMap.get(k);

			addToList(Utility.parseProperDate(k), dayTotal, group);
		}
	}

	//button event handlers
	@FXML
	void createIntakeAction() {
		PageFactory.toNextPage(PageFactory.getSearchPage(PageOption.INTAKE_STOCK));
	}

	//helper functions
	private void addToList(String label, Integer cal, List<IntakeCardComponent> group) {
		for (IntakeCardComponent s : group) {
			s.getStyleClass().add("listCard");
			if (label == TODAY) s.getStyleClass().add("hightlightCard");
			cardList.getChildren().add(0, s);
		}

		Line underline = new Line();
		underline.setStartX(0.0f);
		underline.setStartY(100.0f);
		underline.setEndX(300.0f);
		underline.setEndY(100.0f);
		underline.getStyleClass().add("line");
		cardList.getChildren().add(0, underline);

		AnchorPane titlePane = new AnchorPane();
		Label groupLabel = new Label(label);
		if (label == TODAY) groupLabel.getStyleClass().add("hightlightText");
		groupLabel.getStyleClass().add("labelText");

		titlePane.getChildren().add(groupLabel);
		AnchorPane.setLeftAnchor(groupLabel, 0D);

		Label calLabel = new Label(String.format("%d Cal", cal));
		if (label == TODAY) calLabel.getStyleClass().add("hightlightText");
		calLabel.getStyleClass().add("labelText");

		titlePane.getChildren().add(calLabel);
		AnchorPane.setRightAnchor(calLabel, 0D);

		cardList.getChildren().add(0, titlePane);
	}
}
