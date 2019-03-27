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

  private static final String HOST = ConfigurationProperties.getInstance().getProperty("dbhost");

  private static final String DB_NAME = ConfigurationProperties.getInstance().getProperty("dbname");

  private static final String USERNAME =
          ConfigurationProperties.getInstance().getProperty("dbusername");

  private static final String PASSWORD =
          ConfigurationProperties.getInstance().getProperty("dbpasword");

  private static final String DRIVER =
          ConfigurationProperties.getInstance().getProperty("dbdriver");

  private static boolean test;


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

  private static void createTestConnection() {
    try {
      Class.forName("test" + DRIVER);
      con = DriverManager.getConnection(String.format("%s%s?useSSL=false",
              "test" + HOST,
              "test" + DB_NAME),
              "test" + USERNAME,
              "test" + PASSWORD);
      System.out.println("Test Database Connection Success");
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
        if (test) {
          createTestConnection();
        } else {
          createConnection();
        }
      }
    } catch (SQLException e) {
      Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, null, e);
    }
    return con;
  }

  public static void close() throws SQLException {
    if (con != null && !con.isClosed()) {
      con.close();
    }
  }

  public static void toggleTest() throws SQLException {
    close();
    test = !test;
  }
}
