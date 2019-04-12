package chillingMonsters.Controllers.Recipe;

import java.util.List;
import java.util.Map;

/**
 * Methods and API offered by a RecipeController.
 */
public interface RecipeController {
  /**
   * Searches a recipe by name.
   * @param name the name to filter by.
   * @return all recipes matching the name.
   */
  List<Map<String, Object>> searchRecipe(String name);

  /**
   * Gets a recipe with the matching recipeID. Returns null if none exists.
   * @param recipeID id of the desired recipe.
   * @return Map of the recipe's attributes and values.
   */
  Map<String, Object> getRecipe(long recipeID);

  /**
   * Creates a recipe with the specified parameters.
   * @param name name fo the recipe
   * @param category category of the recipe
   * @param description description of the recipe
   * @param cookTime general time taken to prepare the recipe.
   * @param ingredients the ingredients needed to make the recipe.
   * @return the ID of the generated recipe.
   */
  long createRecipe(String name, String category, String description, double cookTime, Map<Long, Float> ingredients);

  /**
   * Deletes the specified recipe.
   * @param recipeID id of the recipe to delete.
   */
  void deleteRecipe(long recipeID);

  /**
   * Updates an exising recipe with the given parameters.
   * @param recipeID the id of the recipe to update.
   * @param name the new name of the recipe.
   * @param category the new category of the recipe.
   * @param description the new description of the recipe.
   * @param cookTime the new cook time fo the recipe.
   * @param ingredients the new ingredients needed for the recipe.
   */
  void updateRecipe(long recipeID, String name, String category, String description, double cookTime, Map<Long, Float> ingredients);

  /**
   * Displays all recipes that can be made with the user's existing stock.
   * @return the recipes that can be made.
   */
  List<Map<String, Object>> showAvailableRecipes();

  /**
   * Shows all the recipes that the user has created his or herself.
   * @return the recipes made by the logged in user.
   */
  List<Map<String, Object>> showCreatedRecipes();

  /**
   * Shows the recipes that the user has saved.
   * @return saved recipes.
   */
  List<Map<String, Object>> showSavedRecipes();

  /**
   * Saves a recipe as a favorite.
   * @param recipeID the id of the favorited recipe.
   */
  void saveRecipe(long recipeID);

  /**
   * Unsaves a recipe.
   * @param recipeID the id of the recipe to unfavorite.
   */
  void unsaveRecipe(long recipeID);

  /**
   * Returns if a recipe has been saved by the user or not.
   * @param recipeID the id of the target recipe.
   * @return whether or not the recipe is saved.
   */
  boolean isSaved(long recipeID);
}
