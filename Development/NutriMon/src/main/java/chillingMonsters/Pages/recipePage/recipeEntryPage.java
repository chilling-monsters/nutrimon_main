package chillingMonsters.Pages.recipePage;

import chillingMonsters.Pages.Page;
import chillingMonsters.Pages.PageImpl;

public class recipeEntryPage extends PageImpl implements Page {
	public long recipeID;

	public recipeEntryPage(long recipeID) {
		super("recipePage/recipeEntry.fxml", "Your Recipes", "Current page: Recipe Entry Page", new recipeEntryPageController(recipeID));
		this.recipeID = recipeID;
	}
}
