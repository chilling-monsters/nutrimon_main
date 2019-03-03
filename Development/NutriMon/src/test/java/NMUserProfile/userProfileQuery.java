package NMUserProfile;

import NMDatabase.dbInit;

public class userProfileQuery extends dbInit {

    protected boolean checkCredentials(String userEmail, String password) {
        String usr_password = null;

        System.out.println("INFO: `" + userEmail + "` Is Polling User Credential");

        // connect to database
        if (super.connect == null)
            super.connect();

        try {
            // a new MySQL statement
            super.prestmt = super.connect.prepareStatement("select password from userProfile where userEmail = ?");

            // statement execution e.g. query all available user profiles
            super.prestmt.setString(1, userEmail);

            super.rs = super.prestmt.executeQuery();

            if (super.rs.next()) {
                usr_password = super.rs.getString(1);
            } else
                return false;

        } catch (Exception e) {
            // Error msg
            System.out.println("ERROR: Cannot Check Credentials -- " + e);
        }

        // close connection
        if (super.connect != null)
            super.close();

        // System.out.println(password + "\t" + usr_password);

        return password.equals(usr_password);
    }
}
