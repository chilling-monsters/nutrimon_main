package chillingMonsters.Controllers;

import java.util.List;
import java.util.Map;

public interface Controller {

  Map<String, Object> get(long id);

  void create(Map<String, Object> attributes);

  void update(long id, Map<String, Object> attributes);

  void delete(long id);
}
