package chillingMonsters.Controllers;

public interface UserProfileDao {
  public boolean verifyCredentials(String email, String password);

  public void createProfile(String userName, String userEmail, String password);

  public void updateProfile();
}
