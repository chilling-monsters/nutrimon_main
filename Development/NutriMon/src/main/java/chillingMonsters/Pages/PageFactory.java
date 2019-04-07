package chillingMonsters.Pages;

import chillingMonsters.Pages.ingredientPage.ingredientPage;
import chillingMonsters.Pages.loginPage.loginPage;
import chillingMonsters.Pages.registerPage.registerPage;
import chillingMonsters.Pages.searchPage.searchPage;
import chillingMonsters.Pages.stockPage.stockEntryPage;
import chillingMonsters.Pages.stockPage.stockPage;

import java.util.ArrayList;
import java.util.List;

public abstract class PageFactory {
  private static List<Page> pageHistory = new ArrayList<>();

  private static loginPage login = null;
  private static registerPage register = null;
  private static stockPage stock = null;
  private static stockEntryPage stockEntry = null;
  private static searchPage search = null;
  private static ingredientPage ingredient = null;

  public static Page getLastPage() {
    if (pageHistory.isEmpty()) {
      return stock;
    } else {
      pageHistory.remove(0);
      return pageHistory.get(0);
    }
  }

  public static loginPage getLoginPage() {
    if (login == null) login = new loginPage();

    pageHistory.add(0, login);
    return login;
  }

  public static registerPage getRegisterPage() {
    if (register == null) register = new registerPage();

    pageHistory.add(0, register);
    return register;
  }

  public static stockPage getStockPage() {
    if (stock == null) stock = new stockPage();

    pageHistory.add(0, stock);
    return stock;
  }

  public static searchPage getSearchPage(PageOption option) {
    if (search == null || search.option != option) {
      search = new searchPage(option);
    }

    pageHistory.add(0, search);
    return search;
  }

  public static searchPage getSearchPage() {
    return getSearchPage(PageOption.DEFAULT);
  }

  public static stockEntryPage getStockEntryPage(long foodID, PageOption option) {
    if (stockEntry == null || stockEntry.foodID != foodID || stockEntry.option != option) {
      stockEntry = new stockEntryPage(foodID, option);
    }

    pageHistory.add(0, stockEntry);
    return stockEntry;
  }

  public static stockEntryPage getStockEntryPage(long foodID) {
    return getStockEntryPage(foodID, PageOption.DEFAULT);
  }

  public static ingredientPage getIngredientPage(long foodID) {
    if (ingredient == null || ingredient.foodID != foodID) {
      ingredient = new ingredientPage(foodID);
    }

    pageHistory.add(0, ingredient);
    return ingredient;
  }
}