package chillingMonsters.Controllers.Recipe;

import java.util.List;
import java.util.Map;

public interface RecipeController {
  List<Map<String, Object>> searchRecipe(String name);

  Map<String, Object> getRecipe(long recipeID);

  void createRecipe(String name, String category, String description, double cookTime, Map<Long, Float> ingredients);

  void deleteRecipe(long recipeID);

  void updateRecipe(long recipeID, String name, String description, Map<Long, Float> ingredients);

  List<Map<String, Object>> showAvailableRecipes();

  List<Map<String, Object>> showCreatedRecipes();

  List<Map<String, Object>> showSavedRecipes();

  void saveRecipe(long recipeID);

  void unsaveRecipe(long recipeID);

  boolean isSaved(long recipeID);
}
