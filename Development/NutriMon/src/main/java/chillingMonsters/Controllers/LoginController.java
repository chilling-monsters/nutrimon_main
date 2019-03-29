package chillingMonsters.Controllers;

import chillingMonsters.MySQLCon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController extends NutriMonController {
    LoginController() {
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
        String queryString = "Select userEmail, `password` " +
                "FROM userProfile " +
                "WHERE userEmail = ? " +
                "AND `password` = ? ";
        try {
            ResultSet rs;   // Result set

            try (PreparedStatement stmt = MySQLCon.getConnection()
                    .prepareStatement(queryString)) {
                stmt.setString(1, userEmail);
                stmt.setString(2, password);
                rs = stmt.executeQuery();
            }

            List<Map<String, Object>> credentials = MySQLCon.resultsList(rs);   // Get result set from DB

            // Copy result set into map
            for (Map<String, Object> users : credentials) {
                userProfile.put((String) users.get("userEmail"), (String) users.get("password"));
            }

            rs.close();         // Close rs
            MySQLCon.close();   // Close DB

        } catch (SQLException e) {
            Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        if (userProfile.size() == 0) {
            return false;
        }

        return true;
    }
}

