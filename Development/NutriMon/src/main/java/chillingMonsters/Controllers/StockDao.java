package chillingMonsters.Controllers;

import java.util.List;
import java.util.Map;

public interface StockDao {

  List<Map<String, Object>> show();

  List<Map<String, Object>> showStockIngredient(long foodID);

  void createStock(long foodID, double quantity);

  void deleteStock(long stockID);

  void updateStock(long stockID, double quantity, String expDate);
}
