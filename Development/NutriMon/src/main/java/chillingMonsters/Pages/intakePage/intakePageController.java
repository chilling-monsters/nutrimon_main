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

		if (results.isEmpty()) {
			Label emptyLabel = new Label("We ain't got squash.");
			emptyLabel.getStyleClass().add("emptyWarningText");

			cardList.getChildren().add(emptyLabel);
			return;
		}

		Map<String, List<IntakeCardComponent>> componentMap = new TreeMap<>();
		for (String d : results.keySet()) {

			List<IntakeCardComponent> dateGroup = componentMap.get(d);
			if (dateGroup == null) {
				dateGroup = new ArrayList<>();
				componentMap.put(Utility.parseProperDate(d), dateGroup);
			}

			List<Map<String, Object>> byDate = results.get(d);
			for (Map<String, Object> intake : byDate) {
				Long intakeID = Utility.parseID(intake.get("intakeID").toString(), 0);
				int calories = Integer.parseInt(intake.get("Calories").toString());
				String type = intake.get("type").toString();

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

				IntakeCardComponent inCard = new IntakeCardComponent(intakeID, name, String.format("%s: %s", type, category), calories, amountStr);

				dateGroup.add(inCard);
			}
			break;
		}

		for (String k : componentMap.keySet()) {
			List<IntakeCardComponent> group = componentMap.get(k);

			addToList(k, group);
		}
	}

	//button event handlers
	@FXML
	void createIntakeAction() {
		PageFactory.toNextPage(PageFactory.getSearchPage(PageOption.INTAKE_STOCK));
	}

	//helper functions
	private void addToList(String label, List<IntakeCardComponent> group) {
		Label groupLabel = new Label(Utility.toCapitalized(label));
		if (label == TODAY) groupLabel.getStyleClass().add("hightlightText");

		groupLabel.getStyleClass().add("labelText");

		Line underline = new Line();
		underline.setStartX(0.0f);
		underline.setStartY(100.0f);
		underline.setEndX(300.0f);
		underline.setEndY(100.0f);
		underline.getStyleClass().add("line");

		cardList.getChildren().add(groupLabel);
		cardList.getChildren().add(underline);

		for (IntakeCardComponent s : group) {
			s.getStyleClass().add("listCard");
			if (label == TODAY) s.getStyleClass().add("hightlightCard");
			cardList.getChildren().add(s);
		}
	}
}
