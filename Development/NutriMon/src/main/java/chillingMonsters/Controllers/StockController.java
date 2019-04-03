package chillingMonsters.Controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import chillingMonsters.DBConnect;

public class StockController extends NutriMonController implements StockDao {
    StockController() {
        super("stockitems", "stockItemID");
    }

    @Override
    public List<Map<String, Object>> show() {
        List<Map<String, Object>> stocks = new ArrayList<>();
        String query = "SELECT foodID, foodName, fCategory, " +
                "sum(foodQtty) as 'quantity', " +
                "datediff(min(foodExpDate), now()) as 'next_exp' " +
                "FROM stockitems JOIN ingredients using(foodID) " +
                "WHERE userID = ? " +
                "GROUP BY foodID " +
                "ORDER BY next_exp ASC";
        try {
            try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
                stmt.setLong(1, userId);
                ResultSet rs = stmt.executeQuery();
                stocks = DBConnect.resultsList(rs);
                DBConnect.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return stocks;
    }

    public List<Map<String, Object>> showStockIngredient(long foodId) {
        List<Map<String, Object>> stocks = new ArrayList<>();
        String query = "SELECT stockItemID, foodID, foodName, " +
                "foodQtty as 'quantity', " +
                "foodExpDate " +
                "FROM stockitems JOIN ingredients using(foodID) " +
                "WHERE userID = ? AND foodID = ? " +
                "ORDER BY foodExpDate ASC";
        try {
            try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
                stmt.setLong(1, userId);
                stmt.setLong(2, foodId);
                ResultSet rs = stmt.executeQuery();
                stocks = DBConnect.resultsList(rs);
                DBConnect.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return stocks;
    }

    public void deleteStock(long stockID) {
      this.delete(stockID);
    }

    public void createStock(long foodID, double quantity) {
      Map<String, Object> payload = new HashMap<>();
      payload.put("foodID", foodID);
      payload.put("foodQtty", quantity);
      this.create(payload);
    }

    public void updateStock(long stockID, double quantity, String expDate) {
      Map<String, Object> payload = new HashMap<>();
      payload.put("foodQtty", quantity);
      payload.put("foodExpDate", Timestamp.valueOf(expDate));
      this.update(stockID, payload);
    }

    public static String parseFoodName(String name) {
        String[] food = name.toLowerCase().split(",");

        for (int i = 0; i < food.length; i++) {
            String s = food[i];
            String[] subStr =  s.split(" ");
            s = "";
            for (int j = 0; j < subStr.length; j++) {
                String sub = subStr[j];
                sub = sub.substring(0, 1).toUpperCase() + sub.substring(1);

                if (j > 0) s += " ";

                s += sub;
            }

            food[i] = s;
        }

        String foodName = "";
        if (food.length > 1) {
            if (food[1].equals("With Salt")) {
                food[1] = "Salted";
            }

            foodName = food[1] + " " + food[0];
        } else {
            foodName = food[0];
        }

        return foodName;
    }
}
