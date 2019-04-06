package chillingMonsters.Pages.ingredientPage;

import chillingMonsters.Pages.Page;
import chillingMonsters.Pages.PageImpl;

public class ingredientPage extends PageImpl implements Page {
	public long foodID;

	public ingredientPage(long foodID) {
		super("ingredientPage/ingredient.fxml", "Your Ingredients", "Current page: Ingredient Page", new ingredientPageController(foodID));
		this.foodID = foodID;
	}
}
