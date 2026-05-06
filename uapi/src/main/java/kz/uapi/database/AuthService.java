package kz.uapi.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kz.uapi.abs_class.User;

public class AuthService {

    public static User login(String login, String password) {
        String query = "SELECT role FROM users WHERE login = ? AND password = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Password is correct, fetch full user object
                return UserDAO.getUserByLogin(login);
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
            return null;
        }
    }
}