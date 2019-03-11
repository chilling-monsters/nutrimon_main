package chillingMonsters;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MySQLCon {

  private static final String host = "jdbc:mysql://localhost:3306/";
  private static final String db_name = "chillingM";
  private static final String driver = "com.mysql.cj.jdbc.Driver";
  private static final String userName = "root";
  private static final String password = "root";
  private static Connection con;

  private static void createConnection() {
    try {
      Class.forName(driver);
      con = DriverManager.getConnection(String.format("%s%s?useSSL=false", host, db_name), userName, password);
      System.out.println("Database Connection Success");
    } catch (Exception e) {
      Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, null, e);
    }
  }

  public static List<Map<String, Object>> resultsList(ResultSet rs) {
    ArrayList<Map<String, Object>> list = new ArrayList<>();
    try {
      ResultSetMetaData md = rs.getMetaData();
      int columns = md.getColumnCount();
      while (rs.next()) {
        HashMap<String, Object> row = new HashMap<>(columns);
        for (int i = 1; i <= columns; i++) {
          row.put(md.getColumnName(i), rs.getObject(i));
        }
        list.add(row);
      }
    } catch (SQLException e) {
      Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, null, e);
    }
    return list;
  }

  public static Connection getConnection() {
    try {
      if (con == null || con.isClosed()) {
        createConnection();
      }
    } catch (SQLException e) {
      Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, null, e);
    }
    return con;
  }
}
