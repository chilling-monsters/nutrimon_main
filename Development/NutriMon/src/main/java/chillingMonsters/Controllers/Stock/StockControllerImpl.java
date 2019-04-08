package chillingMonsters.Controllers.Stock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import chillingMonsters.Controllers.NutriMonController;
import chillingMonsters.Controllers.Stock.StockController;
import chillingMonsters.DBConnect;

public class StockControllerImpl extends NutriMonController implements StockController {
  public StockControllerImpl() {
    super("stockItems", "stockItemID");
  }


  public float getStockQuantity(long foodID) {
    float quantity = 0;
    String query = "SELECT sum(foodQtty) as 'quantity' " +
            "FROM stockitems JOIN ingredients USING (foodID) " +
            "WHERE userID = ? AND foodID = ? " +
            "GROUP BY foodID";
    try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
      stmt.setLong(1, userId);
      stmt.setLong(2, foodID);
      ResultSet rs = stmt.executeQuery();
      if (rs.first()) {
        quantity = rs.getFloat(1);
      }
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
    return quantity;
  }

  public List<Map<String, Object>> showStocks() {
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
      }
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
    return stocks;
  }

  public List<Map<String, Object>> showStockEntry(long foodId) {
    List<Map<String, Object>> stocks = new ArrayList<>();
    String query = "SELECT stockItemID, foodID, foodName, fCategory, expTime, foodExpDate," +
            "foodQtty as 'quantity', " +
            "date_add(now(), INTERVAL - expTime + datediff(foodExpDate, now()) DAY) as 'added_date', " +
            "datediff(foodExpDate, now()) as 'time_left' " +
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

  public void createStock(long foodID, double quantity, String expDate) {
    Map<String, Object> payload = new HashMap<>();
    payload.put("foodID", foodID);
    payload.put("foodQtty", quantity);
    payload.put("foodExpDate", String.format("\'%s\'", expDate));
    this.create(payload);
  }

  public void updateStock(long stockID, double quantity, String expDate) {
    Map<String, Object> payload = new HashMap<>();
    payload.put("foodQtty", quantity);
    payload.put("foodExpDate", String.format("\'%s\'", expDate));
    this.update(stockID, payload);
  }
}
