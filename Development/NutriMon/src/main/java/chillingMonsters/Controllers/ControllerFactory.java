package chillingMonsters.Controllers;

public abstract class ControllerFactory {
  private static StockController stockControl = null;
  private static UserProfileController loginControl = null;
  private static RecipeController recipeControl = null;
  private static IngredientController ingredientControl = null;
  private static IntakeController intakeControl = null;

  public static StockController makeStockController() {
    if (stockControl == null) {
      stockControl = new StockController();
    }
    return stockControl;
  }

  public static UserProfileController makeUserProfileController() {
    if (loginControl == null) {
      loginControl = new UserProfileController();
    }
    return loginControl;
  }

  public static IngredientController makeIngredientController() {
    if (ingredientControl == null) {
      ingredientControl = new IngredientController();
    }
    return ingredientControl;
  }

  public static IntakeController makeIntakeController() {
    if (intakeControl == null) {
      intakeControl = new IntakeController();
    }
    return intakeControl;
  }

  public static RecipeController makeRecipeController() {
    if (recipeControl == null) {
      recipeControl = new RecipeController();
    }
    return recipeControl;
  }
}
