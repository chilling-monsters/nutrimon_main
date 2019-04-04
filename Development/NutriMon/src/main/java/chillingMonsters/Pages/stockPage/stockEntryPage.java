package chillingMonsters.Pages.stockPage;

import chillingMonsters.Pages.Page;

public class stockEntryPage extends Page {
	public stockEntryPage(long foodID) {
		super("stockPage/stockEntry.fxml", "Your Stock", "Current page: Stock Entry Page", new stockEntryPageController(foodID));
	}
}
