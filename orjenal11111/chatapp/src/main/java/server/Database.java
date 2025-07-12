package server;

import java.sql.*;
import java.util.*;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/chat_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection connection;

    public Database() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Connected to database.");
    }

    public int addOrGetUser(String username) throws SQLException {
        String query = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }

        String insert = "INSERT INTO users (username) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public int addOrGetRoom(String roomName) throws SQLException {
        String query = "SELECT id FROM rooms WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, roomName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }

        String insert = "INSERT INTO rooms (name) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, roomName);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public void addMemberToRoom(int userId, int roomId) throws SQLException {
        String check = "SELECT id FROM room_members WHERE user_id = ? AND room_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(check)) {
            ps.setInt(1, userId);
            ps.setInt(2, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return;
            }
        }
        String insert = "INSERT INTO room_members (user_id, room_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            ps.setInt(1, userId);
            ps.setInt(2, roomId);
            ps.executeUpdate();
        }
    }

    public List<String> getAllRooms() throws SQLException {
        List<String> rooms = new ArrayList<>();
        String query = "SELECT name FROM rooms";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                rooms.add(rs.getString("name"));
            }
        }
        return rooms;
    }

    public List<String> getMessagesForRoom(int roomId) throws SQLException {
        List<String> messages = new ArrayList<>();
        String query = "SELECT u.username, m.content, m.sent_at FROM messages m JOIN users u ON m.user_id = u.id WHERE m.room_id = ? ORDER BY m.sent_at ASC";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String msg = "[" + rs.getTimestamp("sent_at") + "] " + rs.getString("username") + ": " + rs.getString("content");
                    messages.add(msg);
                }
            }
        }
        return messages;
    }

    public void addMessage(int userId, int roomId, String content) throws SQLException {
        String insert = "INSERT INTO messages (user_id, room_id, content) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            ps.setInt(1, userId);
            ps.setInt(2, roomId);
            ps.setString(3, content);
            ps.executeUpdate();
        }
    }
    
      public static boolean validateUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
      
      public int getUserIdByEmail(String email) throws SQLException {
    String query = "SELECT id FROM users WHERE email = ?";
    try (PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setString(1, email);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
    }
    return -1;
}

public static String getUsernameByEmail(String email) {
    String query = "SELECT username FROM users WHERE email = ?";
    try (
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement stmt = conn.prepareStatement(query)
    ) {
        stmt.setString(1, email);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("username");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "Unknown";
}

}

