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
import chillingMonsters.MySQLCon;

public class RecipeController extends  NutriMonController{
  RecipeController(){
    super("recipes", "recipeIngredients", "recipeID")
  }
}


public List<Map<String, Object>> showRecipe() {
        List<Map<String, Object>> recipes = new ArrayList<>();

        /* one of the ways of running a dynamically built SQL statement. */
        String query = "SELECT recipeID, recipeName, recipeDescription " +
                "FROM recipes JOIN ingredients using(recipeID) " +
                "WHERE userID = ? " +
                "GROUP BY recipeID " +
                "ORDER BY next_exp ASC";

        /*try: define a block of code to be tested for errors*/
        try {
            try (PreparedStatement stmt = MySQLCon.getConnection().prepareStatement(query)) {
                stmt.setInt(1, recipeID);
                ResultSet rs = stmt.executeQuery();
                recipes = MySQLCon.resultsList(rs);
                MySQLCon.close();
            }
        }

        /*catch: define a block of code to be executed*/
        catch (SQLException e) {
            Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return recipes;
    }

public List<Map<String, Object>> showRecipeIngredient(int recipeId) {
        List<Map<String, Object>> recipes = new ArrayList<>();
        String query = "SELECT recipeID, recipeName, " +
                "sum(ingredientQtty) as 'quantity', "
                "FROM recipeIngredientss JOIN ingredients using(recipeID) " +
                "WHERE userID = ? " +
                "GROUP BY recipeID " +
                "ORDER BY next_exp ASC";

        try {
            try (PreparedStatement stmt = MySQLCon.getConnection().prepareStatement(query)) {
                stmt.setInt(1, recipeId);
                stmt.setInt(2, foodId);
                ResultSet rs = stmt.executeQuery();
                stocks = MySQLCon.resultsList(rs);
                MySQLCon.close();
            }
        }
        catch (SQLException e) {
            Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return recipes;
    }
}

public void createRecipe(Map<String, Object> recipes) {
        StringBuilder fields = new StringBuilder("(");
        StringBuilder vals = new StringBuilder("(");
        for (Map.Entry<String, Object> entry : values.entrySet())
        {
            fields.append(entry.getKey());
            vals.append(String.valueOf(entry.getValue()));
            fields.append(",");
            vals.append(",");
        }
        fields.append("userID)");
        vals.append(String.format("%d)", userId));
        String query = String.format("INSERT INTO %s %s VALUES %s",
        table, fields.toString(), vals.toString());

        try {
            try (Statement stmt = MySQLCon.getConnection().createStatement()){
                stmt.executeUpdate(query);
            }
            MySQLCon.close();
        }

        catch (SQLException e) {
        Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, "Failed insert", e);
        }

        }
}

public void deleteRecipe(int recipeId) {
        try (PreparedStatement stmt = MySQLCon.getConnection()
        .prepareStatement(String.format("DELETE FROM %s WHERE recipeID = ? AND %s = ?",
        table, pk))) {
        stmt.setInt(1, userId);
        stmt.setInt(2, id);
        stmt.executeUpdate();
        MySQLCon.close();
        }
        catch (SQLException e) {
        Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, "Failed delete", e);
        }
}

public void updateRecipeIngredient(int recipeId, Map<String, Object> recipes) {
        StringBuilder query = new StringBuilder(String.format("UPDATE %s ", table));
        int i = 0;
        int size = values.size();

        for (Map.Entry<String, Object> entry : recipes.entrySet()) {

        query.append(String.format("SET %s = %s", entry.getKey(), String.valueOf(entry.getValue())));

        if (i != size - 1) {
            query.append(", ");
        }
        i++;
        }

        query.append(String.format(" WHERE %s = %d", pk, id));
        try {
        try (Statement stmt = MySQLCon.getConnection().createStatement()) {
        stmt.executeUpdate(query.toString());
        }
        MySQLCon.close();
        }
        catch (SQLException e) {
        Logger.getLogger(MySQLCon.class.getName()).log(Level.SEVERE, "Failed update", e);
        }
}

