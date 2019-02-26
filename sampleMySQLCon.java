import java.sql.*;

// sample class that connects to your local database
class sampleMySQLCon{

	// connection main
	public static void main(String args[]){
		try{
			System.out.println("############...LOADING...###########");
			// load mysql driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// ("jdbc:mysql://localhost:3306/<database name> ","<user name>","<password>")
			Connection con_mysql = DriverManager.getConnection("jdbc:mysql://localhost:3306/chillingM ","root","11111111");
			
			// a new MySQL statement
			Statement stmt = con_mysql.createStatement();

			// statement execution e.g. query all available user profiles
			ResultSet rs = stmt.executeQuery("select foodID, foodName from ingredients limit 10");

			// looping through the output table, print row by row
			while(rs.next())
				System.out.println(rs.getString(1) + " \t" + rs.getString(2));
			// close
			con_mysql.close();

		} catch(Exception e){ System.out.println(e);}

	}
}