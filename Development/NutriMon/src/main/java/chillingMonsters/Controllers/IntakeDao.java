package chillingMonsters.Controllers;

import java.util.List;
import java.util.Map;

public interface IntakeDao {

  void intakeRecipe(long recipeID, int servings);

  void intakeStock(long foodID, float quantity);

  Map<String, List<Map<String, Object>>> showIntakesByDate();
}
