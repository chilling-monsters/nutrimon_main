package chillingMonsters.Pages.stockPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class StockEntryCardComponent extends AnchorPane {
	@FXML
	private Label entryTimeLeft;

	@FXML
	private Label entryAddedDate;

	@FXML
	private Label entryAmount;

	public StockEntryCardComponent(long timeLeft, String addedDate, float amount) {
		super();

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/stockPage/stockEntryCard.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		entryTimeLeft.setText(String.format("Expires in %d days", timeLeft));
		entryAddedDate.setText(String.format("Added %s", addedDate));
		entryAmount.setText(String.format("%.0fg", amount));
	}
}