package chillingMonsters.Pages;

import chillingMonsters.Pages.loginPage.loginPage;
import chillingMonsters.Pages.registerPage.registerPage;
import chillingMonsters.Pages.searchPage.searchPage;
import chillingMonsters.Pages.stockPage.stockEntryPage;
import chillingMonsters.Pages.stockPage.stockPage;

public abstract class PageFactory {
  private static loginPage login = null;
  private static registerPage register = null;
  private static stockPage stock = null;
  private static searchPage addStockSearch = null;
  private static searchPage addRecipeSearch = null;
  private static searchPage search = null;
  private static stockEntryPage addStockEntry = null;
  private static stockEntryPage stockEntry = null;

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

  public static searchPage getAddStockSearchPage() {
    if (addStockSearch == null) addStockSearch = new searchPage(PageOption.ADD_STOCK);
    return addStockSearch;
  }

  public static searchPage getAddRecipeSearchPage() {
    if (addRecipeSearch == null) addRecipeSearch = new searchPage(PageOption.ADD_RECIPE);
    return addRecipeSearch;
  }

  public static searchPage getAllSearchPage() {
    if (search == null) search = new searchPage(PageOption.DEFAULT);
    return search;
  }

  public static stockEntryPage getAddStockEntryPage(long foodID) {
    if (addStockEntry == null) addStockEntry = new stockEntryPage(foodID, PageOption.ADD_STOCK);
    return addStockEntry;
  }

  public static stockEntryPage getStockEntryPage(long foodID) {
    if (stockEntry == null) stockEntry = new stockEntryPage(foodID, PageOption.DEFAULT);
    return stockEntry;
  }
}
