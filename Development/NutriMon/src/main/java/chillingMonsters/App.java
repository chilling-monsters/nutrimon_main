package chillingMonsters;

import java.util.List;
import java.util.Map;

import chillingMonsters.Controllers.ControllerFactory;

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
