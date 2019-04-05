package chillingMonsters.Pages.stockPage;

import chillingMonsters.Utility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Timestamp;

public class StockEntryCardComponent extends AnchorPane {
	private long stockItemID;
	private long timeLeft;
	private Timestamp addedDate;
	private float amount;

	@FXML
	private Label entryTimeLeft;

	@FXML
	private Label entryAddedDate;

	@FXML
	private Label entryAmount;

	public StockEntryCardComponent(long stockItemID, long timeLeft, Timestamp addedDate, float amount) {
		super();
		this.stockItemID = stockItemID;
		this.timeLeft = timeLeft;
		this.addedDate = addedDate;
		this.amount = amount;

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/stockPage/stockEntryCard.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (timeLeft > 0) {
			entryTimeLeft.setText(String.format("Expires in %d day%s", timeLeft, timeLeft > 1 ? "s" : ""));
		} else if (timeLeft < 0) {
			entryTimeLeft.setText(String.format("Expired %d day%s ago", -timeLeft, -timeLeft < -1 ? "s" : ""));
		} else {
			entryTimeLeft.setText("Expires Today");
		}

		entryAddedDate.setText(String.format("Added %s", Utility.parseDate(addedDate)));
		entryAmount.setText(String.format("%.0fg", amount));
	}

	public long getStockItemID() { return stockItemID; }
	public long getTimeLeft() { return timeLeft; }
	public Timestamp getAddedDate() { return addedDate; }
	public float getAmount() { return amount; }
}