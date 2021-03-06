package chillingMonsters.Controllers.Intake;

import chillingMonsters.Controllers.NutriMonController;
import chillingMonsters.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static chillingMonsters.DBConnect.resultsList;

/**
 * Concrete implmentation of Intake controller.
 */
public class IntakeControllerImpl extends NutriMonController implements IntakeController {

  public IntakeControllerImpl() {
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
      results = resultsList(rs);
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

  public boolean intakeStock(long foodID, float quantity, String date) {
    String query = "CALL intake_food(?,?,?,?)";
    try (CallableStatement stmt = DBConnect.getConnection().prepareCall(query)) {
      stmt.setLong(1, userId);
      stmt.setLong(2, foodID);
      stmt.setFloat(3, quantity);
      stmt.setTimestamp(4, Timestamp.valueOf(date));
      stmt.executeQuery();
      
      return true;
    } catch (SQLException e) {
      return false;
    } finally {
      DBConnect.close();
    }
  }

  public boolean intakeRecipe(long recipeID, float serving, String date) {
    String query = "CALL intake_recipe(?,?,?,?)";
    try (CallableStatement stmt = DBConnect.getConnection().prepareCall(query)) {
      stmt.setLong(1, userId);
      stmt.setLong(2, recipeID);
      stmt.setFloat(3, serving);
      stmt.setTimestamp(4, Timestamp.valueOf(date));
      stmt.executeQuery();

      return true;
    } catch(SQLException e) {
      return false;
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
      stmt.executeUpdate();
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
  }

  public Map<String, Object> getIntake(long intakeID) {
    Map<String, Object> intake = null;
    String query = "CALL get_intake(?,?)";
    try (PreparedStatement stmt = DBConnect.getConnection().prepareStatement(query)) {
      stmt.setLong(1, userId);
      stmt.setLong(2, intakeID);
      ResultSet rs = stmt.executeQuery();
      List<Map<String, Object>> results = resultsList(rs);
      if (!results.isEmpty()) {
        intake = results.get(0);
      }
    } catch (SQLException e) {
      Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    } finally {
      DBConnect.close();
    }
    return intake;
  }
}
