package chillingMonsters.Pages.stockPage;

import chillingMonsters.Pages.Page;
import chillingMonsters.Pages.PageOption;

public class stockEntryPage extends Page {
	public stockEntryPage(long foodID, PageOption option) {
		super("stockPage/stockEntry.fxml", "Your Stock", "Current page: Stock Entry Page", new stockEntryPageController(foodID, option));
	}
}
