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

public class UserProfileController extends NutriMonController implements UserProfileDao {
    UserProfileController() {
        super("userProfile", "userID");
    }

    /**
     * A function checks the email and password combination, if exists, retrieve the userID
     *
     * @param userEmail input user email
     * @param password  input password
     * @return true if email and password pair is found
     */
    public boolean verifyCredentials(String userEmail, String password) {
        Map<Long, String> userProfile = new HashMap<>();
        boolean check = false;

        String queryString = "SELECT userID " +
                "FROM userProfile " +
                "WHERE userEmail = ? " +
                "AND `password` = ? ";
        try {
            ResultSet rs;   // Result set

            PreparedStatement stmt = DBConnect.getConnection().prepareStatement(queryString);
            stmt.setString(1, userEmail);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {    // If exists
                setUserId(rs.getLong(1));   // Retrieve userID
                check = true;
            }

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

