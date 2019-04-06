package chillingMonsters.Pages.searchPage;

import chillingMonsters.Pages.Page;
import chillingMonsters.Pages.PageOption;

public class searchPage extends Page {
  public PageOption option;

  public searchPage(PageOption option) {
    super("searchPage/search.fxml", "Search", "Current page: Search Page", new searchPageController(option));

    this.option = option;
  }
}
