package chillingMonsters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Stock extends NutriMonController {
  Stock() {
    super("stockitems", "stockItemID");
  }

  /**
   * Return number of servings of a recipe that can be made from user's stock.
   *
   * @param recipeIngredients map of the food ids and quantities that make up a recipe.
   * @return number of servings that can be made
   */
  public static int includesIngredients(Map<Integer, Float> recipeIngredients) {
    Map<Integer, Float> stockIngredients = new HashMap<>();
    String queryString = "Select foodID, sum(sQuantity) AS 'quantity' " +
            "FROM stockitems " +
            "WHERE userID = ? " +
            "GROUP BY foodID";
    try {
      ResultSet rs;
      try (PreparedStatement stmt = MySQLCon.getConnection()
              .prepareStatement(queryString)) {
        stmt.setInt(1, userId);
        rs = stmt.executeQuery();
      }
      List<Map<String, Object>> stock = MySQLCon.resultsList(rs);
      for (Map<String, Object> stockItem : stock) {
        stockIngredients.put((Integer)stockItem.get("foodID"), (Float)stockItem.get("quantity"));
      }
      rs.close();
      MySQLCon.close();
    } catch (SQLException e) {
      Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, "Failed select", e);
    }
    if (recipeIngredients.size() > stockIngredients.size()) {
      return 0;
    }
    int servings = 0;
    for (Map.Entry<Integer, Float> ingredient : recipeIngredients.entrySet()) {
      if (stockIngredients.containsKey(ingredient.getKey())) {
        servings = Math.max(servings,
                Math.round(stockIngredients.get(ingredient.getKey()) / ingredient.getValue())
        );
      } else {
        return 0;
      }
    }
    return servings;
  }
}
