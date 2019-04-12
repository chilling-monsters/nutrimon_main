package chillingMonsters.Pages;

import chillingMonsters.AlertHandler;
import chillingMonsters.Pages.ingredientPage.ingredientPage;
import chillingMonsters.Pages.intakePage.intakeEntryPage;
import chillingMonsters.Pages.intakePage.intakePage;
import chillingMonsters.Pages.landingPage.landingPage;
import chillingMonsters.Pages.loginPage.loginPage;
import chillingMonsters.Pages.navMenu.navMenu;
import chillingMonsters.Pages.recipePage.recipeCreatePage;
import chillingMonsters.Pages.recipePage.recipeEntryPage;
import chillingMonsters.Pages.recipePage.recipePage;
import chillingMonsters.Pages.registerPage.registerPage;
import chillingMonsters.Pages.searchPage.searchPage;
import chillingMonsters.Pages.stockPage.stockEntryPage;
import chillingMonsters.Pages.stockPage.stockPage;
import chillingMonsters.Pages.userProfilePage.userProfilePage;
import chillingMonsters.Utility;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class PageFactory {
	private static boolean menuShown = false;
	private static boolean formInProgress = false;

	private static ImageView menuButton = new ImageView();
	private static StackPane appRoot;

	private static loginPage login = new loginPage();
	private static registerPage register = new registerPage();
	private static navMenu menu = new navMenu();

	private static searchPage search;

	private static stockPage stock;
	private static stockEntryPage stockEntry;
	private static ingredientPage ingredient;

	private static userProfilePage profile;
	private static intakePage intake;
	private static intakeEntryPage intakeEntry;

	private static landingPage landing;

	private static recipePage recipe;
	private static recipeEntryPage recipeEntry;
	private static recipeCreatePage recipeCreate;

	private static List<Page> pageHistory = new ArrayList<Page>() {{
		add(login);
	}};

	public static void initialize(StackPane s) {
		appRoot = new StackPane(login.getPagePane());
		appRoot.setPickOnBounds(false);
		appRoot.setStyle("-fx-border-color: white");

		appRoot.setPrefSize(360, 740);

		s.getChildren().add(menu.getPagePane());
		s.getChildren().add(appRoot);

		menuButton.getStyleClass().add("myButton");
		menuButton.setFitHeight(30);
		menuButton.setFitWidth(30);

		menuButton.setPickOnBounds(false);
		s.getChildren().add(menuButton);

		StackPane.setAlignment(menuButton, Pos.TOP_LEFT);
		StackPane.setMargin(menuButton, new Insets(20, 0, 0, 20));

		menuButton.setOnMouseClicked(event -> handleBackNavigation());
	}

	public static Page getLoginPage() {
		return login;
	}
	public static Page getRegisterPage() {
		return register;
	}
	public static Page getUserProfilePage() {
		profile = new userProfilePage();
		return profile;
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
	    stockEntry = new stockEntryPage(foodID, option);
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
		recipeEntry = new recipeEntryPage(recipeID);
		return recipeEntry;
	}
	public static Page getRecipeEditPage(long recipeID, PageOption option) {
		if (recipeCreate == null || recipeCreate.recipeID != recipeID || recipeCreate.option != option) {
			recipeCreate = new recipeCreatePage(recipeID, option);
		}

		return recipeCreate;
	}
	public static Page getRecipeCreatePage() {
		return getRecipeEditPage(0, PageOption.RECIPE);
	}
	public static Page getRecipeForm() {
		return recipeCreate;
	}

	public static Page getIntakePage() {
		if (intake == null) intake = new intakePage();
		return intake;
	}
	public static Page getIntakeRefresh() {
		intake = new intakePage();
		return intake;
	}
	public static Page getIntakeEntry(long intakeID, PageOption option) {
		intakeEntry = new intakeEntryPage(intakeID, option);
		return intakeEntry;
	}

	public static Page getLandingPage() {
		if (landing == null) landing = new landingPage();
		return landing;
	}
	public static Page getLandingRefresh() {
		getStockRefresh();
		getRecipeRefresh();
		getIntakeRefresh();

		landing = new landingPage();
		return landing;
	}

	public static Page getCurrentPage() {
		return pageHistory.get(0);
	}
	public static void toNextPage(Page nextPage) {
		setMenuButtonStyle(nextPage);

		Page currentPage = getCurrentPage();
		if (currentPage == nextPage) return;

		if (pageHistory.size() > 1 && nextPage == pageHistory.get(1)) pageHistory.remove(currentPage);

		for (int i = 0; i < pageHistory.size(); i++) {
			Page p = pageHistory.get(i);
			if (p.getClass().equals(nextPage.getClass())) pageHistory.remove(p);
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
		else if (nextPage == landing) menu.setSelected(1);
		else if (nextPage == intake) menu.setSelected(2);
		else if (nextPage == stock) menu.setSelected(3);
		else if (nextPage == recipe) menu.setSelected(4);
	}
	public static void showMenu() {
		KeyFrame start = new KeyFrame(Duration.ZERO);
		KeyFrame end = new KeyFrame(Duration.seconds(Utility.STD_TRANSITION_TIME),
			new KeyValue(appRoot.translateXProperty(), 230),
			new KeyValue(appRoot.prefHeightProperty(), 550),
			new KeyValue(appRoot.maxHeightProperty(), 550)
		);

		Timeline shrink = new Timeline(start, end);
		shrink.play();

		menuShown = true;
		setMenuButtonStyle(menu);
	}
	public static void hideMenu() {
		KeyFrame start = new KeyFrame(Duration.ZERO);
		KeyFrame end = new KeyFrame(Duration.seconds(Utility.STD_TRANSITION_TIME),
			new KeyValue(appRoot.translateXProperty(), 0),
			new KeyValue(appRoot.prefHeightProperty(), 740)
		);

		Timeline shrink = new Timeline(start, end);
		shrink.play();

		menuShown = false;
		setMenuButtonStyle(getCurrentPage());
	}
	public static void setMenuButtonStyle(Page p) {
		if (p == login || p == register) {
			menuButton.setStyle(null);
		} else if (p == stock || p == recipe || p == intake || p == search || p == landing) {
			menuButton.setStyle("-fx-image: url(img/MenuIcon2x.png)");
		} else if (p == menu) {
			menuButton.setStyle("-fx-image: url(img/MenuIconWhite2x.png)");
		} else {
			menuButton.setStyle("-fx-image: url(img/Menu-Back-Icon-White2x.png)");
		}
	}
	public static void handleBackNavigation() {
		if (formInProgress) {
			boolean confirmed = AlertHandler.showConfirmationAlert("Are you sure?", "Unsaved changes will be lost");
			if (confirmed) {
				formInProgress = false;
			} else {
				return;
			}
		}

		Page p = getCurrentPage();
		if (menuShown) {
			hideMenu();
			return;
		}

		if (p == stock || p == recipe || p == intake || p == search || p == landing) {
			showMenu();
			return;
		}

		Page lastPage = pageHistory.get(1);
		if ((p == recipeEntry && lastPage == recipe) || (p == recipeEntry && lastPage == recipeCreate)) {
			lastPage = getRecipeRefresh();
		} else if (p == stockEntry && lastPage == stock) {
			lastPage = getStockRefresh();
		}

		toNextPage(lastPage);
	}
	public static void setFormInProgress(boolean f) {
		formInProgress = f;
	}
}
