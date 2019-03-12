package chillingMonsters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class NutriMonController implements Controller {
  protected static int userId;
  protected final String table;
  protected final String pk;

  protected NutriMonController(String table, String pk) {
    this.table = table;
    this.pk = pk;
  }

  public static void setUserId(int id) {
    userId = id;
  }

  public List<Map<String, Object>> show() {
    List<Map<String,Object>> output = new ArrayList<>();
    try {
      ResultSet rs;
      try (PreparedStatement stmt = MySQLCon.getConnection()
              .prepareStatement(String.format("SELECT * FROM %s WHERE userID = ?", table))) {
        stmt.setInt(1, userId);
        rs = stmt.executeQuery();
      }
      rs.close();
      output = MySQLCon.resultsList(rs);
    } catch (SQLException e) {
      Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, "Failed select", e);
    }
    return output;
  }

  public Map<String, Object> get(int id) {
    try {
      ResultSet rs;
      try (PreparedStatement stmt = MySQLCon.getConnection()
              .prepareStatement(String.format("SELECT * FROM %s WHERE userID = ? AND %s = ?",
                      table, pk))) {
        stmt.setInt(1, userId);
        stmt.setInt(2, id);
        rs = stmt.executeQuery();
      }
      rs.close();
      List<Map<String, Object>> output = MySQLCon.resultsList(rs);
      if (!output.isEmpty()) {
        return output.get(0);
      }
    } catch (SQLException e) {
      Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, "Failed select", e);
    }
    return null;
  }

  public void delete(int id) {

  }

  public void update(int id, Map<String, Object> values) {

  }

  public void create(Map<String, Object> values) {

  }

}
