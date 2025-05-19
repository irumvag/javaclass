package com.dineix.web.utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificationUtil {
    public static void createNotification(DataSource dataSource, int userId, String message) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO notifications (user_id, message) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setString(2, message);
                stmt.executeUpdate();
            }
        }
    }
}