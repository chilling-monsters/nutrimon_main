package chillingMonsters.Pages.recipePage;

import chillingMonsters.Pages.Page;
import chillingMonsters.Pages.PageImpl;
import chillingMonsters.Pages.PageOption;

public class recipeCreatePage extends PageImpl implements Page {
	public long recipeID;
	public PageOption option;

	public recipeCreatePage(long recipeID, PageOption option) {
		super("recipePage/recipeCreate.fxml", "Your Recipes", "Current page: Recipe Create Page", new recipeCreatePageController(recipeID, option));

		this.recipeID = recipeID;
		this.option = option;
	}

	public void addToIngredientList(long foodID) {
		recipeCreatePageController recp = (recipeCreatePageController) this.controller;
		recp.addToIngredientList(foodID, 0);
	}
}
