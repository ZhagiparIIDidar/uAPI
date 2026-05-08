package kz.uapi.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import kz.uapi.database.DatabaseManager;
import kz.uapi.users.Student;

public class StudentMenu {

    static Scanner scanner = Main.getScanner();

    // ================================================================== STUDENT
    static void showStudentMenu(Student student) {
        while (true) {
            StringBuilder menu = new StringBuilder("""
                    ================================================
                    STUDENT MENU
                    ================================================

                    1. View All Courses
                    1.5. View My Courses
                    2. Add/Drop Course
                    3. View Attestation
                    4. View Transcript
                    5. Questionnaire
                    6. Student's Schedule
                    7. Student's Requests
                    8. Student's Exam Schedule
                    9. View Teacher Info
                    10. News
                    11. View messages
                    """);

            if (student.isResearcher()) {
                menu.append("12. Research Projects\n");
                menu.append("13. Publish Research Paper\n");
            }

            menu.append("0. Exit\n\nEnter your choice: ");
            System.out.print(menu);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> CommonMethods.viewCourses();
                case "1.5", "1,5" -> viewMyCourses(student.getLogin());
                case "2" -> addDropCourse(student);
                case "3" -> viewAttestation(student.getLogin());
                case "4" -> viewTranscript(student.getLogin());
                case "5" -> questionnaire(student);
                case "6" -> studentSchedule();
                case "7" -> studentRequests(student);
                case "8" -> examSchedule(student);
                case "9" -> viewTeacherInfo();
                case "10" -> CommonMethods.viewNews();
                case "11" -> CommonMethods.viewMessages(student.getLogin());
                case "12" -> {
                    if (student.isResearcher())
                        CommonMethods.researchProjects(student.getLogin());
                }
                case "13" -> {
                    if (student.isResearcher())
                        CommonMethods.publishResearchPaper(student.getLogin());
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

    static void addDropCourse(Student student) {
        System.out.print("""

                ================================================
                              ADD/DROP COURSE
                ================================================

                1. Add Course
                2. Drop Course
                0. Back

                Enter your choice:\s""");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> addCourse(student);
            case "2" -> dropCourse(student);
            case "0" -> {
                return;
            }
            default -> System.out.println("\nInvalid choice.\n");
        }
    }

    static void addCourse(Student student) {
        CommonMethods.viewCourses();

        System.out.print("Enter Course ID to add: ");
        int courseId = CommonMethods.validateIntInput(scanner.nextLine().trim());
        if (!CommonMethods.entityExists("courses", "course_id", courseId)) {
            System.out.println("\nCourse not found.\n");
            return;
        }
        String insertRequest = """
                INSERT INTO requests (sender_login, course_id, status, request_type, action)
                VALUES (?, ?, 'PENDING', 'REGISTRATION', 'ADD')
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertRequest)) {

            stmt.setString(1, student.getLogin());
            stmt.setInt(2, courseId);
            stmt.executeUpdate();

            System.out.println("\nRequest sent successfully. Waiting for approval.\n");

        } catch (SQLException e) {
            System.out.println("Error sending request: " + e.getMessage());
        }
    }

    static void dropCourse(Student student) {
        CommonMethods.viewCourses();

        System.out.print("Enter Course ID to drop: ");
        int courseId = CommonMethods.validateIntInput(scanner.nextLine().trim());
        if (!CommonMethods.entityExists("courses", "course_id", courseId)) {
            System.out.println("\nCourse not found.\n");
            return;
        }
        String insertRequest = """
                INSERT INTO requests (sender_login, course_id, status, request_type, action)
                VALUES (?, ?, 'PENDING', 'REGISTRATION', 'DROP')
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertRequest)) {

            stmt.setString(1, student.getLogin());
            stmt.setInt(2, courseId);
            stmt.executeUpdate();

            System.out.println("\nRequest sent successfully. Waiting for approval.\n");

        } catch (SQLException e) {
            System.out.println("Error sending request: " + e.getMessage());
        }
    }

    static void viewMyCourses(String studentLogin) {
        String query = """
                SELECT c.course_id, c.name, c.description, c.credits, c.teacher_login
                FROM courses c
                JOIN student_courses sc ON c.course_id = sc.course_id
                WHERE sc.student_login = ?
                ORDER BY c.course_id
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentLogin);
            ResultSet rs = stmt.executeQuery();

            CommonMethods.printHeader("MY COURSES");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Course ID:  " + rs.getInt("course_id"));
                System.out.println("Name:       " + rs.getString("name"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Credits:    " + rs.getInt("credits"));
                System.out.println("Teacher:    " + rs.getString("teacher_login"));
                CommonMethods.printSeparator();
            }

            if (!found) {
                System.out.println("You are not enrolled in any courses.");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching your courses: " + e.getMessage());
        }
    }

    static void viewAttestation(String studentLogin) {
        String query = """
                SELECT c.name, a.att1, a.att2, a.final
                FROM attestations a
                JOIN courses c ON a.course_id = c.course_id
                WHERE a.student_login = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentLogin);
            ResultSet rs = stmt.executeQuery();

            CommonMethods.printHeader("VIEW ATTESTATION");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Course:  " + rs.getString("name"));
                System.out.println("Att 1:   " + rs.getDouble("att1"));
                System.out.println("Att 2:   " + rs.getDouble("att2"));
                System.out.println("Final:   " + rs.getDouble("final"));
                CommonMethods.printSeparator();
            }

            if (!found) {
                System.out.println("No attestations found.");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching attestations: " + e.getMessage());
        }
    }

    static void viewTranscript(String studentLogin) {
        String query = """
                SELECT c.name, m.grade, m.date
                FROM marks m
                JOIN courses c ON m.course_id = c.course_id
                WHERE m.student_login = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentLogin);
            ResultSet rs = stmt.executeQuery();

            CommonMethods.printHeader("VIEW TRANSCRIPT");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Course: " + rs.getString("name"));
                System.out.println("Grade:  " + rs.getDouble("grade"));
                System.out.println("Date:   " + rs.getString("date"));
                CommonMethods.printSeparator();
            }

            if (!found) {
                System.out.println("No transcript found.");
            }

            String gpaQuery = "SELECT gpa FROM transcripts WHERE student_login = ?";
            PreparedStatement gpaStmt = conn.prepareStatement(gpaQuery);
            gpaStmt.setString(1, studentLogin);
            ResultSet gpaRs = gpaStmt.executeQuery();
            if (gpaRs.next()) {
                System.out.println("GPA: " + gpaRs.getDouble("gpa"));
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching transcript: " + e.getMessage());
        }
    }

    static void questionnaire(Student student) {
        CommonMethods.printHeader("QUESTIONNAIRE");

        String query = """
                SELECT DISTINCT c.course_id, c.name, c.teacher_login
                FROM courses c
                JOIN faculty_courses fc ON c.course_id = fc.course_id
                JOIN faculty_students fs ON fc.faculty_id = fs.faculty_id
                WHERE fs.student_login = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, student.getLogin());
            ResultSet rs = stmt.executeQuery();

            System.out.println("Your courses:");
            System.out.println();
            while (rs.next()) {
                System.out.println("Course ID: " + rs.getInt("course_id") +
                        " | " + rs.getString("name") +
                        " | Teacher: " + rs.getString("teacher_login"));
            }

            System.out.println();
            System.out.print("Enter Teacher login to rate: ");
            String teacherLogin = scanner.nextLine().trim();

            System.out.print("Enter rating (1-5): ");
            int rating;
            try {
                rating = Integer.parseInt(scanner.nextLine().trim());
                if (rating < 1 || rating > 5) {
                    System.out.println("\nRating must be between 1 and 5.\n");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid rating.\n");
                return;
            }

            String insertQuery = """
                    INSERT INTO questionnaires (student_login, teacher_login, rating)
                    VALUES (?, ?, ?)
                    """;
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, student.getLogin());
            insertStmt.setString(2, teacherLogin);
            insertStmt.setInt(3, rating);
            insertStmt.executeUpdate();

            String avgQuery = "SELECT AVG(rating) as avg_rating FROM questionnaires WHERE teacher_login = ?";
            PreparedStatement avgStmt = conn.prepareStatement(avgQuery);
            avgStmt.setString(1, teacherLogin);
            ResultSet avgRs = avgStmt.executeQuery();

            if (avgRs.next()) {
                double avgRating = avgRs.getDouble("avg_rating");
                String updateQuery = "UPDATE teachers SET rating = ? WHERE login = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setDouble(1, avgRating);
                updateStmt.setString(2, teacherLogin);
                updateStmt.executeUpdate();
            }

            System.out.println("\nThank you for your feedback!\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void studentSchedule() {
        System.out.println("\nStudent's Schedule feature coming soon...\n");
    }

    static void studentRequests(Student student) {
        System.out.print("""

                ================================================
                            STUDENT'S REQUESTS
                ================================================

                1. View My Requests
                2. Send New Request
                0. Back

                Enter your choice:\s""");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> CommonMethods.viewMyRequestsByLogin(student.getLogin());
            case "2" -> CommonMethods.sendNewRequestByLogin(student.getLogin());
            case "0" -> {
                return;
            }
            default -> System.out.println("\nInvalid choice.\n");
        }
    }

    static void examSchedule(Student student) {
        String query = """
                SELECT c.name, e.date, e.classroom
                FROM exam_schedule e
                JOIN courses c ON e.course_id = c.course_id
                JOIN faculty_courses fc ON c.course_id = fc.course_id
                JOIN faculty_students fs ON fc.faculty_id = fs.faculty_id
                WHERE fs.student_login = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, student.getLogin());
            ResultSet rs = stmt.executeQuery();

            CommonMethods.printHeader("STUDENT'S EXAM SCHEDULE");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Course:    " + rs.getString("name"));
                System.out.println("Date:      " + rs.getString("date"));
                System.out.println("Classroom: " + rs.getString("classroom"));
                CommonMethods.printSeparator();
            }

            if (!found) {
                System.out.println("No exam schedule found.");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching exam schedule: " + e.getMessage());
        }
    }

    static void viewTeacherInfo() {
        CommonMethods.printHeader("VIEW TEACHER INFO");
        System.out.print("Enter Teacher login: ");
        String teacherLogin = scanner.nextLine().trim();

        String query = """
                SELECT u.first_name, u.last_name, t.title, t.rating, t.h_index, t.specializations,
                       e.department
                FROM users u
                JOIN employees e ON u.login = e.login
                JOIN teachers t ON u.login = t.login
                WHERE u.login = ?
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, teacherLogin);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println();
                System.out.println("Name:            " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Title:           " + rs.getString("title"));
                System.out.println("Department:      " + rs.getString("department"));
                System.out.println("Rating:          " + rs.getDouble("rating"));
                System.out.println("H-Index:         " + rs.getInt("h_index"));
                System.out.println("Specializations: " + rs.getString("specializations"));
                System.out.println();
            } else {
                System.out.println("\nTeacher not found.\n");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching teacher info: " + e.getMessage());
        }
    }

}
