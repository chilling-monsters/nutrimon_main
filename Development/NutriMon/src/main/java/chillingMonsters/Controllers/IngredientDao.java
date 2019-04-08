package chillingMonsters.Controllers;

import java.util.List;
import java.util.Map;

public interface IngredientDao {
  public List<Map<String, Object>> search(String name);

  public Map<String, Object> getIngredient(long foodID);
}
