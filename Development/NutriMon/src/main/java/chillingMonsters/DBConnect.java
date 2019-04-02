package chillingMonsters;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DBConnect {

    private DBConnect() {}

    private static final String HOST = ConfigurationProperties.getInstance().getProperty("dbhost");

    private static final String DB_NAME = ConfigurationProperties.getInstance().getProperty("dbname");

    private static final String USERNAME =
            ConfigurationProperties.getInstance().getProperty("dbusername");

    private static final String PASSWORD =
            ConfigurationProperties.getInstance().getProperty("dbpassword");

    private static final String DRIVER =
            ConfigurationProperties.getInstance().getProperty("dbdriver");

    private static final String TEST_HOST = ConfigurationProperties.getInstance().getProperty("testdbhost");

    private static final String TEST_DB_NAME = ConfigurationProperties.getInstance().getProperty("testdbname");

    private static final String TEST_USERNAME = ConfigurationProperties.getInstance().getProperty("testdbusername");

    private static final String TEST_PASSWORD = ConfigurationProperties.getInstance().getProperty("testdbpassword");

    private static final String TEST_DRIVER = ConfigurationProperties.getInstance().getProperty("testdbdriver");

    private static boolean test = false;


    private static Connection con;

    private static void createConnection() {
        String driver = test ? TEST_DRIVER : DRIVER;
        String host = test ? TEST_HOST : HOST;
        String username = test ? TEST_USERNAME : USERNAME;
        String password = test ? TEST_PASSWORD : PASSWORD;
        String dbname = test ? TEST_DB_NAME : DB_NAME;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(String.format("%s%s?allowPublicKeyRetrieval=true&useSSL=false", host, dbname), username, password);

            System.out.println("Database Connection Established...");

        } catch (Exception e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, e);
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
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                createConnection();
            }
        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, e);
        }
        return con;
    }

    public static void close() {
        try {
          if (con != null && !con.isClosed()) {
            con.close();
          }
          System.out.println("Database Connection Closed");
        } catch (SQLException e) {
          Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, "Connection failed to close", e);
        }
    }

    public static void toggleTest() {
        close();
        test = !test;
    }
}
