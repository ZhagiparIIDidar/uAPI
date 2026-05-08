package kz.uapi.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

        public static void initialize() {
                String createUsers = """
                                CREATE TABLE IF NOT EXISTS users (
                                    login      TEXT PRIMARY KEY,
                                    password   TEXT NOT NULL,
                                    role       TEXT NOT NULL,
                                    first_name TEXT NOT NULL,
                                    last_name  TEXT NOT NULL,
                                    created_at TEXT NOT NULL
                                );
                                """;

                String createStudents = """
                                CREATE TABLE IF NOT EXISTS students (
                                    login           TEXT PRIMARY KEY,
                                    gpa             REAL DEFAULT 0.0,
                                    year            INTEGER NOT NULL,
                                    major           TEXT NOT NULL,
                                    credits         INTEGER DEFAULT 0,
                                    is_researcher   INTEGER DEFAULT 0,
                                    supervisor_login TEXT,
                                    FOREIGN KEY (login) REFERENCES users(login),
                                    FOREIGN KEY (supervisor_login) REFERENCES users(login)
                                );
                                """;

                String createEmployees = """
                                CREATE TABLE IF NOT EXISTS employees (
                                    login      TEXT PRIMARY KEY,
                                    department TEXT NOT NULL,
                                    salary     REAL NOT NULL,
                                    FOREIGN KEY (login) REFERENCES users(login)
                                );
                                """;

                String createTeachers = """
                                CREATE TABLE IF NOT EXISTS teachers (
                                    login           TEXT PRIMARY KEY,
                                    title           TEXT NOT NULL,
                                    rating          REAL DEFAULT 0.0,
                                    h_index         INTEGER DEFAULT 0,
                                    specializations TEXT,
                                    FOREIGN KEY (login) REFERENCES employees(login)
                                );
                                """;

                String createManagers = """
                                CREATE TABLE IF NOT EXISTS managers (
                                    login        TEXT PRIMARY KEY,
                                    manager_type TEXT NOT NULL,
                                    FOREIGN KEY (login) REFERENCES employees(login)
                                );
                                """;

                String createAdmins = """
                                CREATE TABLE IF NOT EXISTS admins (
                                    login        TEXT PRIMARY KEY,
                                    access_level INTEGER NOT NULL,
                                    FOREIGN KEY (login) REFERENCES employees(login)
                                );
                                """;

                String createResearcherEmployees = """
                                CREATE TABLE IF NOT EXISTS researcher_employees (
                                    login           TEXT PRIMARY KEY,
                                    h_index         INTEGER DEFAULT 0,
                                    specializations TEXT,
                                    FOREIGN KEY (login) REFERENCES employees(login)
                                );
                                """;
                String createFaculties = """
                                CREATE TABLE IF NOT EXISTS faculties (
                                    faculty_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    name       TEXT NOT NULL
                                );
                                """;

                String createCourses = """
                                CREATE TABLE IF NOT EXISTS courses (
                                    course_id    INTEGER PRIMARY KEY AUTOINCREMENT,
                                    name         TEXT NOT NULL,
                                    description  TEXT,
                                    credits      INTEGER NOT NULL,
                                    teacher_login TEXT,
                                    max_students INTEGER,
                                    faculty_id   INTEGER,
                                    FOREIGN KEY (teacher_login) REFERENCES users(login),
                                    FOREIGN KEY (faculty_id) REFERENCES faculties(faculty_id)

                                );
                                """;

                String createLessons = """
                                CREATE TABLE IF NOT EXISTS lessons (
                                    lesson_id      INTEGER PRIMARY KEY AUTOINCREMENT,
                                    course_id      INTEGER NOT NULL,
                                    topic          TEXT NOT NULL,
                                    used_materials TEXT,
                                    lesson_type    TEXT NOT NULL,
                                    FOREIGN KEY (course_id) REFERENCES courses(course_id)
                                );
                                """;

                String createSchedules = """
                                CREATE TABLE IF NOT EXISTS schedules (
                                    schedule_id    INTEGER PRIMARY KEY AUTOINCREMENT,
                                    student_login  TEXT NOT NULL,
                                    FOREIGN KEY (student_login) REFERENCES users(login)
                                );
                                """;

                String createScheduleSlots = """
                                CREATE TABLE IF NOT EXISTS schedule_slots (
                                    slot_id     INTEGER PRIMARY KEY AUTOINCREMENT,
                                    schedule_id INTEGER NOT NULL,
                                    slot        TEXT NOT NULL,
                                    FOREIGN KEY (schedule_id) REFERENCES schedules(schedule_id)
                                );
                                """;

                String createRequests = """
                                    CREATE TABLE IF NOT EXISTS requests (
                                    request_id   INTEGER PRIMARY KEY AUTOINCREMENT,
                                    sender_login TEXT NOT NULL,
                                    course_id      INTEGER NOT NULL,
                                    status       TEXT NOT NULL DEFAULT 'PENDING',
                                    request_type TEXT NOT NULL,
                                    action       TEXT,  -- ADD или DROP
                                    FOREIGN KEY (sender_login) REFERENCES users(login)
                                );
                                """;

                String createMarks = """
                                CREATE TABLE IF NOT EXISTS marks (
                                    mark_id       INTEGER PRIMARY KEY AUTOINCREMENT,
                                    student_login TEXT NOT NULL,
                                    course_id     INTEGER NOT NULL,
                                    grade         REAL NOT NULL,
                                    date          TEXT NOT NULL,
                                    FOREIGN KEY (student_login) REFERENCES users(login),
                                    FOREIGN KEY (course_id) REFERENCES courses(course_id)
                                );
                                """;

                String createTranscripts = """
                                CREATE TABLE IF NOT EXISTS transcripts (
                                    transcript_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    student_login TEXT NOT NULL,
                                    gpa           REAL NOT NULL,
                                    FOREIGN KEY (student_login) REFERENCES users(login)
                                );
                                """;

                String createNews = """
                                CREATE TABLE IF NOT EXISTS news (
                                    news_id      INTEGER PRIMARY KEY AUTOINCREMENT,
                                    title        TEXT NOT NULL,
                                    content      TEXT NOT NULL,
                                    date         TEXT NOT NULL,
                                    author_login TEXT NOT NULL,
                                    FOREIGN KEY (author_login) REFERENCES users(login)
                                );
                                """;

                String createMessages = """
                                CREATE TABLE IF NOT EXISTS messages (
                                    message_id    INTEGER PRIMARY KEY AUTOINCREMENT,
                                    sender_login  TEXT NOT NULL,
                                    receiver_login TEXT NOT NULL,
                                    content       TEXT NOT NULL,
                                    date          TEXT NOT NULL,
                                    FOREIGN KEY (sender_login) REFERENCES users(login),
                                    FOREIGN KEY (receiver_login) REFERENCES users(login)
                                );
                                """;

                String createLogs = """
                                CREATE TABLE IF NOT EXISTS logs (
                                    log_id     INTEGER PRIMARY KEY AUTOINCREMENT,
                                    user_login TEXT NOT NULL,
                                    action     TEXT NOT NULL,
                                    date       TEXT NOT NULL,
                                    FOREIGN KEY (user_login) REFERENCES users(login)
                                );
                                """;

                String createResearchProjects = """
                                CREATE TABLE IF NOT EXISTS research_projects (
                                    project_id       INTEGER PRIMARY KEY AUTOINCREMENT,
                                    title            TEXT NOT NULL,
                                    description      TEXT,
                                    supervisor_login TEXT NOT NULL,
                                    status           TEXT NOT NULL,
                                    FOREIGN KEY (supervisor_login) REFERENCES users(login)
                                );
                                """;

                String createProjectMembers = """
                                CREATE TABLE IF NOT EXISTS project_members (
                                    project_id   INTEGER NOT NULL,
                                    member_login TEXT NOT NULL,
                                    PRIMARY KEY (project_id, member_login),
                                    FOREIGN KEY (project_id) REFERENCES research_projects(project_id),
                                    FOREIGN KEY (member_login) REFERENCES users(login)
                                );
                                """;

                String createResearchPapers = """
                                CREATE TABLE IF NOT EXISTS research_papers (
                                    paper_id       INTEGER PRIMARY KEY AUTOINCREMENT,
                                    title          TEXT NOT NULL,
                                    content        TEXT,
                                    author_login   TEXT NOT NULL,
                                    project_id     INTEGER NOT NULL,
                                    published_date TEXT NOT NULL,
                                    citations      INTEGER DEFAULT 0,
                                    keywords       TEXT,
                                    metrics        TEXT,
                                    figures        TEXT,
                                    FOREIGN KEY (author_login) REFERENCES users(login),
                                    FOREIGN KEY (project_id) REFERENCES research_projects(project_id)
                                );
                                """;

                String createFacultyStudents = """
                                CREATE TABLE IF NOT EXISTS faculty_students (
                                    faculty_id    INTEGER NOT NULL,
                                    student_login TEXT NOT NULL,
                                    PRIMARY KEY (faculty_id, student_login),
                                    FOREIGN KEY (faculty_id) REFERENCES faculties(faculty_id),
                                    FOREIGN KEY (student_login) REFERENCES users(login)
                                );
                                """;

                String createFacultyCourses = """
                                CREATE TABLE IF NOT EXISTS faculty_courses (
                                    faculty_id INTEGER NOT NULL,
                                    course_id  INTEGER NOT NULL,
                                    PRIMARY KEY (faculty_id, course_id),
                                    FOREIGN KEY (faculty_id) REFERENCES faculties(faculty_id),
                                    FOREIGN KEY (course_id) REFERENCES courses(course_id)
                                );
                                """;
                String createAttestations = """
                                CREATE TABLE IF NOT EXISTS attestations (
                                    attestation_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    student_login  TEXT NOT NULL,
                                    course_id      INTEGER NOT NULL,
                                    att1           REAL DEFAULT 0.0,
                                    att2           REAL DEFAULT 0.0,
                                    final          REAL DEFAULT 0.0,
                                    FOREIGN KEY (student_login) REFERENCES users(login),
                                    FOREIGN KEY (course_id) REFERENCES courses(course_id)
                                );
                                """;
                String createQuestionnaires = """
                                CREATE TABLE IF NOT EXISTS questionnaires (
                                    q_id          INTEGER PRIMARY KEY AUTOINCREMENT,
                                    student_login TEXT NOT NULL,
                                    teacher_login TEXT NOT NULL,
                                    rating        INTEGER NOT NULL CHECK(rating BETWEEN 1 AND 5),
                                    FOREIGN KEY (student_login) REFERENCES users(login),
                                    FOREIGN KEY (teacher_login) REFERENCES users(login)
                                );
                                """;
                String createExamSchedule = """
                                CREATE TABLE IF NOT EXISTS exam_schedule (
                                    exam_id    INTEGER PRIMARY KEY AUTOINCREMENT,
                                    course_id  INTEGER NOT NULL,
                                    date       TEXT NOT NULL,
                                    classroom  TEXT NOT NULL,
                                    FOREIGN KEY (course_id) REFERENCES courses(course_id)
                                );
                                """;
                String createStudentCourses = """
                                                CREATE TABLE IF NOT EXISTS student_courses (
                                    student_login TEXT NOT NULL,
                                    course_id     INTEGER NOT NULL,
                                    PRIMARY KEY (student_login, course_id),
                                    FOREIGN KEY (student_login) REFERENCES users(login),
                                    FOREIGN KEY (course_id) REFERENCES courses(course_id)
                                );
                                                """;

                try (Connection conn = DatabaseManager.getConnection();
                                Statement stmt = conn.createStatement()) {
                        stmt.execute(createUsers);
                        stmt.execute(createStudents);
                        stmt.execute(createEmployees);
                        stmt.execute(createTeachers);
                        stmt.execute(createManagers);
                        stmt.execute(createAdmins);
                        stmt.execute(createResearcherEmployees);
                        stmt.execute(createFaculties);
                        stmt.execute(createCourses);
                        stmt.execute(createLessons);
                        stmt.execute(createSchedules);
                        stmt.execute(createScheduleSlots);
                        stmt.execute(createRequests);
                        stmt.execute(createMarks);
                        stmt.execute(createTranscripts);
                        stmt.execute(createNews);
                        stmt.execute(createMessages);
                        stmt.execute(createLogs);
                        stmt.execute(createResearchProjects);
                        stmt.execute(createProjectMembers);
                        stmt.execute(createResearchPapers);
                        stmt.execute(createFacultyStudents);
                        stmt.execute(createFacultyCourses);
                        stmt.execute(createAttestations);
                        stmt.execute(createExamSchedule);
                        stmt.execute(createQuestionnaires);
                        stmt.execute(createStudentCourses);
                        System.out.println("Database initialized successfully.");

                } catch (SQLException e) {
                        System.out.println("Error initializing database: " + e.getMessage());
                }
        }

        public static void insertTestData() {
                try (Connection conn = DatabaseManager.getConnection();
                                Statement stmt = conn.createStatement()) {
                        stmt.execute(
                                        """
                                                        INSERT OR IGNORE INTO attestations (attestation_id, student_login, course_id, att1, att2, final) VALUES
                                                        (1, 'student1', 1, 75.0, 80.0, 85.0),
                                                        (2, 'student1', 2, 90.0, 88.0, 92.0);
                                                        """);

                        stmt.execute(
                                        """
                                                        INSERT OR IGNORE INTO exam_schedule (exam_id, course_id, date, classroom) VALUES
                                                        (1, 1, '2024-05-20T10:00:00', 'Room 101'),
                                                        (2, 2, '2024-05-22T14:00:00', 'Room 202');
                                                        """);
                        // users
                        stmt.execute(
                                        """
                                                        INSERT OR IGNORE INTO users (login, password, role, first_name, last_name, created_at) VALUES
                                                        ('student1',    'pass123', 'STUDENT',    'Aibek',   'Dzhaksybekov', '2024-01-01T10:00:00'),
                                                        ('student2',    'pass123', 'STUDENT',    'Aibek2',   'Dzhaksybekov2', '2024-01-01T10:00:00'),
                                                        ('teacher1',    'pass123', 'TEACHER',    'Ainur',   'Bekova',        '2024-01-01T10:00:00'),
                                                        ('manager1',    'pass123', 'MANAGER',    'Daniyar', 'Seitkali',      '2024-01-01T10:00:00'),
                                                        ('admin1',      'pass123', 'ADMIN',      'Marat',   'Akhmetov',      '2024-01-01T10:00:00'),
                                                        ('researcher1', 'pass123', 'RESEARCHER', 'Saltanat','Nurlanova',     '2024-01-01T10:00:00');
                                                        """);

                        // students
                        stmt.execute(
                                        """
                                                        INSERT OR IGNORE INTO students (login, gpa, year, major, credits, is_researcher, supervisor_login) VALUES
                                                        ('student1', 3.5, 2, 'Computer Science', 60, 0, NULL);
                                                        """);

                        // employees
                        stmt.execute("""
                                        INSERT OR IGNORE INTO employees (login, department, salary) VALUES
                                        ('teacher1',    'CS Department',  300000),
                                        ('manager1',    'OR Department',  250000),
                                        ('admin1',      'IT Department',  280000),
                                        ('researcher1', 'Research Center',320000);
                                        """);

                        // teachers
                        stmt.execute("""
                                        INSERT OR IGNORE INTO teachers (login, title, rating, h_index, specializations) VALUES
                                        ('teacher1', 'PROFESSOR', 4.8, 10, 'AI,Machine Learning');
                                        """);

                        // managers
                        stmt.execute("""
                                        INSERT OR IGNORE INTO managers (login, manager_type) VALUES
                                        ('manager1', 'OR');
                                        """);

                        // admins
                        stmt.execute("""
                                        INSERT OR IGNORE INTO admins (login, access_level) VALUES
                                        ('admin1', 3);
                                        """);

                        // researcher_employees
                        stmt.execute("""
                                        INSERT OR IGNORE INTO researcher_employees (login, h_index, specializations) VALUES
                                        ('researcher1', 7, 'Data Science,Big Data');
                                        """);

                        // faculties
                        stmt.execute("""
                                        INSERT OR IGNORE INTO faculties (faculty_id, name) VALUES
                                        (1, 'Faculty of Computer Science'),
                                        (2, 'Faculty of Mathematics');
                                        """);

                        // courses
                        stmt.execute(
                                        """
                                                        INSERT OR IGNORE INTO courses (course_id, name, description, credits, teacher_login, max_students, faculty_id) VALUES
                                                        (1, 'OOP',         'Object Oriented Programming', 5, 'teacher1', 30, 1),
                                                        (2, 'Algorithms',  'Data Structures & Algorithms', 4, 'teacher1', 25, 1),
                                                        (3, 'Calculus',    'Mathematical Analysis',        3, 'teacher1', 40, 2);
                                                        """);
                        // st courses
                        stmt.execute("""
                                        INSERT OR IGNORE INTO student_courses (student_login, course_id) VALUES
                                        ('student1', 1),
                                        ('student1', 2)
                                        """);
                        // lessons
                        stmt.execute("""
                                        INSERT OR IGNORE INTO lessons (lesson_id, course_id, topic, used_materials, lesson_type) VALUES
                                        (1, 1, 'Introduction to OOP',    'Slides, Book Chapter 1', 'LECTURE'),
                                        (2, 1, 'Classes and Objects',    'Slides, Book Chapter 2', 'PRACTICE'),
                                        (3, 2, 'Arrays and LinkedLists', 'Slides, Book Chapter 3', 'LAB');
                                        """);

                        // schedules
                        stmt.execute("""
                                        INSERT OR IGNORE INTO schedules (schedule_id, student_login) VALUES
                                        (1, 'student1');
                                        """);

                        // schedule_slots
                        stmt.execute("""
                                        INSERT OR IGNORE INTO schedule_slots (slot_id, schedule_id, slot) VALUES
                                        (1, 1, 'OOP Monday 10:00-12:00'),
                                        (2, 1, 'Algorithms Wednesday 14:00-16:00');
                                        """);

                        // requests
                        stmt.execute(
                                        """
                                                        INSERT OR IGNORE INTO requests (request_id, sender_login, course_id, status, request_type, action) VALUES
                                                        (1, 'student1', 1, 'PENDING', 'SCHEDULE', 'ADD'),
                                                        (2, 'student1', 2, 'PENDING', 'REGISTRATION', 'DROP');
                                                        """);

                        // marks
                        stmt.execute("""
                                        INSERT OR IGNORE INTO marks (mark_id, student_login, course_id, grade, date) VALUES
                                        (1, 'student1', 1, 85.0, '2024-03-01T10:00:00'),
                                        (2, 'student1', 2, 90.0, '2024-03-15T10:00:00');
                                        """);

                        // transcripts
                        stmt.execute("""
                                        INSERT OR IGNORE INTO transcripts (transcript_id, student_login, gpa) VALUES
                                        (1, 'student1', 3.5);
                                        """);

                        // news
                        stmt.execute("""
                                        INSERT OR IGNORE INTO news (news_id, title, content, date, author_login) VALUES
                                        (1, 'Welcome to UAPI', 'Welcome to the new semester!', '2024-01-15T09:00:00', 'manager1'),
                                        (2, 'Exam Schedule',   'Final exams start May 20.',    '2024-05-01T09:00:00', 'manager1');
                                        """);

                        // messages
                        stmt.execute("""
                                        INSERT OR IGNORE INTO messages (message_id, sender_login, receiver_login, content, date) VALUES
                                        (1, 'teacher1', 'student1', 'Please submit your assignment.', '2024-03-10T10:00:00');
                                        """);

                        // logs
                        stmt.execute("""
                                        INSERT OR IGNORE INTO logs (log_id, user_login, action, date) VALUES
                                        (1, 'student1', 'LOGIN', '2024-03-01T08:00:00'),
                                        (2, 'teacher1', 'LOGIN', '2024-03-01T09:00:00');
                                        """);

                        // research_projects
                        stmt.execute(
                                        """
                                                        INSERT OR IGNORE INTO research_projects (project_id, title, description, supervisor_login, status) VALUES
                                                        (1, 'AI in Education', 'Research on AI applications in education', 'researcher1', 'ACTIVE');
                                                        """);

                        // project_members
                        stmt.execute("""
                                        INSERT OR IGNORE INTO project_members (project_id, member_login) VALUES
                                        (1, 'researcher1');
                                        """);

                        // research_papers
                        stmt.execute(
                                        """
                                                        INSERT OR IGNORE INTO research_papers (paper_id, title, content, author_login, project_id, published_date, citations, keywords, metrics, figures) VALUES
                                                        (1, 'AI in Classrooms', 'This paper explores...', 'researcher1', 1, '2024-02-01T00:00:00', 5, 'AI,Education', 'Accuracy: 95%', 'Figure1.png');
                                                        """);

                        // faculty_students
                        stmt.execute("""
                                        INSERT OR IGNORE INTO faculty_students (faculty_id, student_login) VALUES
                                        (1, 'student1');
                                        """);

                        // faculty_courses
                        stmt.execute("""
                                        INSERT OR IGNORE INTO faculty_courses (faculty_id, course_id) VALUES
                                        (1, 1),
                                        (1, 2),
                                        (2, 3);
                                        """);

                        System.out.println("Test data inserted successfully.");

                } catch (SQLException e) {
                        System.out.println("Error inserting test data: " + e.getMessage());
                }
        }
}