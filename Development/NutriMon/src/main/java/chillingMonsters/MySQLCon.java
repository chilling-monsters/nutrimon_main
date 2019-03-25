package chillingMonsters;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MySQLCon {

  private MySQLCon() {}

  private static final String HOST;
  private static final String DB_NAME;
  private static final String DRIVER;
  private static final String USERNAME;
  private static final String PASSWORD;

  static {
    HOST = "jdbc:mysql://localhost:3306/";
    DB_NAME = "chillingM";
    DRIVER = "com.mysql.cj.jdbc.Driver";
    USERNAME = "root";
    PASSWORD = "root";
  }

  private static  Connection con;

  private static void createConnection() {
    try {
      Class.forName(DRIVER);
      con = DriverManager.getConnection(String.format("%s%s?useSSL=false", HOST, DB_NAME), USERNAME, PASSWORD);
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

  public static void close() throws SQLException {
    con.close();
  }
}
