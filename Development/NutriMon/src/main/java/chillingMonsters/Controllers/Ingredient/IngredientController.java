package chillingMonsters.Controllers.Ingredient;

import java.util.List;
import java.util.Map;

public interface IngredientController {
  List<Map<String, Object>> search(String name);

  Map<String, Object> getIngredient(long foodID);
}
