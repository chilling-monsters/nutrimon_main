package chillingMonsters.Controllers.Intake;

import java.util.List;
import java.util.Map;

public interface IntakeController {
  void intakeRecipe(long recipeID, int servings);

  void intakeStock(long foodID, float quantity);

  Map<String, List<Map<String, Object>>> showIntakesByDate();

  void updateIntakeDate(long intakeID, String date);
}
