package chillingMonsters.Controllers.UserProfile;

import chillingMonsters.Controllers.NutriMonController;
import chillingMonsters.DBConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static chillingMonsters.DBConnect.resultsList;

public class UserProfileControllerImpl extends NutriMonController implements UserProfileController {
    public UserProfileControllerImpl() {
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


    public void updateProfile(String userName, String userEmail, String password, String gender) {
        String updateString = "UPDATE userProfile SET userName = ?, userEmail = ?, `password` = ?, gender = ? " +
                    "WHERE userID = ?";

        try {

            PreparedStatement stmt = DBConnect.getConnection().prepareStatement(updateString);
            stmt.setString(1, userName);
            stmt.setString(2, userEmail);
            stmt.setString(3, password);
            if (!gender.equals(""))
                stmt.setString(4, gender);
            else
                stmt.setNull(4, Types.NULL);
            stmt.setLong(5, getUserID());
            stmt.execute();

            stmt.close();         // Close stmt
            DBConnect.close();   // Close DB

        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void deleteProfile() {
        String updateString = "DELETE FROM userProfile WHERE userID = ?";
        try {
            PreparedStatement stmt = DBConnect.getConnection().prepareStatement(updateString);
            stmt.setLong(1, getUserID());
            stmt.execute();

            stmt.close();         // Close stmt
            DBConnect.close();   // Close DB

        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public long getUserID() {
        return getUserId();
    }

    public Map<String, Object> getProfile() {
        String query = "SELECT * FROM userProfile WHERE userID = ?";
        Map<String, Object> profile = null;
        try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
            stmt.setLong(1, getUserID());
            try (ResultSet rs = stmt.executeQuery()) {
                List<Map<String, Object>> profiles = resultsList(rs);
                if (!profiles.isEmpty()) {
                    profile = profiles.get(0);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } finally {
            DBConnect.close();
        }

        for (String k : profile.keySet()) {
            if (profile.get(k) == null) profile.put(k, "");
        }

        return profile;
    }

    public boolean exists(String email) {
        return this.exists("userProfile", "userID", email);
    }
}

