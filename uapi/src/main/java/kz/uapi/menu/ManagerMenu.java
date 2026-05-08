package kz.uapi.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;

import kz.uapi.database.DatabaseManager;
import kz.uapi.enums.ManagerType;
import kz.uapi.users.Employee.Manager;

public class ManagerMenu {

    static Scanner scanner = Main.getScanner();

    // ================================================================== MANAGER
    static void showManagerMenu(Manager manager) {
        ManagerType type = manager.getManagerType();
        while (true) {
            StringBuilder menu = new StringBuilder("""
                    ================================================
                    MANAGER MENU
                    ================================================

                    1. View Courses
                    2. Requests
                    3. View Personal Info
                    4. View User Info
                    5. Messages
                    6. News
                    """);

            switch (type) {
                case OR -> menu.append("7. Registration Requests\n");
                case ACADEMIC -> menu.append("7. Schedule Requests\n");
                case STUDENT -> menu.append("7. Events & News Management\n");
                case HR -> menu.append("7. Add/Drop User\n");
                case FINANCE -> menu.append("7. Salaries & Scholarships\n");
            }

            menu.append("0. Exit\n\nEnter your choice: ");
            System.out.print(menu);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> CommonMethods.viewCourses();
                case "2" -> viewRequests();
                case "3" -> CommonMethods.viewPersonalInfo(manager.getLogin());
                case "4" -> viewUserInfo();
                case "5" -> CommonMethods.sendMessages(manager.getLogin());
                case "6" -> CommonMethods.viewNews();
                case "7" -> handleManagerSpecific(type);
                case "0" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

    static void handleManagerSpecific(ManagerType type) {
        switch (type) {
            case OR -> registrationRequests();
            case ACADEMIC -> scheduleRequests();
            case STUDENT -> manageEventsAndNews();
            case HR -> addDropUser();
            case FINANCE -> salariesAndScholarships();
        }
    }

    // ================================================================== REQUEST
    // HANDLING
    // ================================================================== REQUEST
    // APPROVAL (CONSOLIDATED)
    static void registrationRequests() {
        String query = """
                SELECT request_id, sender_login, action, status
                FROM requests
                WHERE request_type = 'REGISTRATION' AND status = 'PENDING'
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            CommonMethods.printHeader("REGISTRATION REQUESTS");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:      " + rs.getInt("request_id"));
                System.out.println("From:    " + rs.getString("sender_login"));
                System.out.println("Action:  " + rs.getString("action"));
                System.out.println("Status:  " + rs.getString("status"));
                CommonMethods.printSeparator();
            }

            if (!found) {
                System.out.println("No pending registration requests.\n");
                return;
            }

            System.out.print("""

                    1. Approve Request
                    2. Reject Request
                    0. Back

                    Enter your choice:\s""");

            String choice = scanner.nextLine().trim();
            if (choice.equals("0"))
                return;

            System.out.print("Enter Request ID: ");
            int requestId;
            try {
                requestId = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid ID.\n");
                return;
            }

            String newStatus = choice.equals("1") ? "APPROVED" : "REJECTED";

            if (approveRequest(conn, requestId, "REGISTRATION", newStatus)) {
                System.out.println("\nRequest " + newStatus + " successfully!\n");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void scheduleRequests() {
        String query = """
                SELECT request_id, sender_login, action, status
                FROM requests
                WHERE request_type = 'SCHEDULE' AND status = 'PENDING'
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            CommonMethods.printHeader("SCHEDULE REQUESTS");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:      " + rs.getInt("request_id"));
                System.out.println("From:    " + rs.getString("sender_login"));
                System.out.println("Action:  " + rs.getString("action"));
                System.out.println("Status:  " + rs.getString("status"));
                CommonMethods.printSeparator();
            }

            if (!found) {
                System.out.println("No pending schedule requests.\n");
                return;
            }

            System.out.print("""
                    1. Approve Request
                    2. Reject Request
                    0. Back

                    Enter your choice:\s""");

            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) {
                return;
            }

            if (!choice.equals("1") && !choice.equals("2")) {
                System.out.println("\nInvalid choice.\n");
                return;
            }

            System.out.print("Enter Request ID: ");
            int requestId;
            try {
                requestId = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid ID.\n");
                return;
            }

            String newStatus = choice.equals("1") ? "APPROVED" : "REJECTED";

            if (approveRequest(conn, requestId, "SCHEDULE", newStatus)) {
                System.out.println("\nRequest " + newStatus + " successfully!\n");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static boolean approveRequest(Connection conn, int requestId, String requestType, String newStatus) {
        try {
            String lookupQuery = "SELECT sender_login, course_id, action FROM requests WHERE request_id = ?";
            try (PreparedStatement lookupStmt = conn.prepareStatement(lookupQuery)) {
                lookupStmt.setInt(1, requestId);
                ResultSet lookupRs = lookupStmt.executeQuery();

                if (!lookupRs.next()) {
                    System.out.println("\nRequest ID not found.\n");
                    return false;
                }

                String senderLogin = lookupRs.getString("sender_login");
                int courseId = lookupRs.getInt("course_id");
                String action = lookupRs.getString("action");

                String updateQuery = "UPDATE requests SET status = ? WHERE request_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, newStatus);
                    updateStmt.setInt(2, requestId);
                    int updated = updateStmt.executeUpdate();

                    if (updated == 0) {
                        System.out.println("\nRequest ID not found.\n");
                        return false;
                    }
                }

                if (newStatus.equals("APPROVED")) {
                    if (requestType.equals("REGISTRATION")) {
                        handleRegistrationApproval(conn, senderLogin, courseId, action);
                    } else if (requestType.equals("SCHEDULE")) {
                        handleScheduleApproval(conn, senderLogin, requestId, courseId, action);
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    private static void handleRegistrationApproval(Connection conn, String studentLogin, int course_id, String act) {
        try {
            if (act.equals("ADD")) {
                // 1. Добавить в faculty_students
                String getFacultyQuery = "SELECT faculty_id FROM faculty_courses WHERE course_id = ?";
                PreparedStatement getFaculty = conn.prepareStatement(getFacultyQuery);
                getFaculty.setInt(1, course_id);
                ResultSet facultyRs = getFaculty.executeQuery();

                if (facultyRs.next()) {
                    int facultyId = facultyRs.getInt("faculty_id");
                    String insertFacultyStudent = """
                            INSERT OR IGNORE INTO faculty_students (faculty_id, student_login)
                            VALUES (?, ?)
                            """;
                    PreparedStatement insertStmt2 = conn.prepareStatement(insertFacultyStudent);
                    insertStmt2.setInt(1, facultyId);
                    insertStmt2.setString(2, studentLogin);
                    insertStmt2.executeUpdate();
                }

                // 2. Добавить в student_courses
                String insertStudentCourse = """
                        INSERT OR IGNORE INTO student_courses (student_login, course_id)
                        VALUES (?, ?)
                        """;
                PreparedStatement insertStmt = conn.prepareStatement(insertStudentCourse);
                insertStmt.setString(1, studentLogin);
                insertStmt.setInt(2, course_id);
                insertStmt.executeUpdate();

            } else if (act.equals("DROP")) {
                // 1. Удалить из student_courses
                String deleteStudentCourse = """
                        DELETE FROM student_courses WHERE student_login = ? AND course_id = ?
                        """;
                PreparedStatement deleteStmt = conn.prepareStatement(deleteStudentCourse);
                deleteStmt.setString(1, studentLogin);
                deleteStmt.setInt(2, course_id);
                deleteStmt.executeUpdate();

                // 2. Проверить — остались ли ещё курсы студента в этом факультете
                String checkQuery = """
                        SELECT COUNT(*) FROM student_courses sc
                        JOIN faculty_courses fc ON sc.course_id = fc.course_id
                        WHERE sc.student_login = ?
                        AND fc.faculty_id = (SELECT faculty_id FROM faculty_courses WHERE course_id = ?)
                        """;
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, studentLogin);
                checkStmt.setInt(2, course_id);
                ResultSet checkRs = checkStmt.executeQuery();

                if (checkRs.next() && checkRs.getInt(1) == 0) {
                    // Курсов в этом факультете больше нет — удаляем из faculty_students
                    String getFacultyQuery = "SELECT faculty_id FROM faculty_courses WHERE course_id = ?";
                    PreparedStatement getFaculty = conn.prepareStatement(getFacultyQuery);
                    getFaculty.setInt(1, course_id);
                    ResultSet facultyRs = getFaculty.executeQuery();

                    if (facultyRs.next()) {
                        String deleteFacultyStudent = """
                                DELETE FROM faculty_students WHERE faculty_id = ? AND student_login = ?
                                """;
                        PreparedStatement deleteStmt2 = conn.prepareStatement(deleteFacultyStudent);
                        deleteStmt2.setInt(1, facultyRs.getInt("faculty_id"));
                        deleteStmt2.setString(2, studentLogin);
                        deleteStmt2.executeUpdate();
                    }
                }

            }
        } catch (SQLException e) {
            System.out.println("Error processing registration approval: " + e.getMessage());
        }
    }

    private static void handleScheduleApproval(Connection conn, String receiverLogin, int requestId, int courseId,
            String act) {
        String insertMessage = """
                INSERT INTO messages (sender_login, receiver_login, content, date)
                VALUES ('SYSTEM', ?, ?, ?)
                """;

        try (PreparedStatement msgStmt = conn.prepareStatement(insertMessage)) {
            msgStmt.setString(1, receiverLogin);
            msgStmt.setString(2,
                    "Your schedule request #" + requestId + " has been approved.\nRequest details: " + "courseId = "
                            + courseId + ", action = " + act);
            msgStmt.setString(3, java.time.LocalDateTime.now().toString());
            msgStmt.executeUpdate();
        } catch (SQLException e) {
            // Ignore notification failure if messages table or SYSTEM sender is not
            // available
        }
    }

    // ================================================================== REQUEST
    static void viewRequests() {
        String query = """
                SELECT r.request_id, r.sender_login, u.first_name, u.last_name,
                       r.course_id, r.status, r.request_type, r.action
                FROM requests r
                JOIN users u ON r.sender_login = u.login
                ORDER BY r.request_id DESC
                """;
        try (
                Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n========== REQUESTS ==========");
            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                System.out.printf("ID: %d | User: %s %s (%s) | Course ID: %d | Type: %s | Action: %s | Status: %s%n",
                        rs.getInt("request_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("sender_login"),
                        rs.getInt("course_id"),
                        rs.getString("request_type"),
                        rs.getString("action"),
                        rs.getString("status"));
            }
            if (!hasRows)
                System.out.println("No requests found.");
            System.out.println("==============================\n");

        } catch (SQLException e) {
            System.out.println("Error loading requests: " + e.getMessage());
        }
    }

    static void viewUserInfo() {
        System.out.print("Enter user login: ");
        String userLogin = scanner.nextLine().trim();

        String query = """
                SELECT login, first_name, last_name, role, created_at
                FROM users
                WHERE login = ?
                """;
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userLogin);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n========== USER INFO ==========");
                System.out.println("Login     : " + rs.getString("login"));
                System.out.println("Name      : " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Role      : " + rs.getString("role"));
                System.out.println("Created at: " + rs.getString("created_at"));
                System.out.println("===============================\n");
            } else {
                System.out.println("User not found: " + userLogin);
            }

        } catch (SQLException e) {
            System.out.println("Error loading user info: " + e.getMessage());
        }
    } // ================================================================== EVENTS &
      // NEWS MANAGEMENT

    static void manageEventsAndNews() {
        while (true) {
            System.out.print("""
                    ================================================
                    EVENTS & NEWS MANAGEMENT
                    ================================================

                    1. View News
                    2. Add News
                    3. Delete News
                    0. Back

                    Enter your choice:\s""");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> CommonMethods.viewNews();
                case "2" -> addNews();
                case "3" -> deleteNews();
                case "0" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

    private static void addNews() {
        System.out.print("Enter news title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Enter news content: ");
        String content = scanner.nextLine().trim();

        String insertQuery = """
                INSERT INTO news (title, content, date, author_login)
                VALUES (?, ?, ?, 'SYSTEM')
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setString(3, LocalDateTime.now().toString());
            stmt.executeUpdate();

            System.out.println("\nNews item added successfully!\n");
        } catch (SQLException e) {
            System.out.println("Error adding news: " + e.getMessage());
        }
    }

    private static void deleteNews() {
        System.out.print("Enter news ID to delete: ");
        int newsId;
        try {
            newsId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid ID.\n");
            return;
        }

        String deleteQuery = "DELETE FROM news WHERE news_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            stmt.setInt(1, newsId);
            int removed = stmt.executeUpdate();
            if (removed > 0) {
                System.out.println("\nNews item deleted successfully.\n");
            } else {
                System.out.println("\nNews item not found.\n");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting news: " + e.getMessage());
        }
    }

    // ================================================================== ADD/DROP
    // USER

    static void addDropUser() {
        while (true) {
            System.out.print("""
                    ================================================
                    ADD / DROP USER
                    ================================================

                    1. Add User
                    2. Remove User
                    3. List Users
                    0. Back

                    Enter your choice:\s""");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addUser();
                case "2" -> removeUser();
                case "3" -> listUsers();
                case "0" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

    private static void addUser() {
        System.out.print("Enter login: ");
        String login = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Enter role (STUDENT, TEACHER, MANAGER, ADMIN, RESEARCHER): ");
        String role = scanner.nextLine().trim().toUpperCase();

        if (!(role.equals("STUDENT") || role.equals("TEACHER") || role.equals("MANAGER") || role.equals("ADMIN")
                || role.equals("RESEARCHER"))) {
            System.out.println("\nInvalid role.\n");
            return;
        }

        String createdAt = LocalDateTime.now().toString();
        String insertUser = """
                INSERT INTO users (login, password, first_name, last_name, role, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertUser)) {

            stmt.setString(1, login);
            stmt.setString(2, password);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, role);
            stmt.setString(6, createdAt);
            stmt.executeUpdate();

            if (role.equals("STUDENT")) {
                String insertStudent = """
                        INSERT INTO students (login, gpa, year, major, credits, is_researcher)
                        VALUES (?, 0.0, 1, 'Undeclared', 0, 0)
                        """;
                try (PreparedStatement studentStmt = conn.prepareStatement(insertStudent)) {
                    studentStmt.setString(1, login);
                    studentStmt.executeUpdate();
                }
            }

            if (role.equals("TEACHER") || role.equals("MANAGER") || role.equals("RESEARCHER")) {
                System.out.print("Enter department: ");
                String department = scanner.nextLine().trim();

                String insertEmployee = "INSERT INTO employees (login, department) VALUES (?, ?)";
                try (PreparedStatement empStmt = conn.prepareStatement(insertEmployee)) {
                    empStmt.setString(1, login);
                    empStmt.setString(2, department);
                    empStmt.executeUpdate();
                }

                if (role.equals("TEACHER")) {
                    String insertTeacher = """
                            INSERT INTO teachers (login, title, rating, h_index, specializations)
                            VALUES (?, 'Instructor', 0.0, 0, '')
                            """;
                    try (PreparedStatement teacherStmt = conn.prepareStatement(insertTeacher)) {
                        teacherStmt.setString(1, login);
                        teacherStmt.executeUpdate();
                    }
                }

                if (role.equals("MANAGER")) {
                    String insertManager = "INSERT INTO managers (login, manager_type) VALUES (?, 'OR')";
                    try (PreparedStatement managerStmt = conn.prepareStatement(insertManager)) {
                        managerStmt.setString(1, login);
                        managerStmt.executeUpdate();
                    }
                }

                if (role.equals("RESEARCHER")) {
                    String insertResearcher = """
                            INSERT INTO researcher_employees (login, h_index, specializations)
                            VALUES (?, 0, '')
                            """;
                    try (PreparedStatement resStmt = conn.prepareStatement(insertResearcher)) {
                        resStmt.setString(1, login);
                        resStmt.executeUpdate();
                    }
                }
            }

            if (role.equals("ADMIN")) {
                String insertAdmin = "INSERT INTO admins (login, access_level) VALUES (?, 1)";
                try (PreparedStatement adminStmt = conn.prepareStatement(insertAdmin)) {
                    adminStmt.setString(1, login);
                    adminStmt.executeUpdate();
                }
            }

            System.out.println("\nUser added successfully.\n");
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
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

    private static void deleteUserByLogin(String login) {
        String[] queries = {
                "DELETE FROM messages WHERE sender_login = ? OR receiver_login = ?",
                "DELETE FROM requests WHERE sender_login = ?",
                "DELETE FROM faculty_students WHERE student_login = ?",
                "DELETE FROM student_courses WHERE student_login = ?",
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

    // ================================================================== SALARIES &
    // SCHOLARSHIPS

    static void salariesAndScholarships() {
        while (true) {
            System.out.print("""
                    ================================================
                    SALARIES & SCHOLARSHIPS
                    ================================================

                    1. View Salaries
                    2. Update Salary
                    3. View Scholarships
                    4. Update Scholarship
                    0. Back

                    Enter your choice:\s""");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewSalaries();
                case "2" -> updateSalary();
                case "3" -> viewScholarships();
                case "4" -> updateScholarship();
                case "0" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

    private static void viewSalaries() {
        String[] salaryQueries = {
                "SELECT login, salary FROM employees ORDER BY login",
                "SELECT login, amount AS salary FROM employee_salaries ORDER BY login",
                "SELECT employee_login AS login, amount AS salary FROM salaries ORDER BY login"
        };

        try (Connection conn = DatabaseManager.getConnection()) {
            for (String query : salaryQueries) {
                try (PreparedStatement stmt = conn.prepareStatement(query);
                        ResultSet rs = stmt.executeQuery()) {

                    CommonMethods.printHeader("SALARIES");

                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        System.out.println("Login:  " + rs.getString("login"));
                        System.out.println("Salary: " + rs.getDouble("salary"));
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

            System.out.println("\nNo salary information available.\n");
        } catch (SQLException e) {
            System.out.println("Error fetching salaries: " + e.getMessage());
        }
    }

    private static void updateSalary() {
        System.out.print("Enter employee login: ");
        String login = scanner.nextLine().trim();

        System.out.print("Enter salary amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid salary amount.\n");
            return;
        }

        String[] updateQueries = {
                "UPDATE employees SET salary = ? WHERE login = ?",
                "UPDATE employee_salaries SET amount = ? WHERE login = ?",
                "UPDATE salaries SET amount = ? WHERE employee_login = ?"
        };

        try (Connection conn = DatabaseManager.getConnection()) {
            boolean updated = false;
            for (String query : updateQueries) {
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setDouble(1, amount);
                    stmt.setString(2, login);
                    if (stmt.executeUpdate() > 0) {
                        updated = true;
                        break;
                    }
                } catch (SQLException ignored) {
                }
            }

            if (!updated) {
                System.out.println("\nUnable to update salary: employee or salary table not found.\n");
            } else {
                System.out.println("\nSalary updated successfully.\n");
            }
        } catch (SQLException e) {
            System.out.println("Error updating salary: " + e.getMessage());
        }
    }

    private static void viewScholarships() {
        System.out.println("\nScholarship management is not available in the current system.\n");
    }

    private static void updateScholarship() {
        System.out.println("\nScholarship management is not available in the current system.\n");
    }

}
