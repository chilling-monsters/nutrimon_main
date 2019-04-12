package chillingMonsters.Pages.intakePage;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Intake.IntakeController;
import chillingMonsters.Pages.PageController;
import chillingMonsters.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.sql.Date;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class intakePageController implements PageController {
	private static final String TODAY = "Today";

	@FXML
	private VBox cardList;

	@FXML
	private Button stockCreateButton;

	@FXML
	void stockCreateButtonAction(ActionEvent event) {

	}

	@FXML
	public void initialize() {
		IntakeController controller = ControllerFactory.makeIntakeController();
		Map<String, List<Map<String, Object>>> results = controller.showIntakesByDate();

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
				double amount = 0D;
				String name = "", category = "";
				if (intake.get("foodID") != null) {
					ID = Utility.parseID(intake.get("foodID").toString(), 0);
					amount = Utility.parseQuantity(intake.get("intakeQtty").toString(), 0);

					Map<String, Object> ingr = ControllerFactory.makeIngredientController().getIngredient(ID);
					name = Utility.parseFoodName(ingr.get("foodName").toString());
					category = ingr.get("fCategory").toString();
				} else if (intake.get("recipeID") != null) {
					ID = Utility.parseID(intake.get("recipeID").toString(), 0);
					amount = Utility.parseQuantity(intake.get("serving").toString(), 0);

					Map<String, Object> rcp = ControllerFactory.makeRecipeController().getRecipe(ID);
					name = rcp.get("recipeName").toString();
					category = rcp.get("recipeCategory").toString();
				}

				IntakeCardComponent inCard = new IntakeCardComponent(intakeID, name, String.format("%s: %s", type, category), calories, amount);

				dateGroup.add(inCard);
			}
			break;
		}

		for (String k : componentMap.keySet()) {
			List<IntakeCardComponent> group = componentMap.get(k);

			addToList(k, group);
		}
	}

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
