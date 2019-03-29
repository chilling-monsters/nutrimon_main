package chillingMonsters.Controllers;

public abstract class ControllerFactory {
  private static StockController stockControl = null;
  private static UserProfileController loginControl = null;
//  private static RecipeController recipeControl = null;
//  private static IngredientController ingredientControl = null;
//  private static IntakeController intakeControl = null;

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
}
