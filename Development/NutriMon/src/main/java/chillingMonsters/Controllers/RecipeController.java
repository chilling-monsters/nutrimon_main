package chillingMonsters.Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import chillingMonsters.DBConnect;

import static chillingMonsters.DBConnect.resultsList;

public class RecipeController extends NutriMonController implements RecipeDao {

  RecipeController() {
    super("recipes", "recipeID");
  }

  public List<Map<String, Object>> searchRecipes(String name) {
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

  public List<Map<String, Object>> getAvailableRecipes() {
    List<Map<String, Object>> result = null;
    String query = "SELECT * FROM recipes WHERE canBeMade(?,recipeID) > 0";
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

  public List<Map<String, Object>> getMyRecipes() {
    return this.show();
  }

  public void deleteRecipe(long recipeID) {
    this.delete(recipeID);
  }

  public void updateRecipe(long recipeID, String name, String description, Map<Integer, Float> ingredients) {
    String clearIngredients = "DELETE FROM recipeingredients WHERE recipeID = ? AND ? IN (" +
            "SELECT DISTINCT recipeID FROM recipes WHERE userID = ?)";
    String insertIngredients = "INSERT INTO recipeingredients VALUES (?,?,?)";
    Map<String, Object> payload = new HashMap<>();
    payload.put("recipeName", name);
    payload.put("recipeDescription", description);
    this.update(recipeID, payload);
    Connection connection = DBConnect.getConnection();
    try (PreparedStatement clearStmt = connection.prepareStatement(clearIngredients)) {
      clearStmt.setLong(1, recipeID);
      clearStmt.setLong(2, recipeID);
      clearStmt.setLong(3, userId);
      clearStmt.executeUpdate();
      try (PreparedStatement updateIngredients = connection.prepareStatement(insertIngredients)) {
        for (Map.Entry<Integer, Float> entry : ingredients.entrySet()) {
          updateIngredients.setLong(1, entry.getKey());
          updateIngredients.setLong(2, recipeID);
          updateIngredients.setFloat(3, entry.getValue());
          updateIngredients.executeUpdate();
        }
      }
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
  }

  public void createRecipe(String name, String description, Map<Integer, Float> ingredients) {
    Map<String, Object> payload = new HashMap<>();
    payload.put("recipeName", name);
    payload.put("recipeDescription", description);
    int recipeId;
    this.create(payload);
    String selectId = "SELECT recipeID FROM recipe WHERE recipeName like ? AND userID = ?";
    String insertIngredients = "INSERT INTO recipeingredients VALUES (?,?,?)";
    Connection connection = DBConnect.getConnection();
    try (PreparedStatement getId = connection.prepareStatement(selectId)) {
      getId.setString(1, String.format("%%%s%%", name));
      getId.setLong(2, userId);
      ResultSet rs = getId.executeQuery();
      if (rs.first()) {
        recipeId = rs.getInt("recipeID");
        try (PreparedStatement postIngredients = connection.prepareStatement(insertIngredients)) {
          for (Map.Entry<Integer, Float> entry : ingredients.entrySet()) {
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
  }

  public Map<String, Object> getRecipe(long recipeID) {
    String getIngredients = "SELECT foodID, ingredientQtty " +
            "FROM recipeingredients JOIN ingredients USING (foodID) " +
            "WHERE recipeID = ?";
    Map<String, Object> recipe = this.get(recipeID);
    Map<Integer, Float> ingredients = new HashMap<>();
    Connection connection = DBConnect.getConnection();
    if (recipe != null) {
      try (PreparedStatement stmt = connection.prepareStatement(getIngredients)) {
        stmt.setLong(1, recipeID);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
          ingredients.put(rs.getInt("foodID"), rs.getFloat("ingredientQtty"));
        }
        recipe.put("ingredients", ingredients);
      } catch (SQLException e) {
        Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
      } finally {
        DBConnect.close();
      }
    }
    return recipe;
  }
}
