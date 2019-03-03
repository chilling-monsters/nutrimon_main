package chillingMonsters;

import java.sql.*;

// Class that connects to local database
public class dbQuery {

    // MySQL database instances
    private ResultSet rs  = null; // result
    private Statement stmt = null; // the MySQL statement
    private Connection con_mysql = null; // connection


    // Connection
    private void connect(){
        // login
        String db_name, db_usr_name, db_password;

        db_name = "jdbc:mysql://localhost:3306/chillingM";
        db_usr_name = "root";
        db_password = "11111111";

        try{
            System.out.println("############...LOADING...###########");

            // load mysql driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ("jdbc:mysql://localhost:3306/<database name> ","<user name>","<password>")
            con_mysql = DriverManager.getConnection(db_name, db_usr_name, db_password);

            // a new MySQL statement
            stmt = con_mysql.createStatement();

            // statement execution e.g. query all available user profiles
            rs = stmt.executeQuery("select foodID, foodName, fExp from ingredients limit 3");

            // looping through the output table, print row by row
            while(rs.next())
                System.out.println(rs.getString(1) + " \t" + rs.getString(2) + "\t" + rs.getString(3));


        } catch(Exception e){ System.out.println(e);
        } finally{
            close();
        }
    }

    // Close the connection
    private void close() {
        try {
            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (con_mysql != null) {
                con_mysql.close();
            }
        } catch (Exception e) {
            System.out.println("ERROR: Cannot close database properly");
        }
    }

    public void queryTest()
    {
        // not connect error handler
        if(con_mysql != null){
            System.out.println("ERROR: Database not connected");
        }


    }
}
