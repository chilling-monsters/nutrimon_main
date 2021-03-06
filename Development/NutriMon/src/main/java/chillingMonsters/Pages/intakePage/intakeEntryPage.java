package chillingMonsters.Pages.intakePage;

import chillingMonsters.Pages.Page;
import chillingMonsters.Pages.PageImpl;
import chillingMonsters.Pages.PageOption;

public class intakeEntryPage extends PageImpl implements Page {
	public long intakeID;
	public PageOption option;

	public intakeEntryPage(long intakeID, PageOption option) {
		super("intakePage/intakeEntry.fxml", "Your Intakes", "Current page: Intake Page", new intakeEntryPageController(intakeID, option));

		this.intakeID = intakeID;
		this.option = option;
	}
}
