package chillingMonsters.Controllers.Intake;

import java.util.List;
import java.util.Map;

/**
 * Methods and API offered by an Intake Controller.
 */
public interface IntakeController {
  /**
   * Consumes a recipe with the given ID.  Generates a new intake instance documenting the action
   * and automatically updates the stock to remove consumed ingredients.
   * @param recipeID the id of the recipe that was consumed.
   * @param servings how many servings of the recipe were consumed.
   */
  void intakeRecipe(long recipeID, float servings, String date);

  /**
   * Consumes an ingredient with the given ID.  Generates a new intake instance documenting the action
   * and automatically updates the stock to remove consumed ingredients.
   * @param foodID the id of the ingredient that was consumed.
   * @param quantity how many servings of the recipe were consumed.
   */
  void intakeStock(long foodID, float quantity, String date);

  /**
   * Gets all the intakes of the user, grouped by date and ordered by time.
   * @return A mapping of the String date to all intakes that occurred on that date.
   */
  Map<String, List<Map<String, Object>>> showIntakesByDate();

  /**
   * Updates the date of an existing intake.
   * @param intakeID identifier for the intake to update.
   * @param date the new date of the intake.
   */
  void updateIntakeDate(long intakeID, String date);

  /**
   * Gets the intake of the user with the given intakeID.
   * @param intakeID the identifier of the intake instance.
   * @return the desired intake. Returns null if no such intake exists.
   */
  Map<String, Object> getIntake(long intakeID);
}
