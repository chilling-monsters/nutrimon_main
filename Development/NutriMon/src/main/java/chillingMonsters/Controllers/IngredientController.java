package chillingMonsters.Controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import chillingMonsters.DBConnect;

import static chillingMonsters.DBConnect.resultsList;

public class IngredientController extends NutriMonController implements IngredientDao {

  IngredientController() {
    super("ingredients", "foodID");
  }

  public List<Map<String, Object>> search(String foodName) {
    String query = "SELECT * FROM ingredients WHERE foodName like ? ORDER BY foodName ASC";
    List<Map<String, Object>> ingredients = new ArrayList<>();
    try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
      stmt.setString(1, String.format("%%%s%%", foodName));
      ResultSet rs = stmt.executeQuery();
      ingredients = resultsList(rs);
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
    return ingredients;
  }

  public Map<String, Object> getIngredient(long foodID) {
    String query = "SELECT * FROM ingredients WHERE foodID = ?";
    Map<String, Object> ingredient = null;
    try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
      stmt.setLong(1,foodID);
      try (ResultSet rs = stmt.executeQuery()) {
        List<Map<String, Object>> ingredients = resultsList(rs);
        if (!ingredients.isEmpty()) {
          ingredient = ingredients.get(0);
        }
      }
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
    return ingredient;
  }
}
