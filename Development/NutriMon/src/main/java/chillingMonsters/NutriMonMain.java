package chillingMonsters;

import java.util.List;
import java.util.Map;

import chillingMonsters.Controllers.ControllerFactory;
import chillingMonsters.Controllers.StockController;
import chillingMonsters.Pages.loginPage;
import static javafx.application.Application.*;

public class NutriMonMain {

    public static void main(String[] args) {
      StockController stock = ControllerFactory.makeStockController();

      List<Map<String, Object>> results = stock.showStock();

      Map<String, Object> result = results.get(0);

      for (Map.Entry<String, Object> e : result.entrySet()) {
        System.out.println(String.format("{ %s: %s }", e.getKey(), String.valueOf(e.getValue())));
      }
    }
}
