package chillingMonsters.Pages.navMenu;

import chillingMonsters.Pages.Page;
import chillingMonsters.Pages.PageImpl;

public class navMenu extends PageImpl implements Page {
	public navMenu() {
		super("navMenu/navMenu.fxml", "Menu", "Current page: Menu Page");
	}

	public void setSelected(int i) {
		navMenuController controller = (navMenuController) this.controller;
		controller.setSelected(i);
	}
}
