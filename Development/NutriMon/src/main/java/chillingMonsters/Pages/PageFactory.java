package chillingMonsters.Pages;

import chillingMonsters.Pages.loginPage.loginPage;
import chillingMonsters.Pages.registerPage.registerPage;
import chillingMonsters.Pages.searchPage.searchPage;
import chillingMonsters.Pages.stockPage.stockPage;

public abstract class PageFactory {
  private static loginPage login = null;
  private static registerPage register = null;
  private static stockPage stock = null;
  private static searchPage search = null;

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

  public static searchPage getSearchPage() {
    if (search == null) search = new searchPage();
    return search;
  }
}
