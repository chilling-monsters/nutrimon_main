package NMUserProfile;

import NMDatabase.dbInit;
import java.util.Random;

public class userProfileQuery extends dbInit {

    protected boolean checkCredentials(String userEmail, String password) {
        String usr_password = null;

        System.out.println("INFO: `" + userEmail + "` is polling user credentials");

        // connect to database
        super.connect();

        try {
            // a new MySQL statement
            super.prestmt = super.connect.prepareStatement("SELECT password FROM userProfile WHERE userEmail = ?");

            // statement execution e.g. query all available user profiles
            super.prestmt.setString(1, userEmail);

            super.rs = super.prestmt.executeQuery();

            if (super.rs.next()) {
                usr_password = super.rs.getString(1);
            } else {
                super.close();
                return false;
            }

        } catch (Exception e) {
            // Error msg
            System.out.println("ERROR: Cannot get credentials -- " + e);
        }

        // close connection
        super.close();

        // System.out.println(password + "\t" + usr_password);

        return password.equals(usr_password);
    }

    protected void insertProfile(String email, String name, String password, String gender) {

        // log
        System.out.println("INFO: `" + email + "` is updating profile");

        // Temporary string, ADMIN is restricted
        String tempStr = "ADMIN";

        // Generate unique userID
        while (strExists("userProfile","userID",tempStr) || tempStr.equals("ADMIN"))
            tempStr = randID(9);

        // System.out.println(strExists("userProfile","userID", "ADMINTEST"));

        // connect to database
        super.connect();

        try {
            // a new MySQL statement
            super.prestmt = super.connect.prepareStatement("INSERT INTO userProfile VALUE (?,?,?,?,?)");

            super.prestmt.setString(1, tempStr);
            super.prestmt.setString(2, name);
            super.prestmt.setString(3, email);
            super.prestmt.setString(4, gender);
            super.prestmt.setString(5, password);

            super.prestmt.executeUpdate();

            // log
            System.out.println(">> INFO: User ID: `" + tempStr + "` is added to database");

        } catch (Exception e) {
            // Error msg
            System.out.println("ERROR: Cannot update -- " + e);
        }

        // close connection
        super.close();
    }

    // Generate random string with given length
    public String randID(int length) {

        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();

        Random rnd = new Random();

        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        return salt.toString();
    }

    // Check exists for a given string
    public boolean strExists(String table_name, String col, String str){
        boolean check = false;

        // connect to database
        super.connect();

        try {
            // log
            System.out.println(">> INFO: Looking for -- " + str);

            // a new MySQL statement
            super.prestmt = super.connect.prepareStatement("SELECT " + col + " FROM " + table_name + " WHERE " + col +  " = ?");

            super.prestmt.setString(1, str);

            super.rs = super.prestmt.executeQuery();

            if (super.rs.next())
                check = true;

        } catch (Exception e) {
            // Error msg
            System.out.println("ERROR: strExists() -- " + e);
        }

        // close connection
        super.close();

        return check;
    }

}
