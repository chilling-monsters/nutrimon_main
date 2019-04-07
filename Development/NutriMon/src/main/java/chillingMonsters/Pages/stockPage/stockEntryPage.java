package chillingMonsters.Pages.stockPage;

import chillingMonsters.Pages.Page;
import chillingMonsters.Pages.PageImpl;
import chillingMonsters.Pages.PageOption;

public class stockEntryPage extends PageImpl implements Page {
	public long foodID;
	public PageOption option;

	public stockEntryPage(long foodID, PageOption option) {
		super("stockPage/stockEntry.fxml", "Your Stock", "Current page: Stock Entry Page", new stockEntryPageController(foodID, option));
		this.foodID = foodID;
		this.option = option;
	}
}
