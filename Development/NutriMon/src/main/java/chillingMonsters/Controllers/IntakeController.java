package chillingMonsters.Controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import chillingMonsters.DBConnect;

public abstract class IntakeController extends NutriMonController {

  IntakeController() {
    super("userintake", "intakeID");
  }

  /**
   * Shows intakes grouped together by date and ordered by time.
   *
   * @return map with String key being the date and the value being the ordered list of intakes
   */
  public Map<String, List<Map<String, Object>>> showIntakesByDate() {
    String query = "CALL getIntakes(?)";
    Map<String, List<Map<String,Object>>> intakes = new HashMap<>();
    List<Map<String, Object>> results = null;
    try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
      stmt.setLong(1, userId);
      ResultSet rs = stmt.executeQuery();
      results = DBConnect.resultsList(rs);
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
    if (results != null) {
      String date;
      for (Map<String, Object> intake : results) {
        date = String.valueOf(intake.get("date"));
        if (!intakes.containsKey(date)) {
          intakes.put(date, new ArrayList<>());
        }
        intakes.get(date).add(intake);
      }
    }
    return intakes;
  }
}
