package chillingMonsters.Controllers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import chillingMonsters.DBConnect;

public class IntakeController extends NutriMonController implements IntakeDao {

  IntakeController() {
    super("userintake", "intakeID");
  }

  /**
   * Shows intakes grouped together by date and ordered by time.
   *
   * @return map with String key being the date and the value being the ordered list of intakes
   */
  public Map<String, List<Map<String, Object>>> showIntakesByDate() {
    String query = "CALL get_intakes(?)";
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

  public void intakeStock(long foodID, float quantity) {
    String query = "CALL intake_food(?,?,?)";
    try (CallableStatement stmt = DBConnect.getConnection().prepareCall(query)) {
      stmt.setLong(1, userId);
      stmt.setLong(2, foodID);
      stmt.setFloat(3, quantity);
      stmt.executeQuery();
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
  }

  public void intakeRecipe(long recipeID, int serving) {
    String query = "CALL intake_recipe(?,?,?)";
    try (CallableStatement stmt = DBConnect.getConnection().prepareCall(query)) {
      stmt.setLong(1, userId);
      stmt.setLong(2, recipeID);
      stmt.setInt(3, serving);
      stmt.executeQuery();
    } catch(SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
  }

  public void updateIntakeDate(long intakeID, String datetime) {
    String query = "UPDATE userintake SET intakeDate = ? WHERE intakeID = ? AND userID = ?";
    try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
      stmt.setTimestamp(1, Timestamp.valueOf(datetime));
      stmt.setLong(2, intakeID);
      stmt.setLong(3, userId);
      stmt.executeQuery();
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
  }
}
