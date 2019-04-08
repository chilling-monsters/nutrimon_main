package chillingMonsters.Controllers.Stock;

import java.util.List;
import java.util.Map;

public interface StockController {
  List<Map<String, Object>> show();

  List<Map<String, Object>> showStockIngredient(long foodID);

  void createStock(long foodID, double quantity, String expDate);

  void deleteStock(long stockID);

  void updateStock(long stockID, double quantity, String expDate);
}
