package kz.uapi.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;

import kz.uapi.database.DatabaseManager;
import kz.uapi.users.Employee.Teacher;

public class TeacherMenu {

    static Scanner scanner = Main.getScanner();

    // ================================================================== TEACHER
    static void showTeacherMenu(Teacher teacher) {
        while (true) {
            StringBuilder menu = new StringBuilder("""
                    ================================================
                    TEACHER MENU
                    ================================================

                    1. View Courses
                    2. Teacher's Schedule
                    3. Teacher's Requests
                    4. View Personal Info
                    5. View Student Info
                    6. Open Journal
                    7. Send Messages
                    8. News
                    """);

            if (teacher.isResearcher()) {
                menu.append("9. Research Projects\n");
                menu.append("10. Publish Research Paper\n");
            }

            menu.append("0. Exit\n\nEnter your choice: ");
            System.out.print(menu);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> CommonMethods.viewCourses();
                case "2" -> teacherSchedule(teacher);
                case "3" -> teacherRequests(teacher);
                case "4" -> CommonMethods.viewPersonalInfo(teacher.getLogin());
                case "5" -> viewStudentInfo();
                case "6" -> openJournal(teacher);
                case "7" -> CommonMethods.sendMessages(teacher.getLogin());
                case "8" -> CommonMethods.viewNews();
                case "9" -> {
                    if (teacher.isResearcher())
                        CommonMethods.researchProjects(teacher.getLogin());
                }
                case "10" -> {
                    if (teacher.isResearcher())
                        CommonMethods.publishResearchPaper(teacher.getLogin());
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

    static void teacherSchedule(Teacher teacher) {
        String query = """
                SELECT c.name, ss.slot
                FROM schedule_slots ss
                JOIN schedules s ON ss.schedule_id = s.schedule_id
                JOIN courses c ON c.teacher_login = ?
                WHERE s.student_login IN (
                    SELECT fs.student_login
                    FROM faculty_students fs
                    JOIN faculty_courses fc ON fs.faculty_id = fc.faculty_id
                    WHERE fc.course_id = c.course_id
                )
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, teacher.getLogin());
            ResultSet rs = stmt.executeQuery();

            CommonMethods.printHeader("TEACHER'S SCHEDULE");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Course: " + rs.getString("name"));
                System.out.println("Slot:   " + rs.getString("slot"));
                CommonMethods.printSeparator();
            }

            if (!found) {
                System.out.println("No schedule found.");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching schedule: " + e.getMessage());
        }
    }

    static void teacherRequests(Teacher teacher) {
        System.out.print("""

                ================================================
                            TEACHER'S REQUESTS
                ================================================

                1. View My Requests
                2. Send New Request
                0. Back

                Enter your choice:\s""");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> CommonMethods.viewMyRequestsByLogin(teacher.getLogin());
            case "2" -> CommonMethods.sendNewRequestByLogin(teacher.getLogin());
            case "0" -> {
                return;
            }
            default -> System.out.println("\nInvalid choice.\n");
        }
    }

    static void viewStudentInfo() {
        System.out.println();
        System.out.print("Enter Student login: ");
        String studentLogin = scanner.nextLine().trim();

        String query = """
                SELECT u.first_name, u.last_name, s.gpa, s.year, s.major, s.credits, s.is_researcher
                FROM users u
                JOIN students s ON u.login = s.login
                WHERE u.login = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentLogin);
            ResultSet rs = stmt.executeQuery();

            CommonMethods.printHeader("VIEW STUDENT INFO");

            if (rs.next()) {
                System.out.println("Name:         " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("GPA:          " + rs.getDouble("gpa"));
                System.out.println("Year:         " + rs.getInt("year"));
                System.out.println("Major:        " + rs.getString("major"));
                System.out.println("Credits:      " + rs.getInt("credits"));
                System.out.println("Researcher:   " + (rs.getInt("is_researcher") == 1 ? "Yes" : "No"));
                System.out.println();
            } else {
                System.out.println("Student not found.\n");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching student info: " + e.getMessage());
        }
    }

    static void openJournal(Teacher teacher) {
        viewTeacherCourses(teacher);

        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine().trim();

        String query = """
                SELECT u.login, u.first_name, u.last_name, m.grade, m.date
                FROM faculty_students fs
                JOIN users u ON fs.student_login = u.login
                JOIN faculty_courses fc ON fs.faculty_id = fc.faculty_id
                LEFT JOIN marks m ON m.student_login = u.login AND m.course_id = ?
                WHERE fc.course_id = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, courseId);
            stmt.setString(2, courseId);
            ResultSet rs = stmt.executeQuery();

            CommonMethods.printHeader("JOURNAL");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Student: " + rs.getString("first_name") + " " + rs.getString("last_name") +
                        " (" + rs.getString("login") + ")");
                System.out.println("Grade:   " + (rs.getString("grade") == null ? "No grade" : rs.getString("grade")));
                System.out.println("Date:    " + (rs.getString("date") == null ? "-" : rs.getString("date")));
                CommonMethods.printSeparator();
            }

            if (!found) {
                System.out.println("No students found.");
                return;
            }

            System.out.println();
            System.out.print("""
                    1. Put Grade
                    0. Back

                    Enter your choice:\s""");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("Enter Student login: ");
                String studentLogin = scanner.nextLine().trim();

                System.out.print("Enter Grade: ");
                double grade;
                try {
                    grade = Double.parseDouble(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid grade.\n");
                    return;
                }

                String upsertQuery = """
                        INSERT INTO marks (student_login, course_id, grade, date)
                        VALUES (?, ?, ?, ?)
                        ON CONFLICT(student_login, course_id) DO UPDATE SET grade = ?, date = ?
                        """;

                String now = LocalDateTime.now().toString();
                PreparedStatement upsertStmt = conn.prepareStatement(upsertQuery);
                upsertStmt.setString(1, studentLogin);
                upsertStmt.setString(2, courseId);
                upsertStmt.setDouble(3, grade);
                upsertStmt.setString(4, now);
                upsertStmt.setDouble(5, grade);
                upsertStmt.setString(6, now);
                upsertStmt.executeUpdate();

                System.out.println("\nGrade saved successfully!\n");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewTeacherCourses(Teacher teacher) {
        String query = """
                SELECT course_id, name, description, credits, max_students
                FROM courses
                WHERE teacher_login = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, teacher.getLogin());
            ResultSet rs = stmt.executeQuery();

            CommonMethods.printHeader("YOUR COURSES");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:          " + rs.getInt("course_id"));
                System.out.println("Name:        " + rs.getString("name"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Credits:     " + rs.getInt("credits"));
                System.out.println("Max Students:" + rs.getInt("max_students"));
                CommonMethods.printSeparator();
            }

            if (!found) {
                System.out.println("No courses found.");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching courses: " + e.getMessage());
        }
    }

}
