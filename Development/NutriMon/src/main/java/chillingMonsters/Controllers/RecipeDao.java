package chillingMonsters.Controllers;

import java.util.List;
import java.util.Map;

public interface RecipeDao {

  List<Map<String, Object>> searchRecipes(String name);

  List<Map<String, Object>> getAvailableRecipes();

  List<Map<String, Object>> getMyRecipes();

  void createRecipe(String name, String description, Map<Integer, Float> ingredients);

  void deleteRecipe(long recipeID);

  void updateRecipe(long recipeID, String name, String description, Map<Integer, Float> ingredients);

  Map<String, Object> getRecipe(long recipeID);

  List<Map<String, Object>> getSavedRecipes();

  void saveRecipe(long recipeID);
}
