package chillingMonsters.Controllers.Stock;

import java.util.List;
import java.util.Map;

/**
 * Methods and API offered by a Stock Controller.
 */
public interface StockController {
  /**
   * Shows the stocks that the user has on hand.
   * @return list of user's stocks.
   */
  List<Map<String, Object>> showStocks();

  /**
   * Shows individual entries of like stock owned by a user.
   * @param foodID the ingredient of the stock items the user is looking for.
   * @return the list of stock entries
   */
  List<Map<String, Object>> showStockEntry(long foodID);

  /**
   * Adds a new stock item to the user's inventory.
   * @param foodID the ingredient that the user is adding to his stock.
   * @param quantity How much of the ingredient the user is adding.
   * @param expDate The expiration date of the ingredient.
   */
  void createStock(long foodID, double quantity, String expDate);

  /**
   * Removes an item from the user's stock.
   * @param stockID the id of the stock item.
   */
  void deleteStock(long stockID);

  /**
   * Updates a stock item.
   * @param stockID the id of the stock item to update.
   * @param quantity the new quantity of the stock item.
   * @param expDate the new expiration date of the stock item.
   */
  void updateStock(long stockID, double quantity, String expDate);

  /**
   * Returns the total grams of a certain food item that the user has in stock.
   * @param foodID the targeted food item's identifier
   * @return the quantity in grams.
   */
  float getStockQuantity(long foodID);
}
