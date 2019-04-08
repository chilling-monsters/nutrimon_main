package chillingMonsters.Controllers.UserProfile;

public interface UserProfileController {
  boolean verifyCredentials(String email, String password);

  void createProfile(String userName, String userEmail, String password);

  void updateProfile();

  boolean exists(String table, String attr, String record);
}
