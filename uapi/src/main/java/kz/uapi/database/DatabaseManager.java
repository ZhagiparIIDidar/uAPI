package kz.uapi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для управления подключением к базе данных SQLite.
 * Реализует паттерн Singleton для единственного подключения к БД.
 * 
 * <p>
 * Этот класс отвечает за создание, получение и закрытие подключения к базе
 * данных университета.
 * </p>
 * 
 * @author UAPI System
 * @version 1.0
 * @since 1.0
 */
public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:university.db";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}