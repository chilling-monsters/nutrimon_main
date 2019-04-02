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

public class IngredientController extends NutriMonController {
  private static final String READ_ONLY = "Ingredients are READ ONLY";

  IngredientController() {
    super("ingredients", "foodID");
  }

  @Override
  public void create(Map<String, Object> attributes) {
    throw new UnsupportedOperationException(READ_ONLY);
  }

  @Override
  public void update(long id, Map<String, Object> attributes) {
    throw new UnsupportedOperationException(READ_ONLY);
  }

  @Override
  public void delete(long id) {
    throw new UnsupportedOperationException(READ_ONLY);
  }

  @Override
  public List<Map<String, Object>> show() {
    throw new UnsupportedOperationException("Use Search API");
  }

  public List<Map<String, Object>> search(String foodName) {
    String query = "SELECT * FROM ingredients WHERE foodName like ? ORDER BY foodName ASC";
    List<Map<String, Object>> ingredients = new ArrayList<>();
    try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
      stmt.setString(1, String.format("%%%s%%", foodName));
      ResultSet rs = stmt.executeQuery();
      ingredients = DBConnect.resultsList(rs);
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
    DBConnect.close();
    return ingredients;
  }
}
