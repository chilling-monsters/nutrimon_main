package chillingMonsters.Pages.userProfilePage;

import chillingMonsters.Pages.Page;
import chillingMonsters.Pages.PageImpl;

public class userProfilePage extends PageImpl implements Page {
    public userProfilePage() {
        super("userProfilePage/userProfile.fxml", "Your Profile", "Current page: User Profile Page");
    }
}
