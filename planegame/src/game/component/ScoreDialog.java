package game.component;


import game.component.DBConnection;
import javax.swing.*;
import java.awt.BorderLayout;
import java.sql.*;
import java.util.*;

public class ScoreDialog extends JDialog {
    public ScoreDialog(JFrame parent) {
        super(parent, "High Scores", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        String[] columns = {"Username", "Score", "Date"};
        Object[][] data = fetchScores();
        
        JTable table = new JTable(data, columns);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private Object[][] fetchScores() {
        String query = "SELECT u.username, s.score, s.played_at " +
                       "FROM scores s " +
                       "JOIN users u ON s.user_id = u.id " +
                       "ORDER BY s.score DESC LIMIT 10";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            List<Object[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new Object[]{
                    rs.getString("username"),
                    rs.getInt("score"),
                    rs.getTimestamp("played_at")
                });
            }
            return rows.toArray(new Object[0][]);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new Object[0][0];
        }
    }
}