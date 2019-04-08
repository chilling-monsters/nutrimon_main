package chillingMonsters;

import chillingMonsters.Controllers.ControllerFactory;

import java.util.List;
import java.util.Map;

public class App {

  public static void main(String[] args) {
    Map<String, List<Map<String,Object>>> result = ControllerFactory.makeIntakeController().showIntakesByDate();
    for (String date : result.keySet()) {
      List<Map<String, Object>> intakes = result.get(date);
      for (Map<String, Object> intake : intakes) {
        System.out.println(date + intake.entrySet().toString());
      }
    }
  }
}
