package chillingMonsters.Controllers;

import java.util.List;
import java.util.Map;

public interface RecipeDao {

  List<Map<String, Object>> getRecipes();

  List<Map<String, Object>> getAvailableRecipes();

  List<Map<String, Object>> getMyRecipes();

  void createRecipe(Map<String, Object> recipe, Map<Integer, Float> ingredients);

  void deleteRecipe(int recipeID);

  void updateRecipe(Map<String, Object> recipe, Map<Integer, Float> ingredients);

  Map<String, Object> getRecipe(int recipeID);
}
