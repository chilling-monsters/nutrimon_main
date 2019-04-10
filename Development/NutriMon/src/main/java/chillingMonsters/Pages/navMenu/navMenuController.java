package chillingMonsters.Pages.navMenu;

import chillingMonsters.Pages.PageController;
import chillingMonsters.Pages.PageFactory;
import chillingMonsters.Utility;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class navMenuController implements PageController {
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
	private ImageView backButton;

	@FXML
	private HBox activeBox;

	@FXML
	private VBox menuBar;

	@FXML
	public void initialize() {
		backButton.setOnMouseClicked(event -> handleBackOnClick());
		search.setOnMouseEntered(event -> handleHover(search));
		today.setOnMouseEntered(event -> handleHover(today));
		intake.setOnMouseEntered(event -> handleHover(intake));
		stock.setOnMouseEntered(event -> handleHover(stock));
		recipe.setOnMouseEntered(event -> handleHover(recipe));
	}

	private void handleBackOnClick() {
		PageFactory.hideMenu();
	}

	private void handleHover(Node n) {
		KeyFrame start = new KeyFrame(Duration.ZERO,
			new KeyValue(activeBox.layoutYProperty(), activeBox.getLayoutY()));
		KeyFrame end = new KeyFrame(Duration.seconds(0.2),
			new KeyValue(activeBox.layoutYProperty(), menuBar.getLayoutY() + n.getLayoutY()));

		Timeline slide = new Timeline(start, end);
		slide.play();
	}
}
