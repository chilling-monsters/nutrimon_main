package chillingMonsters.Pages.searchPage;

import chillingMonsters.Pages.Page;
import chillingMonsters.Pages.PageOption;

public class searchPage extends Page {
  public searchPage(PageOption t) {
    super("searchPage/search.fxml", "Search", "Current page: Search Page", new searchPageController(t));
  }
}
