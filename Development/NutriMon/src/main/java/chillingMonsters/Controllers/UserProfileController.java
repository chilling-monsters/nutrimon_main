package chillingMonsters.Controllers;

import chillingMonsters.DBConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserProfileController extends NutriMonController {
    UserProfileController() {
        super("userProfile", "userID");
    }

    /**
     * A function checks the email and password combination
     *
     * @param userEmail input user email
     * @param password  input password
     * @return true if email and password pair is found
     */
    public boolean checkCredentials(String userEmail, String password) {
        Map<String, String> userProfile = new HashMap<>();
        String queryString = "SELECT userEmail, `password` " +
                "FROM userProfile " +
                "WHERE userEmail = ? " +
                "AND `password` = ? ";
        try {
            ResultSet rs;   // Result set

            PreparedStatement stmt = DBConnect.getConnection().prepareStatement(queryString);
            stmt.setString(1, userEmail);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            List<Map<String, Object>> credentials = DBConnect.resultsList(rs);   // Get result set from DB

            // Copy result set into map
            for (Map<String, Object> users : credentials) {
                userProfile.put((String) users.get("userEmail"), (String) users.get("password"));
            }

            rs.close();         // Close rs
            stmt.close();       // Close stmt
            DBConnect.close();   // Close DB

        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return userProfile.size() == 1;
    }


    public boolean exists(String table, String attr, String record) {
        boolean check = false;

        String queryString = "SELECT " + attr + " FROM " + table +
                " WHERE " + attr + " = ? ";
        try {
            ResultSet rs;   // Result set

            PreparedStatement stmt = DBConnect.getConnection().prepareStatement(queryString);
            stmt.setString(1, record);

            rs = stmt.executeQuery();   // Statement execution

            check = rs.next();

            rs.close();         // Close rs
            stmt.close();       // Close stmt
            DBConnect.close();   // Close DB

        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return check;
    }


    public void createProfile(String userName, String userEmail, String password) {
        String updateString = "INSERT INTO userProfile (userName, userEmail, `password`) " +
                    "VALUES (?, ?, ?)";
        try {

            PreparedStatement stmt = DBConnect.getConnection().prepareStatement(updateString);
            stmt.setString(1, userName);
            stmt.setString(2, userEmail);
            stmt.setString(3, password);
            stmt.execute();

            stmt.close();         // Close stmt
            DBConnect.close();   // Close DB

        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }


    public void updateProfile() {
        /* TODO: Implement */
    }
}

