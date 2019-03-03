package NMDatabase;

import java.sql.*;

// Class that connects to local database
public class dbInit {

    // MySQL database instances
    protected ResultSet rs = null; // result
    protected Statement stmt = null; // the MySQL statement
    protected PreparedStatement prestmt = null; // prepared statement
    protected Connection connect = null; // connection


    // Connection
    protected void connect() {
        // login
        String db_name, db_usr_name, db_password;

        db_name = "jdbc:mysql://localhost:3306/chillingM";
        db_usr_name = "root";
        db_password = "11111111";

        try {
            // load mysql driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ("jdbc:mysql://localhost:3306/<database name> ","<user name>","<password>")
            connect = DriverManager.getConnection(db_name, db_usr_name, db_password);

        } catch (Exception e) {
            // Error msg
            System.out.println("ERROR: DB Connection Failed -- " + e);
        }
        //System.out.println("############...CONNECTED...###########");
    }

    // Close the connection
    protected void close() {
        try {
            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
            // Error msg
            System.out.println("ERROR: DB Disconnection Failed -- " + e);
        }
        //System.out.println("############....CLOSED.....###########");
    }
}


