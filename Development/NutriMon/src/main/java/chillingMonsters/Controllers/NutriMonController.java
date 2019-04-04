package chillingMonsters.Controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import chillingMonsters.DBConnect;

public abstract class NutriMonController {
    static long userId;
    private final String table;
    private final String pk;

    NutriMonController(String table, String pk) {
        this.table = table;
        this.pk = pk;
    }

    static void setUserId(long id) {
        userId = id;
    }

    static long getUserId() { return userId; }

    List<Map<String, Object>> show() {
        List<Map<String,Object>> output = new ArrayList<>();
        try {
            ResultSet rs;
            try (PreparedStatement stmt = DBConnect.getConnection()
                    .prepareStatement(String.format("SELECT * FROM %s WHERE userID = ?", table))) {
                stmt.setLong(1, userId);
                rs = stmt.executeQuery();
            }
            output = DBConnect.resultsList(rs);
            rs.close();
            DBConnect.close();
        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, "Failed select", e);
        }
        return output;
    }

    Map<String, Object> get(long id) {
        try {
            ResultSet rs;
            try (PreparedStatement stmt = DBConnect.getConnection()
                    .prepareStatement(String.format("SELECT * FROM %s WHERE userID = ? AND %s = ?",
                            table, pk))) {
                stmt.setLong(1, userId);
                stmt.setLong(2, id);
                rs = stmt.executeQuery();
            }
            List<Map<String, Object>> output = DBConnect.resultsList(rs);
            if (!output.isEmpty()) {
                return output.get(0);
            }
            rs.close();
            DBConnect.close();
        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, "Failed select", e);
        }
        return null;
    }

    void delete(long id) {
        try (PreparedStatement stmt = DBConnect.getConnection()
                .prepareStatement(String.format("DELETE FROM %s WHERE userID = ? AND %s = ?",
                        table, pk))) {
            stmt.setLong(1, userId);
            stmt.setLong(2, id);
            stmt.executeUpdate();
            DBConnect.close();
        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, "Failed delete", e);
        }
    }

    void update(long id, Map<String, Object> values) {
        StringBuilder query = new StringBuilder(String.format("UPDATE %s ", table));
        int i = 0;
        int size = values.size();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            query.append(String.format("SET %s = %s", entry.getKey(), String.valueOf(entry.getValue())));
            if (i != size - 1) {
                query.append(", ");
            }
            i++;
        }
        query.append(String.format(" WHERE %s = %d", pk, id));
        try {
            try (Statement stmt = DBConnect.getConnection().createStatement()) {
                stmt.executeUpdate(query.toString());
            }
            DBConnect.close();
        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, "Failed update", e);
        }
    }

    void create(Map<String, Object> values) {
        StringBuilder fields = new StringBuilder("(");
        StringBuilder vals = new StringBuilder("(");
        for (Map.Entry<String, Object> entry : values.entrySet()) {
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
            try (Statement stmt = DBConnect.getConnection().createStatement()) {
                stmt.executeUpdate(query);
            }
            DBConnect.close();
        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, "Failed insert", e);
        }
    }

    /**
     * A function performs single string type attribute query
     *
     * @param table table
     * @param attr attribute
     * @param record input string
     * @return  true if found
     */
    public boolean exists(String table, String attr, String record) {
        boolean check = false;

        String queryString = "SELECT " + attr + " FROM " + table +
                " WHERE " + attr + " = ? ";
        try {
            ResultSet rs;   // Result set

            PreparedStatement stmt = DBConnect.getConnection().prepareStatement(queryString);
            stmt.setString(1, record);

            rs = stmt.executeQuery();   // Statement execution

            check = rs.next();

            rs.close();         // Close rs
            stmt.close();       // Close stmt
            DBConnect.close();   // Close DB

        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        return check;
    }

    public static String parseFoodName(String name) {
        String[] byComma = name.split(",");
        String label = "";

        for (int i = 0; i < byComma.length; i++) {
            byComma[i] = toCapitalized(byComma[i]);
        }

        if (byComma.length > 1) {
            label += String.join(" ", byComma[1], byComma[0]);
        } else {
            label = byComma[0];
        }

        for (int i = 2; i < byComma.length; i++) {
            label += ", " + byComma[i];
        }

        return label;
    }

    private static String toCapitalized(String str) {
        String[] bySpace = str.toLowerCase().split(" ");
        String rtn = "";

        for (String s : bySpace) {
            rtn += toCapitalizedWord(s) + " ";
        }

        return rtn.trim();
    }

    private static String toCapitalizedWord(String str) {
        if (str.length() > 0) {
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        } else {
            return str.toUpperCase();
        }
    }
}
