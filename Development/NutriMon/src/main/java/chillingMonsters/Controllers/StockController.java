package chillingMonsters.Controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import chillingMonsters.DBConnect;

public class StockController extends NutriMonController {
    StockController() {
        super("stockitems", "stockItemID");
    }

    public List<Map<String, Object>> showStock() {
        List<Map<String, Object>> stocks = new ArrayList<>();
        String query = "SELECT foodID, foodName, " +
                "sum(foodQtty) as 'quantity', " +
                "min(foodExpDate) as 'next_exp' " +
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

    public List<Map<String, Object>> showStockIngredient(int foodId) {
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
                stmt.setInt(2, foodId);
                ResultSet rs = stmt.executeQuery();
                stocks = DBConnect.resultsList(rs);
                DBConnect.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return stocks;
    }
}
