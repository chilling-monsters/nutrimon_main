package chillingMonsters.Controllers;

import chillingMonsters.Controllers.Ingredient.IngredientController;
import chillingMonsters.Controllers.Ingredient.IngredientControllerImpl;
import chillingMonsters.Controllers.Intake.IntakeController;
import chillingMonsters.Controllers.Intake.IntakeControllerImpl;
import chillingMonsters.Controllers.Recipe.RecipeController;
import chillingMonsters.Controllers.Recipe.RecipeControllerImpl;
import chillingMonsters.Controllers.Stock.StockController;
import chillingMonsters.Controllers.Stock.StockControllerImpl;
import chillingMonsters.Controllers.UserProfile.UserProfileController;
import chillingMonsters.Controllers.UserProfile.UserProfileControllerImpl;

/**
 * Factory class to build and hold singletons of all controllers.
 */
public abstract class ControllerFactory {
  // Singletons of each controller.
  private static StockController stockControl = null;
  private static UserProfileController loginControl = null;
  private static RecipeController recipeControl = null;
  private static IngredientController ingredientControl = null;
  private static IntakeController intakeControl = null;

  private ControllerFactory(){}

  public static StockController makeStockController() {
    if (stockControl == null) {
      stockControl = new StockControllerImpl();
    }
    return stockControl;
  }

  public static UserProfileController makeUserProfileController() {
    if (loginControl == null) {
      loginControl = new UserProfileControllerImpl();
    }
    return loginControl;
  }

  public static IngredientController makeIngredientController() {
    if (ingredientControl == null) {
      ingredientControl = new IngredientControllerImpl();
    }
    return ingredientControl;
  }

  public static IntakeController makeIntakeController() {
    if (intakeControl == null) {
      intakeControl = new IntakeControllerImpl();
    }
    return intakeControl;
  }

  public static RecipeController makeRecipeController() {
    if (recipeControl == null) {
      recipeControl = new RecipeControllerImpl();
    }
    return recipeControl;
  }
}
