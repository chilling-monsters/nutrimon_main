package chillingMonsters.Pages;

import chillingMonsters.Pages.ingredientPage.ingredientPage;
import chillingMonsters.Pages.loginPage.loginPage;
import chillingMonsters.Pages.navMenu.navMenu;
import chillingMonsters.Pages.recipePage.recipeCreatePage;
import chillingMonsters.Pages.recipePage.recipeEntryPage;
import chillingMonsters.Pages.recipePage.recipePage;
import chillingMonsters.Pages.registerPage.registerPage;
import chillingMonsters.Pages.searchPage.searchPage;
import chillingMonsters.Pages.stockPage.stockEntryPage;
import chillingMonsters.Pages.stockPage.stockPage;
import chillingMonsters.Utility;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class PageFactory {
	private static StackPane appRoot;

	private static loginPage login = new loginPage();
	private static registerPage register = new registerPage();
	private static navMenu menu = new navMenu();

	private static stockPage stock;
	private static stockEntryPage stockEntry;
	private static ingredientPage ingredient;

	private static searchPage search;

	private static recipePage recipe;
	private static recipeEntryPage recipeEntry;
	private static recipeCreatePage recipeCreate;

	private static List<Page> pageHistory = new ArrayList<Page>() {{
		add(login);
	}};

	public static void setApp(StackPane s) {
		appRoot = new StackPane(login.getPagePane());
		appRoot.setPickOnBounds(false);

		appRoot.setPrefSize(360, 740);

		s.getChildren().add(menu.getPagePane());
		s.getChildren().add(appRoot);

		ImageView menuButton = new ImageView("img/MenuIcon2x.png");
		menuButton.getStyleClass().add("myButton");
		menuButton.setFitHeight(30);
		menuButton.setFitWidth(30);

		menuButton.setPickOnBounds(false);
		s.getChildren().add(menuButton);

		StackPane.setAlignment(menuButton, Pos.TOP_LEFT);
		StackPane.setMargin(menuButton, new Insets(20, 0, 0, 20));
		menuButton.setOnMouseClicked(event -> showMenu());
	}

	public static Page getLoginPage() {
		return login;
	}
	public static Page getRegisterPage() {
		return register;
	}
	public static Page getLandingPage() {
		getStockPage();
		getRecipePage();
		return getStockPage();
	}

	public static Page getStockPage() {
	  if (stock == null) stock = new stockPage();
	  return stock;
    }
    public static Page getStockRefresh() {
		stock = new stockPage();
		return stock;
    }
    public static Page getStockEntryPage(long foodID, PageOption option) {
	    if (stockEntry == null || stockEntry.foodID != foodID || stockEntry.option != option) {
		    stockEntry = new stockEntryPage(foodID, option);
	    }
		return stockEntry;
	}
	public static Page getIngredientPage(long foodID) {
		if (ingredient == null || ingredient.foodID != foodID) {
			ingredient = new ingredientPage(foodID);
		}

		return ingredient;
	}

	public static Page getSearchPage(PageOption option) {
		if (search == null || search.option != option) {
			search = new searchPage(option);
		}

		return search;
	}

	public static Page getRecipePage() {
		if (recipe == null) recipe = new recipePage();
		return recipe;
	}
	public static Page getRecipeRefresh() {
		recipe = new recipePage();
		return recipe;
	}
	public static Page getRecipeEntryPage(long recipeID) {
		if (recipeEntry == null || recipeEntry.recipeID != recipeID) {
			recipeEntry = new recipeEntryPage(recipeID);
		}
		return recipeEntry;
	}
	public static Page getRecipeCreatePage(long recipeID, PageOption option) {
		if (recipeCreate == null || recipeCreate.recipeID != recipeID || recipeCreate.option != option) {
			recipeCreate = new recipeCreatePage(recipeID, option);
		}

		return recipeCreate;
	}
	public static Page getRecipeCreatePage() {
		return getRecipeCreatePage(0, PageOption.DEFAULT);
	}

	public static Page getLastPage() {
		if (pageHistory.isEmpty()) {
			return login;
		} else if (pageHistory.size() == 1) {
			return pageHistory.get(0);
		} else {
			return pageHistory.get(1);
		}
	}
	public static void toNextPage(Page nextPage) {
		Page currentPage = pageHistory.get(0);

		if (currentPage == nextPage) return;

		if (pageHistory.size() > 1 && nextPage == pageHistory.get(1)) pageHistory.remove(currentPage);

		for (Page p : pageHistory) {
			if (p.getClass().equals(nextPage.getClass())) {
				pageHistory.remove(p);
			}
		}
		pageHistory.add(0, nextPage);

		AnchorPane curP = currentPage.getPagePane();
		AnchorPane nxtP = nextPage.getPagePane();

		nxtP.prefHeightProperty().bind(appRoot.prefHeightProperty());
		if (appRoot.getChildren().contains(nxtP)) appRoot.getChildren().remove(nxtP);
		appRoot.getChildren().add(nxtP);

		KeyFrame start = new KeyFrame(Duration.ZERO,
			new KeyValue(nxtP.opacityProperty(), 0),
			new KeyValue(curP.opacityProperty(), 1)
		);
		KeyFrame end = new KeyFrame(Duration.seconds(Utility.STD_TRANSITION_TIME),
			new KeyValue(nxtP.opacityProperty(), 1),
			new KeyValue(curP.opacityProperty(), 0)
		);

		Timeline slide = new Timeline(start, end);
		slide.setOnFinished(e -> appRoot.getChildren().remove(curP));
		slide.play();

		if (nextPage == search) menu.setSelected(0);
		else if (nextPage == stock) menu.setSelected(3);
		else if (nextPage == recipe) menu.setSelected(4);
	}
	public static void showMenu() {
		AnchorPane curP = (AnchorPane) appRoot.getChildren().get(0);
		KeyFrame start = new KeyFrame(Duration.ZERO);
		KeyFrame end = new KeyFrame(Duration.seconds(Utility.STD_TRANSITION_TIME),
			new KeyValue(appRoot.translateXProperty(), 230),
			new KeyValue(appRoot.prefHeightProperty(), 550)
		);

		Timeline shrink = new Timeline(start, end);
		shrink.play();
	}
	public static void hideMenu() {
		AnchorPane curP = (AnchorPane) appRoot.getChildren().get(0);

		KeyFrame start = new KeyFrame(Duration.ZERO);
		KeyFrame end = new KeyFrame(Duration.seconds(Utility.STD_TRANSITION_TIME),
			new KeyValue(appRoot.translateXProperty(), 0),
			new KeyValue(appRoot.prefHeightProperty(), 740)
		);

		Timeline shrink = new Timeline(start, end);
		shrink.play();
	}
}
