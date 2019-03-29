package chillingMonsters.Controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import chillingMonsters.MySQLCon;

public abstract class NutriMonController implements Controller {
  static long userId;
  private final String table;
  private final String pk;

  NutriMonController(String table, String pk) {
    this.table = table;
    this.pk = pk;
  }

  public static void setUserId(long id) {
    userId = id;
  }

  public List<Map<String, Object>> show() {
    List<Map<String,Object>> output = new ArrayList<>();
    try {
      ResultSet rs;
      try (PreparedStatement stmt = MySQLCon.getConnection()
              .prepareStatement(String.format("SELECT * FROM %s WHERE userID = ?", table))) {
        stmt.setLong(1, userId);
        rs = stmt.executeQuery();
      }
      output = MySQLCon.resultsList(rs);
      rs.close();
      MySQLCon.close();
    } catch (SQLException e) {
      Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, "Failed select", e);
    }
    return output;
  }

  public Map<String, Object> get(long id) {
    try {
      ResultSet rs;
      try (PreparedStatement stmt = MySQLCon.getConnection()
              .prepareStatement(String.format("SELECT * FROM %s WHERE userID = ? AND %s = ?",
                      table, pk))) {
        stmt.setLong(1, userId);
        stmt.setLong(2, id);
        rs = stmt.executeQuery();
      }
      List<Map<String, Object>> output = MySQLCon.resultsList(rs);
      if (!output.isEmpty()) {
        return output.get(0);
      }
      rs.close();
      MySQLCon.close();
    } catch (SQLException e) {
      Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, "Failed select", e);
    }
    return null;
  }

  public void delete(long id) {
    try (PreparedStatement stmt = MySQLCon.getConnection()
            .prepareStatement(String.format("DELETE FROM %s WHERE userID = ? AND %s = ?",
                    table, pk))) {
      stmt.setLong(1, userId);
      stmt.setLong(2, id);
      stmt.executeUpdate();
      MySQLCon.close();
    } catch (SQLException e) {
      Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, "Failed delete", e);
    }
  }

  public void update(long id, Map<String, Object> values) {
    StringBuilder query = new StringBuilder(String.format("UPDATE %s ", table));
    int i = 0;
    int size = values.size();
    for (Map.Entry<String, Object> entry : values.entrySet()) {
      query.append(String.format("SET %s = %s", entry.getKey(), String.valueOf(entry.getValue())));
      if (i != size - 1) {
        query.append(", ");
      }
      i++;
    }
    query.append(String.format(" WHERE %s = %d", pk, id));
    try {
      try (Statement stmt = MySQLCon.getConnection().createStatement()) {
        stmt.executeUpdate(query.toString());
      }
      MySQLCon.close();
    } catch (SQLException e) {
      Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, "Failed update", e);
    }
  }

  public void create(Map<String, Object> values) {
    StringBuilder fields = new StringBuilder("(");
    StringBuilder vals = new StringBuilder("(");
    for (Map.Entry<String, Object> entry : values.entrySet()) {
      fields.append(entry.getKey());
      vals.append(String.valueOf(entry.getValue()));
      fields.append(",");
      vals.append(",");
    }
    fields.append("userID)");
    vals.append(String.format("%d)", userId));
    String query = String.format("INSERT INTO %s %s VALUES %s",
            table, fields.toString(), vals.toString());

    try {
      try (Statement stmt = MySQLCon.getConnection().createStatement()) {
        stmt.executeUpdate(query);
      }
      MySQLCon.close();
    } catch (SQLException e) {
      Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, "Failed insert", e);
    }
  }
}
