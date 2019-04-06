package chillingMonsters.Pages;

import chillingMonsters.Pages.ingredientPage.ingredientPage;
import chillingMonsters.Pages.loginPage.loginPage;
import chillingMonsters.Pages.registerPage.registerPage;
import chillingMonsters.Pages.searchPage.searchPage;
import chillingMonsters.Pages.stockPage.stockEntryPage;
import chillingMonsters.Pages.stockPage.stockPage;

public abstract class PageFactory {
  private static loginPage login = null;
  private static registerPage register = null;
  private static stockPage stock = null;
  private static stockEntryPage stockEntry = null;
  private static searchPage search = null;
  private static ingredientPage ingredient = null;

  public static loginPage getLoginPage() {
    if (login == null) login = new loginPage();
    return login;
  }

  public static registerPage getRegisterPage() {
    if (register == null) register = new registerPage();
    return register;
  }

  public static stockPage getStockPage() {
    if (stock == null) stock = new stockPage();
    return stock;
  }

  public static searchPage getSearchPage(PageOption option) {
    if (search == null || search.option != option) {
      search = new searchPage(option);
    }

    return search;
  }

  public static searchPage getSearchPage() {
    return getSearchPage(PageOption.DEFAULT);
  }

  public static stockEntryPage getStockEntryPage(long foodID, PageOption option) {
    if (stockEntry == null || stockEntry.foodID != foodID || stockEntry.option != option) {
      stockEntry = new stockEntryPage(foodID, option);
    }

    return stockEntry;
  }

  public static stockEntryPage getStockEntryPage(long foodID) {
    return getStockEntryPage(foodID, PageOption.DEFAULT);
  }

  public static ingredientPage getIngredientPage(long foodID) {
    if (ingredient == null || ingredient.foodID != foodID) {
      ingredient = new ingredientPage(foodID);
    }

    return ingredient;
  }
}
