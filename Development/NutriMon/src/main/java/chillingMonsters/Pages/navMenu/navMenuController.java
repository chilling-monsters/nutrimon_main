package chillingMonsters.Pages.navMenu;

import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Pages.PageOption;
import chillingMonsters.Utility;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class navMenuController implements PageController {
	private static Map<HBox, String> iconWhiteMap;
	private static Map<HBox, String> iconOrangeMap;

	@FXML
	private HBox search;

	@FXML
	private HBox today;

	@FXML
	private HBox intake;

	@FXML
	private HBox stock;

	@FXML
	private HBox recipe;

	@FXML
	private HBox activeBox;

	@FXML
	private VBox menuBar;

	@FXML
	public void initialize() {
		search.setOnMouseEntered(event -> highlightSelected(0));
		today.setOnMouseEntered(event -> highlightSelected(1));
		intake.setOnMouseEntered(event -> highlightSelected(2));
		stock.setOnMouseEntered(event -> highlightSelected(3));
		recipe.setOnMouseEntered(event -> highlightSelected(4));

		search.setOnMouseClicked(event -> {
			PageFactory.hideMenu();
			PageFactory.toNextPage(PageFactory.getSearchPage(PageOption.DEFAULT));
		});
		today.setOnMouseClicked(event -> {
			PageFactory.hideMenu();
			PageFactory.toNextPage(PageFactory.getLandingPage());
		});
		stock.setOnMouseClicked(event -> {
			PageFactory.hideMenu();
			if (PageFactory.getCurrentPage() != PageFactory.getStockPage()) {
				PageFactory.toNextPage(PageFactory.getStockRefresh());
			}
		});
		intake.setOnMouseClicked(event -> {
			PageFactory.hideMenu();
			if (PageFactory.getCurrentPage() != PageFactory.getIntakePage()) {
				PageFactory.toNextPage(PageFactory.getIntakeRefresh());
			}
		});
		recipe.setOnMouseClicked(event -> {
			PageFactory.hideMenu();
			if (PageFactory.getCurrentPage() != PageFactory.getRecipePage()) {
				PageFactory.toNextPage(PageFactory.getRecipeRefresh());
			}
		});

		this.iconWhiteMap = new HashMap<HBox, String>() {{
			put(search, "img/Searchwhite2x.png");
			put(today, "img/Todaywhite2x.png");
			put(intake, "img/Intakewhite2x.png");
			put(stock, "img/Stockwhite2x.png");
			put(recipe, "img/Recipeswhite2x.png");

		}};
		this.iconOrangeMap = new HashMap<HBox, String>() {{
			put(search, "img/Searchorange2x.png");
			put(today, "img/Todayorange2x.png");
			put(intake, "img/Intakeorange2x.png");
			put(stock, "img/Stockorange2x.png");
			put(recipe, "img/Recipesorange2x.png");

		}};

		highlightSelected(0);
	}

	private void highlightSelected(int i) {
		HBox n = (HBox) menuBar.getChildren().get(i);
		KeyFrame start = new KeyFrame(Duration.ZERO,
			new KeyValue(activeBox.layoutYProperty(), activeBox.getLayoutY()));
		KeyFrame end = new KeyFrame(Duration.seconds(Utility.STD_TRANSITION_TIME / 2),
			new KeyValue(activeBox.layoutYProperty(), menuBar.getLayoutY() + n.getLayoutY()));

		Timeline slide = new Timeline(start, end);
		slide.play();
		slide.setOnFinished(event -> {
			for (Node m : menuBar.getChildren()) {
				HBox b = (HBox) m;
				b.getChildren().get(0).setStyle(String.format("-fx-image: url(%s);", iconWhiteMap.get(b)));
				b.getChildren().get(1).setStyle("-fx-text-fill: white;");
			}

			n.getChildren().get(0).setStyle(String.format("-fx-image: url(%s);", iconOrangeMap.get(n)));
			n.getChildren().get(1).setStyle("-fx-text-fill: #F5A623;");
		});
	}

	public void setSelected(int i) {
		highlightSelected(i);
	}
}
