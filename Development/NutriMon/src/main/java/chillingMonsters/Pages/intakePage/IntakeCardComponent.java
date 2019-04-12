package chillingMonsters.Pages.intakePage;

import chillingMonsters.Pages.PageFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class IntakeCardComponent extends AnchorPane {
	private long intakeID;

	@FXML
	private Label cardName;

	@FXML
	private Label cardCategory;

	@FXML
	private Label cardCalories;

	@FXML
	private Label cardAmount;

	public IntakeCardComponent(long intakeID, String name, String category, int calories, double amount) {
		super();
		this.intakeID = intakeID;

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intakePage/intakeCard.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.setOnMouseClicked(event -> handleOnClick());

		cardName.setText(name);
		cardCategory.setText(category.toUpperCase());
		cardCalories.setText(String.format("%d Cal", calories));
		cardAmount.setText(String.format("%.1fg", amount));
	}

	public void handleOnClick() {
		PageFactory.toNextPage(PageFactory.getIntakeEntry(intakeID));
	}
}
