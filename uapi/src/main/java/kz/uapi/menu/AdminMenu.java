package kz.uapi.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import kz.uapi.database.DatabaseManager;
import kz.uapi.users.Employee.Admin;

public class AdminMenu {

    static Scanner scanner = Main.getScanner();

    // ================================================================== ADMIN
    static void showAdminMenu(Admin admin) {
        while (true) {
            System.out.print("""
                    ================================================
                    ADMIN MENU
                    ================================================

                    1. View Personal Info
                    2. Messages
                    3. Users
                    4. Logs
                    5. News
                    0. Exit

                    Enter your choice:\s""");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> CommonMethods.viewPersonalInfo(admin.getLogin());
                case "2" -> CommonMethods.sendMessages(admin.getLogin());
                case "3" -> manageUsers();
                case "4" -> viewLogs();
                case "5" -> CommonMethods.viewNews();
                case "0" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

    // ================================================================== MANAGE
    // USERS

    static void manageUsers() {
        while (true) {
            System.out.print("""
                    ================================================
                    MANAGE USERS
                    ================================================

                    1. List Users
                    2. Search User
                    3. Delete User
                    0. Back

                    Enter your choice:\s""");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> listUsers();
                case "2" -> searchUser();
                case "3" -> removeUser();
                case "0" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

    private static void listUsers() {
        String query = "SELECT login, role, first_name, last_name FROM users ORDER BY role, login";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            CommonMethods.printHeader("USER LIST");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Login:      " + rs.getString("login"));
                System.out.println("Name:       " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Role:       " + rs.getString("role"));
                CommonMethods.printSeparator();
            }

            if (!found) {
                System.out.println("No users found.");
            }

            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error listing users: " + e.getMessage());
        }
    }

    private static void searchUser() {
        System.out.print("Enter user login: ");
        String login = scanner.nextLine().trim();

        String query = """
                SELECT login, first_name, last_name, role, created_at
                FROM users
                WHERE login = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println();
                System.out.println("Login:      " + rs.getString("login"));
                System.out.println("Name:       " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Role:       " + rs.getString("role"));
                System.out.println("Created at: " + rs.getString("created_at"));
                System.out.println();
            } else {
                System.out.println("\nUser not found.\n");
            }
        } catch (SQLException e) {
            System.out.println("Error searching user: " + e.getMessage());
        }
    }

    private static void removeUser() {
        System.out.print("Enter login to remove: ");
        String login = scanner.nextLine().trim();

        if (login.isBlank()) {
            System.out.println("\nLogin cannot be empty.\n");
            return;
        }

        deleteUserByLogin(login);
    }

    private static void deleteUserByLogin(String login) {
        String[] queries = {
                "DELETE FROM messages WHERE sender_login = ? OR receiver_login = ?",
                "DELETE FROM requests WHERE sender_login = ?",
                "DELETE FROM faculty_students WHERE student_login = ?",
                "DELETE FROM marks WHERE student_login = ?",
                "DELETE FROM attestations WHERE student_login = ?",
                "DELETE FROM transcripts WHERE student_login = ?",
                "DELETE FROM questionnaires WHERE student_login = ?",
                "DELETE FROM students WHERE login = ?",
                "DELETE FROM teachers WHERE login = ?",
                "DELETE FROM managers WHERE login = ?",
                "DELETE FROM researcher_employees WHERE login = ?",
                "DELETE FROM admins WHERE login = ?",
                "DELETE FROM employees WHERE login = ?",
                "DELETE FROM users WHERE login = ?"
        };

        try (Connection conn = DatabaseManager.getConnection()) {
            for (String query : queries) {
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, login);
                    if (query.contains("OR receiver_login")) {
                        stmt.setString(2, login);
                    }
                    stmt.executeUpdate();
                } catch (SQLException ignored) {
                }
            }
            System.out.println("\nUser removed successfully (if existed).\n");
        } catch (SQLException e) {
            System.out.println("Error removing user: " + e.getMessage());
        }
    }

    // ================================================================== LOGS

    static void viewLogs() {
        String[] logQueries = {
                "SELECT log_id, user_login, action, date FROM logs ORDER BY date DESC",
                "SELECT id AS log_id, user_login, action, created_at AS date FROM logs ORDER BY created_at DESC"
        };

        try (Connection conn = DatabaseManager.getConnection()) {
            for (String query : logQueries) {
                try (PreparedStatement stmt = conn.prepareStatement(query);
                        ResultSet rs = stmt.executeQuery()) {

                    CommonMethods.printHeader("LOGS");

                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        System.out.println("ID:      " + rs.getInt("log_id"));
                        System.out.println("User:    " + rs.getString("user_login"));
                        System.out.println("Action:  " + rs.getString("action"));
                        System.out.println("Date:    " + rs.getString("date"));
                        CommonMethods.printSeparator();
                    }

                    if (!found) {
                        continue;
                    }

                    System.out.println();
                    return;
                } catch (SQLException ignored) {
                }
            }

            System.out.println("\nNo logs available.\n");
        } catch (SQLException e) {
            System.out.println("Error fetching logs: " + e.getMessage());
        }
    }

}
