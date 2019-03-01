package chillingMonsters;
import java.sql.*;

// sample class that connects to your local database
class sampleMySQLCon{

    // connection main
    public static void main(String args[]){

        // MySQL database
        ResultSet rs  = null; // result
        Statement stmt = null; // the MySQL statement
        Connection con_mysql = null; // connection

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

            // close
            //con_mysql.close();

        } catch(Exception e){

            System.out.println(e);

        } finally{
            try {
                con_mysql.close();
            } catch (SQLException e){}
        }

    }
}
