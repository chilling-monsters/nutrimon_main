package chillingMonsters.Controllers.UserProfile;

import java.util.Map;

public interface UserProfileController {
  boolean verifyCredentials(String email, String password);

  void createProfile(String userName, String userEmail, String password);

  void updateProfile(String userName, String userEmail, String password, String gender);

  void deleteProfile();

  boolean exists(String email);

  long getUserID();

  Map<String, Object> getProfile();

  void logout();
}
