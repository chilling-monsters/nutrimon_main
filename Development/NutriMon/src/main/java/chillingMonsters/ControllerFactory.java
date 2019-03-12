package chillingMonsters;

abstract class ControllerFactory {
  private static Stock stockControl = null;
//  private static RecipeController recipeControl = null;
//  private static IngredientController ingredientControl = null;
//  private static IntakeController intakeControl = null;

  public static Stock makeStockController() {
    if (stockControl == null) {
      stockControl = new Stock();
    }
    return stockControl;
  }
}
