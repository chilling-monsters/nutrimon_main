package chillingMonsters.Pages.ingredientPage;

import chillingMonsters.Pages.Page;

public class ingredientPage  extends Page {
	public long foodID;
	public ingredientPage(long foodID) {
		super("ingredientPage/ingredient.fxml", "Your Ingredient", "Current page: Ingredient Page", new ingredientPageController(foodID));
		this.foodID = foodID;
	}
}
