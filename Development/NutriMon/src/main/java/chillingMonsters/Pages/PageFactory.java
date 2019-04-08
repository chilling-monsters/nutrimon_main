package chillingMonsters.Pages;

import chillingMonsters.Pages.ingredientPage.ingredientPage;
import chillingMonsters.Pages.loginPage.loginPage;
import chillingMonsters.Pages.recipePage.recipeCreatePage;
import chillingMonsters.Pages.recipePage.recipeEntryPage;
import chillingMonsters.Pages.recipePage.recipePage;
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

  private static recipePage recipe = null;
  private static recipeEntryPage recipeEntry = null;
  private static recipeCreatePage recipeCreate = null;

  public static Page getLastPage() {
    if (pageHistory.isEmpty()) {
      return stock;
    } else {
      pageHistory.remove(0);
      return pageHistory.get(0);
    }
  }

  private static void addToHistory(Page newPage) {
    for (Page p : pageHistory) {
      if (p == newPage) {
        pageHistory.remove(p);
        pageHistory.add(0, newPage);
        return;
      }
    }

    pageHistory.add(0, newPage);
  }

  public static Page getLandingPage() {
    //TODO: Landing page here
    return getRecipePage();
  }

  public static loginPage getLoginPage() {
    if (login == null) login = new loginPage();

    addToHistory(login);
    return login;
  }

  public static registerPage getRegisterPage() {
    if (register == null) register = new registerPage();

    addToHistory(register);
    return register;
  }

  public static stockPage getStockPage() {
    if (stock == null) stock = new stockPage();

    addToHistory(stock);
    return stock;
  }

  public static searchPage getSearchPage(PageOption option) {
    if (search == null || search.option != option) {
      search = new searchPage(option);
    }

    addToHistory(search);
    return search;
  }

  public static searchPage getSearchPage() {
    return getSearchPage(PageOption.DEFAULT);
  }

  public static stockEntryPage getStockEntryPage(long foodID, PageOption option) {
    if (stockEntry == null || stockEntry.foodID != foodID || stockEntry.option != option) {
      stockEntry = new stockEntryPage(foodID, option);
    }

    addToHistory(stockEntry);
    return stockEntry;
  }

  public static stockEntryPage getStockEntryPage(long foodID) {
    return getStockEntryPage(foodID, PageOption.DEFAULT);
  }

  public static ingredientPage getIngredientPage(long foodID) {
    if (ingredient == null || ingredient.foodID != foodID) {
      ingredient = new ingredientPage(foodID);
    }

    addToHistory(ingredient);
    return ingredient;
  }

  public static recipePage getRecipePage() {
    if (recipe == null) recipe = new recipePage();

    pageHistory.add(0, recipe);
    return recipe;
  }

  public static recipeEntryPage getRecipeEntryPage(long recipeID) {
    if (recipeEntry == null || recipeEntry.recipeID != recipeID) {
      recipeEntry = new recipeEntryPage(recipeID);
    }

    pageHistory.add(0, recipeEntry);
    return recipeEntry;
  }

  public static recipeCreatePage getRecipeCreatePage() {
    if (recipeCreate == null) recipeCreate = new recipeCreatePage();

    addToHistory(recipeCreate);
    return recipeCreate;
  }
}
