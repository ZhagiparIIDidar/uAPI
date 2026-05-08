package kz.uapi.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import kz.uapi.database.DatabaseManager;

public class CommonMethods {

    static Scanner getScanner() {
        return Main.getScanner();
    }

    // ================================================================== UI HELPERS
    static void printHeader(String title) {
        System.out.println();
        System.out.println("================================================");
        System.out.println("                " + title);
        System.out.println("================================================");
        System.out.println();
    }

    static void printSeparator() {
        System.out.println("------------------------------------------------");
    }

    // ================================================================== COMMON
    // METHODS

    static void viewNews() {
        String query = """
                SELECT news_id, title, content, date, author_login
                FROM news
                ORDER BY date DESC
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            printHeader("NEWS");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:      " + rs.getInt("news_id"));
                System.out.println("Title:   " + rs.getString("title"));
                System.out.println("Content: " + rs.getString("content"));
                System.out.println("Date:    " + rs.getString("date"));
                System.out.println("Author:  " + rs.getString("author_login"));
                printSeparator();
            }

            if (!found) {
                System.out.println("No news found.");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching news: " + e.getMessage());
        }
    }

    // ==================================================================
    static void viewPersonalInfo(String login) {
        String query = """
                SELECT u.first_name, u.last_name, u.role, u.created_at
                FROM users u
                WHERE u.login = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            printHeader("VIEW PERSONAL INFO");

            if (rs.next()) {
                System.out.println("Login:      " + login);
                System.out.println("Name:       " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Role:       " + rs.getString("role"));
                System.out.println("Created At: " + rs.getString("created_at"));

                String role = rs.getString("role");
                switch (role) {
                    case "STUDENT" -> {
                        String sq = "SELECT gpa, year, major, credits FROM students WHERE login = ?";
                        PreparedStatement ss = conn.prepareStatement(sq);
                        ss.setString(1, login);
                        ResultSet sr = ss.executeQuery();
                        if (sr.next()) {
                            System.out.println("GPA:        " + sr.getDouble("gpa"));
                            System.out.println("Year:       " + sr.getInt("year"));
                            System.out.println("Major:      " + sr.getString("major"));
                            System.out.println("Credits:    " + sr.getInt("credits"));
                        }
                    }
                    case "TEACHER" -> {
                        String tq = "SELECT title, rating, h_index, specializations FROM teachers WHERE login = ?";
                        PreparedStatement ts = conn.prepareStatement(tq);
                        ts.setString(1, login);
                        ResultSet tr = ts.executeQuery();
                        if (tr.next()) {
                            System.out.println("Title:          " + tr.getString("title"));
                            System.out.println("Rating:         " + tr.getDouble("rating"));
                            System.out.println("H-Index:        " + tr.getInt("h_index"));
                            System.out.println("Specializations:" + tr.getString("specializations"));
                        }
                    }
                    case "MANAGER" -> {
                        String mq = "SELECT manager_type FROM managers WHERE login = ?";
                        PreparedStatement ms = conn.prepareStatement(mq);
                        ms.setString(1, login);
                        ResultSet mr = ms.executeQuery();
                        if (mr.next()) {
                            System.out.println("Manager Type: " + mr.getString("manager_type"));
                        }
                    }
                    case "ADMIN" -> {
                        String aq = "SELECT access_level FROM admins WHERE login = ?";
                        PreparedStatement as = conn.prepareStatement(aq);
                        as.setString(1, login);
                        ResultSet ar = as.executeQuery();
                        if (ar.next()) {
                            System.out.println("Access Level: " + ar.getInt("access_level"));
                        }
                    }
                    case "RESEARCHER" -> {
                        String rq = "SELECT h_index, specializations FROM researcher_employees WHERE login = ?";
                        PreparedStatement rs2 = conn.prepareStatement(rq);
                        rs2.setString(1, login);
                        ResultSet rr = rs2.executeQuery();
                        if (rr.next()) {
                            System.out.println("H-Index:        " + rr.getInt("h_index"));
                            System.out.println("Specializations:" + rr.getString("specializations"));
                        }
                    }
                }

                System.out.println();
            } else {
                System.out.println("User not found.\n");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching personal info: " + e.getMessage());
        }
    }

    // ==================================================================
    static void sendMessages(String login) {
        System.out.print("""

                ================================================
                              MESSAGES
                ================================================

                1. View Messages
                2. Send Message
                0. Back

                Enter your choice:\s""");

        String choice = getScanner().nextLine().trim();

        switch (choice) {
            case "1" -> viewMessages(login);
            case "2" -> sendMessage(login);
            case "0" -> {
                return;
            }
            default -> System.out.println("\nInvalid choice.\n");
        }
    }

    static void viewMessages(String login) {
        String query = """
                SELECT message_id, sender_login, receiver_login, content, date
                FROM messages
                WHERE sender_login = ? OR receiver_login = ?
                ORDER BY date DESC
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            stmt.setString(2, login);
            ResultSet rs = stmt.executeQuery();

            printHeader("MY MESSAGES");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:   " + rs.getInt("message_id"));
                System.out.println("From: " + rs.getString("sender_login"));
                System.out.println("To:   " + rs.getString("receiver_login"));
                System.out.println("Msg:  " + rs.getString("content"));
                System.out.println("Date: " + rs.getString("date"));
                printSeparator();
            }

            if (!found) {
                System.out.println("No messages found.");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching messages: " + e.getMessage());
        }
    }

    static void sendMessage(String login) {
        System.out.println();
        System.out.print("Enter receiver login: ");
        String receiverLogin = getScanner().nextLine().trim();

        System.out.print("Enter message: ");
        String content = getScanner().nextLine().trim();

        String insertQuery = """
                INSERT INTO messages (sender_login, receiver_login, content, date)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, login);
            stmt.setString(2, receiverLogin);
            stmt.setString(3, content);
            stmt.setString(4, java.time.LocalDateTime.now().toString());
            stmt.executeUpdate();

            System.out.println("\nMessage sent successfully!\n");

        } catch (SQLException e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    static void viewCourses() {
        String query = """
                SELECT course_id, name, description, credits, teacher_login, max_students
                FROM courses
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            printHeader("ALL COURSES");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:          " + rs.getInt("course_id"));
                System.out.println("Name:        " + rs.getString("name"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Credits:     " + rs.getInt("credits"));
                System.out.println("Teacher:     " + rs.getString("teacher_login"));
                System.out.println("Max Students:" + rs.getInt("max_students"));
                printSeparator();
            }

            if (!found) {
                System.out.println("No courses found.");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching courses: " + e.getMessage());
        }
    }

    static void viewMyRequestsByLogin(String login) {
        String query = """
                SELECT request_id, content, status, request_type
                FROM requests
                WHERE sender_login = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            printHeader("MY REQUESTS");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:      " + rs.getInt("request_id"));
                System.out.println("Type:    " + rs.getString("request_type"));
                System.out.println("Content: " + rs.getString("content"));
                System.out.println("Status:  " + rs.getString("status"));
                printSeparator();
            }

            if (!found) {
                System.out.println("No requests found.");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching requests: " + e.getMessage());
        }
    }

    static void sendNewRequestByLogin(String login) {
        printHeader("SEND NEW REQUEST");
        System.out.println("1. Schedule Request");
        System.out.println("2. Research Request");
        System.out.println("0. Back");
        System.out.println();
        System.out.print("Enter your choice: ");

        String choice = getScanner().nextLine().trim();
        String requestType;

        switch (choice) {
            case "1" -> requestType = "SCHEDULE";
            case "2" -> requestType = "RESEARCH";
            case "0" -> {
                return;
            }
            default -> {
                System.out.println("\nInvalid choice.\n");
                return;
            }
        }

        System.out.print("Enter request content: ");
        String content = getScanner().nextLine().trim();

        String insertQuery = """
                INSERT INTO requests (sender_login, content, status, request_type)
                VALUES (?, ?, 'PENDING', ?)
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, login);
            stmt.setString(2, content);
            stmt.setString(3, requestType);
            stmt.executeUpdate();

            System.out.println("\nRequest sent successfully!\n");

        } catch (SQLException e) {
            System.out.println("Error sending request: " + e.getMessage());
        }
    }

    static void researchProjects(String login) {
        String query = """
                SELECT rp.project_id, rp.title, rp.description, rp.supervisor_login, rp.status
                FROM research_projects rp
                LEFT JOIN project_members pm ON rp.project_id = pm.project_id
                WHERE rp.supervisor_login = ? OR pm.member_login = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);
            stmt.setString(2, login);
            ResultSet rs = stmt.executeQuery();

            System.out.println();
            System.out.println("================================================");
            System.out.println("             RESEARCH PROJECTS");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:         " + rs.getInt("project_id"));
                System.out.println("Title:      " + rs.getString("title"));
                System.out.println("Description:" + rs.getString("description"));
                System.out.println("Supervisor: " + rs.getString("supervisor_login"));
                System.out.println("Status:     " + rs.getString("status"));
                System.out.println("------------------------------------------------");
            }

            if (!found) {
                System.out.println("No research projects found.\n");
                return;
            }

            System.out.print("Enter Project ID to view papers (0 to back): ");
            String choice = getScanner().nextLine().trim();
            if (choice.equals("0"))
                return;

            int projectId;
            try {
                projectId = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid ID.\n");
                return;
            }

            // показываем papers проекта
            String papersQuery = """
                    SELECT paper_id, title, author_login, published_date, citations
                    FROM research_papers
                    WHERE project_id = ?
                    """;

            PreparedStatement papersStmt = conn.prepareStatement(papersQuery);
            papersStmt.setInt(1, projectId);
            ResultSet papersRs = papersStmt.executeQuery();

            System.out.println();
            System.out.println("================================================");
            System.out.println("               RESEARCH PAPERS");
            System.out.println("================================================");
            System.out.println();

            boolean papersFound = false;
            while (papersRs.next()) {
                papersFound = true;
                System.out.println("ID:       " + papersRs.getInt("paper_id"));
                System.out.println("Title:    " + papersRs.getString("title"));
                System.out.println("Author:   " + papersRs.getString("author_login"));
                System.out.println("Date:     " + papersRs.getString("published_date"));
                System.out.println("Citations:" + papersRs.getInt("citations"));
                System.out.println("------------------------------------------------");
            }

            if (!papersFound) {
                System.out.println("No papers found for this project.\n");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching research projects: " + e.getMessage());
        }
    }

    static void publishResearchPaper(String login) {
        // сначала показываем проекты пользователя
        String projectsQuery = """
                SELECT rp.project_id, rp.title
                FROM research_projects rp
                LEFT JOIN project_members pm ON rp.project_id = pm.project_id
                WHERE rp.supervisor_login = ? OR pm.member_login = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(projectsQuery)) {

            stmt.setString(1, login);
            stmt.setString(2, login);
            ResultSet rs = stmt.executeQuery();

            System.out.println();
            System.out.println("================================================");
            System.out.println("           PUBLISH RESEARCH PAPER");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID: " + rs.getInt("project_id") + " | " + rs.getString("title"));
            }

            if (!found) {
                System.out.println("No research projects found.\n");
                return;
            }

            System.out.println();
            System.out.print("Enter Project ID: ");
            int projectId;
            try {
                projectId = Integer.parseInt(getScanner().nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid ID.\n");
                return;
            }

            System.out.print("Enter Title: ");
            String title = getScanner().nextLine().trim();

            System.out.print("Enter Content: ");
            String content = getScanner().nextLine().trim();

            System.out.print("Enter Keywords (comma separated): ");
            String keywords = getScanner().nextLine().trim();

            System.out.print("Enter Metrics: ");
            String metrics = getScanner().nextLine().trim();

            System.out.print("Enter Figures (comma separated): ");
            String figures = getScanner().nextLine().trim();

            String insertQuery = """
                    INSERT INTO research_papers (title, content, author_login, project_id, published_date, citations, keywords, metrics, figures)
                    VALUES (?, ?, ?, ?, ?, 0, ?, ?, ?)
                    """;

            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, title);
            insertStmt.setString(2, content);
            insertStmt.setString(3, login);
            insertStmt.setInt(4, projectId);
            insertStmt.setString(5, java.time.LocalDateTime.now().toString());
            insertStmt.setString(6, keywords);
            insertStmt.setString(7, metrics);
            insertStmt.setString(8, figures);
            insertStmt.executeUpdate();

            System.out.println("\nResearch paper published successfully!\n");

        } catch (SQLException e) {
            System.out.println("Error publishing paper: " + e.getMessage());
        }
    }

    // ================================================================== VALIDATION
    // HELPERS

    static int validateIntInput(String input) {
        try {
            int value = Integer.parseInt(input);
            return value;
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    public static boolean entityExists(String table, String column, Object value) {
        String query = "SELECT 1 FROM " + table + " WHERE " + column + " = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            if (value instanceof Integer) {
                stmt.setInt(1, (Integer) value);
            } else if (value instanceof String) {
                stmt.setString(1, (String) value);
            }

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

}
