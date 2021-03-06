package chillingMonsters.Controllers.Recipe;

import chillingMonsters.Controllers.NutriMonController;
import chillingMonsters.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static chillingMonsters.DBConnect.resultsList;

/**
 * Concrete implementation of the Recipe Controller.
 */
public class RecipeControllerImpl extends NutriMonController implements RecipeController {

  public RecipeControllerImpl() {
    super("recipes", "recipeID");
  }

  public List<Map<String, Object>> searchRecipe(String name) {
    List<Map<String, Object>> result = null;
    String query = "SELECT * FROM recipes WHERE recipeName like ?";
    try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
      stmt.setString(1, String.format("%%%s%%", name));
      try (ResultSet rs = stmt.executeQuery()) {
        result = resultsList(rs);
      }
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
    return result;
  }

  public List<Map<String, Object>> showAvailableRecipes() {
    List<Map<String, Object>> result = null;
    String query = "SELECT r.*, calcRecipeCalories(recipeID) as 'caloriesPerServing' " +
        "FROM recipes r WHERE canBeMade(?,recipeID) > 0";
    try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
      stmt.setLong(1, userId);
      try (ResultSet rs = stmt.executeQuery()) {
        result = resultsList(rs);
      }
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
    return result;
  }

  public List<Map<String, Object>> showCreatedRecipes() {
    return this.show();
  }

  public void deleteRecipe(long recipeID) {
    this.delete(recipeID);
  }

  public List<Map<String, Object>> showSavedRecipes() {
    List<Map<String, Object>> result = null;
    String query = "SELECT r.*, calcRecipeCalories(recipeID) as 'caloriesPerServing' " +
        "FROM recipes r JOIN promotedrecipe pr USING (recipeID) " +
            "WHERE pr.userID = ?";
    try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
      stmt.setLong(1, userId);
      try (ResultSet rs = stmt.executeQuery()) {
        result = resultsList(rs);
      }
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
    return result;
  }

  public void saveRecipe(long recipeID) {
    if (isSaved(recipeID)) return;

    String query = "INSERT INTO promotedrecipe VALUES (?,?)";
    try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
      stmt.setLong(1, userId);
      stmt.setLong(2, recipeID);
      stmt.executeUpdate();
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();;
    }
  }

  public void unsaveRecipe(long recipeID) {
    if (!isSaved(recipeID)) return;

    String query = String.format("DELETE FROM promotedrecipe WHERE %s = ?", this.pk);
    try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
      stmt.setLong(1, recipeID);
      stmt.executeUpdate();
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();;
    }
  }

  public boolean isSaved(long recipeID) {
    return this.exists("promotedrecipe", this.pk, Long.toString(recipeID));
  }

  public void updateRecipe(long recipeID, String name, String category, String description, double cookTime, Map<Long, Float> ingredients) {
    String clearIngredients = "DELETE FROM recipeingredients WHERE recipeID = ?";
    String insertIngredients = "INSERT INTO recipeingredients VALUES (?,?,?)";
    Map<String, Object> payload = new HashMap<>();
    payload.put("recipeName", name);
    payload.put("recipeDescription", description);
    payload.put("recipeCategory", category);
    payload.put("recipeCookTIme", cookTime);
    this.update(recipeID, payload);
    Connection connection = DBConnect.getConnection();
    try (PreparedStatement clearStmt = connection.prepareStatement(clearIngredients)) {
      connection.setAutoCommit(false);
      clearStmt.setLong(1, recipeID);
      clearStmt.executeUpdate();
      try (PreparedStatement updateIngredients = connection.prepareStatement(insertIngredients)) {
        for (Map.Entry<Long, Float> entry : ingredients.entrySet()) {
          updateIngredients.setLong(1, entry.getKey());
          updateIngredients.setLong(2, recipeID);
          updateIngredients.setFloat(3, entry.getValue());
          updateIngredients.executeUpdate();
        }
      }
      connection.commit();
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
      try {
        connection.rollback();
      } catch (SQLException e1) {
        Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e1.getMessage(), e1);
      }
    } finally {
      try {
        connection.setAutoCommit(true);
      } catch (SQLException e) {
        Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
      }
      DBConnect.close();
    }
  }

  public long createRecipe(String name, String category, String description, double cookTime, Map<Long, Float> ingredients) {
    Map<String, Object> payload = new HashMap<>();
    payload.put("recipeName", name);
    payload.put("recipeDescription", description);
    payload.put("recipeCategory", category);
    payload.put("recipeCookTIme", cookTime);
    long recipeId = 0;
    this.create(payload);
    String selectId = "SELECT recipeID FROM recipes WHERE recipeName like ? AND userID = ?";
    String insertIngredients = "INSERT INTO recipeingredients VALUES (?,?,?)";
    Connection connection = DBConnect.getConnection();
    try (PreparedStatement getId = connection.prepareStatement(selectId)) {
      getId.setString(1, String.format("%%%s%%", name));
      getId.setLong(2, userId);
      ResultSet rs = getId.executeQuery();
      if (rs.first()) {
        recipeId = rs.getLong("recipeID");
        try (PreparedStatement postIngredients = connection.prepareStatement(insertIngredients)) {
          for (Map.Entry<Long, Float> entry : ingredients.entrySet()) {
            postIngredients.setLong(1, entry.getKey());
            postIngredients.setLong(2, recipeId);
            postIngredients.setFloat(3, entry.getValue());
            postIngredients.executeUpdate();
          }
        }
      }
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }

    return recipeId;
  }

  public Map<String, Object> getRecipe(long recipeID) {
    String getRecipe = "SELECT r.*, calcRecipeCalories(recipeID) as 'caloriesPerServing' " +
            "FROM recipes r WHERE r.recipeID = ?";
    String getIngredients = "SELECT foodID, ingredientQtty " +
            "FROM recipeingredients JOIN ingredients USING (foodID) " +
            "WHERE recipeID = ?";

    Map<String, Object> recipe = new HashMap<>();
    List<Map<String, Object>> ingredients;

    Connection connection = DBConnect.getConnection();
    try {
      PreparedStatement stmtRcp = connection.prepareStatement(getRecipe);
      stmtRcp.setLong(1, recipeID);

      PreparedStatement stmtIngr = connection.prepareStatement(getIngredients);
      stmtIngr.setLong(1, recipeID);

      ResultSet rs = stmtRcp.executeQuery();
      List<Map<String, Object>> recipes = resultsList(rs);
      if (!recipes.isEmpty()) recipe = recipes.get(0);

      rs = stmtIngr.executeQuery();
      ingredients = resultsList(rs);

      recipe.put("ingredients", ingredients);
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
    return recipe;
  }
}
