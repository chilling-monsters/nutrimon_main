package chillingMonsters;

import NMUserProfile.loginPage;
import static javafx.application.Application.*;
import chillingMonsters.Controllers.*;
import NMDatabase.*;

public class NutriMonMain {

    public static void main(String[] args) {
        // launch(loginPage.class, args);

        LoginController LC = ControllerFactory.makeLoginController();

        if (LC.checkCredentials("test@email.com", "11111111")) {
            System.out.println("Combination found!");
        }
    }
}
