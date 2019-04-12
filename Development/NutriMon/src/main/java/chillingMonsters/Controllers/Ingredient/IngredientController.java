package chillingMonsters.Controllers.Ingredient;

import java.util.List;
import java.util.Map;

/**
 * Methods offered by an IngredientController
 */
public interface IngredientController {
  /**
   * Searches for Ingredients that match the specified name.
   * @param name Name of an ingredient to search for.
   * @return A list of ingredients represented as Maps (JSON)
   */
  List<Map<String, Object>> searchIngredient(String name);

  /**
   * Gets teh ingredient with the given ID. Returns null if none exists.
   * @param foodID the ID of the ingredient to search for.
   * @return The ingredient attribute names mapped to values.
   */
  Map<String, Object> getIngredient(long foodID);
}
