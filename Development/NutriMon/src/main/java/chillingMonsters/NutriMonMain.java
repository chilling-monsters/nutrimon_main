package chillingMonsters;

import java.util.List;
import java.util.Map;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.IngredientController;
import chillingMonsters.Controllers.NutriMonController;
import chillingMonsters.Controllers.StockController;
import chillingMonsters.Pages.loginPage;
import static javafx.application.Application.*;

public class NutriMonMain {

    public static void main(String[] args) {
      launch(loginPage.class, args);
    }
}
