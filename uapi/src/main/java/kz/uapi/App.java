package kz.uapi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Scanner;

import kz.uapi.abs_class.*;
import kz.uapi.database.AuthService;
import kz.uapi.database.DatabaseInitializer;
import kz.uapi.database.DatabaseManager;
import kz.uapi.enums.*;
import kz.uapi.users.Student;
import kz.uapi.users.Employee.*;

public class App {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        DatabaseInitializer.insertTestData();
        showMainMenu();
    }

    // ================================================================== MAIN MENU
    static void showMainMenu() {
        while (true) {
            System.out.print("""
                    ================================================
                    UNIVERSITY INFORMATION SYSTEM
                    ================================================

                    1. Login
                    2. Exit

                    Enter your choice:\s""");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> showLoginMenu();
                case "2" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

    // ================================================================== LOGIN MENU
    static void showLoginMenu() {
        System.out.print("""

                ================================================
                                    LOGIN
                ================================================

                Enter login:\s""");
        String login = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        User user = AuthService.login(login, password);

        if (user == null) {
            System.out.println("\nInvalid login or password. Try again.\n");
            return;
        }

        System.out.println("\nWelcome, " + user.getFirstName() + "!\n");

        switch (user.getRole()) {
            case "STUDENT" -> showStudentMenu((Student) user);
            case "TEACHER" -> showTeacherMenu((Teacher) user);
            case "MANAGER" -> showManagerMenu((Manager) user);
            case "ADMIN" -> showAdminMenu((Admin) user);
            case "RESEARCHER" -> showResearcherEmployeeMenu((ResearcherEmployee) user);
            default -> System.out.println("Unknown role.\n");
        }
    }

    // ================================================================== STUDENT
    static void showStudentMenu(Student student) {
        while (true) {
            StringBuilder menu = new StringBuilder("""
                    ================================================
                    STUDENT MENU
                    ================================================

                    1. View Courses
                    2. Add/Drop Course
                    3. View Attestation
                    4. View Transcript
                    5. Questionnaire
                    6. Student's Schedule
                    7. Student's Requests
                    8. Student's Exam Schedule
                    9. View Teacher Info
                    10. News
                    """);

            if (student.isResearcher()) {
                menu.append("11. Research Projects\n");
                menu.append("12. Publish Research Paper\n");
            }

            menu.append("0. Exit\n\nEnter your choice: ");
            System.out.print(menu);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewCourses();
                case "2" -> addDropCourse(student);
                case "3" -> viewAttestation(student.getLogin());
                case "4" -> viewTranscript(student.getLogin());
                case "5" -> questionnaire(student);
                case "6" -> studentSchedule();
                case "7" -> studentRequests(student);
                case "8" -> examSchedule(student);
                case "9" -> viewTeacherInfo();
                case "10" -> viewNews();
                case "11" -> {
                    if (student.isResearcher())
                        researchProjects();
                }
                case "12" -> {
                    if (student.isResearcher())
                        publishResearchPaper();
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

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
                case "1" -> viewCourses();
                case "2" -> teacherSchedule(teacher);
                case "3" -> teacherRequests();
                case "4" -> viewPersonalInfo(teacher.getLogin());
                case "5" -> viewStudentInfo();
                case "6" -> openJournal(teacher);
                case "7" -> sendMessages(teacher.getLogin());
                case "8" -> viewNews();
                case "9" -> {
                    if (teacher.isResearcher())
                        researchProjects();
                }
                case "10" -> {
                    if (teacher.isResearcher())
                        publishResearchPaper();
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }
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
                case "1" -> viewCourses();
                case "2" -> viewRequests();
                case "3" -> viewPersonalInfo(manager.getLogin());
                case "4" -> viewUserInfo();
                case "5" -> sendMessages(manager.getLogin());
                case "6" -> viewNews();
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
                case "1" -> viewPersonalInfo(admin.getLogin());
                case "2" -> sendMessages(admin.getLogin());
                case "3" -> manageUsers();
                case "4" -> viewLogs();
                case "5" -> viewNews();
                case "0" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

    // ==================================================================Researcher
    static void showResearcherEmployeeMenu(ResearcherEmployee researcher) {
        while (true) {
            System.out.print("""
                    ================================================
                    RESEARCHER MENU
                    ================================================

                    1. View Personal Info
                    2. Research Projects
                    3. Publish Research Paper
                    4. Messages
                    5. News
                    0. Exit

                    Enter your choice:\s""");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewPersonalInfo(researcher.getLogin());
                case "2" -> researchProjects();
                case "3" -> publishResearchPaper();
                case "4" -> sendMessages(researcher.getLogin());
                case "5" -> viewNews();
                case "0" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Try again.\n");
            }
        }
    }

    // ==================================================================STUB
    // METHODS
    // These methods need to be implemented

    static void viewCourses() {
        String query = """
                SELECT course_id, name, description, credits, teacher_login, max_students
                FROM courses
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            System.out.println();
            System.out.println("================================================");
            System.out.println("                  ALL COURSES");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:          " + rs.getInt("course_id"));
                System.out.println("Name:        " + rs.getString("name"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Credits:     " + rs.getInt("credits"));
                System.out.println("Teacher:     " + rs.getString("teacher_login"));
                System.out.println("Max Students:" + rs.getInt("max_students"));
                System.out.println("------------------------------------------------");
            }

            if (!found) {
                System.out.println("No courses found.");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching courses: " + e.getMessage());
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
        viewCourses();

        System.out.print("Enter Course ID to add: ");
        String courseId = scanner.nextLine().trim();

        String insertRequest = """
                INSERT INTO requests (sender_login, content, status, request_type)
                VALUES (?, ?, 'PENDING', 'REGISTRATION')
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertRequest)) {

            stmt.setString(1, student.getLogin());
            stmt.setString(2, "ADD course_id=" + courseId);
            stmt.executeUpdate();

            System.out.println("\nRequest sent successfully. Waiting for approval.\n");

        } catch (SQLException e) {
            System.out.println("Error sending request: " + e.getMessage());
        }
    }

    static void dropCourse(Student student) {
        viewCourses();

        System.out.print("Enter Course ID to drop: ");
        String courseId = scanner.nextLine().trim();

        String insertRequest = """
                INSERT INTO requests (sender_login, content, status, request_type)
                VALUES (?, ?, 'PENDING', 'REGISTRATION')
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertRequest)) {

            stmt.setString(1, student.getLogin());
            stmt.setString(2, "DROP course_id=" + courseId);
            stmt.executeUpdate();

            System.out.println("\nRequest sent successfully. Waiting for approval.\n");

        } catch (SQLException e) {
            System.out.println("Error sending request: " + e.getMessage());
        }
    }

    static void addDropCourse() {
        System.out.println("\nAdd/Drop Course feature coming soon...\n");
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

            System.out.println();
            System.out.println("================================================");
            System.out.println("               VIEW ATTESTATION");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Course:  " + rs.getString("name"));
                System.out.println("Att 1:   " + rs.getDouble("att1"));
                System.out.println("Att 2:   " + rs.getDouble("att2"));
                System.out.println("Final:   " + rs.getDouble("final"));
                System.out.println("------------------------------------------------");
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

            System.out.println();
            System.out.println("================================================");
            System.out.println("               VIEW TRANSCRIPT");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Course: " + rs.getString("name"));
                System.out.println("Grade:  " + rs.getDouble("grade"));
                System.out.println("Date:   " + rs.getString("date"));
                System.out.println("------------------------------------------------");
            }

            if (!found) {
                System.out.println("No transcript found.");
            }

            // GPA
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
        System.out.println();
        System.out.println("================================================");
        System.out.println("               QUESTIONNAIRE");
        System.out.println("================================================");
        System.out.println();

        // показываем курсы студента и их учителей
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

            // сохраняем оценку
            String insertQuery = """
                    INSERT INTO questionnaires (student_login, teacher_login, rating)
                    VALUES (?, ?, ?)
                    """;
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, student.getLogin());
            insertStmt.setString(2, teacherLogin);
            insertStmt.setInt(3, rating);
            insertStmt.executeUpdate();

            // пересчитываем средний рейтинг учителя
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
            case "1" -> viewMyRequestsByLogin(student.getLogin());
            case "2" -> sendNewRequestByLogin(student.getLogin());
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

            System.out.println();
            System.out.println("================================================");
            System.out.println("           STUDENT'S EXAM SCHEDULE");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Course:    " + rs.getString("name"));
                System.out.println("Date:      " + rs.getString("date"));
                System.out.println("Classroom: " + rs.getString("classroom"));
                System.out.println("------------------------------------------------");
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
        System.out.println();
        System.out.println("================================================");
        System.out.println("             VIEW TEACHER INFO");
        System.out.println("================================================");
        System.out.println();
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

    static void viewNews() {
        String query = """
                SELECT news_id, title, content, date, author_login
                FROM news
                ORDER BY date DESC
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            System.out.println();
            System.out.println("================================================");
            System.out.println("                    NEWS");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:      " + rs.getInt("news_id"));
                System.out.println("Title:   " + rs.getString("title"));
                System.out.println("Content: " + rs.getString("content"));
                System.out.println("Date:    " + rs.getString("date"));
                System.out.println("Author:  " + rs.getString("author_login"));
                System.out.println("------------------------------------------------");
            }

            if (!found) {
                System.out.println("No news found.");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching news: " + e.getMessage());
        }
    }

    static void researchProjects() {
        System.out.println("\nResearch Projects feature coming soon...\n");
    }

    static void publishResearchPaper() {
        System.out.println("\nPublish Research Paper feature coming soon...\n");
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
            case "1" -> viewMyRequestsByLogin(teacher.getLogin());
            case "2" -> sendNewRequestByLogin(teacher.getLogin());
            case "0" -> {
                return;
            }
            default -> System.out.println("\nInvalid choice.\n");
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

            System.out.println();
            System.out.println("================================================");
            System.out.println("               MY REQUESTS");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:      " + rs.getInt("request_id"));
                System.out.println("Type:    " + rs.getString("request_type"));
                System.out.println("Content: " + rs.getString("content"));
                System.out.println("Status:  " + rs.getString("status"));
                System.out.println("------------------------------------------------");
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
        System.out.println();
        System.out.println("================================================");
        System.out.println("              SEND NEW REQUEST");
        System.out.println("================================================");
        System.out.println();
        System.out.println("1. Schedule Request");
        System.out.println("2. Research Request");
        System.out.println("0. Back");
        System.out.println();
        System.out.print("Enter your choice: ");

        String choice = scanner.nextLine().trim();
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
        String content = scanner.nextLine().trim();

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

            System.out.println();
            System.out.println("================================================");
            System.out.println("             TEACHER'S SCHEDULE");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Course: " + rs.getString("name"));
                System.out.println("Slot:   " + rs.getString("slot"));
                System.out.println("------------------------------------------------");
            }

            if (!found) {
                System.out.println("No schedule found.");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching schedule: " + e.getMessage());
        }
    }

    static void teacherRequests() {
        System.out.println("\nTeacher's Requests feature coming soon...\n");
    }

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

            System.out.println();
            System.out.println("================================================");
            System.out.println("             VIEW PERSONAL INFO");
            System.out.println("================================================");
            System.out.println();

            if (rs.next()) {
                System.out.println("Login:      " + login);
                System.out.println("Name:       " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Role:       " + rs.getString("role"));
                System.out.println("Created At: " + rs.getString("created_at"));

                // доп инфо в зависимости от роли
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

            System.out.println();
            System.out.println("================================================");
            System.out.println("             VIEW STUDENT INFO");
            System.out.println("================================================");
            System.out.println();

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

            System.out.println();
            System.out.println("================================================");
            System.out.println("               YOUR COURSES");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:          " + rs.getInt("course_id"));
                System.out.println("Name:        " + rs.getString("name"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Credits:     " + rs.getInt("credits"));
                System.out.println("Max Students:" + rs.getInt("max_students"));
                System.out.println("------------------------------------------------");
            }

            if (!found) {
                System.out.println("No courses found.");
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error fetching courses: " + e.getMessage());
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

            System.out.println();
            System.out.println("================================================");
            System.out.println("                  JOURNAL");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Student: " + rs.getString("first_name") + " " + rs.getString("last_name") +
                        " (" + rs.getString("login") + ")");
                System.out.println("Grade:   " + (rs.getString("grade") == null ? "No grade" : rs.getString("grade")));
                System.out.println("Date:    " + (rs.getString("date") == null ? "-" : rs.getString("date")));
                System.out.println("------------------------------------------------");
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

                String now = java.time.LocalDateTime.now().toString();
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

    static void sendMessages(String login) {
        System.out.print("""

                ================================================
                              MESSAGES
                ================================================

                1. View Messages
                2. Send Message
                0. Back

                Enter your choice:\s""");

        String choice = scanner.nextLine().trim();

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

            System.out.println();
            System.out.println("================================================");
            System.out.println("                 MY MESSAGES");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:   " + rs.getInt("message_id"));
                System.out.println("From: " + rs.getString("sender_login"));
                System.out.println("To:   " + rs.getString("receiver_login"));
                System.out.println("Msg:  " + rs.getString("content"));
                System.out.println("Date: " + rs.getString("date"));
                System.out.println("------------------------------------------------");
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
        String receiverLogin = scanner.nextLine().trim();

        System.out.print("Enter message: ");
        String content = scanner.nextLine().trim();

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

    static void viewRequests() {
        System.out.println("\nView Requests feature coming soon...\n");
    }

    static void viewUserInfo() {
        System.out.println("\nView User Info feature coming soon...\n");
    }

    static void registrationRequests() {
        String query = """
                SELECT request_id, sender_login, content, status
                FROM requests
                WHERE request_type = 'REGISTRATION' AND status = 'PENDING'
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            System.out.println();
            System.out.println("================================================");
            System.out.println("           REGISTRATION REQUESTS");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:      " + rs.getInt("request_id"));
                System.out.println("From:    " + rs.getString("sender_login"));
                System.out.println("Content: " + rs.getString("content"));
                System.out.println("Status:  " + rs.getString("status"));
                System.out.println("------------------------------------------------");
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

            String updateQuery = "UPDATE requests SET status = ? WHERE request_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setString(1, newStatus);
            updateStmt.setInt(2, requestId);
            updateStmt.executeUpdate();

            // если одобрено — добавляем курс студенту
            if (newStatus.equals("APPROVED")) {
                String getRequest = "SELECT sender_login, content FROM requests WHERE request_id = ?";
                PreparedStatement getStmt = conn.prepareStatement(getRequest);
                getStmt.setInt(1, requestId);
                ResultSet reqRs = getStmt.executeQuery();

                if (reqRs.next()) {
                    String studentLogin = reqRs.getString("sender_login");
                    String content = reqRs.getString("content");

                    // content формат: "ADD course_id=1" или "DROP course_id=1"
                    String[] parts = content.split("=");
                    int courseId = Integer.parseInt(parts[1].trim());

                    if (content.startsWith("ADD")) {
                        String getFacultyQuery = "SELECT faculty_id FROM faculty_courses WHERE course_id = ?";
                        PreparedStatement getFaculty = conn.prepareStatement(getFacultyQuery);
                        getFaculty.setInt(1, courseId);
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
                    } else if (content.startsWith("DROP")) {
                        String getFacultyQuery = "SELECT faculty_id FROM faculty_courses WHERE course_id = ?";
                        PreparedStatement getFaculty = conn.prepareStatement(getFacultyQuery);
                        getFaculty.setInt(1, courseId);
                        ResultSet facultyRs = getFaculty.executeQuery();

                        if (facultyRs.next()) {
                            int facultyId = facultyRs.getInt("faculty_id");
                            String deleteFacultyStudent = """
                                    DELETE FROM faculty_students WHERE faculty_id = ? AND student_login = ?
                                    """;
                            PreparedStatement deleteStmt = conn.prepareStatement(deleteFacultyStudent);
                            deleteStmt.setInt(1, facultyId);
                            deleteStmt.setString(2, studentLogin);
                            deleteStmt.executeUpdate();
                        }
                    }
                }
            }

            System.out.println("\nRequest " + newStatus + " successfully!\n");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ...existing code...
    static void scheduleRequests() {
        String query = """
                SELECT request_id, sender_login, content, status
                FROM requests
                WHERE request_type = 'SCHEDULE' AND status = 'PENDING'
                """;

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            System.out.println();
            System.out.println("================================================");
            System.out.println("             SCHEDULE REQUESTS");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID:      " + rs.getInt("request_id"));
                System.out.println("From:    " + rs.getString("sender_login"));
                System.out.println("Content: " + rs.getString("content"));
                System.out.println("Status:  " + rs.getString("status"));
                System.out.println("------------------------------------------------");
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

            String lookupQuery = "SELECT sender_login, content FROM requests WHERE request_id = ?";
            try (PreparedStatement lookupStmt = conn.prepareStatement(lookupQuery)) {
                lookupStmt.setInt(1, requestId);
                ResultSet lookupRs = lookupStmt.executeQuery();

                if (!lookupRs.next()) {
                    System.out.println("\nRequest ID not found.\n");
                    return;
                }

                String senderLogin = lookupRs.getString("sender_login");
                String content = lookupRs.getString("content");

                String newStatus = choice.equals("1") ? "APPROVED" : "REJECTED";
                String updateQuery = "UPDATE requests SET status = ? WHERE request_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, newStatus);
                    updateStmt.setInt(2, requestId);
                    int updated = updateStmt.executeUpdate();

                    if (updated == 0) {
                        System.out.println("\nRequest ID not found.\n");
                        return;
                    }
                }

                if (newStatus.equals("APPROVED")) {
                    createScheduleApprovalNotification(conn, senderLogin, requestId, content);
                }

                System.out.println("\nRequest " + newStatus + " successfully!\n");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void createScheduleApprovalNotification(Connection conn, String receiverLogin, int requestId,
            String content) {
        String insertMessage = """
                INSERT INTO messages (sender_login, receiver_login, content, date)
                VALUES ('SYSTEM', ?, ?, ?)
                """;

        try (PreparedStatement msgStmt = conn.prepareStatement(insertMessage)) {
            msgStmt.setString(1, receiverLogin);
            msgStmt.setString(2,
                    "Your schedule request #" + requestId + " has been approved.\nRequest details: " + content);
            msgStmt.setString(3, java.time.LocalDateTime.now().toString());
            msgStmt.executeUpdate();
        } catch (SQLException e) {
            // Ignore notification failure if messages table or SYSTEM sender is not
            // available
        }
    }

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
                case "1" -> viewNews();
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
            stmt.setString(3, java.time.LocalDateTime.now().toString());
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

        String createdAt = java.time.LocalDateTime.now().toString();
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

            System.out.println();
            System.out.println("================================================");
            System.out.println("                  USER LIST");
            System.out.println("================================================");
            System.out.println();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Login:      " + rs.getString("login"));
                System.out.println("Name:       " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Role:       " + rs.getString("role"));
                System.out.println("------------------------------------------------");
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

                    System.out.println();
                    System.out.println("================================================");
                    System.out.println("                  SALARIES");
                    System.out.println("================================================");
                    System.out.println();

                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        System.out.println("Login:  " + rs.getString("login"));
                        System.out.println("Salary: " + rs.getDouble("salary"));
                        System.out.println("------------------------------------------------");
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
        String[] scholarshipQueries = {
                "SELECT login, scholarship FROM students ORDER BY login",
                "SELECT login, amount AS scholarship FROM student_scholarships ORDER BY login"
        };

        try (Connection conn = DatabaseManager.getConnection()) {
            for (String query : scholarshipQueries) {
                try (PreparedStatement stmt = conn.prepareStatement(query);
                        ResultSet rs = stmt.executeQuery()) {

                    System.out.println();
                    System.out.println("================================================");
                    System.out.println("               SCHOLARSHIPS");
                    System.out.println("================================================");
                    System.out.println();

                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        System.out.println("Login:       " + rs.getString("login"));
                        System.out.println("Scholarship: " + rs.getDouble("scholarship"));
                        System.out.println("------------------------------------------------");
                    }

                    if (!found) {
                        continue;
                    }

                    System.out.println();
                    return;
                } catch (SQLException ignored) {
                }
            }

            System.out.println("\nNo scholarship information available.\n");
        } catch (SQLException e) {
            System.out.println("Error fetching scholarships: " + e.getMessage());
        }
    }

    private static void updateScholarship() {
        System.out.print("Enter student login: ");
        String login = scanner.nextLine().trim();

        System.out.print("Enter scholarship amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid amount.\n");
            return;
        }

        String[] updateQueries = {
                "UPDATE students SET scholarship = ? WHERE login = ?",
                "UPDATE student_scholarships SET amount = ? WHERE login = ?"
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
                System.out.println("\nUnable to update scholarship: student or scholarship table not found.\n");
            } else {
                System.out.println("\nScholarship updated successfully.\n");
            }
        } catch (SQLException e) {
            System.out.println("Error updating scholarship: " + e.getMessage());
        }
    }

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

    static void viewLogs() {
        String[] logQueries = {
                "SELECT log_id, user_login, action, date FROM logs ORDER BY date DESC",
                "SELECT id AS log_id, user_login, action, created_at AS date FROM logs ORDER BY created_at DESC"
        };

        try (Connection conn = DatabaseManager.getConnection()) {
            for (String query : logQueries) {
                try (PreparedStatement stmt = conn.prepareStatement(query);
                        ResultSet rs = stmt.executeQuery()) {

                    System.out.println();
                    System.out.println("================================================");
                    System.out.println("                    LOGS");
                    System.out.println("================================================");
                    System.out.println();

                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        System.out.println("ID:      " + rs.getInt("log_id"));
                        System.out.println("User:    " + rs.getString("user_login"));
                        System.out.println("Action:  " + rs.getString("action"));
                        System.out.println("Date:    " + rs.getString("date"));
                        System.out.println("------------------------------------------------");
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