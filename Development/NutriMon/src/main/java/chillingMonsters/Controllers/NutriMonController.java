package chillingMonsters.Controllers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import chillingMonsters.DBConnect;

public abstract class NutriMonController {
    static long userId = 1;
    private final String table;
    private final String pk;

    NutriMonController(String table, String pk) {
        this.table = table;
        this.pk = pk;
    }

    static void setUserId(long id) {
        userId = id;
    }

    public static long getUserId() { return userId; }

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
        query.append("SET ");
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            query.append(String.format("%s = %s", entry.getKey(), String.valueOf(entry.getValue())));
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
}
