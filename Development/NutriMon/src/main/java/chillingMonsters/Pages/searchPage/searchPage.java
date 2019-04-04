package chillingMonsters.Pages.searchPage;

import chillingMonsters.Pages.Page;

public class searchPage extends Page {
  public searchPage(SearchPageType t) {
    super("searchPage/search.fxml", "Search", "Current page: Search Page", new searchPageController(t));
  }
}
