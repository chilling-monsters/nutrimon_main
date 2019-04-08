package chillingMonsters.Pages.recipePage;

import chillingMonsters.Pages.Page;
import chillingMonsters.Pages.PageImpl;

public class recipeCreatePage extends PageImpl implements Page {
	public recipeCreatePage() {
		super("recipePage/recipeCreate.fxml", "Your Recipes", "Current page: Recipe Create Page", new recipeCreatePageController());
	}

	public void addToIngredientList(long foodID) {
		recipeCreatePageController recp = (recipeCreatePageController) this.controller;
		recp.addToIngredientList(foodID);
	}
}
